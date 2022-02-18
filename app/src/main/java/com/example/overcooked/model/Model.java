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
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public Firebase firebase = new Firebase();

    MutableLiveData<List<Post>> posts = new MutableLiveData<List<Post>>();

    public enum PostListLoadingState {
        loading,
        loaded
    }



    

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();

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

    public void getAllPosts(GetAllPostsListener listener) {
        executor.execute(() -> {
            List<Post> posts = LocalDb.db.postDao().getAll();
            mainThread.post(() -> {
                listener.onComplete(posts);
            });
        });
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

    public void createUser(User user, Model.AddUserListener listener) {
        firebase.createUser(user, () -> {
            listener.onComplete();
        });
    }

    public void updateUser(User user, Model.UpdateUserListener listener) {
        firebase.updateUser(user, listener);
    }

    public void updatePost(Post post, Model.UpdatePostListener listener) {
        firebase.updatePost(post, () -> {
            updateLocalPost(post, listener);
        });
    }

    public void updateLocalPost(Post post, Model.UpdatePostListener listener){
        executor.execute(() -> {
            LocalDb.db.postDao().updatePost(post);
            mainThread.post(() -> {
                listener.onComplete();
                refreshPostsList();
            });
        });
    }

    public void addPost(Post post, Model.AddPostListener listener) {
        firebase.addPost(post, () -> {
            listener.onComplete();
            refreshPostsList();
        });
    }

    public void deletePost(Post post, Model.DeletePostListener listener) {
        firebase.deletePost(post.getId(), () -> {
            deleteLocalPost(post, listener);
        });
    }

    public void deleteLocalPost(Post post, Model.DeletePostListener listener) {
        executor.execute(() -> {
            LocalDb.db.postDao().delete(post);
            mainThread.post(() -> {
                listener.onComplete();
                refreshPostsList();
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

    public void uploadImage(Bitmap imageBitmap, String imageName, String storageLocation, Model.UploadImageListener listener) {
        firebase.uploadImage(imageBitmap, imageName, storageLocation, listener);
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

    public void getUserById(String uid, GetUserByUidListener listener) {
        firebase.getUserByUid(uid, listener);
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

    public interface UploadImageListener {
        void onComplete(String url);
    }

    public interface AddUserListener {
        void onComplete();
    }

    public interface UpdateUserListener {
        void onComplete();
    }

    public interface GetUserByUidListener {
        void onComplete(User user);
    }

    public interface UpdatePostListener {
        void onComplete();
    }

    public interface DeletePostListener {
        void onComplete();
    }
}
