package com.example.overcooked.helpers;

import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class UtilsFragment extends Fragment {
    ProgressBar progressBar;

    public UtilsFragment() {}

    public void showProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;

        progressBar.setVisibility(View.VISIBLE);
        try {
            ((AppCompatActivity)getActivity())
                    .getWindow()
                    .setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    );
        } catch (NullPointerException ignored) { }

    }

    public void hideProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;

        progressBar.setVisibility(View.GONE);
        try {
            ((AppCompatActivity)getActivity())
                    .getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (NullPointerException ignored) {}
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(progressBar != null) {
            hideProgressBar(progressBar);
        }
    }
}
