package com.example.overcooked.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.overcooked.R;
import com.example.overcooked.model.Model;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView displayNameTv = view.findViewById(R.id.profile_display_name_tv);
        TextView emailTv = view.findViewById(R.id.profile_email_tv);
        ImageView userImage = view.findViewById(R.id.profile_image_imv);

        displayNameTv.setText("Loading user data...");
        emailTv.setText("Loading user data...");
        Model.instance.getUserById(Model.instance.getCurrentUserUID(), (user) -> {
            displayNameTv.setText(user.getDisplayName());
            emailTv.setText(user.getEmail());
            if(user.getImg() != null){
                Picasso.get().load(user.getImg()).into(userImage);
            } else {
                userImage.setImageResource(R.drawable.main_logo);
            }
        });

        return view;
    }
}