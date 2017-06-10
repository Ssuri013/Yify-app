package com.android.shubham.yify;

/**
 * Created by shubham on 09-Jun-17.
 */

public class MovieItem {
    String name;//with year
    String posterUrl; //medium
    long id;
    double rating;
    MovieItem(String x, String y, long a, double b){
        name =x;
        posterUrl = y;
        id =a;
        rating = b;
    }

    String getName(){
        return name;
    }
    String getPosterUrl(){
        return posterUrl;
    }

    public double getRating() {
        return rating;
    }

    public long getId() {
        return id;
    }


}
