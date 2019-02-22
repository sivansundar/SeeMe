package com.seeme;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.seeme.Fragments.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    //  1:58AM. Next batch of work listed below
    //  Setup Google Sign in Auth
    //  Design DB Schema for individual user (Firestore)
    //  Formulate effective means to process data from camera and store.

    private static final String TAG = "Captured Image LOG : ";


    @BindView(R.id.camera)
    CameraKitView camera;
    @BindView(R.id.capture)
    FloatingActionButton capture;
    @BindView(R.id.mainFrame)
    FrameLayout mainFrame;


    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loginFragment = new LoginFragment();
        goToLoginFragment(loginFragment);


    }

    private void goToLoginFragment(LoginFragment loginFragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, loginFragment);
        fragmentTransaction.commit();
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


    private void detetText(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result = textRecognizer.processImage(image)
                .addOnCompleteListener(new OnCompleteListener<FirebaseVisionText>() {
                    @Override
                    public void onComplete(@NonNull Task<FirebaseVisionText> task) {
                        String result = task.getResult().getText();
                        showAlert(result);
                        Log.d(TAG, "onComplete: \n" + result);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void showAlert(String result) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setMessage(result + "\n");
        alertDialog.show();

    }

 /*   @OnClick(R.id.capture)
    public void onViewClicked() {

        camera.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                //   Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Toast.makeText(MainActivity.this, "gfhfhkfkh", Toast.LENGTH_SHORT).show();

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                detetText(bitmap);

            }
        });

    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
