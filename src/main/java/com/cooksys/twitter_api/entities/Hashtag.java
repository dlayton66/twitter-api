package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false, unique = true)
  private String label;

  @CreationTimestamp
  @Column(name = "`firstUsed`", nullable = false)
  private Timestamp firstUsed;

  @UpdateTimestamp
  @Column(name = "`lastUsed`", nullable = false)
  private Timestamp lastUsed;

  @ManyToMany(mappedBy = "hashtags")
  Set<Tweet> tweets;

}
