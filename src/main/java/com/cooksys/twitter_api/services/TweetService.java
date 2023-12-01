package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface TweetService {

    List<TweetResponseDto> getAllTweets();

    ResponseEntity<TweetResponseDto> createTweet(TweetRequestDto tweetRequestDto);

    ResponseEntity<TweetResponseDto> getTweetById(Long id);

    List<TweetResponseDto> getRepliesToTweet(Long id);

    Set<UserResponseDto> getUsersMentionedInTweet(Long id);

    ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(Long id);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

    ResponseEntity<ContextDto> getContextOfTweet(Long id);

    ResponseEntity<UserResponseDto> getLikesOnTweet(Long id);

    ResponseEntity<HashtagDto> getHashtagsOnTweet(Long id);

    ResponseEntity<TweetResponseDto> deleteTweet(Long id, CredentialsDto credentialsDto);

    void likeTweet(Long id, CredentialsDto credentialsDto);

    TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

}
