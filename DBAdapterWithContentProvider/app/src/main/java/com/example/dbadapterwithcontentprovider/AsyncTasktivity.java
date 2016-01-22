package com.example.dbadapterwithcontentprovider;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.paad.todolist.R;

public class AsyncTasktivity extends AppCompatActivity {

    private SeekBar mSeekBar;
    private DoStuff doStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_tasktivity);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        //new DoStuff().execute("Look", 5, true);
        doStuff = new DoStuff();
        doStuff.execute("Look", "At all", "My", "Strings!");
        doStuff.cancel(true); //TODO: Interrupting
    }

    /**
     *  Parameters:
     *  1: Elements passed to background task
     *  2: Progress update data type
     *  3: Result type
     *
     *  TODO: Any of these can be VOID if you don't need them. Often times that's the case
     */
    private class DoStuff extends AsyncTask<String, Integer, Boolean> {

        protected void onPreExecute(){
            Toast.makeText(getApplicationContext(), "Doing stuff on the main thread", Toast.LENGTH_SHORT).show();
        }

        //TODO: Note more than 3 seconds!
        protected Boolean doInBackground(String... whatever) {

            for(int i = 0; i < 1000; i++ ) {
                //TODO: In case you want to cancel the operation
                if ( isCancelled() ) {
                    return false;
                }

                //TODO: Make it visible..
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(i);
            }

            //TODO: Why doesn't this work?
            Toast.makeText(getApplicationContext(), "Doing stuff on the main thread", Toast.LENGTH_SHORT).show();

            return whatever.length % 2 == 0;
        }

        //TODO: Is only called manually
        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        //TODO: Not called on cancel!
        protected void onPostExecute(Boolean result) {
            Toast.makeText(getApplicationContext(), "Success?: " + String.valueOf(result), Toast.LENGTH_SHORT).show();
        }

        //TODO: Called on cancel
        protected void onCancelled(Boolean result){
            Toast.makeText(getApplicationContext(), "Canceled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setProgressPercent(Integer progress){
        mSeekBar.setProgress(progress);
    }

}
