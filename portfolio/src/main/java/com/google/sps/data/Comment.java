package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public final class Comment {
  private final Object author;
  private final Object value;
  private final Object timeMillis;

  public Comment(Object name, Object comment, Object time) {
    this.author = name;
    this.value = comment;
    this.timeMillis = time;
  }

  public static Comment fromEntity(Entity comment) {
    return new Comment(comment.getProperty("name"), comment.getProperty("value"), comment.getProperty("timeMillis"));
  }
}
