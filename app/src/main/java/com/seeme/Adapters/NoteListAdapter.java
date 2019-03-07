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

package com.seeme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.seeme.FirstScreen;
import com.seeme.Model.Note;
import com.seeme.R;
import com.seeme.UpdateNoteActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private Context context;

    public List<Note> notes;
    private static String TAG = "NoteListAdapter LOG : ";
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    
    public String docID;

    public NoteListAdapter(List<Note> note) {

        this.notes=note;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_item.setText(notes.get(position).getTitle());
        holder.content_item.setText(notes.get(position).getContent());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: OnBindViewHolder + position : " + position);
                retrieveDocid(position);


            }
        });





    }

    private void retrieveDocid(int itemposition) {

        mFirestore.collection("users").document(mUser.getUid()).collection("notes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null) {
                    Log.e(TAG, "onEvent: ", e );
                }
                else {

                    docID = queryDocumentSnapshots.getDocuments().get(itemposition).getId();
                    Log.d(TAG, "onEvent: DOC ID  = " + docID);

                    Intent intent;
                    intent = new Intent(context, UpdateNoteActivity.class);
                    intent.putExtra("id", docID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                }


        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextView title_item;
        public TextView content_item;
        public CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();

            title_item = itemView.findViewById(R.id.item_title);
            content_item = itemView.findViewById(R.id.item_content);
            cardView = itemView.findViewById(R.id.noteItem_card);

        }
    }


}
