package com.example.hw7;

import java.util.HashMap;
import java.util.Map;

public class User {
    String userId, firstName, lastName, gender, emailAddress,storagePath,url;

    public User() {

    }

    public User(String userId,String firstName,String lastName, String emailAddress, String gender, String storagePath, String url) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.gender = gender;
        this.storagePath = storagePath;
        this.url = url;
    }

    public User (Map userMap) {

        this.userId = (String) userMap.get("userId");
        this.firstName = (String) userMap.get("firstName");
        this.lastName = (String) userMap.get("lastName");
        this.gender = (String) userMap.get("gender");
        this.emailAddress = (String) userMap.get("email");
        this.url = (String) userMap.get("url");
    }


}
