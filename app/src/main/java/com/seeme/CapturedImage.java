package com.seeme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class CapturedImage extends AppCompatActivity {

    private byte [] capturedImgArray;

    private Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        capturedImgArray = getIntent().getByteArrayExtra("byteArray");

        bmp = BitmapFactory.decodeByteArray(capturedImgArray, 0, capturedImgArray.length);




    }
}
