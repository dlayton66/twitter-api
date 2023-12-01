package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.services.HashtagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping
    public Set<HashtagDto> getAllHashtags() {
        return hashtagService.getAllHashtags();
    }

    @GetMapping("/{label}")
    public ResponseEntity<Set<TweetResponseDto>> getTweetsByHashtag(@PathVariable String label) {
        return hashtagService.getTweetsByHashtag(label);
    }

}
