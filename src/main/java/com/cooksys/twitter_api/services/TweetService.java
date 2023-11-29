package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TweetService {

    List<TweetResponseDto> getAllTweets();

    ResponseEntity<TweetResponseDto> createTweet(TweetRequestDto tweetRequestDto);

    ResponseEntity<TweetResponseDto> getTweetById(int id);

    ResponseEntity<List<TweetResponseDto>> getRepliesToTweet(int id);

    ResponseEntity<List<UserResponseDto>> getUsersMentionedInTweet(int id);

    ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(int id);

    ResponseEntity<TweetResponseDto> repostTweet(int id, CredentialsDto credentialsDto);

    ResponseEntity<ContextDto> getContextOfTweet(int id);

    ResponseEntity<UserResponseDto> getLikesOnTweet(int id);

    ResponseEntity<HashtagDto> getHashtagsOnTweet(int id);


    ResponseEntity<TweetResponseDto> deleteTweet(int id, CredentialsDto credentialsDto);

    void likeTweet(int id, CredentialsDto credentialsDto);

    ResponseEntity<TweetResponseDto> replyToTweet(int id, TweetRequestDto tweetRequestDto);
}
