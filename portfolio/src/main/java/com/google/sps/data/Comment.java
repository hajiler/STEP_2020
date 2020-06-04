package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public final class Comment {
  private final String author;
  private final String value;
  private final long timeMillis;

  public Comment(String name, String comment, long time) {
    this.author = name;
    this.value = comment;
    this.timeMillis = time;
  }

  public static Comment fromEntity(Entity comment) {
    return new Comment((String) comment.getProperty("name"), (String) comment.getProperty("value"), (Long) comment.getProperty("timeMillis"));
  }
}
