package com.example.overcooked.post_page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.overcooked.R;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;

public class PostFragment extends Fragment {

    ImageView post_image_view;
    TextView post_title;
    TextView post_description;
    TextView post_author;
    TextView post_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        String postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        Post post = Model.instance.getPostById(postId);
        post_image_view = view.findViewById(R.id.single_post_image_imv);
        post_title = view.findViewById(R.id.single_post_title_tv);
        post_description = view.findViewById(R.id.single_post_desc_tv);
        post_author = view.findViewById(R.id.single_post_author_tv);
        post_content = view.findViewById(R.id.single_post_content_tv);

        //TODO: replace this with onComplete listener
        post_image_view.setImageResource(post.getImg());
        post_title.setText(post.getTitle());
        post_description.setText(post.getDescription());
        post_author.setText(post.getAuthor());
        post_content.setText(post.getContent());

        return view;
    }
}