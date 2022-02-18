package com.example.overcooked.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Post implements Serializable {
    final public static String COLLECTION_NAME = "posts";
    @PrimaryKey
    @NonNull
    String id;
    String title = "";
    String description = "";
    String author = "";
    String content = "";
    String img;
    Long updateDate = new Long(0);
    Long uploadDate = new Long(0);

    public Post() { }

    public Post(String id, String title, String description, String author, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }


    public void setUploadDate(Long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getUploadDate() {
        return uploadDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        FieldValue timestamp = FieldValue.serverTimestamp();
        json.put("id", id);
        json.put("title", title);
        json.put("description", description);
        json.put("author", author);
        json.put("content", content);
        json.put("updateDate", timestamp);
        json.put("uploadDate", timestamp);
        json.put("image", img);

        return json;
    }

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String title = (String) json.get("title");
        String desc = (String) json.get("description");
        String content = (String) json.get("content");
        String author = (String) json.get("author");
        Timestamp uploadTs = (Timestamp)json.get("uploadDate");
        Timestamp updateTs = (Timestamp)json.get("updateDate");
        Long uploadDate = uploadTs.getSeconds();
        Long updateDate = updateTs.getSeconds();
        String img = (String)json.get("image");

        Post post = new Post(id, title, desc, author, content);
        post.setUpdateDate(updateDate);
        post.setUploadDate(uploadDate);
        post.setImg(img);
        return post;
    }
}
