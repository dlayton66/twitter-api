package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    @Override
    public List<UserRequestDto> getAllUsers() {

        return null;
    }

    @Override
    public UserRequestDto createUser(CredentialsDto credentials, ProfileDto profile) {

        return null;
    }

    @Override
    public UserRequestDto getUserByUsername(String username) {

        return null;
    }

    @Override
    public UserRequestDto updateUserProfile(String username, CredentialsDto credentials, ProfileDto profile) {

        return null;
    }

    @Override
    public UserRequestDto deleteUser(String username, CredentialsDto credentials) {

        return null;
    }

    @Override
    public void followUser(String username, CredentialsDto credentials) {

    }

    @Override
    public void unfollowUser(String username, CredentialsDto credentials) {

    }

    @Override
    public List<TweetRequestDto> getUserFeed(String username) {
        return null;
    }

    @Override
    public List<TweetRequestDto> getUserTweets(String username) {
        return null;
    }

    @Override
    public List<TweetRequestDto> getMentions(String username) {
        return null;
    }

    @Override
    public List<UserRequestDto> getFollowers(String username) {
        return null;
    }

    @Override
    public List<UserRequestDto> getFollowing(String username) {
        return null;
    }

}
