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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.seeme.Adapters.NoteListAdapter;
import com.seeme.Model.Note;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FirstScreen extends AppCompatActivity {

    private static String TAG = "SeeMe : First Screen : ";
    @BindView(R.id.item_recyclerView)
    RecyclerView itemRecyclerView;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    String UID;
    String DID;

    private List<Note> noteList;
    private NoteListAdapter noteListAdapter;

    @BindView(R.id.takenote_edittext)
    TextView takenoteEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        ButterKnife.bind(this);

        noteList = new ArrayList<>();
        noteListAdapter = new NoteListAdapter(noteList);

        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(noteListAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        UID = mUser.getUid();

        retrieveNotes(UID);
    }

    private void retrieveNotes(String userID) {

        mFirestore.collection("users").document(userID).collection("notes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "onEvent: ", e);
                    Toast.makeText(FirstScreen.this, "Error. Check log for details", Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.MODIFIED || doc.getType() == DocumentChange.Type.ADDED || doc.getType() == DocumentChange.Type.REMOVED) {
                        String logdetails = doc.getDocument().getString("Title");
                        Log.d(TAG, "onEvent: " + logdetails);
                        Note note = doc.getDocument().toObject(Note.class);
                        noteList.add(note);

                        noteListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @OnClick(R.id.takenote_edittext)
    public void onViewClicked() {
        startActivity(new Intent(FirstScreen.this, CreateNoteActivity.class));

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

}
