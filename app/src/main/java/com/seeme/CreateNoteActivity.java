/*
 *
 *  * Copyright (c) 2018 Sivan Chakravarthy
 *  *  *
 *  *  * MIT License
 *  *  *
 *  *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  * of this software and associated documentation files (the "Software"), to deal
 *  *  * in the Software without restriction, including without limitation the rights
 *  *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  * copies of the Software, and to permit persons to whom the Software is
 *  *  * furnished to do so, subject to the following conditions:
 *  *  *
 *  *  * The above notice shall be included in all
 *  *  * copies or substantial portions of the Software.
 *  *  *
 *  *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  * SOFTWARE.
 *  *  *
 *  *
 *
 */

package com.seeme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateNoteActivity extends AppCompatActivity {


    FirebaseFirestore firebaseFirestore;
    String UID;
    String DID;
    String docTitle;
    String notetxt;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    private static String TAG = "CreateNoteActvity : ";
    @BindView(R.id.notetext_edittext)
    EditText notetextEdittext;
    @BindView(R.id.title_edittext)
    EditText titleEdittext;

    @BindView(R.id.capture_btn)
    FloatingActionButton captureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        UID = user.getUid();



    }

    @Override
    public void onBackPressed() {
        notetxt = notetextEdittext.getText().toString();
        docTitle = titleEdittext.getText().toString().trim();

        if (notetxt.isEmpty() && docTitle.isEmpty()) {
            Toast.makeText(this, "Content empty", Toast.LENGTH_SHORT).show();
        } else {

            writeNote(UID, notetxt, docTitle);
        }
        Toast.makeText(this, "Back pressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }

    private void writeNote(String userID, String noteText, String documentTitle) {

        Map<String, Object> docHashMap = new HashMap<>();
        docHashMap.put("title", documentTitle);
        docHashMap.put("content", noteText);

        firebaseFirestore.collection("users").document(userID).collection("notes").add(docHashMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d(TAG, "onSuccess: Note created successfully with title : " + documentTitle);
                        Toast.makeText(CreateNoteActivity.this, "Created successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure: ", e);
                        Toast.makeText(CreateNoteActivity.this, "Error occured. Check log", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @OnClick(R.id.capture_btn)
    public void onViewClicked() {

      dispatchTakePictureIntent();
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Toast.makeText(this, "Image captured", Toast.LENGTH_SHORT).show();
                }
    }


}
