package com.example.overcooked.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overcooked.R;
import com.example.overcooked.login.LoginActivity;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;

import java.util.List;


public class FeedFragment extends Fragment {

    List<Post> posts;
    Model postsModel = Model.instance;
    PostAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView feed = view.findViewById(R.id.feed_rv);
        feed.setHasFixedSize(true);

        feed.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter();
        feed.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener((v, position) -> {
            String postId = posts.get(position).getId();
            Navigation.findNavController(v).navigate((NavDirections) FeedFragmentDirections.actionFeedFragmentToPostFragment(postId));
        });

        setHasOptionsMenu(true);
        refresh();
        return view;
    }

    private void refresh() {
        postsModel.getAllPosts((list) -> {
            posts = list;
            postAdapter.notifyDataSetChanged();
        });
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView descriptionTv;
        TextView authorTv;
        ImageView thumbnailImv;

        public PostViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.post_title_tv);
            descriptionTv = itemView.findViewById(R.id.post_desc_tv);
            authorTv = itemView.findViewById(R.id.post_author_tv);
            thumbnailImv = itemView.findViewById(R.id.post_image_imv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }

        void bind(Post post) {
            titleTv.setText(post.getTitle());
            descriptionTv.setText(post.getDescription());
            authorTv.setText(post.getAuthor());
            thumbnailImv.setImageResource(R.drawable.main_logo);
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_row, parent, false);
            PostViewHolder holder = new PostViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            Post post = posts.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if(posts == null){
                return 0;
            }
            return posts.size();
        }
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.base_menu, menu);
//    }


    //TODO: add onOptionsItemSelected
}