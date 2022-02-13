package com.example.overcooked.model;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public boolean isUserSignedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        return currentUser != null;
    }

    public void signIn(String email, String password, Model.UserAuthentication listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> onAuthenticationComplete(task, listener));
    }

    public void register(String email, String password, Model.UserAuthentication listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> onAuthenticationComplete(task, listener));
    }

    public void signOut(Model.UserSignOut listener) {
        firebaseAuth.signOut();
        listener.onComplete();
    }

    private void onAuthenticationComplete(Task<AuthResult> task, Model.UserAuthentication listener) {
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
}
