package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

  @Id
  @GeneratedValue
  private Long id;

  private String label;

  @Column(name = "`firstUsed`")
  private Timestamp firstUsed;

  @Column(name = "`lastUsed`")
  private Timestamp lastUsed;

  @ManyToMany(mappedBy = "hashtags")
  Set<Tweet> tweets;

}
