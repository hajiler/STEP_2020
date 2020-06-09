// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/** Servlet that returns comments*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer maxComments = Integer.valueOf(request.getParameter("maxComments"));
    Gson gson = new Gson();
    
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(getDatastoreComments(maxComments)));
  }

  private final Map<String, List<Entity>> getDatastoreComments(int maxComments) {
    Map<String, List<Entity>> commentsByName = new HashMap<>();
    Query query = new Query("Comment").addSort("timeMillis", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    datastore.prepare(query).asList(FetchOptions.Builder.withLimit(maxComments)).forEach((entity)-> {
      String name = (String) entity.getProperty("author");
      if(!commentsByName.containsKey(name)) {
        commentsByName.put(name, new ArrayList<Entity>());
      }
      commentsByName.get(name).add(entity);
    });

    return commentsByName;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //prevents adding empty comments to datastore
    if (!request.getParameter("Comment:").isEmpty()) {
      DatastoreServiceFactory.getDatastoreService().put(getCommentEntityFrom(request));
    }
    response.sendRedirect("/comments.html");
  }

  private final Entity getCommentEntityFrom(HttpServletRequest request) {
    Entity commentEntity = new Entity("Comment");
    String comment = request.getParameter("Comment:");

    commentEntity.setProperty("value", comment);
    commentEntity.setProperty("timeMillis", System.currentTimeMillis());
    commentEntity.setProperty("author", request.getParameter("Name:"));
    try {
      commentEntity.setProperty("sentiment", getSentimentScoreFrom(comment));
    } catch (IOException e) {
      commentEntity.setProperty("sentiment", null);
    }
    return commentEntity;
  }

  private final Integer getSentimentScoreFrom(String comment) throws IOException{
    Document doc =
        Document.newBuilder().setContent(comment).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    float score = languageService.analyzeSentiment(doc).getDocumentSentiment().getScore();
    languageService.close();
    return Math.round(score);
  }
}
