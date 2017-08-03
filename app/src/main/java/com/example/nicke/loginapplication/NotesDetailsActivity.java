package com.example.nicke.loginapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class NotesDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText titleHolder_editText,
             contentHolder_editText;

    Button saveNoteBtn,
           deleteNoteBtn;

    int id;

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);



        init();
        catchData();
        setListeners();
    }

    private void init() {
        titleHolder_editText = (EditText) findViewById(R.id.notes_title_holder);
        contentHolder_editText = (EditText) findViewById(R.id.notes_content);

        saveNoteBtn = (Button) findViewById(R.id.notes_save_button);
        deleteNoteBtn = (Button) findViewById(R.id.notes_delete_button);


    }

    private void catchData() {

        Intent intentCatch = getIntent();

        id = intentCatch.getIntExtra("noteId", 0);
        String title = intentCatch.getStringExtra("noteTitle");
        String content = intentCatch.getStringExtra("noteContent");

        if(intentCatch.hasExtra("noteTitle")) {
            titleHolder_editText.setText(title);
        }
        if(intentCatch.hasExtra("noteContent")) {
            contentHolder_editText.setText(content);
        }
    }

    private void setListeners() {
        saveNoteBtn.setOnClickListener(this);
        deleteNoteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notes_save_button:
                String titleMessage = titleHolder_editText.getText().toString();
                String contentMessage = contentHolder_editText.getText().toString();

                if(titleMessage.trim().equals("")) {
                    titleMessage = getResources().getText(R.string.noTitle).toString();
                }

                Date date = new Date();
                String formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).format(date);

                intent.putExtra("noteId", id);
                intent.putExtra("titleMessage", titleMessage);
                intent.putExtra("dateMessage", formatDate);
                intent.putExtra("contentMessage", contentMessage);

                setResult(1, intent);
                finish();

                break;
            case R.id.notes_delete_button:

                intent.putExtra("idToDelete", id);
                setResult(2, intent);
                finish();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
    }
}
