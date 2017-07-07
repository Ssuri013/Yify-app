package com.android.shubham.yify.api;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.shubham.yify.YifyApp;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by shubham on 13-Jun-17.
 */


public class VolleySingleton {

    // Singleton Instance
    private static VolleySingleton instance;
    public static VolleySingleton getInstance() {
        if (instance == null) {
            instance = new VolleySingleton();
        }
        return instance;
    }

    // Member objects
    public RequestQueue requestQueue;
    public ImageLoader imageLoader;

    // Constructor
    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(YifyApp.getAppContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

}
