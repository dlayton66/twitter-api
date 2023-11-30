package com.cooksys.twitter_api.embeddables;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Profile {

  private String firstName;
  private String lastName;
  private String email;
  private String phone;

}
