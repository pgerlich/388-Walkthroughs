package com.example.adapterswalkthrough;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup our list view to play with
        listview = (ListView) findViewById(R.id.listview);

        String[] views;

        //TODO: Small array of words
//        views = new String[]{"One","Two","Three","Four"};

        //TODO: Empty array example
//        views = new String[0]; //TODO: Show an empty string to show the empty string textview display

        //TODO: Huge array of words
        views = new String[50];
        for(int i = 0; i < 50; i++){
            views[i] = "I am a huge list murray";
        }

        //TODO: Base/default behaviour example
//        setupListViewWithBaseArrayAdapter(views);

        //TODO: Custom BASIC adapter example
//        setupListViewWithCustomArrayAdapter(views);

        //TODO: Complex custom example
        try {
            createHashmapAndDisplayWithCustomArrayAdapter(views);
        } catch (JSONException e) {
            Toast.makeText(this, "You messed up.", Toast.LENGTH_SHORT).show();
        }
    }

    //Loads up the list with default behaviour TODO: Default example
//    public void setupListViewWithBaseArrayAdapter(String[] views){
//        //Notice the base adapter takes a view.
//        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, views);
//        listview.setAdapter(adapter);
//        listview.setEmptyView((TextView) findViewById(R.id.emptyListViewText));
//    }

    //Loads up the list with a custom adapter and a TODO:BASIC EXAMPLE
//    public void setupListViewWithCustomArrayAdapter(String[] views){
//        //Our custom adapter doesn't take a view. Ours accesses it from within the class - so it is paired specifically to our listarrayadapter class
//        ListArrayAdapter adapter = new ListArrayAdapter(MainActivity.this, views);
//        listview.setAdapter(adapter);
//        listview.setEmptyView((TextView) findViewById(R.id.emptyListViewText));
//    }

    //Loads up the list with a custom adapter and a TODO: COMPLEX EXAMPLE
    public void createHashmapAndDisplayWithCustomArrayAdapter(String[] views) throws JSONException {
        HashMap<String, String> complexViews = new HashMap<>();

        //Put title value and another arbitrary value (say an int) in a hashmap to pass to our custom view.
        for(int i = 0; i < views.length; i++){
            JSONObject thisView = new JSONObject();
            thisView.put("Title", views[i]);
            thisView.put("ID", i);
            complexViews.put(String.valueOf(i), thisView.toString());
        }

        //I know, I know -- All this repeated code :(
        ListArrayAdapter adapter = new ListArrayAdapter(MainActivity.this, complexViews);
        listview.setAdapter(adapter);
        listview.setEmptyView((TextView) findViewById(R.id.emptyListViewText));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
