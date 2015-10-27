package com.theironyard;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        //User user = new User();
        ArrayList<Post> posts = new ArrayList();
       //Spark.staticFileLocation("/public");
        //Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    if (name == null){
                        return new ModelAndView(new HashMap(), "not-logged-in.html");
                    }
                    HashMap m = new HashMap();
                    m.put("username", name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "/logged-in.html");
                }),
                new MustacheTemplateEngine()
        );//end get
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("username");
                    Session session = request.session();
                    session.attribute("username", name);
                    response.redirect("/");
                    return "";
                })
        );//end post
        Spark.post(
                 "/create-post",
                 ((request, response) -> {
                     Post post = new Post();
                     post.id = posts.size() + 1; //haha don't have to make a new int above and ++ it
                     post.text = request.queryParams("text");
                     posts.add(post);
                     response.redirect("/");
                     return "";
                 })
         );//end 2nd post
        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try{
                        int idNum = Integer.valueOf(id);
                        posts.remove(idNum - 1);
                        for (int i =0; i<posts.size(); i++){
                            posts.get(i).id = i + 1;
                        }
                    }
                    catch (Exception e){
                    }
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try{
                        int idNum = Integer.valueOf(id);
                        posts.get(idNum-1).text = request.queryParams("edit");
                        for (int i = 0; i<posts.size(); i++){
                            posts.get(i).id = i+1;
                        }
                    }catch (Exception e){

                    }
                    response.redirect("/");
                    return "";
                })
        );
    }
}
