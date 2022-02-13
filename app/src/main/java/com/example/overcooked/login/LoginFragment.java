package com.example.overcooked.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.overcooked.R;
import com.example.overcooked.feed.BaseActivity;
import com.example.overcooked.model.Model;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView email_tv = view.findViewById(R.id.login_email_tv);
        TextView password_tv = view.findViewById(R.id.login_password_tv);
        Button loginBtn = view.findViewById(R.id.login_login_btn);

        loginBtn.setOnClickListener(v -> {

            Model.instance.signIn(email_tv.getText().toString(), password_tv.getText().toString(), user -> {
                if(user != null) {
                    toFeedActivity();
                } else {
                    //TODO: display error label to client
                }
            });

        });

        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), BaseActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}