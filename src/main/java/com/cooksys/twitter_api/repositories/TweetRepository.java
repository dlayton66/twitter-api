package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Tweet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    List<Tweet> findByInReplyTo(Tweet parentTweet, Sort sort);
    List<Tweet> getByDeletedFalse(Sort sort);

}
