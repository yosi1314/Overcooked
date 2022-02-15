package com.example.overcooked.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    final public static String COLLECTION_NAME = "posts";
    @PrimaryKey
    @NonNull
    String id;
    String title = "";
    String description = "";
    String author = "";
    String content = "";
    int img;
//    Long updateDate = new Long(0);


    public Post() { }

    public Post(String id, String title, String description, String author, int img, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.img = img;
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public Long getUpdateDate() {
//        return updateDate;
//    }

//    public void setUpdateDate(Long updateDate) {
//        this.updateDate = updateDate;
//    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", id);
        json.put("title", title);
        json.put("description", description);
        json.put("author", author);
        json.put("content", content);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("image", img);

        return json;
    }

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String title = (String) json.get("title");
        String desc = (String) json.get("description");
        String content = (String) json.get("content");
        String author = (String) json.get("author");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        int img = (int)json.get("image");

        Post post = new Post(id, title, desc, author, img, content);
//        post.setUpdateDate(updateDate);
        post.setImg(img);
        return post;
    }

}
