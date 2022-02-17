package com.example.overcooked.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

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
            profileImageImv.setImageResource(R.drawable.main_logo);
        }
        displayNameEt.setText(user.getDisplayName());
        emailEt.setText(user.getEmail());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, REQUEST_GALLERY);

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
                profileImageImv.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                profileImageImv.setImageBitmap(imageBitmap);

            }
        }
    }

    private void update() {
        progressBar.setVisibility(View.VISIBLE);
        disableFAB(galleryBtn);
        disableFAB(cameraBtn);
        disableButton(saveBtn);
        String displayName = displayNameEt.getText().toString();
        String email = user.getEmail();
        String id = user.getUid();

        User newUser = new User(id, displayName, email);
        if (imageBitmap != null) {
            Model.instance.uploadImage(imageBitmap, id + ".jpg", getString(R.string.storage_posts), url -> {
                newUser.setImg(url);
                Model.instance.updateUser(newUser, () -> {
                    Navigation.findNavController(displayNameEt).navigateUp();
                });
            });
        } else {
            newUser.setImg(user.getImg());
            Model.instance.updateUser(newUser, () -> {
                Navigation.findNavController(displayNameEt).navigateUp();
            });
        }
    }

    private void disableButton(Button button) {
        button.setEnabled(false);
        button.setClickable(false);
        button.setAlpha(.5f);
    }

    private void disableFAB(FloatingActionButton button) {
        button.setEnabled(false);
        button.setClickable(false);
        button.setAlpha(.5f);
    }
}