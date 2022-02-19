package com.example.overcooked.posts;

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
import com.example.overcooked.model.Post;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class CreatePostFragment extends ImageHandlerFragment {

    Post post;
    boolean isPostCreation;

    EditText titleEt;
    EditText descriptionEt;
    EditText contentEt;
    FloatingActionButton cameraBtn;
    FloatingActionButton galleryBtn;
    ImageView imageImv;
    Bitmap imageBitmap;
    Button actionBtn;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        post = CreatePostFragmentArgs.fromBundle(getArguments()).getPost();

        isPostCreation = post.getId() == null;

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(isPostCreation ? "Create New Post" : "Edit Post");

        titleEt = view.findViewById(R.id.create_post_title_tv);
        descriptionEt = view.findViewById(R.id.create_post_description_tv);
        contentEt = view.findViewById(R.id.create_post_content_tv);
        imageImv = view.findViewById(R.id.create_post_imv);
        cameraBtn = view.findViewById(R.id.create_post_camera_button);
        galleryBtn = view.findViewById(R.id.create_post_gallery_button);
        actionBtn = view.findViewById(R.id.create_post_done_btn);
        progressBar = view.findViewById(R.id.create_post_progress_bar);
        progressBar.setVisibility(View.GONE);

        galleryBtn.setOnClickListener(v -> openGallery());
        cameraBtn.setOnClickListener(v -> openCamera());
        actionBtn.setOnClickListener(v -> create());

        setPostData();

        return view;
    }

    private void setPostData() {
        if (!isPostCreation) {
            titleEt.setText(post.getTitle());
            descriptionEt.setText(post.getDescription());
            contentEt.setText(post.getContent());
            actionBtn.setText(getString(R.string.saveText));
            setImage(imageImv, post.getImg(), R.drawable.main_logo);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageBitmap = onResult(requestCode, resultCode, data, imageImv);
    }

    private void create() {
        String title = titleEt.getText().toString();
        String description = descriptionEt.getText().toString();
        String content = contentEt.getText().toString();
        String author = Model.instance.getCurrentUserUID();
        String id = post.getId() == null ? java.util.UUID.randomUUID().toString() : post.getId();

        boolean shouldSubmit = isShouldSubmit(title, description, content);

        if (shouldSubmit) {
            showProgressBar(progressBar);

            Post newPost = new Post(id, title, description, author, content);
            if (imageBitmap != null) {
                Model.instance.uploadImage(imageBitmap, id + ".jpg", getString(R.string.storage_posts), url -> {
                    newPost.setImg(url);
                    handleUserAction(newPost);
                });
            } else {
                newPost.setImg(post.getImg());
                handleUserAction(newPost);
            }
        }
    }

    private boolean isShouldSubmit(String title, String description, String content) {
        boolean shouldSubmit = true;

        if (title.isEmpty()) {
            titleEt.setError("Title cannot be empty");
            shouldSubmit = false;
        }

        if (description.isEmpty()) {
            descriptionEt.setError("Description cannot be empty");
            shouldSubmit = false;
        }

        if (content.isEmpty()) {
            contentEt.setError("Content cannot be empty");
            shouldSubmit = false;
        }

        return shouldSubmit;
    }

    private void handleUserAction(Post newPost) {
        if (isPostCreation) {
            Model.instance.addPost(newPost, () -> {
                hideProgressBar(progressBar);
                Navigation.findNavController(titleEt).navigateUp();
            });
        } else {
            newPost.setUploadDate(post.getUploadDate());
            Model.instance.updatePost(newPost, () -> {
                hideProgressBar(progressBar);
                Navigation.findNavController(titleEt).navigateUp();
            });
        }
    }
}