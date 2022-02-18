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
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

    public String getCurrentUserUID() {
        return firebaseAuth.getUid();
    }

    public boolean isUserSignedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        return currentUser != null;
    }

    public void signIn(String email, String password, FirebaseUserOnCompleteListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> onAuthenticationComplete(task, listener));
    }

    public void register(String email, String password, FirebaseUserOnCompleteListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> onAuthenticationComplete(task, listener));
    }

    public void signOut(EmptyOnCompleteListener listener) {
        firebaseAuth.signOut();
        listener.onComplete();
    }

    private void onAuthenticationComplete(Task<AuthResult> task, FirebaseUserOnCompleteListener listener) {
        FirebaseUser user;
        if (task.isSuccessful()) {
            Log.d("UserAuthentication", "signInWithEmail:success");
            user = firebaseAuth.getCurrentUser();
        } else {
            Log.w("UserAuthentication", "signInWithEmail:failure", task.getException());
            user = null;
        }

        listener.onComplete(user);
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void uploadImage(Bitmap imageBitmap, String imageName, String storageLocation, ImageOnCompleteListener listener) {
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child(storageLocation + imageName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnCompleteListener(task -> imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Uri downloadUrl = uri;
                    listener.onComplete(downloadUrl.toString());
                }));
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
