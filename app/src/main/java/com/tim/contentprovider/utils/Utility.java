package com.tim.contentprovider.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Utility{

    private ImageView ivPerson;

//    public void save(Context context)
//    {
//        SharedPreferences preferences;
//        SharedPreferences.Editor editor;
//
//        preferences = context.getSharedPreferences("temp",1);
//        editor = preferences.edit();
//
//        ivPerson = (ImageView)
//
//        Bitmap bitmap = ((BitmapDrawable) ivPerson.getDrawable()).getBitmap();
//        String imageIncodedString = encodeToBase64(bitmap);
//
//        editor.putString("Photo", imageIncodedString);
//
//        editor.apply();
//    }
//
//    public void load()
//    {
//
//    }

    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image was encoded: ", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }

}
