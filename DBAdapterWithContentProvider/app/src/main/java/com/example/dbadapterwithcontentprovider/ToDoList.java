package com.example.dbadapterwithcontentprovider;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dbadapterwithcontentprovider.SQLMagic.ToDoDBAdapter;
import com.example.dbadapterwithcontentprovider.SQLMagic.ToDoItem;
import com.example.dbadapterwithcontentprovider.UIAdaptersAndExtensions.ToDoItemAdapter;
import com.paad.todolist.R;

public class ToDoList extends ActionBarActivity {

  private static final String TEXT_ENTRY_KEY = "TEXT_ENTRY_KEY";
  private static final String ADDING_ITEM_KEY = "ADDING_ITEM_KEY";
  private static final String SELECTED_INDEX_KEY = "SELECTED_INDEX_KEY";

  private boolean addingNew = false;
  private ArrayList<ToDoItem> todoItems;
  private ListView myListView;
  private EditText myEditText;
  private ToDoItemAdapter aa;

  ToDoDBAdapter toDoDBAdapter;
  Cursor toDoListCursor;

  /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);

    myListView = (ListView)findViewById(R.id.myListView);
    myEditText = (EditText)findViewById(R.id.myEditText);

    todoItems = new ArrayList<ToDoItem>();
    int resID = R.layout.todolist_item;
    aa = new ToDoItemAdapter(this, resID, todoItems);
    myListView.setAdapter(aa);

      myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          final int finalPosition = position;
          new AlertDialog.Builder(ToDoList.this)
                  .setIcon(android.R.drawable.ic_dialog_alert)
                  .setTitle(R.string.deleteTitle)
                  .setMessage(R.string.delete)
                  .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      removeItem(finalPosition);
                    }

                  })
                  .setNegativeButton(R.string.no, null)
                  .show();

        }
    });

    registerForContextMenu(myListView);

    toDoDBAdapter = new ToDoDBAdapter(this);

    // Open or create the database
    toDoDBAdapter.open();

    populateTodoList();
  }

    /**
     * OnClick to create a new task
     * @param view
     */
  public void createTask(View view){
    String val = myEditText.getText().toString();
    if ( val.length() > 0 ) {
      ToDoItem newItem = new ToDoItem(val);
      toDoDBAdapter.insertTask(newItem);
      updateArray();
      myEditText.setText("");
      aa.notifyDataSetChanged();
    }
  }

  private void populateTodoList() {
    // Get all the todo list items from the database.
    toDoListCursor = toDoDBAdapter.getAllToDoItemsCursor();
    startManagingCursor(toDoListCursor);

    // Update the array.
    updateArray();
  }

  private void updateArray() {
	  toDoListCursor.requery();

	  todoItems.clear();

	  if (toDoListCursor.moveToFirst())
	    do {
	      String task = toDoListCursor.getString(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_TASK));
	      long created = toDoListCursor.getLong(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_CREATION_DATE));
          int id = toDoListCursor.getInt(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_ID));

	      ToDoItem newItem = new ToDoItem(task, new Date(created));
          newItem.setId(id);
	      todoItems.add(0, newItem);
	    } while(toDoListCursor.moveToNext());

	  aa.notifyDataSetChanged();
	}

    @Override
    protected void onResume(){
        super.onResume();

    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_todolist, menu);

    // Associate searchable configuration with the SearchView
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    searchView.setQueryHint("Find an Item");
    searchView.setIconifiedByDefault(false);
      searchView.setSubmitButtonEnabled(true);


    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            aa.filter(s);
            return false;
        }

    });

    return true;
  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_content_provider) {
            Intent cp = new Intent(this, MainActivity.class);
            startActivity(cp);
            return true;
        } else if ( id == R.id.action_cursor_loader) {
            Intent cl = new Intent(this, CursorLoaderExample.class);
            startActivity(cl);
        } else if ( id == R.id.action_async ) {
            Intent s = new Intent(this, AsyncTasktivity.class);
            startActivity(s);
        } else if ( id == R.id.action_bg_service ) {
            Intent intent = new Intent(this, FullService.class);
            startService(intent);
        }

        return super.onOptionsItemSelected(item);
    }

  @Override
  public void onDestroy() {
    super.onDestroy();

    // Close the database
    toDoDBAdapter.close();
  }

  private void removeItem(int _index) {
    // Items are added to the listview in reverse order, so invert the index.
    toDoDBAdapter.removeTask(todoItems.get(_index).getId());
    updateArray();
  }
}