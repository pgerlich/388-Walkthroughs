package com.example.filesandpreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView myBook;
    private TextView myBookName;

    private SharedPreferences sp;
    private SharedPreferences.Editor e;

    public final static String SHARED_PREF = "com.example.filesandpreferences.thehobbit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBook = (TextView) findViewById(R.id.myBook);
        myBookName = (TextView) findViewById(R.id.myBookName);

        PreferenceManager.setDefaultValues(this, R.xml.book_preferences, false);

        sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        //TODO: Mode: 0 (MODE_PRIVATE) --> Private (only this application) MODE_WORLD_READABLE && WRITEABLE (You can guess.)

        //sp = getPreferences(MODE_PRIVATE); //TODO: What happened?

        e = sp.edit();

        //Register us as a listener
        sp.registerOnSharedPreferenceChangeListener(this);

        loadFromPreferences(sp);
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
            Intent preferences = new Intent(this, NewSettingsActivity.class);
            startActivity(preferences);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
       super.onSaveInstanceState(outState); //TODO: What if we don't call this?
    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
       super.onRestoreInstanceState(inState); //TODO: What if we don't call this?
    }

    /**
     * Save our book in the file system
     * @param view
     */
    public void saveBookToFile(View view){
        File bookFile = getFileForBook();

        try {
            FileOutputStream out = new FileOutputStream(bookFile, true);
            OutputStreamWriter outputWriter=new OutputStreamWriter(out);
            outputWriter.write(myBook.getText().toString());
            outputWriter.close();

            yell("Saved book to file");
        } catch (FileNotFoundException e1) {
            yell (e1.getMessage());
        } catch (IOException e1) {
            yell (e1.getMessage());
        }

    }

    /**
     * Load our book from the file system
     * @param view
     */
    public void loadBookFromFile(View view){
        File bookFile = getFileForBook();

        try {
            FileReader fr = new FileReader(bookFile);
            BufferedReader br = new BufferedReader(fr);

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            myBook.setText(sb.toString());

            br.close();

            yell("Loaded book from file");
        } catch (FileNotFoundException e1) {
            yell(e1.getMessage());
        } catch (IOException e1) {
            yell(e1.getMessage());
        }

    }

    /**
     * Grabs the name of the book title and returns the file associated to it
     * @return
     */
    public File getFileForBook(){
        File bookFile;

        //TODO: External examples
//        String state = Environment.getExternalStorageState();
//        if ( Environment.MEDIA_MOUNTED.equals(state) ) {
//            yell("External Storage Available");
//             bookFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), myBookName.getText().toString()); //TODO: Store this for the public
//             bookFile = new File(getExternalFilesDir(null), myBookName.getText().toString()); //TODO: No applicable subdirectory && Private file
//        }

        //TODO: Internal Cache
//        try {
//            bookFile = File.createTempFile(myBookName.getText().toString(), null, getCacheDir());
//        } catch (IOException e) {
//            yell(e.getMessage());
//        }

        //TODO: internal
        bookFile = new File(getFilesDir(), myBookName.getText().toString());

        //yell(String.valueOf(bookFile.getFreeSpace())); //TODO: Rule of Thumb: <90% full or a few MB more than you need
        //yell(String.valueOf(bookFile.getTotalSpace()));

        return bookFile;
    }

    /**
     * Saves the book text to a preference value where
     * Key: myBookName textview value
     * Value: myBook textview value
     * @param view
     */
    public void saveBookToPreferences(View view){
        //TODO: Set values with Editor object

        e.putString(myBookName.getText().toString().replace(" ", ""), myBook.getText().toString());
        e.commit(); //TODO: Commit or it won't save. Where do you commit in a preference activity/fragment?

        yell("Saved book to preferences");
    }

    /**
     * Loads
     * @param view
     */
    public void loadBookFromPreferences(View view){
        //TODO: Get values from SharedPreferences object
        myBook.setText(sp.getString(myBookName.getText().toString().replace(" ",""), "An Empty Book!"));

        yell("Loaded book from preferences");
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadFromPreferences(sharedPreferences);
        //TODO: Not the best approach, but it avoids a huge switch statement or redundant code
    }

    /**
     * Loads the values from the preferences to adjust UI compoenents, etc.
     * @param sharedPref
     */
    public void loadFromPreferences(SharedPreferences sharedPref){
        boolean boldTitle = sharedPref.getBoolean("boldTitle", true);
        String bookFirstLine = sharedPref.getString("firstLine", "In a hole in the ground there lived a hobbit.");

        if ( boldTitle ) {
            myBookName.setTypeface(null, Typeface.BOLD);
        }

        myBook.setHint(bookFirstLine);
    }

    /**
     * Toast a msg
     * @param msg Message to be toasted
     */
    public void yell(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
