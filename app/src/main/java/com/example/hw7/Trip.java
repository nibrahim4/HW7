package com.example.hw7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip {

    public String _userId, _tripId, _title, _description;
    public ArrayList<User> _friends = new ArrayList<>();

    public Trip(String userId, String tripId, String title, String description, ArrayList<User> friends) {
        this._userId = userId;
        this._tripId = tripId;
        this._title = title;
        this._description = description;
        this._friends = friends;
    }

    public Trip (Map tripMap) {

        this._userId = (String) tripMap.get("_userId");
        this._tripId = (String) tripMap.get("_tripId");
        this._title = (String) tripMap.get("_title");
        this._description = (String) tripMap.get("_description");
        this._friends = (ArrayList<User>) tripMap.get("_friends");
    }

    public String getUserId() {
        return _userId;
    }

    public String getTripId() {
        return _tripId;
    }

    public void setUserId(String userId) {
        this._userId = userId;
    }

    public void setTripId(String tripId) {
        this._tripId = tripId;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public void setFriends(ArrayList<User> friends) {
        this._friends = friends;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public ArrayList<User> getFriends() {
        return _friends;
    }
}
