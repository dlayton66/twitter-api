package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

  Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

  TweetResponseDto entityToResponseDto(Tweet tweet);

  List<Tweet> requestDtosToEntities(List<TweetRequestDto> tweetRequestDtos);

  List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> tweets);

}
