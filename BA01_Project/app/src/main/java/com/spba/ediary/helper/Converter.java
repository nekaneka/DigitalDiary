package com.spba.ediary.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.spba.ediary.views.CreateEntryActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Converter extends AppCompatActivity {

    private static Converter converter = null;
    private static final int PREFERRED_WIDTH = 250;
    private static final int PREFERRED_HEIGHT = 270;

    private Converter(){}

    public static Converter getConverter() {
        return converter = (converter == null ) ? new Converter() : converter;
    }

    public Bitmap convertUriToBitmap(Context context, Uri uri){
        Bitmap bitmap = null;

        try {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }



    public String bitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }

    public static Bitmap stringToBitmap(String image){

        byte [] encodeByte = Base64.decode(image,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        return bitmap;
    }


    public static Bitmap resizeBitmap(Bitmap bitmap) {

        Integer width = bitmap.getWidth();
        Integer height = bitmap.getHeight();
        float scaleWidth = PREFERRED_WIDTH / width.floatValue();
        float scaleHeight = PREFERRED_HEIGHT / height.floatValue();

        Matrix matrix = new Matrix();
        //matrix.postRotate(90);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();

        return resizedBitmap;
    }


}
