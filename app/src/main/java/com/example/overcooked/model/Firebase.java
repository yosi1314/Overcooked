package com.example.overcooked.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.overcooked.model.interfaces.EmptyOnCompleteListener;
import com.example.overcooked.model.interfaces.FirebaseUserOnCompleteListener;
import com.example.overcooked.model.interfaces.ImageOnCompleteListener;
import com.example.overcooked.model.interfaces.PostsOnCompleteListener;
import com.example.overcooked.model.interfaces.UserOnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Empty;

import java.io.ByteArrayOutputStream;
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
        db.collection(Post.COLLECTION_NAME).document(post.id)
                .set(json).addOnCompleteListener(task -> {
            listener.onComplete();
            Model.instance.refreshPostsList();
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
        db.collection(User.COLLECTION_NAME).document(user.uid)
                .set(user).addOnCompleteListener(task -> listener.onComplete());
    }

    public void getUserByUid(String uid, UserOnCompleteListener listener) {
        db.collection(User.COLLECTION_NAME)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    User user = null;
                    DocumentSnapshot data = task.getResult();
                    if (task.isSuccessful() & data != null) {
                        user = User.create(data.getData());
                    }
                    listener.onComplete(user);
                });
    }

    public void deletePost(String id, EmptyOnCompleteListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .document(id).delete().addOnCompleteListener(task -> {
                    listener.onComplete();
        });
    }
}
