package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {

  @Mapping(target = "username", source = "credentials.username")
  UserResponseDto entityToResponseDto(User user);

  User requestDtoToEntity(UserRequestDto userRequestDto);

  List<UserResponseDto> entitiesToResponseDtos(List<User> users);
}
