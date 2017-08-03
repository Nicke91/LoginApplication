package com.example.nicke.loginapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicke.loginapplication.ActivityUserAccount;
import com.example.nicke.loginapplication.NotesDetailsActivity;
import com.example.nicke.loginapplication.R;
import com.example.nicke.loginapplication.SQLDatabase.SQLDatabaseManager;
import com.example.nicke.loginapplication.adapters.NoteAdapter;
import com.example.nicke.loginapplication.models.Notes;
import com.example.nicke.loginapplication.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * Created by Nicke on 7/21/2017.
 */

public class FragmentHome extends Fragment {

    TextView welcomeTextView,
             nameHolder;

    Button addButton;

    RecyclerView recyclerView;
    NoteAdapter notesAdapter;

    Notes note;

    List<Notes> notesList = new ArrayList<>();
    List<Notes> testList;

    String userName;

    SQLDatabaseManager sqlDatabaseManager;

    Bundle bundle;

    public static final int intentDelete = 2;
    public static final int intentSave = 1;

    public static final int RESULT_OK = 1;
    public static final int RESULT_DELETE = 2;
    public static final int RESULT_CANCEL = 3;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bundle = this.getArguments();
        String name = bundle.getString("name");
        userName = bundle.getString("username");

        init(view);


        setTexts(name);

        setListeners();

        return view;
    }

    private void init(View view) {
        welcomeTextView = (TextView) view.findViewById(R.id.welcome_textView);
        nameHolder = (TextView) view.findViewById(R.id.home_name_holder);

        addButton = (Button) view.findViewById(R.id.add_button);

        recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);

        sqlDatabaseManager = new SQLDatabaseManager(getContext());

        getNotes();

    }

    private void getNotes() {


        if((testList = sqlDatabaseManager.getNotes(userName)) != null) {
            notesList = testList;
            notesAdapter = new NoteAdapter(notesList, getContext());
            recyclerView.setAdapter(notesAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void setTexts(String name) {
        welcomeTextView.setText(getResources().getString(R.string.welcome));
        nameHolder.setText(name);
    }

    private void setListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotesDetailsActivity.class);
                startActivityForResult(intent, intentSave);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == intentSave) {
            if(resultCode == RESULT_OK) {

                addNote(data);

            } else if(resultCode == RESULT_CANCEL) {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == intentDelete) {
            if (resultCode == RESULT_OK) {
                editNote(data);
            }

            if(resultCode == RESULT_DELETE) {
                int idToDelete = data.getIntExtra("idToDelete", 0);

                sqlDatabaseManager.deleteNote(idToDelete);

                notesList.clear();
                notesList = sqlDatabaseManager.getNotes(userName);
                if(notesList != null) {

                    notesAdapter = new NoteAdapter(notesList, getContext());
                    notesAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(notesAdapter);

                }

                notesAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(notesAdapter);


            }else if(resultCode == RESULT_CANCEL) {
                Toast.makeText(getContext(), "Activity Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNote(Intent data) {
        String title = data.getStringExtra("titleMessage");
        String date = data.getStringExtra("dateMessage");
        String content = data.getStringExtra("contentMessage");

        note = sqlDatabaseManager.addNote(title, date, content, userName);

        notesList.add(note);

        notesAdapter = new NoteAdapter(notesList, getContext());
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void editNote(Intent data) {
        int id = data.getIntExtra("noteId", 0);
        String title = data.getStringExtra("titleMessage");
        String date = data.getStringExtra("dateMessage");
        String content = data.getStringExtra("contentMessage");

        for (int i = 0; i <notesList.size() ; i++) {
            if(id == notesList.get(i).getId()) {
                sqlDatabaseManager.changeNote(id, title, date, content);
                notesList.remove(notesList.get(i));

                Notes note = sqlDatabaseManager.getNote(id);
                notesList.add(note);
            }

        }


        notesAdapter = new NoteAdapter(notesList, getContext());
        recyclerView.setAdapter(notesAdapter);


    }

}
