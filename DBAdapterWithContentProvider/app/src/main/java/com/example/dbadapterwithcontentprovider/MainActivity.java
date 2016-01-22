package com.example.dbadapterwithcontentprovider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.dbadapterwithcontentprovider.ContentProvider.ToDoContract;
import com.paad.todolist.R;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.myListView);

        // setupListViewWithWords();
        setupListViewWithToDo();
    }

    /**
     * Populates a listview using a cursor adapter and a content resolver
     */
    public void setupListViewWithToDo(){
        Uri myQueryURI =  ToDoContract.CONTENT_URI;
        String[] projection = new String[]{ToDoContract.COL_TASK, ToDoContract.COL_CREATION_DATE};

        Cursor cursor =
                getContentResolver().query(myQueryURI,
                        projection, null,null, null);

        String[] mWordListColumns =
                {
                        ToDoContract.COL_TASK,   // Contract class constant containing the task column name
                        ToDoContract.COL_CREATION_DATE  // Contract class constant containing the creation date
                };

        int[] mWordListItems = { R.id.row, R.id.rowDate};

        SimpleCursorAdapter mCursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),               // The application's Context object
                R.layout.todolist_item,                  // A layout in XML for one row in the ListView
                cursor,                               // The result from the query
                mWordListColumns,                      // A string array of column names in the cursor
                mWordListItems,                        // An integer array of view IDs in the row layout
                0);

        mListView.setAdapter(mCursorAdapter);
    }

    /**
     * Displays the search dialog
     * @param view
     */
    public void showSearch(View view){
        onSearchRequested();
    }
}
