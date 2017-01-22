package com.faster.comm.communicate;

/**
 * Created by Aniruddha 1 on 30-12-2016.
 */

public class User {
    String Name, semester, email, userType, stream;

    public User(String name, String semester, String email, String userType, String stream) {
        Name = name;
        this.semester = semester;
        this.email = email;
        this.userType = userType;
        this.stream = stream;
    }
}
