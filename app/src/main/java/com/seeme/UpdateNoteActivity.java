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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateNoteActivity extends AppCompatActivity {

    @BindView(R.id.content_editText)
    TextInputEditText content_editText;
    @BindView(R.id.capture_btn)
    FloatingActionButton captureBtn;
    @BindView(R.id.title_edittext)
    TextInputEditText titleEdittext;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public String documentID;

    public static String TAG = "UpdateNoteActivity : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        ButterKnife.bind(this);

        documentID = getIntent().getStringExtra("id");

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        Toast.makeText(this, "" + documentID, Toast.LENGTH_SHORT).show();

        getDocDetails(documentID);
    }

    @Override
    public void onBackPressed() {

      updateNote();

        super.onBackPressed();
     }

    @Override
    protected void onPause() {
        Toast.makeText(this, "PAUSED", Toast.LENGTH_SHORT).show();
        updateNote();
        super.onPause();
    }

    private void updateNote() {

        String string_title = titleEdittext.getText().toString();
        String string_content = content_editText.getText().toString();

        HashMap<String, Object> updateHash = new HashMap<>();
        updateHash.put("title", string_title);
        updateHash.put("content", string_content);

        mFirestore.collection("users").document(mUser.getUid()).collection("notes").document(documentID).update(updateHash)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateNoteActivity.this, "UPDATED!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Update func ERROR", e );
                    }


                });


        Intent mIntent = new Intent(UpdateNoteActivity.this, FirstScreen.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);

    }

    private void getDocDetails(String docID) {
        mFirestore.collection("users").document(mUser.getUid()).collection("notes").document(docID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: ", e);
                } else {
                    String docTitle = documentSnapshot.get("title").toString();
                    String docContent = documentSnapshot.get("content").toString();

                    content_editText.setText(docContent);
                    titleEdittext.setText(docTitle);
                    Log.d(TAG, "onEvent: Document title = " + docTitle + "\n Document Content = " + docContent);


                }
            }
        });
    }
}
