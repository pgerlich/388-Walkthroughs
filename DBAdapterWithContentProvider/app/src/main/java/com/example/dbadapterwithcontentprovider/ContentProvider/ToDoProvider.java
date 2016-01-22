package com.example.dbadapterwithcontentprovider.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.dbadapterwithcontentprovider.SQLMagic.ToDoDBAdapter;

/**
 * Created by Paul on 11/13/2015.
 */
public class ToDoProvider extends ContentProvider {

    private ToDoDBAdapter toDoDBAdapter;

    // Creates a UriMatcher object.
    private static UriMatcher sUriMatcher;

    @Override
    public boolean onCreate() {
        toDoDBAdapter = new ToDoDBAdapter(getContext());
        toDoDBAdapter.open();

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //TODO: URI Matcher helps parse and match the specified URL so you don't have to do the heavy lifting
        //TODO: * vs # wildcard
        sUriMatcher.addURI("com.example.dbadapterwithcontentprovider.provider", "todo", 1);
        sUriMatcher.addURI("com.example.dbadapterwithcontentprovider.provider", "todo/#", 2);
        sUriMatcher.addURI("com.example.dbadapterwithcontentprovider.provider", "todo/*", 3);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch( sUriMatcher.match(uri) ) {
            //TODO: Grabbing all
            case 1:
                //TODO: because we have an SQLAdapter - we don't even have to pass along the project and selection
                //TODO: Which means we have easily mitigated sql injection risks
                return toDoDBAdapter.getAllToDoItemsCursor();

            //TODO: Grabbing by ID
            case 2:
                try {
                    toDoDBAdapter.getToDoItem(Integer.valueOf(uri.getLastPathSegment()));
                    //TODO: Why wouldn't this work?
                } catch ( NumberFormatException e ) {
                    throw new NullPointerException("Invalid ID Type");
                }
                break;

            //TODO: SearchView widget querying
            case 3:
                String query = uri.getLastPathSegment().toLowerCase();
                return toDoDBAdapter.getPathsMatchingQuery(query);
            default:
                toDoDBAdapter.getAllToDoItemsCursor();
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        switch ( sUriMatcher.match(uri) ) {
            case 1:
                return "vnd.android.cursor.dir/vnd.com.dbadapterwithcontentprovider.provider.todo";
            case 2:
                return "vnd.android.cursor.item/vnd.com.dbadapterwithcontentprovider.provider.todo";
            default:
                return "I don't know, man.";

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //TODO: No insertions
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //TODO: No deletions
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO: No updating
        return 0;
    }
}
