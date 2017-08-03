package com.example.nicke.loginapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicke.loginapplication.R;
import com.example.nicke.loginapplication.models.Comment;

import java.util.List;

/**
 * Created by Nicke on 7/23/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    List<Comment> commentList;

    TextView commentName_textView,
             commentEmail_textView,
             comment_textView;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_comments_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        commentName_textView.setText(comment.getName());
        commentEmail_textView.setText(comment.getEmail());
        comment_textView.setText(comment.getBody());

    }

    @Override
    public int getItemCount() {
        try {
            return commentList.size();
        }catch (NullPointerException e) {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);

            commentName_textView = (TextView) itemView.findViewById(R.id.comment_name_item);
            commentEmail_textView = (TextView) itemView.findViewById(R.id.comment_email_item);
            comment_textView = (TextView) itemView.findViewById(R.id.comment_item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
