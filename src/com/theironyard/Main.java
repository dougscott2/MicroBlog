package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //ArrayList<User> users = new ArrayList();
        //int place;
        //int x = 0;
        User user = new User();
        //Post post = new Post();
        ArrayList<Post> posts = new ArrayList();
        Spark.staticFileLocation("/public");
        Spark.post(
                "/create-account",
                ((request, response) -> {
                    user.name = request.queryParams("username");
                    response.redirect("/posts");
                    return "";
                })
        );//end post
         Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.text = request.queryParams("posts");
                    posts.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );//end 2nd post
        Spark.get(
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("username", user.name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()

        );//end get
        System.out.println("");
    }
}
