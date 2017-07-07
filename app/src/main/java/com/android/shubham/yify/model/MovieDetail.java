package com.android.shubham.yify.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shubham on 13-Jun-17.
 */


public class MovieDetail implements Parcelable {

    // Attributes
    public String id;
    public String title;

    public String releaseDate;
    public String runtime;
    public String overview;
    public String voteAverage;
    public String torrent720p;
    public String torrent1080p;


    public String posterImage;


    // Getters
    public String getYear() {
        String year = "";
        if (releaseDate != null && !releaseDate.equals("null")) {
            year = releaseDate.substring(0, 4);
        }
        return year;
    }

    // Constructors
    public MovieDetail(String id, String title,  String releaseDate, String runtime,
                       String overview, String voteAverage, String posterImage, String torrent720p, String torrent1080p) {
        this.id = id;

        this.title = title;

        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.overview = overview;
        this.voteAverage = voteAverage;

        this.posterImage = posterImage;
        this.torrent720p = torrent720p;
        this.torrent1080p =torrent1080p;

    }
    public MovieDetail(Parcel in) {
        this.id = in.readString();

        this.title = in.readString();

        this.releaseDate = in.readString();
        this.runtime = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readString();


        this.posterImage = in.readString();

    }

    // Parcelable Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    // Helper methods
    public String getSubtitle() {
        try {
            boolean isReleaseDateNull = (releaseDate == null || releaseDate.equals("null"));
            boolean isRuntimeNull = (runtime == null || runtime.equals("null") || runtime.equals("0"));

            if (isReleaseDateNull && isRuntimeNull) {
                return "";
            } else if (isReleaseDateNull) {
                return runtime + " mins";
            } else if (isRuntimeNull) {
                return getFormattedDate();
            } else {
                return getFormattedDate() + "\n" + runtime + " mins";
            }
        } catch (Exception ex) {
            return "";
        }
    }
    private String getFormattedDate() {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = oldFormat.parse(releaseDate);
        } catch (Exception ignored) { }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMMM yyyy");
        return newFormat.format(date);
    }

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);

        out.writeString(title);

        out.writeString(releaseDate);
        out.writeString(runtime);
        out.writeString(overview);
        out.writeString(voteAverage);

        out.writeString(posterImage);

    }
    @Override
    public int describeContents() {
        return 0;
    }
}