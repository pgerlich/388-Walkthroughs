package com.example.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionManagement {

    //The shared preference file
    public SharedPreferences pref;

    public Editor edit;

    //The context
    public Context _context;

    //The name of the shared preference storing the data
    private static final String prefName = "recipes_storage";

     /**
     * Createa a new session with the given context. Suppressed a warning that pref.edit
     * isn't actually commiting any changes.
     * @param context
     */
    @SuppressLint("CommitPrefEdits") public  SessionManagement(Context context) {
        _context = context;
        pref = context.getSharedPreferences(prefName, 0); // 0 - for private mode
        edit = pref.edit();
    }

    /**
     * Store our drawer values
     * @param values
     */
    public void putDrawerValues(ArrayList<String> values){
        try {
            JSONArray valuesAsJSON = new JSONArray(values.toArray());
            edit.putString("values", valuesAsJSON.toString());
            edit.commit();
        } catch (JSONException e) {
            yell(e.getMessage());
        }
    }

    /**
     * Get the drawer values
     * @return
     */
    public ArrayList<String> getDrawerValues(){
        try {
            ArrayList<String> values = new ArrayList<>();
            JSONArray valuesAsJson = new JSONArray(pref.getString("values", "none"));
            for(int i = 0; i < valuesAsJson.length(); i++ ) {
                values.add(valuesAsJson.getString(i));
            }
            return values;
        } catch (JSONException e ) {
            yell(e.getMessage());
        }
        return null;
    }

    /**
     * Used to display toasts
     * @param message
     */
    private void yell(String message){
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }
}