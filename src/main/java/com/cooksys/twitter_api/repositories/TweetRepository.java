package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {

}
