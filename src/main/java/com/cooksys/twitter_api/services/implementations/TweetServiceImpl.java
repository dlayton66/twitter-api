package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.TweetService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TweetServiceImpl implements TweetService {

    private TweetRepository tweetRepository;

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return null;
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        return null;
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
