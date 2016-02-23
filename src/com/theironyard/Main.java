package com.theironyard;
import com.oracle.javafx.jmx.SGMXBean;
import javafx.collections.ObservableList;
import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    //static User user;
    public static void main(String[] args) {
       // ArrayList<Post> posts = new ArrayList();
        HashMap<String, User> users = new HashMap<>();
        //User doug = new User("Doug", "1234");
        User doug = new User();
        doug.name = "Doug";
        doug.password = "1234";
        doug.posts = new ArrayList<Post>();
        Post post = new Post();
        users.put(doug.name, doug);



        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");
                    User user = users.get(name);
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
                       User user = new User();
                        user.name = name;
                        user.password = password;
                        user.posts = new ArrayList<Post>();
                        users.put(user.name, user);
                        Session session = request.session();
                        session.attribute("username", name);
                        response.redirect("/");
                    } else if (password.equals(users.get(name).password)){
                      User user = users.get(name);
                        Session session = request.session();
                        session.attribute("username", name);
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
                     Session session = request.session();
                     String name = session.attribute("username");
                     User user = users.get(name);
                     Post p = new Post();
                     p.id = user.posts.size() + 1; //haha don't have to make a new int above and ++ it
                     p.text = request.queryParams("text");
                     user.posts.add(p);
                     saveContacts(users);
                     response.redirect("/");
                     return "";
                 })
         );//end 2nd post
        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    Session session = request.session();
                    User user = users.get(session.attribute("username"));
                    String id = request.queryParams("postid");
                    try{
                        int idNum = Integer.valueOf(id);
                        user.posts.remove(idNum - 1);
                        for (int i =0; i<user.posts.size(); i++){
                            user.posts.get(i).id = i + 1;
                            saveContacts(users);
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
                    Session session = request.session();
                    User user = users.get(session.attribute("username"));
                    String id = request.queryParams("postid");
                    int idNum = Integer.valueOf(id);
                    try{
                        user.posts.get(idNum-1).text = request.queryParams("edit");
                    }catch (Exception e){
                    }
                    response.redirect("/");
                    saveContacts(users);
                    return "";
                })
        );
    }

    public static void saveContacts(HashMap<String, User> users) {
        File f = new File("posts.json");
        JsonSerializer serializer = new JsonSerializer();  //json serializer
        String contentToSave = serializer.serialize(users);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(contentToSave);
            fw.close();
        } catch (Exception e) {
            System.out.println("Something went wrong with saveContacts()...sorry!");
        }
    }
}
