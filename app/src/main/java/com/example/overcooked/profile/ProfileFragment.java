package com.example.overcooked.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.overcooked.R;
import com.example.overcooked.feed.FeedFragmentDirections;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView displayNameTv = view.findViewById(R.id.profile_display_name_tv);
        TextView emailTv = view.findViewById(R.id.profile_email_tv);
        ImageView userImage = view.findViewById(R.id.profile_image_imv);
        FloatingActionButton editButton = view.findViewById(R.id.profile_edit_button);

        if (user == null) {
            editButton.setEnabled(false);
        }

        displayNameTv.setText("Loading user data...");
        emailTv.setText("Loading user data...");
        Model.instance.getUserById(Model.instance.getCurrentUserUID(), (user) -> {
            this.user = user;
            displayNameTv.setText(user.getDisplayName());
            emailTv.setText(user.getEmail());
            if(user.getImg() != null){
                Picasso.get().load(user.getImg()).into(userImage);
            } else {
                userImage.setImageResource(R.drawable.main_logo);
            }
            editButton.setEnabled(true);
        });

        editButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user)));

        return view;
    }
}