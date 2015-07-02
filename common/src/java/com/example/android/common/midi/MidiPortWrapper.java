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

import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiDeviceInfo.PortInfo;

// Wrapper for a MIDI device and port description.
public class MidiPortWrapper {
    private MidiDeviceInfo mInfo;
    private int mPortIndex;
    private String mString;

    public MidiPortWrapper(MidiDeviceInfo info, int portIndex) {
        mInfo = info;
        mPortIndex = portIndex;
        if (mInfo == null) {
            mString = "- - - - - -";
        } else {
            StringBuilder sb = new StringBuilder();
            String name = mInfo.getProperties()
                    .getString(MidiDeviceInfo.PROPERTY_NAME);
            if (name == null) {
                name = mInfo.getProperties()
                        .getString(MidiDeviceInfo.PROPERTY_MANUFACTURER)
                       + ", " + mInfo.getProperties()
                       .getString(MidiDeviceInfo.PROPERTY_PRODUCT);
            }
            sb.append("#" + mInfo.getId()).append(", ").append(name);
            PortInfo portInfo = mInfo.getPorts()[portIndex];
            sb.append(", ").append(portInfo.getName());
            mString = sb.toString();
        }
    }

    public int getPortIndex() {
        return mPortIndex;
    }

    public MidiDeviceInfo getDeviceInfo() {
        return mInfo;
    }

    @Override
    public String toString() {
        return mString;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof MidiPortWrapper))
            return false;
        MidiPortWrapper otherWrapper = (MidiPortWrapper) other;
        if (mPortIndex != otherWrapper.mPortIndex)
            return false;
        if (mInfo == null)
            return (otherWrapper.mInfo == null);
        return mInfo.equals(otherWrapper.mInfo);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
