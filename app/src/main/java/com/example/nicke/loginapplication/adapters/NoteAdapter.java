package com.example.nicke.loginapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicke.loginapplication.ActivityUserAccount;
import com.example.nicke.loginapplication.NotesDetailsActivity;
import com.example.nicke.loginapplication.R;
import com.example.nicke.loginapplication.models.Notes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicke on 7/26/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    List<Notes> notesList;

    TextView titleHolderItem,
             dateHolderItem;

    Context context;

    public NoteAdapter(List<Notes> notesList, Context context) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_items, parent, false);

        return new MyViewHolder(view, notesList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notes note = notesList.get(position);

        titleHolderItem.setText(note.getTitle());
        dateHolderItem.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        List<Notes> notes = new ArrayList<>();

        public MyViewHolder(View itemView, final List<Notes> notes) {
            super(itemView);
            this.notes = notes;

            titleHolderItem = (TextView) itemView.findViewById(R.id.title_holder_item);
            dateHolderItem = (TextView) itemView.findViewById(R.id.date_holder_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Notes note = notes.get(position);

                    Intent intent = new Intent(context, NotesDetailsActivity.class);
                    intent.putExtra("noteTitle", note.getTitle());
                    intent.putExtra("noteContent", note.getContent());
                    intent.putExtra("noteId", note.getId());

                    ((Activity)context).startActivityForResult(intent, 2);
                }
            });
        }

    }


}
