package com.cooksys.twitter_api.entities;

import com.cooksys.twitter_api.embeddables.Credentials;
import com.cooksys.twitter_api.embeddables.Profile;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@Data
public class User {

  @Id
  @GeneratedValue
  private Integer id;

  @CreationTimestamp
  @Column(nullable = false)
  private Timestamp joined;

  private boolean deleted = false;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "username", column = @Column(nullable = false, unique = true)),
          @AttributeOverride(name = "password", column = @Column(nullable = false))
  })
  private Credentials credentials;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "firstName", column = @Column(name="`firstName`")),
          @AttributeOverride(name = "lastName", column = @Column(name="`lastName`")),
          @AttributeOverride(name = "email", column = @Column(nullable = false))
  })
  private Profile profile;

  @ManyToMany(mappedBy = "likes")
  Set<Tweet> likes  = new HashSet<>();

  @ManyToMany(mappedBy = "mentions")
  Set<Tweet> mentions = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "followers_following",
          joinColumns = @JoinColumn(name = "follower_id"),
          inverseJoinColumns = @JoinColumn(name = "following_id"))
  Set<User> followers = new HashSet<>();

  @ManyToMany(mappedBy = "followers")
  Set<User> following = new HashSet<>();
}
