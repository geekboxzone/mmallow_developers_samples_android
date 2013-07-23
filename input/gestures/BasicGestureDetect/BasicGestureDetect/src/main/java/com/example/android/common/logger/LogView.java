package com.example.android.common.logger;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.*;
import android.widget.TextView;

/**
 * Created by alexlucas on 6/4/13.
 */


public class LogView extends TextView implements LogNode {

    public LogView(Context context) {
        super(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Formats the log data and prints it out to the LogView.
     * @param priority Log level of the data being logged.  Verbose, Error, etc.
     * @param tag Tag for for the log data.  Can be used to organize log statements.
     * @param msg The actual message to be logged. The actual message to be logged.
     * @param tr If an exception was thrown, this can be sent along for the logging facilities
     *           to extract and print useful information.
     */
    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {
        String priorityStr = null;

        // For the purposes of this View, we want to print the priority as readable text.
        switch(priority) {
            case android.util.Log.VERBOSE:
                priorityStr = "VERBOSE";
                break;
            case android.util.Log.DEBUG:
                priorityStr = "DEBUG";
                break;
            case android.util.Log.INFO:
                priorityStr = "INFO";
                break;
            case android.util.Log.WARN:
                priorityStr = "WARN";
                break;
            case android.util.Log.ERROR:
                priorityStr = "ERROR";
                break;
            case android.util.Log.ASSERT:
                priorityStr = "ASSERT";
                break;
            default:
                break;
        }

        // Handily, the Log class has a facility for converting a stack trace into a useable string.
        String exceptionStr = null;
        if (tr != null) {
            exceptionStr = android.util.Log.getStackTraceString(tr);
        }

        // Take the priority, tag, message, and exception, and concatonate as necessary
        // into one usable line of text.
        String outputStr =  "";
        outputStr = appendIfNotNull(outputStr, priorityStr, "\t");
        outputStr = appendIfNotNull(outputStr, tag, "\t");
        outputStr = appendIfNotNull(outputStr, msg, "\t");
        outputStr = appendIfNotNull(outputStr, exceptionStr, "\t");

        // Actually display the text we just generated within the LogView.
        appendToLog(outputStr);

        if (mNext != null) {
            mNext.println(priority, tag, msg, tr);
        }
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
            sourceStr += delimiter + appendStr;
        }
        return sourceStr;
    }

    // The next LogNode in the chain.
    LogNode mNext;

    /** Outputs the string as a new line of log data in the LogView. */
    public void appendToLog(String s) {
        append("\n" + s);
    }


}
