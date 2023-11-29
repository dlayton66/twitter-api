package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
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
    public ResponseEntity<UserRequestDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        UserRequestDto createdUser = userService.createUser(userRequestDto.getCredentials(), userRequestDto.getProfile());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserRequestDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PatchMapping("/{username}")
    public ResponseEntity<UserRequestDto> updateUserProfile(
            @PathVariable String username,
            @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity.ok(userService.updateUserProfile(username, userRequestDto.getCredentials(), userRequestDto.getProfile()));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UserRequestDto> deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(userService.deleteUser(username, credentials));
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<Void> followUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        userService.followUser(username, credentials);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{username}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        userService.unfollowUser(username, credentials);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/feed")
    public ResponseEntity<List<TweetRequestDto>> getUserFeed(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserFeed(username));
    }

    @GetMapping("/{username}/tweets")
    public ResponseEntity<List<TweetRequestDto>> getUserTweets(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserTweets(username));
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
