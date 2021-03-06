package com.example.overcooked.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.feed.BaseActivity;
import com.example.overcooked.helpers.ImageHandlerFragment;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class RegisterFragment extends ImageHandlerFragment {

    ConstraintLayout registerCl;
    EditText displayNameEt;
    EditText emailEt;
    EditText passwordEt;
    EditText confirmPasswordEt;
    Button registerButton;
    Button goToSignIn;
    ProgressBar progressBar;
    FloatingActionButton galleryBtn;
    FloatingActionButton cameraBtn;
    ImageView userImageImv;
    Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        registerCl = view.findViewById(R.id.register_cl);
        displayNameEt = view.findViewById(R.id.register_display_name_et);
        emailEt = view.findViewById(R.id.register_email_et);
        passwordEt = view.findViewById(R.id.register_password_et);
        confirmPasswordEt = view.findViewById(R.id.register_confirm_password_et);
        registerButton = view.findViewById(R.id.register_signup_btn);
        goToSignIn = view.findViewById(R.id.register_signin_btn);

        progressBar = view.findViewById(R.id.register_progress_bar);
        progressBar.setVisibility(View.GONE);

        galleryBtn = view.findViewById(R.id.register_gallery_button);
        cameraBtn = view.findViewById(R.id.register_camera_button);
        userImageImv = view.findViewById(R.id.register_image_imv);

        galleryBtn.setOnClickListener(v -> openGallery());
        cameraBtn.setOnClickListener(v -> openCamera());

        goToSignIn.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        registerButton.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        hideKeyboard();
        String displayName = displayNameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String passwordConfirmation = confirmPasswordEt.getText().toString();

        boolean shouldSubmit = isShouldSubmit(displayName, email, password, passwordConfirmation);

        if (shouldSubmit) {
            showProgressBar(progressBar);
            Model.instance.signUp(email, password, (user, ex) -> {
                if (user != null) {
                    String uid = user.getUid();
                    User userToAdd = new User(uid, displayName, email);
                    if (imageBitmap != null) {
                        Model.instance.uploadImage(imageBitmap, uid + ".jpg", getString(R.string.storage_users), url -> {
                            userToAdd.setImg(url);
                            Model.instance.createUser(userToAdd, this::toFeedActivity);
                        });
                    } else {
                        Model.instance.createUser(userToAdd, this::toFeedActivity);
                    }
                } else {
                    hideProgressBar(progressBar);
                    Snackbar.make(registerCl, ex.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        }
    }

    private boolean isShouldSubmit(String displayName, String email, String password, String passwordConfirmation) {
        boolean shouldSubmit = true;

        if (displayName.isEmpty()) {
            displayNameEt.setError("Display name cannot be empty");
            shouldSubmit = false;
        }

        if (email.isEmpty()) {
            emailEt.setError("Email cannot be empty");
            shouldSubmit = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Invalid email");
            shouldSubmit = false;
        }

        if (password.isEmpty()) {
            passwordEt.setError("Password cannot be empty");
            shouldSubmit = false;
        }

        if (!passwordConfirmation.equals(password)) {
            confirmPasswordEt.setError("Passwords do not match");
            shouldSubmit = false;
        }

        return shouldSubmit;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageBitmap = onResult(requestCode, resultCode, data, userImageImv);
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), BaseActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
