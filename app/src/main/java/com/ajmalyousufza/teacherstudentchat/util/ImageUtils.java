package com.ajmalyousufza.teacherstudentchat.util;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public String getStringImage(Bitmap bmp){
        if(bmp == null){
            return "";
        }
        Log.i("logggg","inside getStringimage");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes;
        String encodedImage;
        try{
            System.gc();
            imageBytes = baos.toByteArray();
            if((imageBytes.length/1024)>350){
                baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                imageBytes = baos.toByteArray();
            }
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }//catch(Exception e){
        //   Log.i("logggg","error: "+e.getMessage());
        //   return "";
        //}
        catch(OutOfMemoryError e){
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
    }
}
