package com.cooksys.twitter_api.dtos;

import com.cooksys.twitter_api.entities.Tweet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class ContextDto {

  public TweetResponseDto target;
  public Set<TweetResponseDto> before;
  public Set<TweetResponseDto> after;

}
