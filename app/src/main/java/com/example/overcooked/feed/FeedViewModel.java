package com.example.overcooked.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;

import java.util.LinkedList;
import java.util.List;

public class FeedViewModel extends ViewModel {
    LiveData<List<Post>> posts;

    public FeedViewModel(){
        posts = Model.instance.getAll();
    }

    public LiveData<List<Post>> getPosts() { return posts; }
    public LiveData<List<Post>> getMyPosts(String userUid) {
        List<Post> myPosts = new LinkedList<>();
        MutableLiveData<List<Post>> postsToReturn = new MutableLiveData<List<Post>>();
        if (posts.getValue() != null) {
            for(Post post : posts.getValue()){
                if(post.getAuthor().equals(userUid)){
                    myPosts.add(post);
                }
            }
        }
        postsToReturn.setValue(myPosts);
        return postsToReturn;
    }
}
