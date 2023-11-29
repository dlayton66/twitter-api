package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.embeddables.Credentials;
import com.cooksys.twitter_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByCredentialsUsername(String username);
}

