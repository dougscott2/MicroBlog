package com.theironyard;

import spark.Spark;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
        ArrayList<User> users = new ArrayList();


        Spark.staticFileLocation("/public");

        /*Spark.get(
                "/posts",
                ((request, response) -> {


                })

        );//end get*/
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    User user = new User();
                    user.name = request.queryParams("username");
                    users.add(user);
                    response.redirect("/posts");
                    return "Created new user!";
                })
        );//end post
        Spark.post(
                "/create-post",
                ((request, response) -> {


                })
        );//end 2nd post
    }
}
