package com.example.overcooked.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    final public static String COLLECTION_NAME = "users";

    String uid;
    String displayName = "";
    String img;

    public User() { }

    public User(String uid, String displayName) {
        this.uid = uid;
        this.displayName = displayName;
    }

    public User(String uid, String displayName, String img) {
        this.uid = uid;
        this.displayName = displayName;
        this.img = img;
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
        json.put("img", img);

        return json;
    }

    public static User create(Map<String, Object> json) {
        String uid = (String) json.get("uid");
        String displayName = (String) json.get("displayName");
        String imageUrl = (String) json.get("img");

        User user = new User(uid, displayName);
        user.setImg(imageUrl);
        return user;
    }
}
