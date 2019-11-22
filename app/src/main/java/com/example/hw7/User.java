package com.example.hw7;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    String userId, firstName, lastName, gender, emailAddress,storagePath,url;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGender() {
        return gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getUrl() {
        return url;
    }

    public User(String userId, String firstName, String lastName, String emailAddress, String gender, String storagePath, String url) {
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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
