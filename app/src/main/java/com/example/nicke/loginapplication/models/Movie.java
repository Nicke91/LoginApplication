package com.example.nicke.loginapplication.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicke on 7/23/2017.
 */

public class Movie {
    private List<Movies> movies = new ArrayList<>();

    public List<Movies> getMovies() {
        return movies;
    }

    public void setMovies(List<Movies> movies) {
        this.movies = movies;
    }
}
