package com.example.overcooked.model;

public class Post {
    int id;
    String title = "";
    String description = "";
    String author = "";
    int img;
    public Post() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post(int id, String title, String description, String author, int img) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.img = img;
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


}
