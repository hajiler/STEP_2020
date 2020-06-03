package com.google.sps.data;

public final class Comment {
  private final String value;
  private final long timeStamp;

  public Comment(String comment, long time){
    this.value = comment;
    this.timeStamp = time;
  }
}