package com.example.overcooked.model.firebase;

import android.util.Log;

import com.example.overcooked.model.interfaces.EmptyOnCompleteListener;
import com.example.overcooked.model.interfaces.FirebaseUserOnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public Authentication() {}

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
        Exception ex = null;
        if (task.isSuccessful()) {
            user = firebaseAuth.getCurrentUser();
        } else {
            user = null;
            ex = task.getException();
        }
        listener.onComplete(user, ex);
    }
}
