package com.example.gesturesactionbarnotifications;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Paul on 9/25/2015.
 */
public class CustomViewGroup extends RelativeLayout {

    //Gesture tracking variables
    float y1, y2 = 0;
    float x1 ,x2 = 0;
    static final float SWIPETHRESHOLD = 100;

    Context context;

    boolean disallowTouchEvents = false;

    public CustomViewGroup(Context context) {
        super(context);
        this.context = context;
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * Called when any child view is experiencing a touch event. We can analyze the event and determine if we want to intercept it or not
     *
     * TODO: Parent --> Child
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }

    /**
     * Regular on touch event action
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( disallowTouchEvents ) {
            return false;
        }

        switch (event.getAction()){
            //Record x when we start a motion event
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                x1 = event.getX();
                yell("ViewGroup: Seeing action");
                break;

            //Record x when we finish, and act if our criteria are met (horizontal change in 35 pixels)
            case MotionEvent.ACTION_UP:
                y2 = event.getY();
                x2 = event.getX();

                //Check for vetical movement of 35px (in either direction
                if ( Math.abs(y1 - y2) > SWIPETHRESHOLD ) {

                    //Determine if we went down --> up (y2 < y1) or vice versa
                    if ( y2 < y1 ) {
                        yell("ViewGroup: Swipe Up");
                    } else {
                        yell("ViewGroup: Swipe Down");
                    }

                    return true;
                }

                //Check for horizontal movement of 35px (in either direction
                if ( Math.abs(x1 - x2) > SWIPETHRESHOLD ) {

                    //Determine if we went down --> up (y2 < y1) or vice versa
                    if ( x2 > x1 ) {
                        yell("ViewGroup: Swipe Right");
                    } else {
                        yell("ViewGroup: Swipe Left");
                    }

                    return true;
                }
        }
        return true;
    }

    /*
        If a child view has requested all parents don't interrupt it!
        TODO: Child --> Parent
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept){
        //Always pass to parent view
        this.getParent().requestDisallowInterceptTouchEvent(disallowIntercept);

        disallowTouchEvents = disallowIntercept;
    }

    /** Used for sending toasts */
    public void yell(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}