package com.theironyard;
import com.oracle.javafx.jmx.SGMXBean;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    public static void main(String[] args) {
       // ArrayList<Post> posts = new ArrayList();
        HashMap<String, User> users = new HashMap<>();
        User doug = new User("Doug", "1234");
        doug.posts = new ArrayList<>();
        users.put(doug.name, doug);



        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    String password = session.attribute("password");
                    //user = new User(name, password);
                    if (user==null){
                        return new ModelAndView(new HashMap(), "not-logged-in.html");
                    } else {
                        HashMap m = new HashMap();
                        m.put("username", user.name);
                        m.put("posts", user.posts);
                        return new ModelAndView(m, "/logged-in.html");
                    }


                }),
                new MustacheTemplateEngine()
        );//end get



        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("username");
                    String password = request.queryParams("password");
                    if (!users.containsKey(name)){
                        user = new User(name,password);
                        user.posts = new ArrayList<Post>();
                        users.put(user.name, user);
                        Session session = request.session();
                        session.attribute("username", name);
                        response.redirect("/");
                    } else if (password.equals(users.get(name).password)){
                        user = users.get(name);
                        response.redirect("/");
                    } else {
                        response.redirect("/");
                    }

                    return "";
                })
        );//end post
        Spark.post(
                 "/create-post",
                 ((request, response) -> {
                     Post post = new Post();
                     post.id = user.posts.size() + 1; //haha don't have to make a new int above and ++ it
                     post.text = request.queryParams("text");
                     user.posts.add(post);
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
                        user.posts.remove(idNum - 1);
                        for (int i =0; i<user.posts.size(); i++){
                            user.posts.get(i).id = i + 1;
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
                    int idNum = Integer.valueOf(id);
                    try{
                        user.posts.get(idNum-1).text = request.queryParams("edit");
                    }catch (Exception e){
                    }
                    response.redirect("/");
                    return "";
                })
        );
    }
}
