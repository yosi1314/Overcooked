package com.example.overcooked.helpers;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.overcooked.feed.BaseActivity;
import com.example.overcooked.login.LoginActivity;

public class IntentHelper {

    public void toFeedActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), BaseActivity.class);
        fragment.startActivity(intent);
        fragment.getActivity().finish();
    }

    public void toLoginActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), LoginActivity.class);
        fragment.startActivity(intent);
        fragment.getActivity().finish();
    }

    public void toLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
