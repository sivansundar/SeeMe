package com.seeme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstScreen extends AppCompatActivity {


    @BindView(R.id.takenote_edittext)
    TextView takenoteEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.takenote_edittext)
    public void onViewClicked() {
        startActivity(new Intent(FirstScreen.this, CreateNoteActivity.class));

    }
}
