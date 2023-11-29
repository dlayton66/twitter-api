package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.embeddables.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

  Profile dtoToEntity(ProfileDto profileDto);

  ProfileDto entityToDto(Profile profile);

}