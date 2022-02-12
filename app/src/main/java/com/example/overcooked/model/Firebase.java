package com.example.overcooked.model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public boolean isUserSignedIn() {
       FirebaseUser currentUser = firebaseAuth.getCurrentUser();

       return currentUser != null;
   }

   public void signIn(String email, String password) {
       
   }

}
