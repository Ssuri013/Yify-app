package com.android.shubham.yify;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by shubham on 09-Jun-17.
 */


public class MovieUtil {
    private static String LOG_TAG = MovieUtil.class.getSimpleName();
    private static final String BASE_ADR_LIST = "https://yts.ag/api/v2/list_movies.json";

    public static ArrayList<MovieItem> urlBuilder(int pg){
        Uri uri = Uri.parse(BASE_ADR_LIST).buildUpon().appendQueryParameter("sort_by", "desc").appendQueryParameter("page", Integer.toString(pg) ).build();

        URL url = null;
        String str= null;
        try {
            url =  new URL(uri.toString());
            Log.e("url is","" + url.toString());
            str = HttpRequestHandle(url);

            return JsonParser(str);

        }
        catch(MalformedURLException m){
            Log.e(LOG_TAG, "URL malformed");
        }
        catch (JSONException j){
            Log.e(LOG_TAG, "json error");
        }

        return null;
    }

    private static String HttpRequestHandle (URL url){
        HttpURLConnection conn=null;
        try{
            conn = (HttpURLConnection) url.openConnection();
            Scanner sc = new Scanner(conn.getInputStream()).useDelimiter("\\A");
            return sc.hasNext()?sc.next():null;
        }
        catch (IOException io){
            Log.e("ioexception",io.toString() + "   a");
        }
        finally{
            conn.disconnect();
        }
        return null;
    }

    private static ArrayList<MovieItem> JsonParser(String str) throws JSONException{
        if(str == null ){return null;}
        JSONObject obj = new JSONObject(str);
        ArrayList<MovieItem> list = new ArrayList<>();
        obj = obj.getJSONObject("data");
        JSONArray jr = obj.getJSONArray("movies");

        for(int i=0; i<jr.length(); i++){
            obj = jr.getJSONObject(i);
            list.add( new MovieItem( obj.getString("title_english"),obj.getString("medium_cover_image"), obj.getLong("id"),
                    obj.getDouble("rating")));
        }
        return list;
    }
    private static MovieDetail JsonParser2(String str) throws JSONException{
        if(str == null ){return null;}
        JSONObject obj = new JSONObject(str);
        obj = obj.getJSONObject("data");
        obj = obj.getJSONObject("movie");

        JSONArray torrents = obj.getJSONArray("torrents");
        JSONObject o = torrents.getJSONObject(0);
        return new MovieDetail(obj.getString("title_long"),   obj.getString("description_full"),
                obj.getString("medium_cover_img"), o.getString("url") ,
                obj.getDouble("rating"),  obj.getInt("runtime"));
    }


    public static MovieDetail singleMovie(String id) {
        Uri uri = Uri.parse("https://yts.ag/api/v2/movie_details.json").buildUpon()
                .appendQueryParameter("movie_id", id).build();

        URL url = null;
        String str = null;
        MovieDetail md = null;
        try {
            url = new URL(uri.toString());
            Log.e("url is", "" + url.toString());
            str = HttpRequestHandle(url);
            md = JsonParser2(str);


        } catch (MalformedURLException m) {

        }
        catch (JSONException je){

        }

       // Log.e("url", "https:\/\/yts.ag\/torrent\/download\/48B694395B872F0BB0BACF23E1AFE95BC937DAD9");

        return md;
    }


}
