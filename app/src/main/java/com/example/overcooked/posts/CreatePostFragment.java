package com.example.overcooked.posts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.overcooked.R;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;


public class CreatePostFragment extends Fragment {
    EditText titleEt;
    EditText descriptionEt;
    EditText contentEt;
    Button createBtn;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        titleEt = view.findViewById(R.id.create_post_title_tv);
        descriptionEt = view.findViewById(R.id.create_post_description_tv);
        contentEt = view.findViewById(R.id.create_post_content_tv);
        createBtn = view.findViewById(R.id.create_post_done_btn);
        progressBar = view.findViewById(R.id.create_post_progress_bar);
        progressBar.setVisibility(View.GONE);

        createBtn.setOnClickListener(v -> {
            create();
        });


        return view;
    }

    private void create() {
        progressBar.setVisibility(View.VISIBLE);
        createBtn.setEnabled(false);
        String title = titleEt.getText().toString();
        String description = descriptionEt.getText().toString();
        String content = contentEt.getText().toString();
        String author = Model.instance.getCurrentUserUID();
        String id = java.util.UUID.randomUUID().toString();

        Post post = new Post(id, title, description, author, R.drawable.main_logo, content);

        Model.instance.addPost(post, () -> {
            Navigation.findNavController(titleEt).navigateUp();
        });
    }
}