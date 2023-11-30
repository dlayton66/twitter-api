package com.cooksys.twitter_api.utils;

import com.cooksys.twitter_api.entities.User;

import java.util.Iterator;
import java.util.Set;

public class UserUtils {

  public static Set<User> filterDeletedUsers(Set<User> users) {
    Iterator<User> iterator = users.iterator();

    while (iterator.hasNext()) {
      if (iterator.next().isDeleted()) {
        iterator.remove();
      }
    }

    return users;
  }
}
