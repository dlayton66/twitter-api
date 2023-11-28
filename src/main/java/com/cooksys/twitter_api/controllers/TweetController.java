package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private TweetService tweetService;

    @GetMapping
    List<TweetResponseDto> getAllTweets(){
        return tweetService.getAllTweets();
    }



}
