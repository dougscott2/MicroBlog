package com.theironyard;

import java.util.ArrayList;

/**
 * Created by DrScott on 2/22/16.
 */
public class User {
    String name;
    String password;
    ArrayList<Post> posts;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }


}
