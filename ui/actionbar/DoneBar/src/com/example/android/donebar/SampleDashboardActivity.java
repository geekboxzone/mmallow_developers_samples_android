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

package com.example.android.donebar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * A simple launcher activity offering access to the individual samples in this project.
 */
public class SampleDashboardActivity extends Activity implements AdapterView.OnItemClickListener {
    /**
     * The collection of samples that will be used to populate the 'dashboard' grid.
     */
    private Sample[] mSamples;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_dashboard);

        // Prepare list of samples in this dashboard.
        mSamples = new Sample[]{
                new Sample(R.string.done_bar_title, R.string.done_bar_description,
                        DoneBarActivity.class),
                new Sample(R.string.done_button_title, R.string.done_button_description,
                        DoneButtonActivity.class),
        };

        // Use the custom adapter in the GridView and hook up a click listener to handle
        // selection of individual samples.
        GridView gridView = (GridView) findViewById(android.R.id.list);
        gridView.setAdapter(new SampleAdapter());
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> container, View view, int position, long id) {
        // A sample was selected in the dashboard; open it.
        startActivity(mSamples[position].intent);
    }

    /**
     * A custom array-based adapter, designed for use with a {@link GridView}.
     */
    private class SampleAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSamples.length;
        }

        @Override
        public Sample getItem(int position) {
            return mSamples[position];
        }

        @Override
        public long getItemId(int position) {
            // The title string ID should be unique per sample, so use it as an ID.
            return mSamples[position].titleResId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                // If there was no re-usable view that can be simply repopulated, create
                // a new root view for this grid item.
                convertView = getLayoutInflater().inflate(
                        R.layout.sample_dashboard_item, container, false);
            }

            // Populate the view's children with real data about this sample.
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(
                    mSamples[position].titleResId);
            ((TextView) convertView.findViewById(android.R.id.text2)).setText(
                    mSamples[position].descriptionResId);
            return convertView;
        }
    }

    /**
     * A simple class that stores information about a sample: a title, description, and
     * the intent to call
     * {@link android.content.Context#startActivity(android.content.Intent) startActivity}
     * with in order to open the sample.
     */
    private class Sample {
        int titleResId;
        int descriptionResId;
        Intent intent;

        /**
         * Instantiate a new sample object with a title, description, and intent.
         */
        private Sample(int titleResId, int descriptionResId, Intent intent) {
            this.intent = intent;
            this.titleResId = titleResId;
            this.descriptionResId = descriptionResId;
        }

        /**
         * Instantiate a new sample object with a title, description, and {@link Activity}
         * subclass. An intent will automatically be created for the given activity.
         */
        private Sample(int titleResId, int descriptionResId,
                Class<? extends Activity> activityClass) {
            this(titleResId, descriptionResId,
                    new Intent(SampleDashboardActivity.this, activityClass));
        }
    }
}
