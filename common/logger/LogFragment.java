/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.common.logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Simple fragment which contains a TextView and uses is to output log data it receives
 * through the LogNode interface.
 */
public class LogFragment extends Fragment implements LogNode {

    // The next LogNode in the chain.
    LogNode mNext;

    public LogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.log_fragment, container, false);
    }

    public TextView getTextView() {
        return (TextView) getActivity().findViewById(R.id.log_output);
    }

    /** Outputs the string as a new line of log data in the TextView. */
    public void appendToLog(String s) {
        getTextView().append("\n" + s);
        ((ScrollView) getActivity().findViewById(R.id.log_scroll)).fullScroll(View.FOCUS_DOWN);
    }

    public LogNode getNext() {
        return mNext;
    }

    public void setNext(LogNode node) {
        mNext = node;
    }

    /** Takes a string and adds to it, with a seperator, if the bit to be added isn't null. Since
     * the logger takes so many arguments that might be null, this method helps cut out some of the
     * agonizing tedium of writing the same 3 lines over and over.
     * @param sourceStr The String to append to.
     * @param appendStr The String to append
     * @param delimiter The String to seperate the source and appendee strings. A tab or comma,
     *                  for instance.
     * @return The fully concatonated String
     */
    private String appendIfNotNull(String sourceStr, String appendStr, String delimiter) {
        if (appendStr != null) {
            if (appendStr.length() == 0) {
                delimiter = "";
            }
            sourceStr = TextUtils.concat(sourceStr, delimiter, appendStr);
        }
        return sourceStr;
    }

    /**
     * Formats the log data and prints it out to the TextView.
     * @param priority Log level of the data being logged.  Verbose, Error, etc.
     * @param tag Tag for for the log data.  Can be used to organize log statements.
     * @param msg The actual message to be logged. The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     *           to extract and print useful information.
     */
    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {
        String priorityStr = null;

        // For the purposes of this Fragment, we want to print the priority as readable text.
        switch(priority) {
            case Log.VERBOSE:
                priorityStr = "V";
                break;
            case Log.DEBUG:
                priorityStr = "D";
                break;
            case Log.INFO:
                priorityStr = "I";
                break;
            case Log.WARN:
                priorityStr = "W";
                break;
            case Log.ERROR:
                priorityStr = "E";
                break;
            case Log.ASSERT:
                priorityStr = "A";
                break;
            default:
                break;
        }

        // Handily, the Log class has a facility for converting a stack trace into a useable string.
        String exceptionStr = null;
        if (tr != null) {
            exceptionStr = Log.getStackTraceString(tr);
        }

        // Take the priority, tag, message, and exception, and concatonate as necessary
        // into one usable line of text.
        String outputStr =  "";
        outputStr = appendIfNotNull(outputStr, priorityStr, "\t");
        outputStr = appendIfNotNull(outputStr, tag, "\t");
        outputStr = appendIfNotNull(outputStr, msg, "\t");
        outputStr = appendIfNotNull(outputStr, exceptionStr, "\t");

        // Actually display the text we just generated within the TextView.
        appendToLog(outputStr);

        if (mNext != null) {
            mNext.println(priority, tag, msg, tr);
        }
    }
}
