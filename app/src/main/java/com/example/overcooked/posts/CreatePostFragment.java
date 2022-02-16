package com.example.overcooked.posts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.overcooked.R;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;


public class CreatePostFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    EditText titleEt;
    EditText descriptionEt;
    EditText contentEt;
    ImageView imageImv;
    Bitmap imageBitmap;
    Button createBtn;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        titleEt = view.findViewById(R.id.create_post_title_tv);
        descriptionEt = view.findViewById(R.id.create_post_description_tv);
        contentEt = view.findViewById(R.id.create_post_content_tv);
        imageImv = view.findViewById(R.id.create_post_imv);
        createBtn = view.findViewById(R.id.create_post_done_btn);
        progressBar = view.findViewById(R.id.create_post_progress_bar);
        progressBar.setVisibility(View.GONE);

        imageImv.setOnClickListener(v -> {
            openCamera();
        });

        createBtn.setOnClickListener(v -> {
            create();
        });


        return view;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    int resultCode = result.getResultCode();
//                    if (resultCode == REQUEST_GALLERY) {
//                        if (resultCode == Activity.RESULT_OK) {
//                            Bundle extras = result.getData().getExtras();
//                            imageBitmap = (Bitmap) extras.get("data");
//                            imageImv.setImageBitmap(imageBitmap);
//                        }
//                    }
//                }
//        );
//    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, REQUEST_GALLERY);

    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageImv.setImageBitmap(imageBitmap);
            }
        }
        else if (requestCode == REQUEST_CAMERA){
            if (resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageImv.setImageBitmap(imageBitmap);

            }
        }
    }

    private void create() {
        progressBar.setVisibility(View.VISIBLE);
        createBtn.setEnabled(false);
        String title = titleEt.getText().toString();
        String description = descriptionEt.getText().toString();
        String content = contentEt.getText().toString();
        String author = Model.instance.getCurrentUserUID();
        String id = java.util.UUID.randomUUID().toString();

        Post post = new Post(id, title, description, author, content);
        if(imageBitmap != null){
            Model.instance.uploadImage(imageBitmap, id + ".jpg", getString(R.string.storage_posts), url -> {
                post.setImg(url);
                Model.instance.addPost(post, () -> {
                    Navigation.findNavController(titleEt).navigateUp();
                });
            });
        } else {
            Model.instance.addPost(post, () -> {
                Navigation.findNavController(titleEt).navigateUp();
            });
        }

    }
}