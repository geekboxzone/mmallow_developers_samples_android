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
package com.example.android.singleviewsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.android.common.logger.Log;

/**
 * This fragment will contain all of the logic specific to your sample.  Use the override for
 * onOptionsItemSelected below to define the behavior for when a user clicks a menu item in the
 * sample application.
 */
public class SingleViewSampleFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Make *sure* this line is in your onCreate method.  It gives the fragment the ability
        // to react when the user clicks a menu item.
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("SingleViewSampleFragment", "No action defined for this item.");
        return true;
    }
}
