package com.seeme;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static String TAG = "CreateNoteActvity : ";
    @BindView(R.id.notetext_edittext)
    EditText notetextEdittext;
    String notetxt;
    @BindView(R.id.capture)
    ImageView capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

    }



    @Override
    public void onBackPressed() {
        notetxt = notetextEdittext.getText().toString();
        getNote();
        this.finish();
        Toast.makeText(this, "Back pressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }

    private void getNote() {

        Map<String, Object> note = new HashMap<>();
        note.put("content", notetxt);
        UID = "User1";
        DID = "DocID1";
        firebaseFirestore.collection(UID).document("DID").set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Snapshot written");
                Toast.makeText(CreateNoteActivity.this, "Successfully written", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @OnClick(R.id.capture)
    public void onViewClicked() {

        getNote();

    }
}
