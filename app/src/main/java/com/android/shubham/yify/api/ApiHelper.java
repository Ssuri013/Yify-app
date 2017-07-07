package com.android.shubham.yify.api;

import android.content.Context;
import android.net.Uri;

/**
 * Created by shubham on 13-Jun-17.
 */

public class ApiHelper {

    // Constructor
    private ApiHelper() { }

    // API Endpoints
    public static String getMostPopularMoviesLink(Context context, int page) {
        return "https://yts.ag/api/v2/list_movies.json?page=" + page ;
    }
    public static String getHighestRatedMoviesLink(Context context, int page) {
        return "https://yts.ag/api/v2/list_movies.json?page=" + page + "&&minimum_rating=7&&sort_by=rating";
    }
    public static String getUpcomingMoviesLink(Context context, int page) {
        return "https://yts.ag/api/v2/list_upcoming.json";
    }

    public static String getSearchMoviesLink(Context context, String query, int page) {
        return Uri.parse("\"https://yts.ag/api/v2/list_movies.json")
                .buildUpon()
                .appendQueryParameter("query_term", query)
                .appendQueryParameter("page", page + "")
                .build().toString();
    }
    public static String getMovieDetailLink(Context context, String id) {
        return "https://yts.ag/api/v2/movie_details.json?movie_id=" + id ;
    }

    // URLs for getting images
    public static String getImageURL(String baseURL, int widthPx) {
        //widthpx is used for IMDB
        return baseURL;
    }

}
