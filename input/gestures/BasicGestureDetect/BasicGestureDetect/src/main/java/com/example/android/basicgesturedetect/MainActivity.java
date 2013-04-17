/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.basicgesturedetect;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.Log;
import com.example.android.common.widgets.SimpleTextFragment;
import com.example.android.common.logger.MessageOnlyLogFilter;

/**
 * Sample application demonstrating how to use GestureDetector go detect when a user performs
 * a gesture that's recognized by the framework.  This example uses SimpleGestureDetector.  If you
 * want to detect
 * as well as customize that shortcut with metadata to send along to the application it activates.
 * Code is also included for removing your shortcut from the homescreen, for situations where that
 * is necessary (for instance, removing a shortcut to some data when that data is deleted from your
 * app).
 */
public class MainActivity extends FragmentActivity {

    public static final String TAG = "Basic Gesture Detector";

    // Reference to the fragment showing events, so we can clear it with a button as necessary.
    private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleTextFragment actionFragment = (SimpleTextFragment)
                    getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        actionFragment.setText(R.string.intro_message);
        actionFragment.getView().setClickable(true);
        actionFragment.getView().setFocusable(true);
        actionFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);


        // BEGIN_INCLUDE(init_detector)

        // First create the GestureListener that will include all our callbacks.
        // Then create the GestureDetector, which takes that listener as an argument.
        GestureDetector.SimpleOnGestureListener gestureListener = getGestureListener();
        final GestureDetector gd = new GestureDetector(this, gestureListener);

        /* For the view where gestures will occur, create an onTouchListener that sends
         * all motion events to the gesture detector.  When the gesture detector
         * actually detects an event, it will use the callbacks you created in the
         * SimpleOnGestureListener to alert your application.
        */

        actionFragment.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gd.onTouchEvent(motionEvent);
                return false;
            }
        });
        // END_INCLUDE(init_detector)

        initializeLogging();
    }

    /** Create a gesture listener which will be attached to the GestureDetector.
     *  This listener is where you can write all your callbacks for when certain events occur.
     * @return The listener your GestureDetector will use to inform your application when
     * the supported gestures occur.
     */
    public GestureDetector.SimpleOnGestureListener getGestureListener() {
        return new GestureListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sample_action:
                // Clears the log fragment when clicked.
                mLogFragment.getLogView().setText("");
                return true;
        }
        return false;
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}