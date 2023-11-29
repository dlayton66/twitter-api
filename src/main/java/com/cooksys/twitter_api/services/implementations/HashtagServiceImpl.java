package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.HashtagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagServiceImpl implements HashtagService {

    private final TweetRepository tweetRepository;

    public HashtagServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<String> getAllHashtags() {
        return null;
    }

    @Override
    public List<TweetResponseDto> getTweetsByHashtag(String label) {
        return null;
    }
}
