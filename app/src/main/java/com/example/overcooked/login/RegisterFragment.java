package com.example.overcooked.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.helpers.IntentHelper;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;

public class RegisterFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    TextView displayNameTv;
    TextView emailTv;
    TextView passwordTv;
    TextView confirmPasswordTv;
    Button registerButton;
    TextView goToSignIn;
    ImageView userImageImv;
    Bitmap imageBitmap;
    private IntentHelper intentHelper = new IntentHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        displayNameTv = view.findViewById(R.id.register_display_name_tv);
        emailTv = view.findViewById(R.id.register_email_tv);
        passwordTv = view.findViewById(R.id.register_password_tv);
        confirmPasswordTv = view.findViewById(R.id.register_confirm_password_tv);
        registerButton = view.findViewById(R.id.register_signup_btn);
        goToSignIn = view.findViewById(R.id.register_signin_tv);

        userImageImv = view.findViewById(R.id.register_image_imv);
        userImageImv.setOnClickListener(v -> openCamera());

        goToSignIn.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        registerButton.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String displayName = displayNameTv.getText().toString();
        String email = emailTv.getText().toString();
        String password = passwordTv.getText().toString();
        String passwordConfirmation = confirmPasswordTv.getText().toString();

        if (displayName == null) {
            displayNameTv.setError("Display name cannot be empty");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTv.setError("Email is not valid");
            return;
        }

        if (!password.equals(passwordConfirmation)) {
            confirmPasswordTv.setError("Passwords do not match");
            return;
        }

        Model.instance.signUp(email, password, user -> {
            if (user != null) {
                String uid = user.getUid();
                User userToAdd = new User(uid, displayName, email);
                if (imageBitmap != null) {
                    Model.instance.uploadImage(imageBitmap, uid + ".jpg", getString(R.string.storage_users), url -> {
                        userToAdd.setImg(url);
                        Model.instance.createUser(userToAdd, () -> {
                            intentHelper.toFeedActivity(this);
                        });
                    });
                } else {
                    Model.instance.createUser(userToAdd, () -> {
                        intentHelper.toFeedActivity(this);
                    });
                }
            } else {
                //TODO: display error label to client
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                userImageImv.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                userImageImv.setImageBitmap(imageBitmap);

            }
        }
    }
}