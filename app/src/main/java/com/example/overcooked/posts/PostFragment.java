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
import com.example.overcooked.helpers.UtilsFragment;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostFragment extends UtilsFragment {

    ImageView post_image_view;
    ImageView contentSeparator;
    TextView post_title;
    TextView post_description;
    TextView post_author;
    TextView post_content;
    TextView postUploadDate;
    TextView separator;
    ProgressBar progressBar;
    FloatingActionsMenu fabMenu;
    FloatingActionButton editFab;
    FloatingActionButton deleteFab;

    Post post;

    String bla = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        String postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();

        post_image_view = view.findViewById(R.id.single_post_image_imv);
        post_title = view.findViewById(R.id.single_post_title_tv);
        post_description = view.findViewById(R.id.single_post_desc_tv);
        post_author = view.findViewById(R.id.single_post_author_tv);
        post_content = view.findViewById(R.id.single_post_content_tv);
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
        Model.instance.deletePost(post, () -> {
            Navigation.findNavController(v).navigateUp();
        });
    }

    private void goToEditPost(View v) {
        fabMenu.toggle();
        Navigation.findNavController(v).navigate(PostFragmentDirections.actionGlobalCreatePostFragment(post));
    }

    private void setPostValues(String displayName) {
        if(post.getImg() != null){
            Picasso.get().load(post.getImg()).into(post_image_view);
        } else {
            post_image_view.setImageResource(R.drawable.main_logo);
        }
        contentSeparator.setImageResource(R.drawable.dotted_divider);
        post_author.setText(displayName);
        post_title.setText(post.getTitle());
        try {
            postUploadDate.setText(getDate(post.getUploadDate()));
            separator.setText("/");
        } catch (ParseException e) {
            postUploadDate.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
        post_description.setText(post.getDescription());
        post_content.setText(post.getContent());
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