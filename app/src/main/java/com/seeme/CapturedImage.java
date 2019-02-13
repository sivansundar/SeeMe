package com.seeme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Surface;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.nio.ByteBuffer;

import javax.xml.transform.Result;

public class CapturedImage extends AppCompatActivity {

    private static final String TAG = "Captured Image LOG : ";
    private byte [] capturedImgArray;

    private static final String MY_CAMERA_ID = "my_camera_id";


    private Bitmap bmp;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        capturedImgArray = getIntent().getByteArrayExtra("byteArray");
        bmp = BitmapFactory.decodeByteArray(capturedImgArray, 0, capturedImgArray.length);


        getImageMetaData();
    }

    private FirebaseVisionImageMetadata getImageMetaData() {


        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setWidth(480)   // 480x360 is sufficient by default but this can be changed.
                .setHeight(360)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21) //NV21 for older Camera API. Use YUV_420_888 for Camera2 API
                .build();

        return metadata;
    }


    FirebaseVisionImage image = FirebaseVisionImage.fromByteBuffer(ByteBuffer.wrap(capturedImgArray), getImageMetaData());

        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();


}
