package com.cooksys.twitter_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  private Timestamp posted;

  private boolean deleted = false;

  private String content;

  @ManyToOne
  @JoinColumn(name = "`inReplyTo_id`")
  private Tweet inReplyTo;

  @ManyToOne
  @JoinColumn(name = "`repostOf_id`")
  private Tweet repostOf;

  @ManyToMany
  @JoinTable(
          name = "tweet_hashtags",
          joinColumns = @JoinColumn(name = "tweet_id"),
          inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
  Set<Hashtag> hashtags;

  @ManyToMany
  @JoinTable(
          name = "user_likes",
          joinColumns = @JoinColumn(name = "tweet_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))
  Set<User> likes;

  @ManyToMany
  @JoinTable(
          name = "user_mentions",
          joinColumns = @JoinColumn(name = "tweet_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))
  Set<User> mentions;

}
