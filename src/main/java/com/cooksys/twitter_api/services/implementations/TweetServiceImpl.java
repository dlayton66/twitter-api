package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.TweetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private final HashtagRepository hashtagRepository;

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<TweetResponseDto> getAllTweets() {
        List<Tweet> tweets = tweetRepository.getByDeletedFalse(Sort.by("posted").descending());
        return tweetMapper.entitiesToResponseDtos(tweets);
    }
    @Override
    @Transactional
    public ResponseEntity<TweetResponseDto> createTweet(TweetRequestDto tweetRequestDto) {
        //TODO: Check Credentials using the validationService probably, instead of this way.
        Optional<User> author = userRepository.findByCredentialsUsername(tweetRequestDto.getCredentials().getUsername());
        if(author.isEmpty()){
            throw new NotFoundException("No user found");
        }
        if(!tweetRequestDto.getCredentials().getPassword().equals(author.get().getCredentials().password)){
            throw new NotAuthorizedException();
        }
        // Map request to Entity.
        Tweet newTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
        newTweet.setAuthor(author.get());

        // Scan text for hashtags, such as #hashtags.
        String content = newTweet.getContent();
        Pattern hashtagPattern = Pattern.compile("#[A-Za-z0-9]+(?:\\b|$)");
        Matcher hashtagMatcher = hashtagPattern.matcher(content);
        // Add all hashtags to a set, which ensures no duplicate hashtags.
        Set<String> allHashtagStrings = new HashSet<>();
        while (hashtagMatcher.find()) {
            allHashtagStrings.add(hashtagMatcher.group());
        }

        // Lists for new and existing hashtags.
        List<Hashtag> newHashtagEntities = new ArrayList<>();
        List<Hashtag> existingHashtags = new ArrayList<>();
        for (String eachHashtag : allHashtagStrings) {
            // Check if they are already represented in the Repository,
            // and put them in one of the two lists.
            Optional<Hashtag> hashtagsDatabaseEntry = hashtagRepository.findByLabel(eachHashtag);
            if (hashtagsDatabaseEntry.isEmpty()) {
                // Build a new hashtag entity for new hashtags.
                Hashtag newHashtagEntity = new Hashtag();
                newHashtagEntity.setFirstUsed(Timestamp.from(Instant.now()));
                newHashtagEntity.setLastUsed(newHashtagEntity.getFirstUsed());
                newHashtagEntity.setLabel(eachHashtag);

                // Initialize the set of tweets for the new hashtag and add the current tweet.
                newHashtagEntity.setTweets(new HashSet<>());
                newHashtagEntity.getTweets().add(newTweet);

                newHashtagEntities.add(newHashtagEntity); // Add to new hashtags list.
            } else {
                // Add existing hashtag to the list.
                Hashtag existingHashtag = hashtagsDatabaseEntry.get();
                existingHashtags.add(existingHashtag);
                // Also, add the current tweet to the existing hashtag's tweets.
                existingHashtag.getTweets().add(newTweet);
            }
        }

        // Save all new hashtags in a batch.
        hashtagRepository.saveAll(newHashtagEntities);

        // Add all new and existing hashtags to the tweet's hashtags.
        newTweet.getHashtags().addAll(newHashtagEntities);
        newTweet.getHashtags().addAll(existingHashtags);

        // For hashtags IN the repository:
        for (Hashtag eachExistingHashtag : existingHashtags) {
            // Set the Hashtag->Tweets relationship.
            eachExistingHashtag.getTweets().add(newTweet);
            // Update lastUsed timestamp.
            eachExistingHashtag.setLastUsed(Timestamp.from(Instant.now()));
            // Save the Hashtag into the repository.
            hashtagRepository.save(eachExistingHashtag); // Optionally use saveAndFlush if immediate flush is needed.
        }

        //Scan content for Mentions, such as @ACoolUsername
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9]+)(?:\\b|$)");
        Matcher mentionMatcher = mentionPattern.matcher(content);
        // Add all mentions to a set, which ensures no duplicate mentions.
        Set<String> mentionStrings = new HashSet<>();
        while (mentionMatcher.find()) {
            mentionStrings.add(mentionMatcher.group(1));
        }
        List<User> mentionedExistingUsers = new ArrayList<>();
        for (String eachMention : mentionStrings){
            Optional<User> mentionedUser = userRepository.findByCredentialsUsername(eachMention);
            if(mentionedUser.isPresent()){
                newTweet.getMentions().add(mentionedUser.get());
                mentionedUser.get().getMentions().add(newTweet);
                mentionedExistingUsers.add(mentionedUser.get());
            }
        }
        userRepository.saveAllAndFlush(mentionedExistingUsers);

        // Save the tweet with all relationships set, then return it.
        TweetResponseDto response = tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(newTweet));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<TweetResponseDto> getTweetById(Long id) {
        Optional<Tweet> requestedTweet = tweetRepository.findById(id);
        if(requestedTweet.isEmpty()){
            throw new NotFoundException("No tweet found with id: " + id);
        }
        return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToResponseDto(requestedTweet.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TweetResponseDto>> getRepliesToTweet(Long id) {
        Optional<Tweet> originalTweet = tweetRepository.findById(id);
        if(originalTweet.isEmpty() || originalTweet.get().isDeleted()){
            throw new NotFoundException("No tweet found with id: " + id);
        }
        List<TweetResponseDto> response = tweetMapper.entitiesToResponseDtos( tweetRepository.findByInReplyTo(originalTweet.get(), Sort.by("posted").descending()));
        return new ResponseEntity<List<TweetResponseDto>>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getUsersMentionedInTweet(Long id) {
        Optional<Tweet> requestedTweet = tweetRepository.findById(id);
        if (requestedTweet.isEmpty()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }

        return ResponseEntity.ok(userMapper.entitiesToResponseDtos(List.copyOf(requestedTweet.get().getMentions())));
    }

    @Override
    public ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<TweetResponseDto> repostTweet(Long id, CredentialsDto credentialsDto) {
        return null;
    }

    @Override
    public ResponseEntity<ContextDto> getContextOfTweet(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<UserResponseDto> getLikesOnTweet(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<HashtagDto> getHashtagsOnTweet(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<TweetResponseDto> deleteTweet(Long id, CredentialsDto credentialsDto) {
        return null;
    }

    @Override
    public void likeTweet(Long id, CredentialsDto credentialsDto) {

    }

    @Override
    public ResponseEntity<TweetResponseDto> replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
        return null;
    }


}
