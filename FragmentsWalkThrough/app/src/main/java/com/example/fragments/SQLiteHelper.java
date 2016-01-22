package com.example.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class helps with database setup. None of its methods (except for the constructor)
 * should be called directly.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Table name
    public static final String TABLE_ITEMS = "items";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MESSAGE = "message";

    // Database name
    private static final String DATABASE_NAME = "items.db";

    // Increment this number to clear everything in database
    private static final int DATABASE_VERSION = 1;

    /**
     * Returns an instance of this helper object given the activity
     * @param context
     */
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //TODO: Don't copy & paste - you can paste in incorrectly formatted spaces that break everything.
        String rawSQL = "CREATE TABLE " + TABLE_ITEMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MESSAGE + " TEXT NOT NULL" +
                ");";

        //TODO: http://developer.android.com/intl/zh-cn/training/basics/data-storage/databases.html
        //TODO: http://developer.android.com/intl/zh-cn/reference/android/database/Cursor.html

        db.execSQL(rawSQL);
    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

}