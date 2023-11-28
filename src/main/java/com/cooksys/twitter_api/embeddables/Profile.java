package com.cooksys.twitter_api.embeddables;

import jakarta.persistence.Embeddable;

@Embeddable
public class Profile {

  private String firstName;
  private String lastName;
  private String email;
  private String phone;

}
