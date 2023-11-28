package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;

public interface UserMapper {

  User requestDtoToEntity(UserRequestDto userRequestDto);

  UserResponseDto entityToResponseDto(User user);

}
