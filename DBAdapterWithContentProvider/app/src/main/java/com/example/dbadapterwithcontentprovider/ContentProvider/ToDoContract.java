package com.example.dbadapterwithcontentprovider.ContentProvider;

import android.net.Uri;

/**
 * Created by Paul on 11/13/2015.
 */
public final class ToDoContract {
    //TODO: Authorities and tables - note table doesn't have to match our SQL table name
    public static final String AUTHORITY = "com.example.dbadapterwithcontentprovider.provider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/todo");

    //TODO: Columns - need to match for my example (cursor adapter)
    public static final String COL_ID = "_id";
    public static final String COL_TASK = "task";
    public static final String COL_CREATION_DATE = "creation_date";
}
