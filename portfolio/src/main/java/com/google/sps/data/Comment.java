package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public final class Comment {
  private final String author;
  private final String value;
  private final String entityKey;
  private final long timeMillis;

  public Comment(String name, String comment, String key, long time) {
    this.author = name;
    this.value = comment;
    this.entityKey = key;
    this.timeMillis = time;
  }

  public static Comment fromEntity(Entity comment) {
    return new Comment((String) comment.getProperty("name"), (String) comment.getProperty("value"), KeyFactory.keyToString(comment.getKey()), (Long) comment.getProperty("timeMillis"));
  }
}
