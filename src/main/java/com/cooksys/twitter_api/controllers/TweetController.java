package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;


    @GetMapping
    public List<TweetResponseDto> getAllTweets(){
        return tweetService.getAllTweets();
    }

    @PostMapping
    public ResponseEntity<TweetResponseDto> createTweet(@RequestBody TweetRequestDto tweetRequestDto){
        return tweetService.createTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseDto> getTweetById(@PathVariable int id){
        return tweetService.getTweetById(id);
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<TweetResponseDto>> getRepliesToTweet(@PathVariable int id){
        return tweetService.getRepliesToTweet(id);
    }

    @GetMapping("/{id}/mentions")
    public ResponseEntity<List<UserResponseDto>> getUsersMentionedInTweet(@PathVariable int id){
        return tweetService.getUsersMentionedInTweet(id);
    }

    @GetMapping("/{id}/reposts")
    public ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(@PathVariable int id){
        return tweetService.getRepostsOfTweet(id);
    }

    @PostMapping("/{id}/repost")
    public ResponseEntity<TweetResponseDto> repostTweet(@PathVariable int id, @RequestBody CredentialsDto credentialsDto){
        return tweetService.repostTweet(id, credentialsDto);
    }

    @GetMapping("/{id}/context")
    public ResponseEntity<ContextDto> getContextOfTweet(@PathVariable int id){
        return tweetService.getContextOfTweet(id);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<UserResponseDto> getLikesOnTweet(@PathVariable int id){
        return tweetService.getLikesOnTweet(id);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<HashtagDto> getHashtagsOnTweet(@PathVariable int id){
        return tweetService.getHashtagsOnTweet(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TweetResponseDto> deleteTweet(@PathVariable int id, @RequestBody CredentialsDto credentialsDto){
        return tweetService.deleteTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/like")
    public void likeTweet(@PathVariable int id, @RequestBody CredentialsDto credentialsDto){
        tweetService.likeTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<TweetResponseDto> replyToTweet(@PathVariable int id, @RequestBody TweetRequestDto tweetRequestDto){
        return tweetService.replyToTweet(id, tweetRequestDto);
    }
}
