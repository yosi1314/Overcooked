package com.example.overcooked.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.overcooked.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ImageHandlerFragment extends UtilsFragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    public ImageHandlerFragment(){}

    public void setImage(ImageView imv, String img, int defaultImage) {
        if (img != null) {
            Picasso.get().load(img).into(imv);
        } else {
            imv.setImageResource(defaultImage);
        }
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    public Bitmap onResult(int requestCode, int resultCode, @Nullable Intent data, ImageView imageImv) {
        Bitmap imageBitmap = null;
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        imageImv.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            try {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageImv.setImageBitmap(imageBitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return imageBitmap;
    }
}
