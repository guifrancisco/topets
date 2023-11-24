package com.example.topets.system;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

import com.example.topets.model.Pet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ImageHandler {
    public static Uri copyImageToAppStorage(Uri originalUri, Context context, String imageName){
        //create new file
        File newFile = new File(context.getFilesDir(), imageName);
        if(newFile.exists()){
            Log.i(ImageHandler.class.getSimpleName(), "Already existing image file: " + imageName);
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(originalUri);
            OutputStream outputStream = new FileOutputStream(newFile);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
            Log.i(ImageHandler.class.getSimpleName(), "created file: "+ Uri.fromFile(newFile).toString());
            return Uri.fromFile(newFile);

        } catch (IOException e) {
            Log.e(ImageHandler.class.getSimpleName(), "Unable to copy image to storage. Cause : " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public static Uri getProfileUriFromPetId(String petId, Context context){
        if(petId == null){return null;}

        File file = new File(context.getFilesDir(), petId+".jpg");
        if (!file.exists()){
            Log.e(ImageHandler.class.getSimpleName(), "No image file for pet id: " + petId);
            Log.i(ImageHandler.class.getSimpleName(), "File not found: " + file.getAbsolutePath());
            return null;
        }
        return Uri.fromFile(file);
    }
}
