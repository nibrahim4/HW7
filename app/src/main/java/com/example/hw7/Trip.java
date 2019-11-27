package com.example.hw7;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip implements Serializable {

    public String _userId, _tripId, _title, _description, _city, _url;
    public String _date;
    public long _latitude, _longitude;
    public ArrayList<User> _friends = new ArrayList<>();

    public Trip(){


    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public Trip(String userId,
                String tripId,
                String title,
                String description,
                ArrayList<User> friends,
                String date,
                String city,
                long latitude,
                long longitude,
                String url) {
        this._userId = userId;
        this._tripId = tripId;
        this._title = title;
        this._description = description;
        this._friends = friends;
        this._date = date;
        this._city = city;
        this._latitude = latitude;
        this._longitude = longitude;
        this._url = url;
    }

    public String get_url() {
        return _url;
    }

    public Trip (Map tripMap) {

        this._userId = (String) tripMap.get("_userId");
        this._tripId = (String) tripMap.get("_tripId");
        this._title = (String) tripMap.get("_title");
        this._description = (String) tripMap.get("_description");
        this._friends = (ArrayList<User>) tripMap.get("_friends");
        this._date = (String) tripMap.get("_date");
        this._city = (String) tripMap.get("_city");
        this._latitude = (long) tripMap.get("_latitude");
        this._longitude = (long) tripMap.get("_longitude");
        this._url = (String) tripMap.get("_url");
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

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
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
