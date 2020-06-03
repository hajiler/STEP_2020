package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public final class Comment {
  private final String value;
  private final long timeMillis;

  public Comment(String comment, long time){
    this.value = comment;
    this.timeMillis = time;
  }
  static Comment entityToComment(Entity comment) {
    return new Comment((String) comment.getProperty("value"),(Long) comment.getProperty("timeMillis"));
  }
}