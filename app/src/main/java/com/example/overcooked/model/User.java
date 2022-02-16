package com.example.overcooked.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    final public static String COLLECTION_NAME = "users";

    String uid;
    String displayName = "";
    String email = "";
    String img;

    public User() {
    }

    public User(String uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("uid", uid);
        json.put("displayName", displayName);
        json.put("email", email);
        json.put("img", img);

        return json;
    }

    public static User create(Map<String, Object> json) {
        String uid = (String) json.get("uid");
        String displayName = (String) json.get("displayName");
        String email = (String) json.get("email");
        String imageUrl = (String) json.get("img");

        User user = new User(uid, displayName, email);
        user.setImg(imageUrl);
        return user;
    }
}
