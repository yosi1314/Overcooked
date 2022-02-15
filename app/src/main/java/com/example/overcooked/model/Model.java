package com.example.overcooked.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.overcooked.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public Firebase firebase = new Firebase();

    private List<Post> posts;

    private Model(){}

    public String getCurrentUserUID(){
        return firebase.getCurrentUserUID();
    }

    public boolean isSignedIn() {
        return firebase.isUserSignedIn();
    }

    public void getAllPosts(GetAllPostsListener listener) {
        executor.execute(() -> {
            List<Post> posts = LocalDb.db.postDao().getAll();
            mainThread.post(() -> {
                listener.onComplete(posts);
            });
        });
    }

    public void addPost(Post post, Model.AddPostListener listener){
        executor.execute(() -> {
            LocalDb.db.postDao().insertAll(post);;
            mainThread.post(() -> {
                listener.onComplete();
            });
        });
    }

    public void getPostById(String id, Model.GetPostByIdListener listener) {
        executor.execute(() -> {
            Post post = LocalDb.db.postDao().getPostById(id);
            mainThread.post(() -> {
                listener.onComplete(post);
            });
        });
    }

    public void signIn(String email, String password, UserAuthentication listener) {
        firebase.signIn(email, password, listener);
    }

    public void signUp(String email, String password, UserAuthentication listener) {
        firebase.register(email, password, listener);
    }

    public void signOut(UserSignOut listener) {
        firebase.signOut(listener);
    }

    public interface UserAuthentication {
        void onComplete(FirebaseUser user);
    }

    public interface UserSignOut {
        void onComplete();
    }

    public interface GetPostsListener {
        void onComplete(List<Post> posts);
    }

    public interface GetAllPostsListener {
        void onComplete(List<Post> posts);
    }

    public interface AddPostListener {
        void onComplete();
    }

    public interface GetPostByIdListener {
        void onComplete(Post post);
    }
}
