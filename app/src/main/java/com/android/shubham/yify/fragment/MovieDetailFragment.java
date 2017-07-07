package com.android.shubham.yify.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shubham.yify.R;
import com.android.shubham.yify.YifyApp;
import com.android.shubham.yify.api.ApiHelper;
import com.android.shubham.yify.api.VolleySingleton;
import com.android.shubham.yify.data.MovieColumns;
import com.android.shubham.yify.data.MovieProvider;
import com.android.shubham.yify.model.MovieDetail;
import com.android.shubham.yify.util.TextUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MovieDetailFragment extends Fragment {

    // Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_text_holder)
    View toolbarTextHolder;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarSubtitle;
    // Main views
    @BindView(R.id.progress_circle)
    View progressCircle;
    @BindView(R.id.error_message)
    View errorMessage;
    @BindView(R.id.movie_detail_holder)
    NestedScrollView movieHolder;
    @BindView(R.id.fab_menu)
    FloatingActionMenu floatingActionsMenu;
    @BindView(R.id.fab_watched)
    FloatingActionButton watchedButton;
    @BindView(R.id.fab_to_see)
    FloatingActionButton toWatchButton;



    // Image views
    @BindView(R.id.poster_image)
    NetworkImageView posterImage;
    @BindView(R.id.poster_image_default)
    ImageView posterImageDefault;
    // Basic info
    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.movie_subtitle)
    TextView movieSubtitle;
    @BindView(R.id.movie_rating_holder)
    View movieRatingHolder;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.movie_vote_count)
    TextView movieVoteCount;
    // Overview
    @BindView(R.id.movie_overview_holder)
    View movieOverviewHolder;
    @BindView(R.id.movie_overview_value)
    TextView movieOverviewValue;
    private Unbinder unbinder;
    private String id;
    private MovieDetail movie;
    private boolean isMovieWatched;
    private boolean isMovieToWatch;



    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, v);

        // Setup toolbar
        toolbar.setTitle(R.string.loading);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.action_home));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        // Download movie details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(YifyApp.MOVIE_ID)
                && savedInstanceState.containsKey(YifyApp.MOVIE_OBJECT))) {
            id = getArguments().getString(YifyApp.MOVIE_ID);

            if (TextUtil.isNullOrEmpty(id)) {
                progressCircle.setVisibility(View.GONE);
                toolbarTextHolder.setVisibility(View.GONE);
                toolbar.setTitle("");
            } else {
                downloadMovieDetails(id);
            }
        } else {
            id = savedInstanceState.getString(YifyApp.MOVIE_ID);
            movie = savedInstanceState.getParcelable(YifyApp.MOVIE_OBJECT);
            onDownloadSuccessful();
        }

        // Setup FAB
        //updateFABs();
        movieHolder.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY < scrollY) {
                    floatingActionsMenu.hideMenuButton(true);
                } else {
                    floatingActionsMenu.showMenuButton(true);
                }
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movie != null && id != null) {
            outState.putString(YifyApp.MOVIE_ID, id);
            outState.putParcelable(YifyApp.MOVIE_OBJECT, movie);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        VolleySingleton.getInstance().requestQueue.cancelAll(this.getClass().getName());
        unbinder.unbind();
    }

    // Toolbar menu click
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        if (item.getItemId() == R.id.action_share) {
//            if (movie != null) {
//                // Share the movie
//                String shareText = getString(R.string.action_share_text, movie.title, ApiHelper.getMovieShareURL(movie.id));
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, movie.title);
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
//                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.action_share_using)));
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

    // JSON parsing and display
    private void downloadMovieDetails(String id) {//TODO:parse this
        String urlToDownload = ApiHelper.getMovieDetailLink(getActivity(), id);
        Log.e("json", "reached");
        Log.e("url", urlToDownload);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, urlToDownload, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            jsonObject = jsonObject.getJSONObject("data");
                            jsonObject = jsonObject.getJSONObject("movie");
                            Log.e("inside", "parsing");

                          //  String backdropImage = jsonObject.getString("backdrop_path");
                            String id = jsonObject.getString("id");

                            String overview = jsonObject.getString("description_intro");
                            String posterImage = jsonObject.getString("medium_cover_image");
                            String releaseDate = jsonObject.getString("date_uploaded");
                            String runtime = jsonObject.getString("runtime");

                            String title = jsonObject.getString("title");
                            String voteAverage = jsonObject.getString("rating");

                            JSONArray torrents = jsonObject.getJSONArray("torrents");

                            jsonObject = torrents.getJSONObject(0);
                            String torrent720p = jsonObject.getString("url");
                            jsonObject = torrents.getJSONObject(1);
                            String torrent1080p = jsonObject.getString("url");

                            movie = new MovieDetail(id, title, releaseDate, runtime, overview, voteAverage,
                                    posterImage, torrent720p, torrent1080p);

                            onDownloadSuccessful();

                        } catch (Exception ex) {
                            // Parsing error
                            onDownloadFailed();
                            Log.d("Parse Error", ex.getMessage(), ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Network error
                        Log.e("download", "failed");
                        onDownloadFailed();
                    }
                });
        request.setTag(this.getClass().getName());
        Log.e("json", "end");
        VolleySingleton.getInstance().requestQueue.add(request);
    }


    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieHolder.setVisibility(View.VISIBLE);
        floatingActionsMenu.setVisibility(View.VISIBLE);

        // Set title and tagline
        if (TextUtil.isNullOrEmpty(movie.title)) {
            toolbar.setTitle(movie.title);
            toolbarTextHolder.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("");
            toolbarTextHolder.setVisibility(View.VISIBLE);
            toolbarTitle.setText(movie.title);
            //toolbarSubtitle.setText(movie.tagline);
        }



        // Basic info
        if (!TextUtil.isNullOrEmpty(movie.posterImage)) {
            int posterImageWidth = (int) getResources().getDimension(R.dimen.movie_list_poster_width);
            posterImage.setImageUrl(ApiHelper.getImageURL(movie.posterImage, posterImageWidth),
                    VolleySingleton.getInstance().imageLoader);
        } else {
            posterImageDefault.setVisibility(View.VISIBLE);
            posterImage.setVisibility(View.GONE);
        }
        movieTitle.setText(movie.title);
        movieSubtitle.setText(movie.getSubtitle());
        if (TextUtil.isNullOrEmpty(movie.voteAverage) || movie.voteAverage.equals("0.0")) {
            movieRatingHolder.setVisibility(View.GONE);
        } else {
            movieRating.setText(movie.voteAverage);
           // movieVoteCount.setText(getString(R.string.detail_vote_count, movie.voteCount));
        }

        // Overview
        if (TextUtil.isNullOrEmpty(movie.overview)) {
            movieOverviewHolder.setVisibility(View.GONE);
        } else {
            movieOverviewValue.setText(movie.overview);
        }


    }

    private void onDownloadFailed() {
        errorMessage.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        movieHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }


    @OnClick(R.id.try_again)
    public void onTryAgainClicked() {
        errorMessage.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        downloadMovieDetails(id);
    }


    // FAB related functions
    private void updateFABs() {
        final String movieId = id;
        // Look in WATCHED table
        getLoaderManager().initLoader(42, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getContext(),
                        MovieProvider.Watched.CONTENT_URI,
                        new String[]{},
                        MovieColumns.TMDB_ID + " = '" + movieId + "'",
                        null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.getCount() > 0) {
                    isMovieWatched = true;
                    watchedButton.setLabelText(getString(R.string.detail_fab_watched_remove));
                    toWatchButton.setVisibility(View.GONE);
                } else {
                    isMovieWatched = false;
                    watchedButton.setLabelText(getString(R.string.detail_fab_watched_add));
                    toWatchButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
        // Look in TO_SEE table
        getLoaderManager().initLoader(43, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getContext(),
                        MovieProvider.ToSee.CONTENT_URI,
                        new String[]{},
                        MovieColumns.TMDB_ID + " = '" + movieId + "'",
                        null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.getCount() > 0) {
                    isMovieToWatch = true;
                    toWatchButton.setLabelText(getString(R.string.detail_fab_to_watch_remove));
                } else {
                    isMovieToWatch = false;
                    toWatchButton.setLabelText(getString(R.string.detail_fab_to_watch_add));
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }

    @OnClick(R.id.fab_watched)
    public void onWatchedButtonClicked() {
        if (!isMovieWatched) {
            // Add movie to WATCHED table
            ContentValues values = new ContentValues();
            values.put(MovieColumns.TMDB_ID, movie.id);
            values.put(MovieColumns.TITLE, movie.title);
            values.put(MovieColumns.YEAR, movie.getYear());
            values.put(MovieColumns.OVERVIEW, movie.overview);
            values.put(MovieColumns.RATING, movie.voteAverage);
            values.put(MovieColumns.POSTER, movie.posterImage);
            getContext().getContentResolver().insert(MovieProvider.Watched.CONTENT_URI, values);
            Toast.makeText(getContext(), R.string.detail_watched_added, Toast.LENGTH_SHORT).show();
            // Remove from "TO_SEE" table

            //TODO: COMPLETE THIS
            if (isMovieToWatch) {
                getContext().getContentResolver().
                        delete(MovieProvider.ToSee.CONTENT_URI,
                                MovieColumns.TMDB_ID + " = '" + id + "'",
                                null);
            }
        } else {
            // Remove from WATCHED table
            getContext().getContentResolver().
                    delete(MovieProvider.Watched.CONTENT_URI,
                            MovieColumns.TMDB_ID + " = '" + id + "'",
                            null);
            Toast.makeText(getContext(), R.string.detail_watched_removed, Toast.LENGTH_SHORT).show();
        }
        // Update FABs
        updateFABs();
    }

    @OnClick(R.id.button1080p)
    public void download1080p(){

        Log.e("intent", movie.torrent1080p);

        Intent i = new Intent(Intent.ACTION_VIEW);
      // no need// i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setType("application/x-bittorrent");
        i.setData(Uri.parse(movie.torrent1080p));
        startActivity(Intent.createChooser(i, "view"));

    }

    @OnClick(R.id.button720p)
    public void download720p(){

        Log.e("intent", movie.torrent720p);

        Intent i = new Intent(Intent.ACTION_VIEW);
        // no need// i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setType("application/x-bittorrent");
        i.setData(Uri.parse(movie.torrent720p));
        startActivity(Intent.createChooser(i, "view"));

    }



    @OnClick(R.id.fab_to_see)
    public void onToWatchButtonClicked() {
        if (!isMovieToWatch) {
            // Add movie to "TO SEE" table
            ContentValues values = new ContentValues();
            values.put(MovieColumns.TMDB_ID, movie.id);
            values.put(MovieColumns.TITLE, movie.title);
            values.put(MovieColumns.YEAR, movie.getYear());
            values.put(MovieColumns.OVERVIEW, movie.overview);
            values.put(MovieColumns.RATING, movie.voteAverage);
            values.put(MovieColumns.POSTER, movie.posterImage);
          //  values.put(MovieColumns.BACKDROP, movie.backdropImage);
            getContext().getContentResolver().insert(MovieProvider.ToSee.CONTENT_URI, values);
            Toast.makeText(getContext(), R.string.detail_to_watch_added, Toast.LENGTH_SHORT).show();
        } else {
            // Remove from "TO SEE" table
            getContext().getContentResolver().
                    delete(MovieProvider.ToSee.CONTENT_URI,
                            MovieColumns.TMDB_ID + " = '" + id + "'",
                            null);
            Toast.makeText(getContext(), R.string.detail_to_watch_removed, Toast.LENGTH_SHORT).show();
        }
        // Update FABs
         updateFABs();
    }
}