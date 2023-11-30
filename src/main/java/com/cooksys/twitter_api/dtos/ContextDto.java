package com.cooksys.twitter_api.dtos;

import com.cooksys.twitter_api.entities.Tweet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {

  public TweetResponseDto target;
  public List<TweetResponseDto> before;
  public List<TweetResponseDto> after;

}
