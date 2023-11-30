package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserRequestDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserRequestDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto.getCredentials(), userRequestDto.getProfile());
    }

    @GetMapping("/{username}")
    public UserRequestDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/{username}")
    public UserRequestDto updateUserProfile(
            @PathVariable String username,
            @RequestBody UserRequestDto userRequestDto
    ) {
        return userService.updateUserProfile(username, userRequestDto.getCredentials(), userRequestDto.getProfile());
    }

    @DeleteMapping("/{username}")
    public UserRequestDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
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
    public List<TweetRequestDto> getUserFeed(@PathVariable String username) {
        return userService.getUserFeed(username);
    }

    @GetMapping("/{username}/tweets")
    public List<TweetRequestDto> getUserTweets(@PathVariable String username) {
        return userService.getUserTweets(username);
    }

    @GetMapping("/{username}/mentions")
    public ResponseEntity<List<TweetRequestDto>> getMentions(@PathVariable String username) {
        return ResponseEntity.ok(userService.getMentions(username));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserRequestDto>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserRequestDto>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowing(username));
    }

}
