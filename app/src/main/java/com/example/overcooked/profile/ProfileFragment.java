package com.example.overcooked.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.helpers.ImageHandlerFragment;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class ProfileFragment extends ImageHandlerFragment {
    User user;
    TextView displayNameTitleTv;
    TextView emailTitleTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");

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

        Model.instance.getUserById(Model.instance.getCurrentUserUID(), user -> {
            hideProgressBar(progressBar);
            showInitialData();

            if(user != null) {
                this.user = user;
                displayNameTv.setText(user.getDisplayName());
                emailTv.setText(user.getEmail());
                setImage(userImage, user.getImg(), R.mipmap.ic_launcher_round);
                editButton.setEnabled(true);
            }
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