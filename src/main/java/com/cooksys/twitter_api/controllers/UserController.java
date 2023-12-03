package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Set<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto.getCredentials(), userRequestDto.getProfile());
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/{username}")
    public UserResponseDto updateUserProfile(
            @PathVariable String username,
            @RequestBody UserRequestDto userRequestDto
    ) {
        return userService.updateUserProfile(username, userRequestDto);
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
    public Set<TweetResponseDto> getUserFeed(@PathVariable String username) {
        return userService.getUserFeed(username);
    }

    @GetMapping("/{username}/tweets")
    public Set<TweetResponseDto> getUserTweets(@PathVariable String username) {
        return userService.getUserTweets(username);
    }

    @GetMapping("/{username}/mentions")
    public ResponseEntity<Set<TweetResponseDto>> getMentions(@PathVariable String username) {
        return ResponseEntity.ok(userService.getMentions(username));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<Set<UserResponseDto>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<Set<UserResponseDto>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowing(username));
    }

}
