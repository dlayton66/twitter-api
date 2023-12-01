package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.*;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto createUser(CredentialsDto credentials, ProfileDto profile);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto updateUserProfile(String username, CredentialsDto credentials, ProfileDto profile);

    UserResponseDto deleteUser(String username, CredentialsDto credentials);

    void followUser(String username, CredentialsDto credentials);

    void unfollowUser(String username, CredentialsDto credentials);

    List<TweetResponseDto> getUserFeed(String username);

    List<TweetResponseDto> getUserTweets(String username);

    List<TweetResponseDto> getMentions(String username);

    List<UserResponseDto> getFollowers(String username);

    List<UserResponseDto> getFollowing(String username);

}
