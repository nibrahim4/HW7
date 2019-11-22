package com.example.hw7;

import java.util.ArrayList;

public class Trip {

    public String userId, tripId, title, description;
    public ArrayList<User> friends = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }
}
