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


    User areCredentialsValid(CredentialsDto credentialsDto){
        Optional<User> user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        if(user.isEmpty()){throw new NotAuthorizedException();}
        if(credentialsDto.getPassword().equals(user.get().getCredentials().getPassword())){
            return user.get();
        }
        else {
            throw new NotAuthorizedException();
        }
    }

    private Set<String> extractHashtags(String content) {
        Pattern hashtagPattern = Pattern.compile("#[A-Za-z0-9]+(?:\\b|$)");
        Matcher hashtagMatcher = hashtagPattern.matcher(content);
        Set<String> allHashtagStrings = new HashSet<>();
        while (hashtagMatcher.find()) {
            allHashtagStrings.add(hashtagMatcher.group());
        }
        return allHashtagStrings;
    }

    private Set<String> extractMentions(String content) {
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9]+)(?:\\b|$)");
        Matcher mentionMatcher = mentionPattern.matcher(content);
        Set<String> mentionStrings = new HashSet<>();
        while (mentionMatcher.find()) {
            mentionStrings.add(mentionMatcher.group(1));
        }
        return mentionStrings;
    }

    private void processHashtags(Tweet tweet) {
        String content = tweet.getContent();
        // Scan text for hashtags, such as #hashtags.
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
                newHashtagEntity.getTweets().add(tweet);

                newHashtagEntities.add(newHashtagEntity); // Add to new hashtags list.
            } else {
                // Add existing hashtag to the list.
                Hashtag existingHashtag = hashtagsDatabaseEntry.get();
                existingHashtags.add(existingHashtag);
                // Also, add the current tweet to the existing hashtag's tweets.
                existingHashtag.getTweets().add(tweet);
            }
        }

        // Save all new hashtags in a batch.
        hashtagRepository.saveAll(newHashtagEntities);

        // Add all new and existing hashtags to the tweet's hashtags.
        tweet.getHashtags().addAll(newHashtagEntities);
        tweet.getHashtags().addAll(existingHashtags);

        // For hashtags IN the repository:
        for (Hashtag eachExistingHashtag : existingHashtags) {
            // Set the Hashtag->Tweets relationship.
            eachExistingHashtag.getTweets().add(tweet);
            // Update lastUsed timestamp.
            eachExistingHashtag.setLastUsed(Timestamp.from(Instant.now()));
            // Save the Hashtag into the repository.
            hashtagRepository.save(eachExistingHashtag);
        }
    }

    private void processMentions(Tweet tweet) {
        String content = tweet.getContent();
        // Scan content for Mentions, such as @ACoolUsername
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9]+)(?:\\b|$)");
        Matcher mentionMatcher = mentionPattern.matcher(content);
        // Add all mentions to a set, which ensures no duplicate mentions.
        Set<String> mentionStrings = new HashSet<>();
        while (mentionMatcher.find()) {
            mentionStrings.add(mentionMatcher.group(1));
        }
        List<User> mentionedExistingUsers = new ArrayList<>();
        for (String eachMention : mentionStrings) {
            Optional<User> mentionedUser = userRepository.findByCredentialsUsername(eachMention);
            if (mentionedUser.isPresent()) {
                tweet.getMentions().add(mentionedUser.get());
                mentionedUser.get().getMentions().add(tweet);
                mentionedExistingUsers.add(mentionedUser.get());
            }
        }
        userRepository.saveAllAndFlush(mentionedExistingUsers);
    }

    @Override
    public List<TweetResponseDto> getAllTweets() {
        List<Tweet> tweets = tweetRepository.getByDeletedFalse(Sort.by("posted").descending());
        return tweetMapper.entitiesToResponseDtos(tweets);
    }

    @Override
    @Transactional
    public ResponseEntity<TweetResponseDto> createTweet(TweetRequestDto tweetRequestDto) {
        User author = areCredentialsValid(tweetRequestDto.getCredentials());
        // Map request to Entity.
        Tweet newTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
        newTweet.setAuthor(author);
        author.getTweets().add(newTweet);
        userRepository.save(author);

        // Process hashtags.
        processHashtags(newTweet);

        // Process mentions.
        processMentions(newTweet);

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
    public List<TweetResponseDto> getRepliesToTweet(Long id) {
        Optional<Tweet> originalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if(originalTweet.isEmpty()){
            throw new NotFoundException("No tweet found with id: " + id);
        }
        return tweetMapper.entitiesToResponseDtos( tweetRepository.findByInReplyToAndDeletedFalse(originalTweet.get(), Sort.by("posted").descending()));
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getUsersMentionedInTweet(Long id) {
        Optional<Tweet> requestedTweet = tweetRepository.findById(id);
        if (requestedTweet.isEmpty()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }

        Set<User> mentionedUsers = requestedTweet.get().getMentions();
        mentionedUsers.removeIf(User::isDeleted);
        return ResponseEntity.ok(userMapper.entitiesToResponseDtos(List.copyOf(mentionedUsers)));
    }

    @Override
    public ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(Long id) {
        return null;
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
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
    public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
        // Credentials validation
        User author = areCredentialsValid(tweetRequestDto.getCredentials());

        // Find the parent tweet, handling the case where it might not exist.
        Optional<Tweet> parentTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (parentTweet.isEmpty()) {
            throw new NotFoundException("Tweet is either deleted or never existed.");
        }

        // Map the request DTO to a new Tweet entity and set its properties.
        Tweet newReplyTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
        newReplyTweet.setAuthor(author);
        newReplyTweet.setInReplyTo(parentTweet.get());

        // Process hashtags and mentions for the reply tweet.
        processHashtags(newReplyTweet);
        processMentions(newReplyTweet);

        // Save the new reply tweet and update the parent tweet with the reply.
        Tweet savedReplyTweet = tweetRepository.saveAndFlush(newReplyTweet);
        parentTweet.get().getReplies().add(savedReplyTweet);
        tweetRepository.saveAndFlush(parentTweet.get());

        // Convert the saved reply tweet to a DTO and return it.
        return tweetMapper.entityToResponseDto(savedReplyTweet);
    }


}
