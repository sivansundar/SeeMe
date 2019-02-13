package com.seeme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.camerakit.CameraKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraKitView camera;
    @BindView(R.id.capture)
    FloatingActionButton capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);




    }


    @Override
    protected void onStart() {
        super.onStart();
        camera.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.onResume();
    }

    @Override
    protected void onPause() {
        camera.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        camera.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.capture)
    public void onViewClicked() {

        camera.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
             //   Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                Intent i = new Intent(MainActivity.this, CapturedImage.class);
                i.putExtra("byteArray", bytes);
                startActivity(i);

            }
        });

    }
}
