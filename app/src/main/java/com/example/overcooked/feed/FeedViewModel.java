package com.example.overcooked.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;

import java.util.List;

public class FeedViewModel extends ViewModel {
    LiveData<List<Post>> posts;

    public FeedViewModel(){
        posts = Model.instance.getAll();
    }

    public LiveData<List<Post>> getPosts() { return posts; }
}
