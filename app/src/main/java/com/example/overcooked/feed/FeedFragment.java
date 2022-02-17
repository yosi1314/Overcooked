package com.example.overcooked.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.overcooked.R;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FeedFragment extends Fragment {

    String userUid;
    FeedViewModel viewModel;
    PostAdapter postAdapter;
    SwipeRefreshLayout swipeRefresh;
    boolean isGlobalFeed;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        userUid = FeedFragmentArgs.fromBundle(getArguments()).getUserUid();

        setRelevantFragmentData();

        swipeRefresh = view.findViewById(R.id.feed_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPostsList());

        RecyclerView feed = view.findViewById(R.id.feed_rv);
        feed.setHasFixedSize(true);

        feed.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter();
        feed.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getPosts().getValue().get(position).getId();
            //String postId = liveDataPosts.getValue().get(position).getId();
            Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToPostFragment(postId));
        });

        setHasOptionsMenu(true);

        viewModel.getPosts().observe(getViewLifecycleOwner(), postList -> refresh());
        //liveDataPosts.observe(getViewLifecycleOwner(), postList -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getPostListLoadingState().getValue() == Model.PostListLoadingState.loading);
        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), postListLoadingState -> {
            swipeRefresh.setRefreshing(postListLoadingState == Model.PostListLoadingState.loading);
        });

        return view;
    }

    private void setRelevantFragmentData(){
        isGlobalFeed = userUid == null;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(isGlobalFeed ? "Feed" : "My Posts");
    }

    private void refresh() {
        postAdapter.notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView descriptionTv;
        ImageView thumbnailImv;

        public PostViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.post_title_tv);
            descriptionTv = itemView.findViewById(R.id.post_desc_tv);
            thumbnailImv = itemView.findViewById(R.id.post_image_imv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }

        void bind(Post post) {
            titleTv.setText(post.getTitle());
            descriptionTv.setText(post.getDescription());
            if (post.getImg() != null){
                Picasso.get()
                        .load(post.getImg())
                        .into(thumbnailImv);
            } else {
                thumbnailImv.setImageResource(R.drawable.main_logo);
            }
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
//            Post post = viewModel.getPosts().getValue().get(position);
            LiveData<List<Post>> data = isGlobalFeed ? viewModel.getPosts() : viewModel.getMyPosts(userUid);
            Post post = data.getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
//            List<Post> data = viewModel.getPosts().getValue();
            LiveData<List<Post>> viewPosts = isGlobalFeed ? viewModel.getPosts() : viewModel.getMyPosts(userUid);
            List<Post> data = viewPosts.getValue();
            if (data == null) {
                return 0;
            }
            return data.size();
        }
    }
}