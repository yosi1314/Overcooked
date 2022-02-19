package com.example.overcooked.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.overcooked.MyApplication;
import com.example.overcooked.model.enums.FirebaseDataLoadingState;
import com.example.overcooked.model.firebase.Authentication;
import com.example.overcooked.model.firebase.Firebase;
import com.example.overcooked.model.firebase.Storage;
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
    public Authentication authentication = new Authentication();
    public Storage storage = new Storage();

    MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    MutableLiveData<FirebaseDataLoadingState> postListLoadingState = new MutableLiveData<>();

    MutableLiveData<List<User>> users = new MutableLiveData<>();
    MutableLiveData<FirebaseDataLoadingState> usersLoadingState = new MutableLiveData<>();

    public MutableLiveData<FirebaseDataLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    private Model() {
        postListLoadingState.setValue(FirebaseDataLoadingState.loaded);
    }

    public String getCurrentUserUID() {
        return authentication.getCurrentUserUID();
    }

    public boolean isSignedIn() {
        return authentication.isUserSignedIn();
    }

    public void signIn(String email, String password, FirebaseUserOnCompleteListener listener) {
        authentication.signIn(email, password, listener);
    }

    public void signUp(String email, String password, FirebaseUserOnCompleteListener listener) {
        authentication.register(email, password, listener);
    }

    public void signOut(EmptyOnCompleteListener listener) {
        authentication.signOut(listener);
    }

    public void createUser(User user, EmptyOnCompleteListener listener) {
        firebase.createUser(user, () -> {
            listener.onComplete();
            refreshUsersList();
        });
    }

    public void updateUser(User user, EmptyOnCompleteListener listener) {
        firebase.updateUser(user, () -> updateLocalUser(user, listener));
    }

    public void updateLocalUser(User user, EmptyOnCompleteListener listener){
        executor.execute(() -> {
            LocalDb.db.userDao().updateUser(user);
            mainThread.post(() -> {
                listener.onComplete();
                refreshUsersList();
            });
        });
    }

    public void getUserById(String uid, UserOnCompleteListener listener) {
        executor.execute(() -> {
            User user = LocalDb.db.userDao().getUserById(uid);
            mainThread.post(() -> listener.onComplete(user));
        });
    }

    public LiveData<List<Post>> getAllPosts() {
        if (posts.getValue() == null) {
            refreshPostsList();
            refreshUsersList();
        }
        return posts;
    }

    public LiveData<List<User>> getUsers(){
        if(users.getValue() == null){
            refreshUsersList();
        }
        return users;
    }

    public void refreshUsersList() {
        usersLoadingState.setValue(FirebaseDataLoadingState.loading);

        Long lastUpdateDate = MyApplication.getContext()
                .getSharedPreferences("refreshUsersList", Context.MODE_PRIVATE)
                .getLong("UsersLastUpdate", 0);

        executor.execute(() -> {
            List<User> usersList = LocalDb.db.userDao().getAll();
            users.postValue(usersList);
        });

        firebase.getUsers(lastUpdateDate, fbUsers -> executor.execute(() -> {
            Long localLastUpdate = 0L;
            for (User user : fbUsers) {
                LocalDb.db.userDao().insertAll(user);
                if (localLastUpdate < user.getUpdateDate()) {
                    localLastUpdate = user.getUpdateDate();
                }
            }
            MyApplication.getContext()
                    .getSharedPreferences("refreshUsersList", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("UsersLastUpdate", localLastUpdate)
                    .commit();

            List<User> list = LocalDb.db.userDao().getAll();
            users.postValue(list);
            usersLoadingState.postValue(FirebaseDataLoadingState.loaded);
        }));
    }

    public void refreshPostsList() {
        postListLoadingState.setValue(FirebaseDataLoadingState.loading);

        Long lastUpdateDate = MyApplication.getContext()
                .getSharedPreferences("refreshPostsList", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdate", 0);

        executor.execute(() -> {
            List<Post> postsList = LocalDb.db.postDao().getAll();
            posts.postValue(postsList);
        });

        firebase.getPosts(lastUpdateDate, fbPosts -> executor.execute(() -> {
            Long localLastUpdate = 0L;
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
            postListLoadingState.postValue(FirebaseDataLoadingState.loaded);
        }));
    }

    public void addPost(Post post, EmptyOnCompleteListener listener) {
        firebase.addPost(post, () -> {
            listener.onComplete();
            refreshPostsList();
            refreshUsersList();
        });
    }

    public void updatePost(Post post, EmptyOnCompleteListener listener) {
        firebase.updatePost(post, () -> updateLocalPost(post, listener));
    }

    public void deletePost(Post post, EmptyOnCompleteListener listener) {
        firebase.deletePost(post.getId(), () -> deleteLocalPost(post, listener));
    }

    public void updateLocalPost(Post post, EmptyOnCompleteListener listener){
        executor.execute(() -> {
            LocalDb.db.postDao().updatePost(post);
            mainThread.post(() -> {
                listener.onComplete();
                refreshPostsList();
                refreshUsersList();
            });
        });
    }

    public void deleteLocalPost(Post post, EmptyOnCompleteListener listener) {
        executor.execute(() -> {
            LocalDb.db.postDao().delete(post);
            mainThread.post(() -> {
                listener.onComplete();
                refreshPostsList();
                refreshUsersList();
            });
        });
    }

    public void getPostById(String id, SinglePostOnCompleteListener listener) {
        executor.execute(() -> {
            Post post = LocalDb.db.postDao().getPostById(id);
            mainThread.post(() -> listener.onComplete(post));
        });
    }

    public void uploadImage(Bitmap imageBitmap, String imageName, String storageLocation, ImageOnCompleteListener listener) {
        storage.uploadImage(imageBitmap, imageName, storageLocation, listener);
    }
}
