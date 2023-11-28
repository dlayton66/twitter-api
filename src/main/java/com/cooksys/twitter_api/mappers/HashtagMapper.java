package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;

public interface HashtagMapper {

  Hashtag dtoToEntity(HashtagDto hashtagDto);

  HashtagDto entityToDto(Hashtag hashtag);

}
