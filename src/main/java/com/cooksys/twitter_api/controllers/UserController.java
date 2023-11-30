package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto.getCredentials(), userRequestDto.getProfile());
    }

    @GetMapping("/{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/{username}")
    public UserResponseDto updateUserProfile(
            @PathVariable String username,
            @RequestBody UserRequestDto userRequestDto
    ) {
        return userService.updateUserProfile(username, userRequestDto.getCredentials(), userRequestDto.getProfile());
    }

    @DeleteMapping("/{username}")
    public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        return userService.deleteUser(username, credentials);
    }

    @PostMapping("/{username}/follow")
    public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        userService.followUser(username, credentials);
    }

    @PostMapping("/{username}/unfollow")
    public void unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        userService.unfollowUser(username, credentials);
    }

    @GetMapping("/{username}/feed")
    public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
        return userService.getUserFeed(username);
    }

    @GetMapping("/{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
        return userService.getUserTweets(username);
    }

    @GetMapping("/{username}/mentions")
    public ResponseEntity<List<TweetResponseDto>> getMentions(@PathVariable String username) {
        return ResponseEntity.ok(userService.getMentions(username));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserResponseDto>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserResponseDto>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowing(username));
    }

}
