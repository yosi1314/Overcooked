package com.example.overcooked.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.overcooked.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    private List<Post> posts;

    public boolean isSignedIn() {
        return false;
    }

    public List<Post> getAllPosts() {
        if(posts != null) return posts;
        posts = new ArrayList<Post>();
        for(int i=0; i<20; i++){
            posts.add(
                    new Post("" + i,
                            "Title" + i,
                            "Description" + i,
                            "Author" + i,
                            R.drawable.main_logo,
                            "content" + i)
            );
        }
        return posts;
    }

    public Post getPostById(String id) {
        if (posts != null) {
            return posts.get(3);
        }
        return null;
    }
}
