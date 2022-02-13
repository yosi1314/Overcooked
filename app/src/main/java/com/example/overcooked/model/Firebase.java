package com.example.overcooked.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public boolean isUserSignedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        return currentUser != null;
    }

    public void signIn(String email, String password, Model.UserSignIn listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    FirebaseUser user;
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login", "signInWithEmail:success");
                        user = firebaseAuth.getCurrentUser();
//                           updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Login", "signInWithEmail:failure", task.getException());

                        user = null;
                    }
                    listener.onComplete(user);
                });
    }

    public void signOut(Model.UserSignOut listener) {
        firebaseAuth.signOut();
        listener.onComplete();
    }

}
