package com.example.overcooked.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class User implements Serializable {
    final public static String COLLECTION_NAME = "users";

    @PrimaryKey
    @NonNull
    String uid;
    String displayName = "";
    String email = "";
    String img;
    Long updateDate = new Long(0);

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

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        FieldValue timestamp = FieldValue.serverTimestamp();
        json.put("uid", uid);
        json.put("displayName", displayName);
        json.put("email", email);
        json.put("img", img);
        json.put("updateDate", timestamp);

        return json;
    }

    public static User create(Map<String, Object> json) {
        String uid = (String) json.get("uid");
        String displayName = (String) json.get("displayName");
        String email = (String) json.get("email");
        String imageUrl = (String) json.get("img");
        Timestamp updateTs = (Timestamp)json.get("updateDate");
        Long updateDate = updateTs.getSeconds();

        User user = new User(uid, displayName, email);
        user.setUpdateDate(updateDate);
        user.setImg(imageUrl);
        return user;
    }

}
