package com.google.sps.data;

public final class Comment {
  private final String value;
  private final long timeMillis;

  public Comment(Object comment, Object time){
    this.value = (String) comment;
    this.timeMillis = (Long) time;
  }
}