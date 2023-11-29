package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;

import java.util.List;

public interface UserService {

    List<UserRequestDto> getAllUsers();

    UserRequestDto createUser(CredentialsDto credentials, ProfileDto profile);

    UserRequestDto getUserByUsername(String username);

    UserRequestDto updateUserProfile(String username, CredentialsDto credentials, ProfileDto profile);

    UserRequestDto deleteUser(String username, CredentialsDto credentials);

    void followUser(String username, CredentialsDto credentials);

    void unfollowUser(String username, CredentialsDto credentials);

    List<TweetRequestDto> getUserFeed(String username);

    List<TweetRequestDto> getUserTweets(String username);

    List<TweetRequestDto> getMentions(String username);

    List<UserRequestDto> getFollowers(String username);

    List<UserRequestDto> getFollowing(String username);

}
