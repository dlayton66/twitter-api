package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.TweetService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetServiceImpl implements TweetService {

    private TweetRepository tweetRepository;
    private TweetMapper tweetMapper;

    private HashtagRepository hashtagRepository;
    private HashtagMapper hashtagMapper;

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return null;
    }
    @Override
    @Transactional
    public ResponseEntity<TweetResponseDto> createTweet(TweetRequestDto tweetRequestDto) {
        //TODO: Check Credentials.

        // Map request to Entity.
        Tweet newTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);

        // Scan text for hashtags, such as #hashtags.
        String content = newTweet.getContent();
        Pattern hashtagFinder = Pattern.compile("#[A-Za-z0-9]+(?:\\b|$)");
        Matcher hashtagMatcher = hashtagFinder.matcher(content);
        // Add all hashtags to a set, which ensures no duplicate hashtags.
        Set<String> allHashtags = new HashSet<>();
        while (hashtagMatcher.find()) {
            allHashtags.add(hashtagMatcher.group());
        }

        // Lists for new and existing hashtags.
        List<Hashtag> newHashtagEntities = new ArrayList<>();
        List<Hashtag> existingHashtags = new ArrayList<>();
        for (String eachHashtag : allHashtags) {
            // Check if they are already represented in the Repository,
            // and put them in one of the two lists.
            Optional<Hashtag> hashtagsDatabaseEntry = hashtagRepository.findByContent(eachHashtag);
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

        // Save the tweet with all relationships set.
        TweetResponseDto response = tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(newTweet));


        //TODO: Handle Mentions
        //Scan text for Mentions, such as @ACoolUsername
        //If Username is attached to an existing User:
            //Link Tweet with User

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<TweetResponseDto> getTweetById(int id) {
        return null;
    }

    @Override
    public ResponseEntity<List<TweetResponseDto>> getRepliesToTweet(int id) {
        return null;
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getUsersMentionedInTweet(int id) {
        return null;
    }

    @Override
    public ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(int id) {
        return null;
    }

    @Override
    public ResponseEntity<TweetResponseDto> repostTweet(int id, CredentialsDto credentialsDto) {
        return null;
    }

    @Override
    public ResponseEntity<ContextDto> getContextOfTweet(int id) {
        return null;
    }

    @Override
    public ResponseEntity<UserResponseDto> getLikesOnTweet(int id) {
        return null;
    }

    @Override
    public ResponseEntity<HashtagDto> getHashtagsOnTweet(int id) {
        return null;
    }

    @Override
    public ResponseEntity<TweetResponseDto> deleteTweet(int id, CredentialsDto credentialsDto) {
        return null;
    }

    @Override
    public void likeTweet(int id, CredentialsDto credentialsDto) {

    }

    @Override
    public ResponseEntity<TweetResponseDto> replyToTweet(int id, TweetRequestDto tweetRequestDto) {
        return null;
    }


}
