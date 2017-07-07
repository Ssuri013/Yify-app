package com.android.shubham.yify.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by shubham on 13-Jun-17.
 */

@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {

    public static final int VERSION = 1;

    @Table(MovieColumns.class) public static final String WATCHED = "watched";
    @Table(MovieColumns.class) public static final String TO_SEE = "to_see";
}

