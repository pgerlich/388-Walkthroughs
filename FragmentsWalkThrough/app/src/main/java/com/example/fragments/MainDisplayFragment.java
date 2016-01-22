package com.example.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainDisplayFragment.OnMainDisplayInteraction} interface
 * to handle interaction events.
 * Use the {@link MainDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDisplayFragment extends Fragment {

    //The activity
    private OnMainDisplayInteraction mListener;

    //Our views
    private TextView mTitle;
    private TextView mMessage;

    //Argument constants
    public static String TITLE = "title";
    public static String MSG = "message";

    // TODO: Rename and change types and number of parameters
    public static MainDisplayFragment newInstance(String title, String message) {
        //TODO: This is called when it is created programatically

        MainDisplayFragment fragment = new MainDisplayFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MSG, message);
        fragment.setArguments(args);
        return fragment;
    }


    public MainDisplayFragment() {
        //TODO: This is called when a fragment is created from the XML
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Grab the baseview
        View baseView = inflater.inflate(R.layout.fragment_main_display, container, false);

        //Setup our views
        mTitle = (TextView) baseView.findViewById(R.id.main_title);
        mMessage = (TextView) baseView.findViewById(R.id.main_message);

        //Set values
        if ( getArguments() != null ) {
            mTitle.setText(getArguments().getString(TITLE));
            mMessage.setText(getArguments().getString(MSG));
        }

        return baseView;
    }

    @Override //TODO: Note this is instantiated with an Activity as an argument which is the deprecated version (an older version of fragment associates to this method)
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMainDisplayInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainDisplayInteraction");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface for our main activity to implement so this fragment can communicate with it
     */
    public interface OnMainDisplayInteraction {

        public void OnMainDisplayInteraction();
    }

}
