package com.example.overcooked.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.overcooked.MyApplication;
import com.example.overcooked.model.enums.PostListLoadingState;
import com.example.overcooked.model.interfaces.EmptyOnCompleteListener;
import com.example.overcooked.model.interfaces.FirebaseUserOnCompleteListener;
import com.example.overcooked.model.interfaces.ImageOnCompleteListener;
import com.example.overcooked.model.interfaces.SinglePostOnCompleteListener;
import com.example.overcooked.model.interfaces.UserOnCompleteListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public Firebase firebase = new Firebase();

    MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<>();

    public MutableLiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    private Model() {
        postListLoadingState.setValue(PostListLoadingState.loaded);
    }

    public String getCurrentUserUID() {
        return firebase.getCurrentUserUID();
    }

    public boolean isSignedIn() {
        return firebase.isUserSignedIn();
    }

    public void createUser(User user, EmptyOnCompleteListener listener) {
        firebase.createUser(user, () -> {
            listener.onComplete();
        });
    }

    public void updateUser(User user, EmptyOnCompleteListener listener) {
        firebase.updateUser(user, listener);
    }

    public void signIn(String email, String password, FirebaseUserOnCompleteListener listener) {
        firebase.signIn(email, password, listener);
    }

    public void signUp(String email, String password, FirebaseUserOnCompleteListener listener) {
        firebase.register(email, password, listener);
    }

    public void signOut(EmptyOnCompleteListener listener) {
        firebase.signOut(listener);
    }

    public void getUserById(String uid, UserOnCompleteListener listener) {
        firebase.getUserByUid(uid, listener);
    }

    public LiveData<List<Post>> getAll() {
        if (posts.getValue() == null) {
            refreshPostsList();
        }
        return posts;
    }

    public void refreshPostsList() {
        postListLoadingState.setValue(PostListLoadingState.loading);

        Long lastUpdateDate = MyApplication.getContext()
                .getSharedPreferences("refreshPostsList", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdate", 0);

        executor.execute(() -> {
            List<Post> postsList = LocalDb.db.postDao().getAll();
            posts.postValue(postsList);
        });

        firebase.getPosts(lastUpdateDate, fbPosts -> {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Long localLastUpdate = new Long(0);
                    Log.d("TAG", "fb returned " + fbPosts.size());
                    for (Post post : fbPosts) {
                        LocalDb.db.postDao().insertAll(post);
                        if (localLastUpdate < post.getUpdateDate()) {
                            localLastUpdate = post.getUpdateDate();
                        }
                    }
                    MyApplication.getContext()
                            .getSharedPreferences("refreshPostsList", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("PostsLastUpdate", localLastUpdate)
                            .commit();

                    List<Post> list = LocalDb.db.postDao().getAll();
                    posts.postValue(list);
                    postListLoadingState.postValue(PostListLoadingState.loaded);
                }
            });
        });
    }

    public void addPost(Post post, EmptyOnCompleteListener listener) {
        firebase.addPost(post, () -> {
            listener.onComplete();
            refreshPostsList();
        });
    }

    public void updatePost(Post post, EmptyOnCompleteListener listener) {
        firebase.updatePost(post, () -> {
            updateLocalPost(post, listener);
        });
    }

    public void deletePost(Post post, EmptyOnCompleteListener listener) {
        firebase.deletePost(post.getId(), () -> {
            deleteLocalPost(post, listener);
        });
    }

    public void updateLocalPost(Post post, EmptyOnCompleteListener listener){
        executor.execute(() -> {
            LocalDb.db.postDao().updatePost(post);
            mainThread.post(() -> {
                listener.onComplete();
                refreshPostsList();
            });
        });
    }

    public void deleteLocalPost(Post post, EmptyOnCompleteListener listener) {
        executor.execute(() -> {
            LocalDb.db.postDao().delete(post);
            mainThread.post(() -> {
                listener.onComplete();
                refreshPostsList();
            });
        });
    }

    public void getPostById(String id, SinglePostOnCompleteListener listener) {
        executor.execute(() -> {
            Post post = LocalDb.db.postDao().getPostById(id);
            mainThread.post(() -> {
                listener.onComplete(post);
            });
        });
    }

    public void uploadImage(Bitmap imageBitmap, String imageName, String storageLocation, ImageOnCompleteListener listener) {
        firebase.uploadImage(imageBitmap, imageName, storageLocation, listener);
    }
}
