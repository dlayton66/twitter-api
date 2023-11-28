package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
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
import java.util.Set;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@Data
public class User {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @CreationTimestamp
  @Column(nullable = false)
  private Timestamp joined;

  private boolean deleted = false;

  @Column(name="`firstName`")
  private String firstName;

  @Column(name="`lastName`")
  private String lastName;

  @Column(nullable = false)
  private String email;

  private String phone;

  @ManyToMany(mappedBy = "likes")
  Set<Tweet> likes;

  @ManyToMany(mappedBy = "mentions")
  Set<Tweet> mentions;

  @ManyToMany
  @JoinTable(
          name = "followers_following",
          joinColumns = @JoinColumn(name = "follower_id"),
          inverseJoinColumns = @JoinColumn(name = "following_id"))
  Set<User> followers;

  @ManyToMany(mappedBy = "followers")
  Set<User> following;
}
