package com.android.shubham.yify.util;

/**
 * Created by shubham on 13-Jun-17.
 */


public class TextUtil {

    // Constructor
    private TextUtil() { }

    // Check if given string is null or empty
    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.equals("null") || str.equals(""));
    }

}
