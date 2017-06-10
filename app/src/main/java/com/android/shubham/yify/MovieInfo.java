package com.android.shubham.yify;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieInfo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetail> {

    String id;
    MovieDetail data;

    ImageView poster;
    TextView tv;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("id")!= null)
        {
            id = bundle.getString("id");
        }

        poster = (ImageView) findViewById(R.id.image_poster);
        tv = (TextView) findViewById(R.id.title_m);
        bt = (Button) findViewById(R.id.bt213 );
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("url", data.getTorrent());
            }
        });

        getSupportLoaderManager().initLoader(13, null, this);
    }


    void downloadTorrent(){
        Log.e("intent", data.getTorrent());

        Intent i = new Intent(Intent.ACTION_VIEW);
      // no need// i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setType("application/x-bittorrent");
        i.setData(Uri.parse(data.getTorrent()));
       // startActivity(Intent.createChooser(i, "view"));
    }

    @Override
    public Loader<MovieDetail> onCreateLoader(int id, Bundle args) {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if( !isConnected){

            Log.e("no connetion", " ");
            return null;
        }
                else {
            return new AsyncTaskLoader<MovieDetail>(this) {

                @Override
                protected void onStartLoading() {
                    forceLoad();
                }

                @Override
                public MovieDetail loadInBackground() {
                    return MovieUtil.singleMovie( getId2() );
                }

                @Override
                public void deliverResult(MovieDetail data2) {
                    super.deliverResult(data2);
                }
            };

        }

    }

    @Override
    public void onLoadFinished(Loader<MovieDetail> loader, MovieDetail data2) {
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
            Log.e("data", "retrived");
        } else {
            // showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieDetail> loader) {

    }

    String getId2(){
        return id;
    }

}
