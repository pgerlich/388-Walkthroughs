package com.example.dbadapterwithcontentprovider.UIAdaptersAndExtensions;

import java.text.SimpleDateFormat;
import android.content.Context;
import java.util.*;
import android.view.*;
import android.widget.*;

import com.example.dbadapterwithcontentprovider.SQLMagic.ToDoItem;
import com.paad.todolist.R;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

  int resource;
  private ArrayList<ToDoItem> mItems;
  private ArrayList<ToDoItem> mItemsBackup;
  private boolean filtering;

  public ToDoItemAdapter(Context _context, 
                             int _resource, 
                             ArrayList<ToDoItem> _items) {
    super(_context, _resource, _items);

    mItems = _items;

    //For updating
    filtering = false;

    //Tracking our visible items
    mItemsBackup = new ArrayList<>();
    mItemsBackup.addAll(mItems);

    resource = _resource;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LinearLayout todoView;

    ToDoItem item = mItems.get(position);

    String taskString = item.getTask();
    Date createdDate = item.getCreated();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String dateString = sdf.format(createdDate);

    if (convertView == null) {
      todoView = new LinearLayout(getContext());
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
      vi.inflate(resource, todoView, true);
    } else {
      todoView = (LinearLayout) convertView;
    }

    TextView dateView = (TextView)todoView.findViewById(R.id.rowDate);
    TextView taskView = (TextView)todoView.findViewById(R.id.row);
      
    dateView.setText(dateString);
    taskView.setText(taskString);

    return todoView;
  }

  @Override
  public void notifyDataSetChanged(){
    super.notifyDataSetChanged();

    //TODO: Keeping integrity of the backup for filtering
    if ( !filtering ) {
      mItemsBackup.clear();
      mItemsBackup.addAll(mItems);
    } else {
      filtering = false;
    }
  }

  /**
   * Function for filtering the data in the list
   * @param text The input text to filter
   */
  public void filter(String text) {
    text = text.toLowerCase();
    mItems.clear();

    filtering = true;

    if (text.length() == 0) {
      mItems.addAll(mItemsBackup);
    }
    else {

      for (ToDoItem todo : mItemsBackup)
      {

        //Add each item that meets the search term
        if ( (todo.getTask().toLowerCase().contains(text.toLowerCase()) ) )
        {
          mItems.add(todo);
        }
      }
    }

    notifyDataSetChanged();
  }

}