package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.ProfileMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CredentialsMapper credentialsMapper;
    private final ProfileMapper profileMapper;



    @Override
    public Set<UserResponseDto> getAllUsers() {
        Optional<Set<User>> nondeletedUsers = userRepository.getUsersByDeletedIsFalse();
        if(nondeletedUsers.isPresent()) {return userMapper.entitiesToResponseDtos(nondeletedUsers.get());}
        throw new NotFoundException("Error finding all (non-deleted) users.");
    }

    @Override
    public UserResponseDto createUser(CredentialsDto credentials, ProfileDto profile) {
        if(credentials == null || profile == null){throw new BadRequestException("Credentials and Profile are required.");}
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
        if(requestedUser.isEmpty()){throw new NotFoundException("No one exists with username: " + username);}
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
    public Set<TweetResponseDto> getUserFeed(String username) {
        return null;
    }

    @Override
    public Set<TweetResponseDto> getUserTweets(String username) {
        return null;
    }

    @Override
    public Set<TweetResponseDto> getMentions(String username) {
        return null;
    }

    @Override
    public Set<UserResponseDto> getFollowers(String username) {
        return null;
    }

    @Override
    public Set<UserResponseDto> getFollowing(String username) {
        return null;
    }

}
