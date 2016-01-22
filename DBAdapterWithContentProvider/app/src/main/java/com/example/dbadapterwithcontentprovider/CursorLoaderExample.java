package com.example.dbadapterwithcontentprovider;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.dbadapterwithcontentprovider.ContentProvider.ToDoContract;
import com.example.dbadapterwithcontentprovider.SQLMagic.ToDoDBAdapter;
import com.paad.todolist.R;

public class CursorLoaderExample extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int URL_LOADER = 0;
    private ListView mListView;
    private SimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursor_loader_example);

        getLoaderManager().initLoader(URL_LOADER, null, this);
        mListView = (ListView) findViewById(R.id.cursorLoaderListView);

        setupListViewAdapter();
    }

    /*
    * Callback that's invoked when the system has initialized the Loader and
    * is ready to start the query. This usually happens when initLoader() is
    * called. The loaderID argument contains the ID value passed to the
    * initLoader() call.
    */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle)
    {
    /*
     * Takes action based on the ID of the Loader that's being created
     */
        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getApplicationContext(),   // Parent activity context
                        ToDoContract.CONTENT_URI,        // Table to query
                        new String[] {ToDoContract.COL_ID, ToDoContract.COL_TASK, ToDoContract.COL_CREATION_DATE},     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //TODO: Makes your code a bit more modular
        mCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Clears out the adapter's reference to the Cursor.
         * This prevents memory leaks.
         */
        mCursorAdapter.changeCursor(null);
    }

    /**
     * Sets up the list view with an adapter.
     */
    public void setupListViewAdapter(){
        String[] mWordListColumns =
                {
                        ToDoContract.COL_TASK,   // Contract class constant containing the task column name
                        ToDoContract.COL_CREATION_DATE  // Contract class constant containing the creation date
                };

        int[] mWordListItems = { R.id.row, R.id.rowDate};

        mCursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),               // The application's Context object
                R.layout.todolist_item,                  // A layout in XML for one row in the ListView
                null,                               // The result from the query
                mWordListColumns,                      // A string array of column names in the cursor
                mWordListItems,                        // An integer array of view IDs in the row layout
                0);

        mListView.setAdapter(mCursorAdapter);
    }

}
