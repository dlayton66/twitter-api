package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.services.HashtagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class HashtagController {

    private final HashtagService hashtagService;

    public HashtagController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllHashtags() {
        return ResponseEntity.ok(hashtagService.getAllHashtags());
    }

    @GetMapping("/{label}")
    public ResponseEntity<List<TweetResponseDto>> getTweetsByHashtag(@PathVariable String label) {
        return ResponseEntity.ok(hashtagService.getTweetsByHashtag(label));
    }

}
