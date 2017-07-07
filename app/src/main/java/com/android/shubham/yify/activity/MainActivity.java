package com.android.shubham.yify.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.shubham.yify.R;

import butterknife.ButterKnife;

/**
 * Created by shubham on 13-Jun-17.
 */


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


}

//public class MainActivity extends AppCompatActivity
//        implements LoaderManager.LoaderCallbacks<ArrayList<MovieItem>>,MovieAdapter.ListItemClickListener {
//
//    final int LOADER_ID =13;
//    ArrayList<MovieItem> data = null;
//    MovieAdapter ma =null;
//    ProgressBar mLoadingIndicator;
//    Toast mToast;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_list);
//        ma = new MovieAdapter(this);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(ma);
//        final LoaderManager lm = getSupportLoaderManager();
//        lm.initLoader(LOADER_ID, null, this);
//
//    }
//
//    @Override
//    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, Bundle args) {
//        ConnectivityManager cm =
//                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//        if( !isConnected){
//            // SomeError.setVisibility(View.VISIBLE);
//            Log.e("no connetion", " ");
//            return null;
//
//        }
//        else {
//            AsyncTaskLoader<ArrayList<MovieItem>> atl = new AsyncTaskLoader<ArrayList<MovieItem>>(this) {
//
//                @Override
//                protected void onStartLoading() {
//                    forceLoad();
//                }
//
//                @Override
//                public ArrayList<MovieItem> loadInBackground() {
//                    return MovieUtil.urlBuilder(1);
//                }
//
//                @Override
//                public void deliverResult(ArrayList<MovieItem> data2) {
//                    super.deliverResult(data2);
//                }
//            };
//            return atl;
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data2) {
//        data =data2;
//        // swipe.setRefreshing(false);
////        ma.clear();
////        if(data!=null) {
////            ma.addAll(data);
////            SomeError.setVisibility(View.GONE);
////        }
////        else{
////            SomeError.setVisibility(View.VISIBLE);
////            Log.e("some error", " ");
////        }
//
//        if (data2 != null) {
//            //showWeatherDataView();
//            Log.e("hey", "reacg");
//           // Log.e("1st", data.get(0).getName() );
//           // ma.setmMovieData(data2);
//        } else {
//            // showErrorMessage();
//        }
//    }
//
//    @Override
//    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {
//
//    }
//
//    @Override
//    public void onListItemClick(int clickedItemIndex) {
//
//
//        Intent in = new Intent(this, MovieInfo.class);
//        in.putExtra("id",  String.valueOf(data.get(clickedItemIndex).getId() ) );
//        startActivity(in);
//    }

//}
