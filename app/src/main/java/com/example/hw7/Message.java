package com.example.hw7;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;

public class Message {

    public String email, date, message, imageUrl, messageId, tripId;

    public Message() {
    }

    public Message (Map tripMap) {

        this.email = (String) tripMap.get("email");
        this.date = (String) tripMap.get("date");
        this.message = (String) tripMap.get("message");
        this.imageUrl = (String) tripMap.get("imageUrl");
        this.messageId = (String) tripMap.get("messageId");
        this.tripId = (String) tripMap.get("tripId");
    }

    @Override
    public String toString() {
        return "Message{" +
                "email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
