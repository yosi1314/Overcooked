package com.example.overcooked.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.feed.BaseActivity;
import com.example.overcooked.model.Model;

public class LoginFragment extends Fragment {
    EditText emailEt;
    EditText passwordEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEt = view.findViewById(R.id.login_email_et);
        passwordEt = view.findViewById(R.id.login_password_et);
        Button loginBtn = view.findViewById(R.id.login_login_btn);
        Button goToSignUp = view.findViewById(R.id.login_signup_btn);

        goToSignUp.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
        });

        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            boolean shouldSubmit = isShouldSubmit(email, password);

            if(shouldSubmit) {
                Model.instance.signIn(email, password, user -> {
                    if(user != null) {
                        toFeedActivity();
                    } else {
                        //TODO: display error label to client
                    }
                });
            }
        });

        return view;
    }

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), BaseActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private boolean isShouldSubmit(String email, String password) {
        boolean shouldSubmit = true;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Invalid email");
            shouldSubmit = false;
        }

        if (email.isEmpty()) {
            emailEt.setError("Please fill in your email address");
            shouldSubmit = false;
        }

        if (password.isEmpty()) {
            passwordEt.setError("Please fill in your password");
            shouldSubmit = false;
        }
        return shouldSubmit;
    }
}