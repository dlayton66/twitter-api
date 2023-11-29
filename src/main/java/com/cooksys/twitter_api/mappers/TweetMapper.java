package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TweetMapper {

  Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

  TweetResponseDto entityToResponseDto(Tweet tweet);

}
