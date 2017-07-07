package com.android.shubham.yify.fragment;

/**
 * Created by shubham on 13-Jun-17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.shubham.yify.R;
import com.android.shubham.yify.YifyApp;
import com.android.shubham.yify.activity.MovieDetailActivity;
import com.android.shubham.yify.adapter.MovieAdapter;
import com.android.shubham.yify.adapter.MovieCursorAdapter;
import com.android.shubham.yify.data.MovieColumns;
import com.android.shubham.yify.data.MovieProvider;
import com.android.shubham.yify.views.PaddingDecorationView;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class  MovieSavedFragment extends Fragment implements MovieAdapter.OnMovieClickListener, LoaderManager.LoaderCallbacks<Cursor>,MovieCursorAdapter.OnMovieClickListener {

    private static final int CURSOR_LOADER_ID = 42;

    private Context context;
    private Unbinder unbinder;

    private MovieCursorAdapter adapter;
    private GridLayoutManager layoutManager;

    private int viewType;

    @BindView(R.id.no_results)
    View noResults;
    @BindView(R.id.no_results_message)
    TextView noResultsMessage;
    @BindView(R.id.progress_circle)         View progressCircle;
    @BindView(R.id.movie_grid)
    RecyclerView recyclerView;

    // Fragment life cycle
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_saved, container, false);
        unbinder = ButterKnife.bind(this, v);
        setRetainInstance(true);

        // Initialize variable
        context = getContext();
        viewType = getArguments().getInt(YifyApp.VIEW_TYPE);

        // Setup RecyclerView
        adapter = new MovieCursorAdapter(context, this, null);
        layoutManager = new GridLayoutManager(context, getNumberOfColumns());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new PaddingDecorationView(context, R.dimen.recycler_item_padding));
        recyclerView.setAdapter(adapter);

        // Load movies from database
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        return v;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Cursor Loader
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case CURSOR_LOADER_ID:
                // Returns a new CursorLoader
                Uri contentUri;
                if (viewType == YifyApp.VIEW_TYPE_WATCHED) {
                    contentUri = MovieProvider.Watched.CONTENT_URI;
                } else {
                    contentUri = MovieProvider.ToSee.CONTENT_URI;
                }
                return new CursorLoader(context, contentUri,
                        new String[]{ MovieColumns._ID, MovieColumns.TMDB_ID, MovieColumns.TITLE, MovieColumns.YEAR,
                                MovieColumns.OVERVIEW, MovieColumns.RATING, MovieColumns.POSTER},
                        null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            progressCircle.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
            noResultsMessage.setText(R.string.saved_empty);
        } else {
            progressCircle.setVisibility(View.GONE);
            noResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.swapCursor(data);
        }
        // Load detail fragment if in tablet mode
//        if (isTablet) {
//            if (data.getCount() > 0) {
//                data.moveToFirst();
//                loadDetailFragmentWith(data.getString(data.getColumnIndex(MovieColumns.TMDB_ID)));
//            } else {
//                loadDetailFragmentWith("null");
//            }
//        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    // Helper methods
    public int getNumberOfColumns() {
        // Get screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float widthPx = displayMetrics.widthPixels;
        // Calculate desired width
        SharedPreferences preferences = context.getSharedPreferences(YifyApp.TABLE_USER, Context.MODE_PRIVATE);
        if (preferences.getInt(YifyApp.VIEW_MODE, YifyApp.VIEW_MODE_GRID) == YifyApp.VIEW_MODE_GRID) {
            float desiredPx = getResources().getDimensionPixelSize(R.dimen.movie_card_width);
            int columns = Math.round(widthPx / desiredPx);
            return columns > 2 ? columns : 2;
        } else {
            float desiredPx = getResources().getDimensionPixelSize(R.dimen.movie_list_card_width);
            int columns = Math.round(widthPx / desiredPx);
            return columns > 1 ? columns : 1;
        }
    }
    public void refreshLayout() {
        Parcelable state = layoutManager.onSaveInstanceState();
        layoutManager = new GridLayoutManager(getContext(), getNumberOfColumns());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.onRestoreInstanceState(state);
    }
//    public void loadDetailFragmentWith(final String id) {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                ((MovieActivity)getActivity()).loadDetailFragmentWith(id);
//            }
//        });
//    }

    // Click events
    @Override
    public void onMovieClicked(int position) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);
//        if (isTablet) {
//            loadDetailFragmentWith(cursor.getString(cursor.getColumnIndex(MovieColumns.TMDB_ID)));
//        } else {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(YifyApp.MOVIE_ID, cursor.getString(cursor.getColumnIndex(MovieColumns.TMDB_ID)));
        startActivity(intent);
        //   }
    }
}
