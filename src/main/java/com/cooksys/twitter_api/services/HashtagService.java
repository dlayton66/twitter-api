package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HashtagService {

    List<HashtagDto> getAllHashtags();

    ResponseEntity<List<TweetResponseDto>> getTweetsByHashtag(String label);
}