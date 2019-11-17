package com.example.hw7;

import java.util.HashMap;
import java.util.Map;

public class User {
    String userId, firstName, lastName, gender, emailAddress,storagePath;

    public User() {

    }

    public User(String userId,String firstName,String lastName, String emailAddress, String gender, String storagePath) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.gender = gender;
        this.storagePath = storagePath;
    }

    public User (Map userMap) {

        this.userId = (String) userMap.get("userId");
        this.firstName = (String) userMap.get("firstName");
    }
}
