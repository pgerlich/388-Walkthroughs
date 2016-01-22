package com.example.fragments;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentContainer;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MainDisplayFragment.OnMainDisplayInteraction, CustomListFragment.OnListItemSelected {

    //Gesture tracking variables
    private float y1, y2 = 0;
    private float x1, x2 = 0;
    private static final float SWIPETHRESHOLD = 250;

    //Current dialog number (for displaying different dialog types)
    private int stackNum = 0;

    //Tracking our main fragment display for our fancy stuff
    private int currentListItem = 0;
    private String currentListTitle;
    private String currentListMessage;

    private SessionManagement session;

    private ItemDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup a fragment manager for accessing fragments
        FragmentManager fragmentManager = getFragmentManager();

        //Check if our fragment is null
        MainDisplayFragment mainDisplayFragment = (MainDisplayFragment) fragmentManager.findFragmentById(R.id.main_fragment);

        //If it's null, initialize our stuff
        if ( mainDisplayFragment == null ) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.main_fragment, new MainDisplayFragment());
            fragmentTransaction.add(R.id.list_fragment, new CustomListFragment());
            fragmentTransaction.commit(); //TODO: What if we don't commit?
        }

        //TODO: Shared preferences for storage
        session = new SessionManagement(getApplicationContext());

        //TODO: SQL lite for storage
        datasource = ItemDataSource.getInstance(getApplicationContext());
        datasource.open();
    }

    /**
     * Gesture tracking to show/hide the listFragment
     */
    public void checkGestureForSwipe(MotionEvent event){

        switch (event.getAction()){
            //Record x when we start a motion event
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                x1 = event.getX();
                break;

            //Record x when we finish, and act if our criteria are met (horizontal change in 35 pixels)
            case MotionEvent.ACTION_UP:
                y2 = event.getY();
                x2 = event.getX();

                //Check for vetical movement of 35px (in either direction
                if ( Math.abs(x1 - x2) > SWIPETHRESHOLD ) {

                    //Determine if we went left --> up (y2 < y1) or vice versa
                    if ( x2 < x1 ) {
                        hideListFragment();
                    } else {
                        showListFragment();
                    }
                }

        }

    }

    /**
     * Use swipe gestures to hide the list fragment
     */
    public void hideListFragment(){
        //Setup the animation and transition
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);

        //Grab our fragment
        CustomListFragment clf = (CustomListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);

        //TODO: Save to shared preferences
        session.putDrawerValues(clf.dummyItems);

        //TODO: Save to SQL
//        for(int i = 0; i < clf.dummyItems.size(); i++ ) {
//            datasource.createItem(clf.dummyItems.get(i));
//        }

        //Remove the fragment
        ft.remove(clf);
        ft.commit();

        makeFullScreen();
    }

    /**
     * Switch to full screen view
     */
    public void makeFullScreen(){
        setContentView(R.layout.fragment_main);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.base_fragment, MainDisplayFragment.newInstance(currentListTitle, currentListMessage));
        fragmentTransaction.commit(); //TODO: What if we don't commit?
    }

    /**
     * Switch to split screen view
     */
    public void makeSplitScreen(){
        setContentView(R.layout.activity_main);

        //Setup a fragment manager for accessing fragments
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, MainDisplayFragment.newInstance(currentListTitle, currentListMessage));
        fragmentTransaction.commit();

    }

    /**
     * Use gestures to show the list fragment
     */
    public void showListFragment(){
        makeSplitScreen();

        //Setup the animation and transition
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);

        //Create a new fragment
        CustomListFragment clf = new CustomListFragment();

        //Add it in
        ft.add(R.id.list_fragment, clf);
        ft.commit();
    }

    /**
     * Show a dialog
     * @param
     */
    public void showDialog() {
        stackNum++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = CustomDialogFragment.newInstance(stackNum);
        newFragment.show(ft, "dialog");
    }

    /**
     * Close the dialog
     */
    public void closeDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if (prev != null ) {
            ft.remove(prev);
        }

        ft.commit();
    }

    /**
     * When this is called, the Activity intercepts the touch event before any part of the window receives it.
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        checkGestureForSwipe(event);
        return super.dispatchTouchEvent(event);
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
        if (id == R.id.action_addItem) {
            //Notify that the data changed
            FragmentManager fm = getFragmentManager();
            CustomListFragment clf = (CustomListFragment) fm.findFragmentById(R.id.list_fragment);

            //Add a new dummy item
            clf.dummyItems.add("another" + (clf.dummyItems.size() - 1));
            clf.notifyDataSetChanged();

            return true;
        } else if ( id == R.id.action_show_dialog) {
            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Remove this item from the data
     * @param view
     */
    public void removeData(View view){
        //Grab our two fragments
        FragmentManager fm = getFragmentManager();
        CustomListFragment clf = (CustomListFragment) fm.findFragmentById(R.id.list_fragment);
        MainDisplayFragment mdf = (MainDisplayFragment) fm.findFragmentById(R.id.main_fragment);

        //Remove the item and notify the adapter
        if ( currentListItem >= 0 && currentListItem < clf.dummyItems.size() ) {
            clf.dummyItems.remove(currentListItem);
        }

        //Remove from database if applicable? TODO: Where clause is slightly broken, show if fixxed
        //datasource.deleteItem(currentListMessage);

        //Set a generic fragment (replace the old)
        currentListTitle = "Choose an Item";
        currentListMessage = "";

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, MainDisplayFragment.newInstance(currentListTitle, currentListMessage));
        ft.commit();


        clf.notifyDataSetChanged();

        //To prevent from deleting something again without selecting one
        currentListItem = -1;
    }

    @Override
    public void OnMainDisplayInteraction() {
        yell("Interacted with main screen");
    }

    /**
     * When an item is selected in the list fragment, update the main fragment
     * @param id
     */
    @Override
    public void OnListItemSelected(int id) {
        currentListItem = id;

        //Grab the fragment
        CustomListFragment clf = (CustomListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);

        //Make our new fragment
        currentListTitle = "Article #" + String.valueOf(id);
        currentListMessage = clf.dummyItems.get(id);

        //Setup a transaction
        MainDisplayFragment newFragment = MainDisplayFragment.newInstance(currentListTitle, currentListMessage);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //Replace fragment with new fragment
        transaction.replace(R.id.main_fragment, newFragment);
        transaction.addToBackStack(null);

        //Commit our changes
        transaction.commit();
    }

    /**
     * Used for toasts
     * @param message
     */
    public void yell(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
