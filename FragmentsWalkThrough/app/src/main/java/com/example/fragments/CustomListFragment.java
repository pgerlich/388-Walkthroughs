package com.example.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListItemSelected}
 * interface.
 */
public class CustomListFragment extends ListFragment {

    //Listener for communication with activity
    private OnListItemSelected mListener;

    //Adapter and dummy items for displaying
    private ArrayAdapter<String> listAdapter;
    public ArrayList<String> dummyItems;

    //session for storing and restoring data
    private  SessionManagement session;

    private ItemDataSource datasource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup the session for shared preferences given a context
        session = new SessionManagement(getActivity().getApplicationContext());

        //TODO: SQL lite for storage
//        datasource = ItemDataSource.getInstance(getActivity().getApplicationContext());
//        datasource.open();

        //TODO: Grab from shared preferences
        if ( session.getDrawerValues() != null ) {
            dummyItems = session.getDrawerValues();
        }

        //TODO: grab from SQL
//        ArrayList<String> potentialFromSQL = datasource.getAllItems();
//
//        if ( potentialFromSQL != null ) {
//            dummyItems = potentialFromSQL;
//        }

        else {
            dummyItems = new ArrayList<>();
            prePopulateArraylist();
        }

        //Just a basic adapter
        listAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, dummyItems);

        setListAdapter(listAdapter);
    }

    public void prePopulateArraylist(){
        dummyItems.add("one");
        dummyItems.add("two");
        dummyItems.add("three");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnListItemSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListItemSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void notifyDataSetChanged(){
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //Visual display of being selected
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.OnListItemSelected(position);
        }
    }

    public interface OnListItemSelected {
        public void OnListItemSelected(int id);
    }

}
