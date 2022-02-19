package com.example.overcooked.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.overcooked.model.interfaces.ImageOnCompleteListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Storage {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public Storage() {}

    public void uploadImage(Bitmap imageBitmap, String imageName, String storageLocation, ImageOnCompleteListener listener) {
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child(storageLocation + imageName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnCompleteListener(task -> imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Uri downloadUrl = uri;
                    listener.onComplete(downloadUrl.toString());
                }));
    }
}
