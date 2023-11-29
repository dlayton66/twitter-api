package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

  Hashtag dtoToEntity(HashtagDto hashtagDto);

  HashtagDto entityToDto(Hashtag hashtag);

}
