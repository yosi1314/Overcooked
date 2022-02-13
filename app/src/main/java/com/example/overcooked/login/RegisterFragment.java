package com.example.overcooked.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.overcooked.R;
import com.example.overcooked.helpers.IntentHelper;
import com.example.overcooked.model.Model;

public class RegisterFragment extends Fragment {
    private IntentHelper intentHelper = new IntentHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        TextView emailTv = view.findViewById(R.id.register_email_tv);
        TextView passwordTv = view.findViewById(R.id.register_password_tv);
        TextView confirmPasswordTv = view.findViewById(R.id.register_confirm_password_tv);
        Button registerButton = view.findViewById(R.id.register_signup_btn);
        TextView goToSignIn = view.findViewById(R.id.register_signin_tv);

        goToSignIn.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        registerButton.setOnClickListener(v -> {
            String email = emailTv.getText().toString();
            String password = passwordTv.getText().toString();
            String passwordConfirmation = confirmPasswordTv.getText().toString();

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTv.setError("Email is not valid");
                return;
            }

            if (!password.equals(passwordConfirmation)) {
                confirmPasswordTv.setError("Passwords do not match");
                return;
            }

            Model.instance.signUp(email, password, user -> {
                if(user != null) {
                    intentHelper.toFeedActivity(this);
                } else {
                    //TODO: display error label to client
                }
            });
        });

        return view;
    }
}