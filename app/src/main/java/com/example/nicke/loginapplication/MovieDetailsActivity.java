package com.example.nicke.loginapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicke.loginapplication.adapters.CommentAdapter;
import com.example.nicke.loginapplication.listeners.BackgroundListener;
import com.example.nicke.loginapplication.models.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



public class MovieDetailsActivity extends AppCompatActivity {

    TextView movieTitle_textView,
             movieTagline_textView,
             movieYear_textView,
             movieDuration_textView,
             movieDirector_textView,
             movieCast_textView,
             movieStory_textView;

    ImageView movieIcon_imageView;

    RatingBar movieRatingBar;

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        init();
        imageLoader();
        getData();

        startConectionForComments();
    }

    private void init() {
        movieTitle_textView = (TextView) findViewById(R.id.details_movie_title);
        movieTagline_textView = (TextView) findViewById(R.id.details_movie_tagline);
        movieYear_textView = (TextView) findViewById(R.id.details_movie_year);
        movieDuration_textView = (TextView) findViewById(R.id.details_movie_duration);
        movieDirector_textView = (TextView) findViewById(R.id.details_movie_director);
        movieCast_textView = (TextView) findViewById(R.id.details_movie_cast);
        movieStory_textView = (TextView) findViewById(R.id.details_movie_story);

        movieIcon_imageView = (ImageView) findViewById(R.id.details_movie_icon);

        movieRatingBar = (RatingBar) findViewById(R.id.details_movie_ratingBar);

        recyclerView = (RecyclerView) findViewById(R.id.details_movie_recyclerView);

    }

    private void imageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }


    private void getData() {
        Intent intent = getIntent();

        setTexts(intent);
        setImage(intent);

    }
    private void setTexts(Intent intent) {

        String yearResource = getResources().getString(R.string.year_textView);
        String durationResource = getResources().getString(R.string.duration_textView);
        String directorResource = getResources().getString(R.string.director_textView);
        String castResource = getResources().getString(R.string.cast_textView);


        movieTitle_textView.setText(intent.getStringExtra("movie"));
        movieYear_textView.setText(yearResource + " " + String.valueOf(intent.getIntExtra("year", 0)));
        movieTagline_textView.setText(intent.getStringExtra("tagline"));
        movieDuration_textView.setText(durationResource + " " + intent.getStringExtra("duration"));
        movieDirector_textView.setText(directorResource + " " + intent.getStringExtra("director"));

        movieCast_textView.setText(castResource + " " + intent.getStringExtra("cast"));
        movieStory_textView.setText(intent.getStringExtra("story"));


        movieRatingBar.setRating(intent.getFloatExtra("rating", 0));
    }
    private void setImage(Intent intent) {
        ImageLoader.getInstance().displayImage(intent.getStringExtra("image"), movieIcon_imageView);

    }

    private void startConectionForComments() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Data...");
        dialog.show();

        new BackroundTask(new BackgroundListener() {
            @Override
            public void onDownloadComplete(String json) {
                Gson gson = new Gson();
                List<Comment> commentList = gson.fromJson(json, new TypeToken<List<Comment>>(){}.getType());

                try {
                    commentAdapter = new CommentAdapter(commentList);
                    recyclerView.setAdapter(commentAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } catch (NullPointerException e) {
                    Toast.makeText(MovieDetailsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    private class BackroundTask extends AsyncTask<String, Void, String> {

        BackgroundListener backgroundListener;

        public BackroundTask(BackgroundListener backgroundListener) {
            this.backgroundListener = backgroundListener;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;

            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/comments");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder builder = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                return builder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            backgroundListener.onDownloadComplete(json);

            dialog.dismiss();
        }
    }



}
