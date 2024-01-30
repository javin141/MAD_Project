package com.sp.mad_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    public static Bitmap compressImage(Uri imageUri, Context context) throws IOException {
        InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
        Bitmap originalBitmap = BitmapFactory.decodeStream(imageStream);

        // Specify the desired width and height
        int desiredWidth = 800;
        int desiredHeight = 600;

        // Create a new scaled bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);

        // Compress the scaled bitmap
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

        // Decode the compressed stream into a new bitmap
        byte[] compressedBytes = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.length);
    }
    public static byte[] getBytes(android.graphics.Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static android.graphics.Bitmap getImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

}
