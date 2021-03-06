package com.google.sps.data;

import com.google.appengine.api.users.UserService;

public final class LoginInfo {
  private final Boolean loginStatus;
  private final String linkToAction;

  LoginInfo(Boolean loggedIn, String relevantLink) {
    this.loginStatus = loggedIn;
    this.linkToAction = relevantLink;
  }

  public static LoginInfo getInfoFrom(UserService user) {
    if (user.isUserLoggedIn()) {
      return new LoginInfo(true, user.createLogoutURL("/comments.html"));
    } else {
      return new LoginInfo(false, user.createLoginURL("/comments.html"));
    }
  }
}
