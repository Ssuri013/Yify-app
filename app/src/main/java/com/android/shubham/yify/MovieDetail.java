package com.android.shubham.yify;


/**
 * Created by shubham on 09-Jun-17.
 */

public class MovieDetail {

    String title_long;
    String imgUrl;
    String des;
    String torrent;
    double rating;
    int runtime;


    MovieDetail(String a, String b, String c, String d,  double x, int y){
        title_long =a;
        des = b;
        imgUrl = c;
        torrent = d;
        rating = x;
        runtime = y;
    }

    public double getRating() {
        return rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getDes() {
        return des;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle_long() {
        return title_long;
    }

    public String getTorrent() {
        return torrent;
    }

}
