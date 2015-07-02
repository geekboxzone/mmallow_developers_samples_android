/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.example.android.common.midi;

import android.app.Activity;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiManager.DeviceCallback;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Base class that uses a Spinner to select available MIDI ports.
 */
public abstract class MidiPortSelector extends DeviceCallback {

    public static final int TYPE_INPUT = 0;
    public static final int TYPE_OUTPUT = 1;
    private int mType = TYPE_INPUT;
    protected ArrayAdapter<MidiPortWrapper> mAdapter;
    private Spinner mSpinner;
    protected MidiManager mMidiManager;
    protected Activity mActivity;
    private MidiPortWrapper mCurrentWrapper;

    /**
     *
     * @param midiManager
     * @param activity
     * @param spinnerId ID from the layout resource
     * @param type TYPE_INPUT or TYPE_OUTPUT
     */
    public MidiPortSelector(MidiManager midiManager, Activity activity,
            int spinnerId, int type) {
        mMidiManager = midiManager;
        mActivity = activity;
        mType = type;
        mAdapter = new ArrayAdapter<MidiPortWrapper>(activity,
                android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mAdapter.add(new MidiPortWrapper(null, 0));

        mSpinner = (Spinner) activity.findViewById(spinnerId);
        mSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parent, View view,
                            int pos, long id) {
                        mCurrentWrapper = mAdapter.getItem(pos);
                        onPortSelected(mCurrentWrapper);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        onPortSelected(null);
                        mCurrentWrapper = null;
                    }
                });
        mSpinner.setAdapter(mAdapter);

        mMidiManager.registerDeviceCallback(this,
                new Handler(Looper.getMainLooper()));

        MidiDeviceInfo[] infos = mMidiManager.getDevices();
        for (MidiDeviceInfo info : infos) {
            onDeviceAdded(info);
        }
    }

    private int getInfoPortCount(final MidiDeviceInfo info) {
        int portCount = (mType == TYPE_INPUT) ? info.getInputPortCount()
                : info.getOutputPortCount();
        return portCount;
    }

    @Override
    public void onDeviceAdded(final MidiDeviceInfo info) {
        int portCount = getInfoPortCount(info);
        for (int i = 0; i < portCount; ++i) {
            mAdapter.add(new MidiPortWrapper(info, i));
        }
    }

    @Override
    public void onDeviceRemoved(final MidiDeviceInfo info) {
        int portCount = getInfoPortCount(info);
        for (int i = 0; i < portCount; ++i) {
            MidiPortWrapper wrapper = new MidiPortWrapper(info, i);
            MidiPortWrapper currentWrapper = mCurrentWrapper;
            mAdapter.remove(wrapper);
            // If the currently selected port was removed then select no port.
            if (wrapper.equals(currentWrapper)) {
                mSpinner.setSelection(0);
            }
        }
    }

    /**
     * Implement this method to handle the user selecting a port on a device.
     *
     * @param wrapper
     */
    public abstract void onPortSelected(MidiPortWrapper wrapper);

    /**
     * Implement this method to clean up any open resources.
     */
    public abstract void onClose();
}
