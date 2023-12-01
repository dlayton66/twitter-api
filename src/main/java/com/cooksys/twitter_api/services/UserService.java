package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.*;

import java.util.Set;

public interface UserService {

    Set<UserResponseDto> getAllUsers();

    UserResponseDto createUser(CredentialsDto credentials, ProfileDto profile);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto updateUserProfile(String username, CredentialsDto credentials, ProfileDto profile);

    UserResponseDto deleteUser(String username, CredentialsDto credentials);

    void followUser(String username, CredentialsDto credentials);

    void unfollowUser(String username, CredentialsDto credentials);

    Set<TweetResponseDto> getUserFeed(String username);

    Set<TweetResponseDto> getUserTweets(String username);

    Set<TweetResponseDto> getMentions(String username);

    Set<UserResponseDto> getFollowers(String username);

    Set<UserResponseDto> getFollowing(String username);

}
