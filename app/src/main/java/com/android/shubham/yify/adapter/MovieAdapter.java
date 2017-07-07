package com.android.shubham.yify.adapter;

/**
 * Created by shubham on 09-Jun-17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shubham.yify.MovieItem;
import com.android.shubham.yify.R;
import com.android.shubham.yify.YifyApp;
import com.android.shubham.yify.api.ApiHelper;
import com.android.shubham.yify.api.VolleySingleton;
import com.android.shubham.yify.model.Movie;
import com.android.shubham.yify.util.TextUtil;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shubham on 17-May-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int imageWidth;
    private SharedPreferences sharedPref;

    public ArrayList<Movie> movieList;
    private final OnMovieClickListener onMovieClickListener;

    // Constructor
    public MovieAdapter(Context context, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.movieList = new ArrayList<>();
        this.onMovieClickListener = onMovieClickListener;
        sharedPref = context.getSharedPreferences(YifyApp.TABLE_USER, Context.MODE_PRIVATE);
        imageWidth = sharedPref.getInt(YifyApp.THUMBNAIL_SIZE, 0);   // Load image width for grid view
    }

    // RecyclerView methods
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (sharedPref.getInt(YifyApp.VIEW_MODE, YifyApp.VIEW_MODE_GRID));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // GRID MODE
            final ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
            ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Update width integer and save to storage for next use
                        int width = v.findViewById(R.id.movie_poster).getWidth();
                        if (width > imageWidth) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(YifyApp.THUMBNAIL_SIZE, width);
                            editor.apply();
                        }
                        // Unregister LayoutListener
                        v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });
            }
            return new MovieGridViewHolder(v, onMovieClickListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(0);
        Movie movie = movieList.get(position);
        if (viewType == YifyApp.VIEW_MODE_GRID) {
            // GRID MODE
            MovieGridViewHolder movieViewHolder = (MovieGridViewHolder) viewHolder;

            // Title and year
            movieViewHolder.movieName.setText(movie.title);
            movieViewHolder.releaseYear.setText(movie.year);
            // Load image
            if (!TextUtil.isNullOrEmpty(movie.backdropImage)) {
                String imageUrl = ApiHelper.getImageURL(movie.backdropImage, imageWidth);
                movieViewHolder.imageView.setImageUrl(imageUrl, VolleySingleton.getInstance().imageLoader);
                movieViewHolder.imageView.setVisibility(View.VISIBLE);
                movieViewHolder.defaultImageView.setVisibility(View.GONE);
            } else if (!TextUtil.isNullOrEmpty(movie.posterImage)) {
                String imageUrl = ApiHelper.getImageURL(movie.posterImage, imageWidth);
                movieViewHolder.imageView.setImageUrl(imageUrl, VolleySingleton.getInstance().imageLoader);
                movieViewHolder.imageView.setVisibility(View.VISIBLE);
                movieViewHolder.defaultImageView.setVisibility(View.GONE);
            } else {
                movieViewHolder.defaultImageView.setVisibility(View.VISIBLE);
                movieViewHolder.imageView.setVisibility(View.GONE);
            }
            // Display movie rating
            if (TextUtil.isNullOrEmpty(movie.rating) || movie.rating.equals("0")) {
                movieViewHolder.movieRatingIcon.setVisibility(View.GONE);
                movieViewHolder.movieRating.setVisibility(View.GONE);
            } else {
                movieViewHolder.movieRatingIcon.setVisibility(View.VISIBLE);
                movieViewHolder.movieRating.setVisibility(View.VISIBLE);
                movieViewHolder.movieRating.setText(movie.rating);
            }
        }
    }

    // ViewHolders
    public class MovieGridViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_card)
        CardView cardView;
        @BindView(R.id.movie_poster_default)
        ImageView defaultImageView;
        @BindView(R.id.movie_poster)
        NetworkImageView imageView;
        @BindView(R.id.movie_title)
        TextView movieName;
        @BindView(R.id.movie_year)
        TextView releaseYear;
        @BindView(R.id.movie_rating)
        TextView movieRating;
        @BindView(R.id.rating_icon)
        ImageView movieRatingIcon;

        public MovieGridViewHolder(final ViewGroup itemView, final OnMovieClickListener onMovieClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMovieClickListener.onMovieClicked(getAdapterPosition());
                }
            });
        }
    }



    // Click listener interface
    public interface OnMovieClickListener {
        void onMovieClicked(final int position);
    }
}
