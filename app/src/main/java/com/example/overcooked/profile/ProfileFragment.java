package com.example.overcooked.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.overcooked.R;
import com.example.overcooked.feed.FeedFragmentDirections;
import com.example.overcooked.helpers.UtilsFragment;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends UtilsFragment {
    User user;
    TextView displayNameTitleTv;
    TextView emailTitleTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView displayNameTv = view.findViewById(R.id.profile_display_name_tv);
        TextView emailTv = view.findViewById(R.id.profile_email_tv);
        displayNameTitleTv = view.findViewById(R.id.profile_display_name_title_tv);
        emailTitleTv = view.findViewById(R.id.profile_email_title_tv);
        ImageView userImage = view.findViewById(R.id.profile_image_imv);
        FloatingActionButton editButton = view.findViewById(R.id.profile_edit_button);
        FloatingActionsMenu fabMenu = view.findViewById(R.id.profile_fab_menu);

        ProgressBar progressBar = view.findViewById(R.id.profile_progress_bar);

        hideInitialData();
        showProgressBar(progressBar);

//        if (user == null) {
//            editButton.setEnabled(false);
//        }

        Model.instance.getUserById(Model.instance.getCurrentUserUID(), (user) -> {
            this.user = user;
            hideProgressBar(progressBar);
            showInitialData();

            displayNameTv.setText(user.getDisplayName());
            emailTv.setText(user.getEmail());
            if(user.getImg() != null){
                Picasso.get().load(user.getImg()).into(userImage);
            } else {
                userImage.setImageResource(R.mipmap.ic_launcher_round);
            }
            editButton.setEnabled(true);
        });

        editButton.setOnClickListener(v -> {
            fabMenu.toggle();
            Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user));
        });

        return view;
    }

    private void hideInitialData() {
        displayNameTitleTv.setVisibility(View.GONE);
        emailTitleTv.setVisibility(View.GONE);
    }

    private void showInitialData() {
        displayNameTitleTv.setVisibility(View.VISIBLE);
        emailTitleTv.setVisibility(View.VISIBLE);
    }
}