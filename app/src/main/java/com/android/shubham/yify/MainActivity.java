package com.android.shubham.yify;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<MovieItem>>,MovieAdapter.ListItemClickListener {

    final int LOADER_ID =13;
    ArrayList<MovieItem> data = null;
    MovieAdapter ma =null;
    ProgressBar mLoadingIndicator;
    Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        ma = new MovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(ma);
        final LoaderManager lm = getSupportLoaderManager();
        lm.initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, Bundle args) {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if( !isConnected){
           // SomeError.setVisibility(View.VISIBLE);
            Log.e("no connetion", " ");
            return null;

        }
        else {
            AsyncTaskLoader<ArrayList<MovieItem>> atl = new AsyncTaskLoader<ArrayList<MovieItem>>(this) {

                @Override
                protected void onStartLoading() {
                    forceLoad();
                }

                @Override
                public ArrayList<MovieItem> loadInBackground() {
                    return MovieUtil.urlBuilder(1);
                }

                @Override
                public void deliverResult(ArrayList<MovieItem> data2) {
                    super.deliverResult(data2);
                }
            };
            return atl;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data2) {
        data =data2;
       // swipe.setRefreshing(false);
//        ma.clear();
//        if(data!=null) {
//            ma.addAll(data);
//            SomeError.setVisibility(View.GONE);
//        }
//        else{
//            SomeError.setVisibility(View.VISIBLE);
//            Log.e("some error", " ");
//        }

        if (data2 != null) {
            //showWeatherDataView();
            Log.e("hey", "reacg");
            Log.e("1st", data.get(0).getName() );
            ma.setmMovieData(data2);
        } else {
            // showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
       // COMPLETED (11) In the beginning of the method, cancel the Toast if it isn't null
        /*
         * Even if a Toast isn't showing, it's okay to cancel it. Doing so
         * ensures that our new Toast will show immediately, rather than
         * being delayed while other pending Toasts are shown.
         *
         * Comment out these three lines, run the app, and click on a bunch of
         * different items if you're not sure what I'm talking about.
         */
//        if (mToast != null) {
//            mToast.cancel();
//        }
//
//        // COMPLETED (12) Show a Toast when an item is clicked, displaying that item number that was clicked
//        /*
//         * Create a Toast and store it in our Toast field.
//         * The Toast that shows up will have a message similar to the following:
//         *
//         *                     Item #42 clicked.
//         */
//        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
//        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
//
//        mToast.show();

        Intent in = new Intent(this, MovieInfo.class);
        in.putExtra("id",  String.valueOf(data.get(clickedItemIndex).getId() ) );
        startActivity(in);
    }

}
