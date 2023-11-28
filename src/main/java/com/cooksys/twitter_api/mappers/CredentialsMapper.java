package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.embeddables.Credentials;

public interface CredentialsMapper {

  Credentials dtoToEntity(CredentialsDto credentialsDto);

  CredentialsDto entityToDto(Credentials credentials);

}
