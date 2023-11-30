package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.controllers.HashtagController;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;


    @Override
    public List<HashtagDto> getAllHashtags() {
        return hashtagMapper.entitiesToDto(hashtagRepository.findAll());
    }

    @Override
    public List<TweetResponseDto> getTweetsByHashtag(String label) {
        return null;
    }
}
