package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.ProfileDto;
import com.cooksys.twitter_api.embeddables.Profile;

public interface ProfileMapper {

  Profile dtoToEntity(ProfileDto profileDto);

  ProfileDto entityToDto(Profile profile);

}