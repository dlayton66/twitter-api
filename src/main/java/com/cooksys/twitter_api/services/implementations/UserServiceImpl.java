package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.ProfileMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CredentialsMapper credentialsMapper;
    private final ProfileMapper profileMapper;



    @Override
    public List<UserResponseDto> getAllUsers() {
        Optional<List<User>> nondeletedUsers = userRepository.getUsersByDeletedIsFalse();
        if(nondeletedUsers.isPresent()) {return userMapper.entitiesToResponseDtos(nondeletedUsers.get());}
        throw new NotFoundException("Error finding all (non-deleted) users.");
    }

    @Override
    public UserResponseDto createUser(CredentialsDto credentials, ProfileDto profile) {
        String desiredUsername = credentials.getUsername();
        String password = credentials.getPassword();
        String email = profile.getEmail();
        if(desiredUsername == null || password == null || email == null){
            throw new BadRequestException("Username, password, and email are required.");
        }
        Optional<User> possibleUser = userRepository.findByCredentialsUsername(desiredUsername);
        if(possibleUser.isEmpty()){
            User newUser = new User();
            newUser.setCredentials(credentialsMapper.dtoToEntity(credentials));
            newUser.setProfile(profileMapper.dtoToEntity(profile));
            User savedUser = userRepository.saveAndFlush(newUser);
            return userMapper.entityToResponseDto(savedUser);
        }
        //By this point we know user exists
        if(possibleUser.get().isDeleted()){
            possibleUser.get().setDeleted(false);
            User savedUser = userRepository.saveAndFlush(possibleUser.get());
            return userMapper.entityToResponseDto(savedUser);
        }
        //User exists and is not deleted.
        throw new BadRequestException("Username already taken.");
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        Optional<User> requestedUser = userRepository.findByCredentialsUsername(username);
        if(requestedUser.isEmpty()){throw new NotFoundException("No one exists with that username.");}
        return userMapper.entityToResponseDto( requestedUser.get());
    }

    @Override
    public UserResponseDto updateUserProfile(String username, CredentialsDto credentials, ProfileDto profile) {

        return null;
    }

    @Override
    public UserResponseDto deleteUser(String username, CredentialsDto credentials) {

        return null;
    }

    @Override
    public void followUser(String username, CredentialsDto credentials) {

    }

    @Override
    public void unfollowUser(String username, CredentialsDto credentials) {

    }

    @Override
    public List<TweetResponseDto> getUserFeed(String username) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getUserTweets(String username) {
        return null;
    }

    @Override
    public List<TweetResponseDto> getMentions(String username) {
        return null;
    }

    @Override
    public List<UserResponseDto> getFollowers(String username) {
        return null;
    }

    @Override
    public List<UserResponseDto> getFollowing(String username) {
        return null;
    }

}
