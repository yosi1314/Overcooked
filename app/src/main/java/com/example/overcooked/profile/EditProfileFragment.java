package com.example.overcooked.profile;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.helpers.ImageHandlerFragment;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends ImageHandlerFragment {

    User user;
    ImageView profileImageImv;
    EditText displayNameEt;
    EditText emailEt;
    FloatingActionButton cameraBtn;
    FloatingActionButton galleryBtn;
    Button saveBtn;
    Bitmap imageBitmap;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");

        user = EditProfileFragmentArgs.fromBundle(getArguments()).getUser();

        profileImageImv = view.findViewById(R.id.edit_profile_image_imv);
        displayNameEt = view.findViewById(R.id.edit_profile_display_name_et);
        emailEt = view.findViewById(R.id.edit_profile_email_et);
        cameraBtn = view.findViewById(R.id.edit_button_camera_button);
        galleryBtn = view.findViewById(R.id.edit_button_gallery_button);
        saveBtn = view.findViewById(R.id.edit_profile_save_button);

        progressBar = view.findViewById(R.id.edit_profile_progressbar);
        progressBar.setVisibility(View.GONE);

        setProfileData();

        cameraBtn.setOnClickListener(v -> openCamera());

        galleryBtn.setOnClickListener(v -> openGallery());

        saveBtn.setOnClickListener(v -> update());

        return view;
    }

    private void setProfileData() {
        String img = user.getImg();
        if (img != null) {
            Picasso.get().load(img).into(profileImageImv);
        } else {
            profileImageImv.setImageResource(R.mipmap.ic_launcher_round);
        }
        displayNameEt.setText(user.getDisplayName());
        emailEt.setText(user.getEmail());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageBitmap = onResult(requestCode, resultCode, data, profileImageImv);
    }

    private void update() {
        String displayName = displayNameEt.getText().toString();
        String email = user.getEmail();
        String id = user.getUid();

        boolean shouldSubmit = isShouldSubmit(displayName);

        if (shouldSubmit) {
            showProgressBar(progressBar);

            User newUser = new User(id, displayName, email);
            if (imageBitmap != null) {
                Model.instance.uploadImage(imageBitmap, id + ".jpg", getString(R.string.storage_posts), url -> {
                    newUser.setImg(url);
                    Model.instance.updateUser(newUser, () -> {
                        hideProgressBar(progressBar);
                        Navigation.findNavController(displayNameEt).navigateUp();
                    });
                });
            } else {
                newUser.setImg(user.getImg());
                Model.instance.updateUser(newUser, () -> {
                    hideProgressBar(progressBar);
                    Navigation.findNavController(displayNameEt).navigateUp();
                });
            }
        }
    }

    private boolean isShouldSubmit(String displayName) {
        boolean shouldSubmit = true;

        if (displayName.isEmpty()) {
            displayNameEt.setError("Display name cannot be empty");
            shouldSubmit = false;
        }

        return shouldSubmit;
    }
}