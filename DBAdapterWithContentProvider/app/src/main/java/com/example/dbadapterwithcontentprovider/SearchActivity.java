package com.example.dbadapterwithcontentprovider;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.paad.todolist.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ArrayList<String> simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        simpleList = new ArrayList<>();
        simpleList.add("bob");
        simpleList.add("john");
        simpleList.add("jake");

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, simpleList);
        ((ListView) findViewById(R.id.listView)).setAdapter(arrayAdapter);
    }

    public void doMySearch(String query){
        Toast.makeText(this, "Query text: " + query + " In our list?: " + String.valueOf(simpleList.contains(query.toLowerCase())), Toast.LENGTH_SHORT).show();
    }
}
