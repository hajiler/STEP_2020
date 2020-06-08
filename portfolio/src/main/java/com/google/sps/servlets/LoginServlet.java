package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    response.setContentType("text/html");
    
    if (userService.isUserLoggedIn()) {
      String logoutLink = "<p>Logout <a href=\"".concat(userService.createLogoutURL("/login")).concat("\"> here</a>.</p>"); 
      response.getWriter().println(logoutLink);
    } else {
      String loginLink = "<p>Login <a href=\"".concat(userService.createLoginURL("/login")).concat("\"> here</a>.</p>"); 
      response.getWriter().println(loginLink);
    }
  }
  
}