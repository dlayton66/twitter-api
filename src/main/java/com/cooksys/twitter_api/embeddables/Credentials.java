package com.cooksys.twitter_api.embeddables;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Credentials {

  public String username;
  public String password;

}
