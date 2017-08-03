package com.example.nicke.loginapplication.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nicke.loginapplication.ActivityUserAccount;
import com.example.nicke.loginapplication.R;
import com.example.nicke.loginapplication.adapters.MovieAdapter;
import com.example.nicke.loginapplication.listeners.BackgroundListener;
import com.example.nicke.loginapplication.models.Movie;
import com.example.nicke.loginapplication.models.Movies;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicke on 7/22/2017.
 */

public class FragmentMovies extends Fragment {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;

    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.movie_recyclerView);
        startConnection();

        return view;
    }

    public void startConnection() {

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading Data..");
        dialog.show();

        new BackgroundTask(new BackgroundListener() {

            @Override
            public void onDownloadComplete(String json) {

                Gson gson = new Gson();

                Movie movie = gson.fromJson(json, Movie.class);

                List<Movies> movies = new ArrayList<>();
                boolean isInternetConnected = true;
                try {


                    for (int i = 0; i < movie.getMovies().size(); i++) {
                        movies.add(movie.getMovies().get(i));
                    }
                }catch (NullPointerException e) {
                    Log.e("NullPointerexception: ", "No internet connection probably");
                    isInternetConnected = false;
                }

                if (!isInternetConnected) {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }else {

                    movieAdapter = new MovieAdapter(movies, getContext());

                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        }).execute();

    }


    private class BackgroundTask extends AsyncTask<String, Void, String> {

        BackgroundListener backgroundListener;
        public BackgroundTask(BackgroundListener backgroundListener) {
            this.backgroundListener = backgroundListener;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection;

            try {
                URL url = new URL("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
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

            dialog.dismiss();

            super.onPostExecute(json);

            backgroundListener.onDownloadComplete(json);


        }
    }
}
