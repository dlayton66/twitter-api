package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.embeddables.Profile;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.CredentialsMapper;
import com.cooksys.twitter_api.mappers.ProfileMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;
import com.cooksys.twitter_api.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CredentialsMapper credentialsMapper;
    private final ProfileMapper profileMapper;

    private final ValidateService validationService;

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
        Optional<User> possibleUser = userRepository.findByCredentialsUsernameAndDeletedFalse(desiredUsername);
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
        Optional<User> requestedUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if(requestedUser.isEmpty()){throw new NotFoundException("No one exists with username: " + username);}
        return userMapper.entityToResponseDto( requestedUser.get());
    }

    @Override
    public UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto) {
        if (!validationService.usernameExists(username) || userRequestDto == null) {
            throw new BadRequestException("Please provide a username");
        }

        Optional<User> existingUserOptional = userRepository.findByCredentialsUsername(username);

        if (existingUserOptional.isEmpty()) {
            throw new BadRequestException("No one exists with username: " + username);
        }

        User existingUser = existingUserOptional.get();

        if (existingUser.isDeleted()) {
            throw new NotFoundException("User with username " + username + " is deleted");
        }

        // Update the profile
        Profile profileToUpdate = existingUser.getProfile();
        ProfileDto updatedProfileDto = userRequestDto.getProfile();

        profileToUpdate.setFirstName(updatedProfileDto.getFirstName());
        profileToUpdate.setLastName(updatedProfileDto.getLastName());
        profileToUpdate.setEmail(updatedProfileDto.getEmail());
        profileToUpdate.setPhone(updatedProfileDto.getPhone());

        // Save and return the updated profile
        userRepository.saveAndFlush(existingUser);

        // Construct the response
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUsername(existingUser.getProfile().getFirstName());
        responseDto.setJoined(existingUser.getJoined());
        responseDto.setProfile(new ProfileDto(
                profileToUpdate.getFirstName(),
                profileToUpdate.getLastName(),
                profileToUpdate.getEmail(),
                profileToUpdate.getPhone()
        ));

        return responseDto;
    }

    @Override
    public UserResponseDto deleteUser(String username, CredentialsDto credentials) {
        if(username == null || credentials == null) throw new BadRequestException("Credentials and Profile are required.");

        Optional<User> authenticatedUser = userRepository.findByCredentialsUsername(username);

        Optional<User> existingUser = userRepository.findByCredentialsUsername(username);
        if (existingUser.isEmpty()) {throw new NotFoundException("No user exists with username: " + username);}

        User userToDelete = existingUser.get();

        // Check if the authenticated user has the authority to delete the existing user
        if (!authenticatedUser.equals(existingUser)) {throw new NotAuthorizedException("You are not authorized to delete this user");}

        userToDelete.setDeleted(true);

        User savedUser = userRepository.saveAndFlush(userToDelete);

        return userMapper.entityToResponseDto(savedUser);
    }


    @Override
    public void followUser(String followerUsername, CredentialsDto credentials) {
        if(followerUsername == null || credentials == null) {throw new BadRequestException("Credentials and Profile are required.");}

        // Find the user who is being followed
        Optional<User> userToBeFollowedOptional = userRepository.findByCredentialsUsername(followerUsername);
        if (userToBeFollowedOptional.isEmpty()) {throw new NotFoundException("No user found with username: " + followerUsername);}

        User userToBeFollowed = userToBeFollowedOptional.get();
        // Find the follower
        Optional<User> followerOptional = userRepository.findByCredentialsUsername(credentials.getUsername());
        if (followerOptional.isEmpty()) {throw new NotFoundException("Follower not found with username: " + credentials.getUsername());}

        User follower = followerOptional.get();
        // Add the follower to the user's followers
        userToBeFollowed.getFollowers().add(follower);

        userRepository.save(userToBeFollowed);
    }


    @Override
    public void unfollowUser(String username, CredentialsDto credentials) {
        if(username == null || credentials == null) throw new BadRequestException("Credentials and Profile are required.");

        // Find the user who is being unfollowed
        Optional<User> userToBeUnfollowedOptional = userRepository.findByCredentialsUsername(username);
        if (userToBeUnfollowedOptional.isEmpty()) throw new NotFoundException("No user found with username: " + username);

        User userToBeUnfollowed = userToBeUnfollowedOptional.get();
        // Find the follower
        Optional<User> unFollowerOptional = userRepository.findByCredentialsUsername(credentials.getUsername());
        if (unFollowerOptional.isEmpty()) throw new NotFoundException("Follower not found with username: " + credentials.getUsername());

        User unFollower = unFollowerOptional.get();
        // Remove the follower to the user's followers
        userToBeUnfollowed.getFollowers().remove(unFollower);

        userRepository.save(userToBeUnfollowed);
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
