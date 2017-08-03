package com.example.nicke.loginapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicke.loginapplication.MovieDetailsActivity;
import com.example.nicke.loginapplication.R;
import com.example.nicke.loginapplication.models.Cast;
import com.example.nicke.loginapplication.models.Movies;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicke on 7/23/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    List<com.example.nicke.loginapplication.models.Movies> movieList;


    TextView title_textView,
             year_textView,
             cast_textView;

    ImageView icon_imageView;

    public MovieAdapter(List<com.example.nicke.loginapplication.models.Movies> movieList, Context context) {
        this.movieList = movieList;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        notifyDataSetChanged();

        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent, false);

        return new MyViewHolder(view, movieList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        com.example.nicke.loginapplication.models.Movies movies = movieList.get(position);

        title_textView.setText(movies.getMovie());
        year_textView.setText(String.valueOf(movies.getYear()));

        String castNames = "";

        for (int i = 0; i <movies.getCast().size() ; i++) {
            if(i < movies.getCast().size() - 1) {
                castNames += movies.getCast().get(i).getName() + ", ";
            } else {
                castNames += movies.getCast().get(i).getName() + "\n";
            }
        }

        cast_textView.setText(castNames);

        ImageLoader.getInstance().displayImage(movies.getImage(), icon_imageView);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        List<Movies> movies = new ArrayList<>();

        public MyViewHolder(View itemView, List<Movies> movies) {
            super(itemView);
            this.movies = movies;
            title_textView = (TextView) itemView.findViewById(R.id.movieTitle_item);
            year_textView = (TextView) itemView.findViewById(R.id.movieYear_item);
            cast_textView =(TextView) itemView.findViewById(R.id.movieCast_item);

            icon_imageView = (ImageView) itemView.findViewById(R.id.movie_pic_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            Movies movie = movies.get(position);

            List<Cast> castList = new ArrayList<>();
            String cast = "";


            Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
            intent.putExtra("movie", movie.getMovie());
            intent.putExtra("year", movie.getYear());
            intent.putExtra("rating", movie.getRating());
            intent.putExtra("duration", movie.getDuration());
            intent.putExtra("director", movie.getDirector());
            intent.putExtra("tagline", movie.getTagline());

            for (int i = 0; i <movie.getCast().size() ; i++) {
                castList.add(movie.getCast().get(i));
                if (i < movie.getCast().size() - 1) {
                    cast += castList.get(i).getName() + ", ";
                } else {
                    cast += castList.get(i).getName();
                }
            }

            intent.putExtra("cast", cast);
            intent.putExtra("image", movie.getImage());
            intent.putExtra("story", movie.getStory());

            v.getContext().startActivity(intent);



        }
    }


    //Ova dva metoda sredjuju adapter da ne duplira podatke
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
