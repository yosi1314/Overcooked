package com.example.overcooked.model;

public class Post {
    String id;
    String title = "";
    String description = "";
    String author = "";
    int img;

    String content;
    public Post() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Post(String id, String title, String description, String author, int img, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.img = img;
        this.content = content;
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


}
