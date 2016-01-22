package com.example.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * This class contains methods that open, close, and query the agenda database.
 */
public class ItemDataSource {

    /**
     * Singleton instance of AgendaDataSource
     */
    private static ItemDataSource dsInstance = null;

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    /**
     * Array of all column titles in Events table
     */
    private String[] allColumns = { SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_MESSAGE};

    /**
     * Returns an instance of AgendaDataSource if it exists, otherwise creates
     * a new AgendaDataSource object and returns it
     * @param context
     * The Activity that called this method
     * @return
     * An instance of AgendaDataSource
     */
    public static ItemDataSource getInstance(Context context) {
        if (dsInstance == null) {
            dsInstance = new ItemDataSource(context.getApplicationContext());
        }
        return dsInstance;
    }

    /**
     * Constructor that should never be called by user
     * @param context
     * The Activity that called this method
     */
    private ItemDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Opens the Agenda database for writing
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the Agenda database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Insert an item into the database
     * @param title
     */
    public void createItem(String title) {

        // Put keys (row columns) and values (parameters) into ContentValues object
        ContentValues value = new ContentValues();
        value.put(SQLiteHelper.COLUMN_MESSAGE, title);


        // Insert ContentValues into row in events table and obtain row ID
        // HINT: database.insert(...) returns the id of the row you insert
        database.insert(SQLiteHelper.TABLE_ITEMS, null, value);

        //TODO: For this simple example I don't care about the ID - we just keep it for SQL Indexing purposes and uniqueness of columns
    }

    /**
     * Queries database for all events stored and creates list of Event objects
     * from returned data.
     * @return
     * List of all Event objects in database
     */
    public ArrayList<String> getAllItems() {
        ArrayList<String> items = new ArrayList<>();

        // Query of all events
        Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMS, allColumns, null,
                null, null, null, null);

        //TODO: Move to the first row - Returns FALSE if cursor is empty
        cursor.moveToFirst();

        //TODO: Returns true if we're pointing at the spot after the last element
        while (!cursor.isAfterLast()) {
            items.add(cursorToItem(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return items;
    }

    /*
     * Helper method to convert row data into Event
     */
    private String cursorToItem(Cursor cursor) {
        // TODO: Note I'm grabbing 1 (as 0 will be the ID of this item)

        int example = cursor.getColumnIndex(SQLiteHelper.COLUMN_ID);

        try {
            return cursor.getString(1);
        } catch (Exception e ) {
            //TODO: More specific error handling
            return "Not Found";
        }

    }

    public  void deleteItem(String title){
        //database.delete(SQLiteHelper.TABLE_ITEMS, SQLiteHelper.COLUMN_MESSAGE + " = " + title, null);
        database.delete(SQLiteHelper.TABLE_ITEMS, "? = ?", new String[] {SQLiteHelper.COLUMN_MESSAGE, title});
    }
}
