package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Tweet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByInReplyToAndDeletedFalse(Tweet parentTweet, Sort sort);
    List<Tweet> getByDeletedFalse(Sort sort);
    List<Tweet> findByHashtags_LabelAndDeletedFalse(String hashtagLabel, Sort sort);
    Optional<Tweet> findByIdAndDeletedFalse(Long id);

}
