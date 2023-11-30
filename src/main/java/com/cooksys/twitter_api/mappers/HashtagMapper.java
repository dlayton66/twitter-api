package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

  Hashtag dtoToEntity(HashtagDto hashtagDto);

  HashtagDto entityToDto(Hashtag hashtag);

  List<HashtagDto> entitiesToDto(List<Hashtag> hashtags);

  List<Hashtag> DtoToEntities(List<HashtagDto> hashtagDtos);

}
