package com.example.overcooked.model.firebase;

import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;
import com.example.overcooked.model.User;
import com.example.overcooked.model.interfaces.EmptyOnCompleteListener;
import com.example.overcooked.model.interfaces.PostsOnCompleteListener;
import com.example.overcooked.model.interfaces.UsersOnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Firebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Firebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void getPosts(Long lastUpdateDate, PostsOnCompleteListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Post post = Post.create(doc.getData());
                            if (post != null) {
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addPost(Post post, EmptyOnCompleteListener listener) {
        Map<String, Object> json = post.toJson();
        db.collection(Post.COLLECTION_NAME)
                .document(post.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void updatePost(Post post, EmptyOnCompleteListener listener) {
        Map<String, Object> json = post.toJson();
        db.collection(Post.COLLECTION_NAME).document(post.getId())
                .set(json).addOnCompleteListener(task -> {
            listener.onComplete();
            Model.instance.refreshPostsList();
            Model.instance.refreshUsersList();
        });
    }

    public void deletePost(String id, EmptyOnCompleteListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .document(id).delete().addOnCompleteListener(task -> listener.onComplete());
    }

    public void getUsers(Long lastUpdateDate, UsersOnCompleteListener listener) {
        db.collection(User.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User user = User.create(doc.getData());
                            if (user != null) {
                                list.add(user);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void createUser(User user, EmptyOnCompleteListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getUid())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }


    public void updateUser(User user, EmptyOnCompleteListener listener) {
        db.collection(User.COLLECTION_NAME).document(user.getUid())
                .set(user).addOnCompleteListener(task -> listener.onComplete());
    }
}
