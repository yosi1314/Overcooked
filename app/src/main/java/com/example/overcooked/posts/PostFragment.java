package com.example.overcooked.posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.helpers.ImageHandlerFragment;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostFragment extends ImageHandlerFragment {

    ImageView postImageView;
    ImageView contentSeparator;
    TextView postTitle;
    TextView postDescription;
    TextView postAuthor;
    TextView postContent;
    TextView postUploadDate;
    TextView separator;
    ProgressBar progressBar;
    FloatingActionsMenu fabMenu;
    FloatingActionButton editFab;
    FloatingActionButton deleteFab;

    Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        String postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();

        postImageView = view.findViewById(R.id.single_post_image_imv);
        postTitle = view.findViewById(R.id.single_post_title_tv);
        postDescription = view.findViewById(R.id.single_post_desc_tv);
        postAuthor = view.findViewById(R.id.single_post_author_tv);
        postContent = view.findViewById(R.id.single_post_content_tv);
        postUploadDate = view.findViewById(R.id.single_post_upload_date_tv);
        separator = view.findViewById(R.id.single_post_separator);
        fabMenu = view.findViewById(R.id.single_post_fab_menu);
        editFab = view.findViewById(R.id.single_post_edit_button);
        deleteFab = view.findViewById(R.id.single_post_delete_fab);
        contentSeparator = view.findViewById(R.id.single_post_content_separator);

        progressBar = view.findViewById(R.id.single_post_progress_bar);
        showProgressBar(progressBar);

        fabMenu.setVisibility(View.GONE);
        fabMenu.setEnabled(false);

        editFab.setOnClickListener(this::goToEditPost);
        deleteFab.setOnClickListener(this::deletePost);

        Model.instance.getPostById(postId, (result) -> {
            post = result;
            setData();
            if(post.getAuthor().equals(Model.instance.getCurrentUserUID())){
                fabMenu.setVisibility(View.VISIBLE);
                fabMenu.setEnabled(true);
            }
        });

        return view;
    }

    private void deletePost(View v) {
        Model.instance.deletePost(post, () -> Navigation.findNavController(v).navigateUp());
    }

    private void goToEditPost(View v) {
        fabMenu.toggle();
        Navigation.findNavController(v).navigate(PostFragmentDirections.actionGlobalCreatePostFragment(post));
    }

    private void setPostValues(String displayName) {
        setImage(postImageView, post.getImg(), R.drawable.main_logo);
        contentSeparator.setImageResource(R.drawable.dotted_divider);
        postAuthor.setText(displayName);
        postTitle.setText(post.getTitle());
        try {
            postUploadDate.setText(getDate(post.getUploadDate()));
            separator.setText("/");
        } catch (ParseException e) {
            postUploadDate.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
        postDescription.setText(post.getDescription());
        postContent.setText(post.getContent());
    }

    private void setData(){
        Model.instance.getUserById(post.getAuthor(), user -> {
            setPostValues(user.getDisplayName());
            hideProgressBar(progressBar);
        });
    }

    private String getDate(Long uploadDate) throws ParseException {
        Date date = new Date(uploadDate * 1000);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return formatter.format(date);
    }
}