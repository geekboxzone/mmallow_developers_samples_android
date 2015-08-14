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

import android.media.midi.MidiDevice;
import android.media.midi.MidiDevice.MidiConnection;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Simple wrapper for connecting MIDI ports.
 */
public class MidiPortConnector {
    private final MidiManager mMidiManager;
    private MidiDevice mSourceDevice;
    private MidiDevice mDestinationDevice;
    private MidiConnection mConnection;

    /**
     * @param mMidiManager
     */
    public MidiPortConnector(MidiManager midiManager) {
        mMidiManager = midiManager;
    }

    public void close() throws IOException {
        if (mConnection != null) {
            mConnection.close();
            mConnection = null;
        }
        if (mSourceDevice != null) {
            mSourceDevice.close();
            mSourceDevice = null;
        }
        if (mDestinationDevice != null) {
            mDestinationDevice.close();
            mDestinationDevice = null;
        }
    }

    /**
     * @return a device that matches the manufacturer and product or null
     */
    public MidiDeviceInfo findDevice(String manufacturer, String product) {
        for (MidiDeviceInfo info : mMidiManager.getDevices()) {
            String deviceManufacturer = info.getProperties()
                    .getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);
            if ((manufacturer != null)
                    && manufacturer.equals(deviceManufacturer)) {
                String deviceProduct = info.getProperties()
                        .getString(MidiDeviceInfo.PROPERTY_PRODUCT);
                if ((product != null) && product.equals(deviceProduct)) {
                    return info;
                }
            }
        }
        return null;
    }

    /**
     * Listener class used for receiving the results of
     * {@link #connectToDevicePort}
     */
    public interface OnPortsConnectedListener {
        /**
         * Called to respond to a {@link #connectToDevicePort} request
         *
         * @param connection
         *            a {@link MidiConnection} that represents the connected
         *            ports, or null if connection failed
         */
        abstract public void onPortsConnected(MidiConnection connection);
    }

    /**
     * Open two devices and connect their ports.
     *
     * @param sourceDeviceInfo
     * @param sourcePortIndex
     * @param destinationDeviceInfo
     * @param destinationPortIndex
     */
    public void connectToDevicePort(final MidiDeviceInfo sourceDeviceInfo,
            final int sourcePortIndex,
            final MidiDeviceInfo destinationDeviceInfo,
            final int destinationPortIndex) {
        connectToDevicePort(sourceDeviceInfo, sourcePortIndex,
                destinationDeviceInfo, destinationPortIndex, null, null);
    }

    /**
     * Open two devices and connect their ports.
     *
     * @param sourceDeviceInfo
     * @param sourcePortIndex
     * @param destinationDeviceInfo
     * @param destinationPortIndex
     */
    public void connectToDevicePort(final MidiDeviceInfo sourceDeviceInfo,
            final int sourcePortIndex,
            final MidiDeviceInfo destinationDeviceInfo,
            final int destinationPortIndex,
            final OnPortsConnectedListener listener, final Handler handler) {
        mMidiManager.openDevice(destinationDeviceInfo,
                new MidiManager.OnDeviceOpenedListener() {
                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            Log.e(MidiConstants.TAG,
                                    "could not open " + destinationDeviceInfo);
                            if (listener != null) {
                                listener.onPortsConnected(null);
                            }
                        } else {
                            Log.i(MidiConstants.TAG,
                                    "connectToDevicePort opened "
                                            + destinationDeviceInfo);
                            // Destination device was opened so go to next step.
                            mDestinationDevice = device;
                            MidiInputPort destinationInputPort = device
                                    .openInputPort(destinationPortIndex);
                            if (destinationInputPort != null) {
                                Log.i(MidiConstants.TAG,
                                        "connectToDevicePort opened port on "
                                                + destinationDeviceInfo);
                                connectToDevicePort(sourceDeviceInfo,
                                        sourcePortIndex, destinationInputPort,
                                        listener, handler);
                            } else {
                                Log.e(MidiConstants.TAG,
                                        "could not open port on "
                                                + destinationDeviceInfo);
                                if (listener != null) {
                                    listener.onPortsConnected(null);
                                }
                            }
                        }
                    }
                }, handler);
    }

    /**
     * Open a source device and connect its output port to the
     * destinationInputPort.
     *
     * @param sourceDeviceInfo
     * @param sourcePortIndex
     * @param destinationInputPort
     */
    public void connectToDevicePort(final MidiDeviceInfo sourceDeviceInfo,
            final int sourcePortIndex,
            final MidiInputPort destinationInputPort) {
        connectToDevicePort(sourceDeviceInfo, sourcePortIndex,
                destinationInputPort, null, null);
    }

    /**
     * Open a source device and connect its output port to the
     * destinationInputPort.
     *
     * @param sourceDeviceInfo
     * @param sourcePortIndex
     * @param destinationInputPort
     */
    public void connectToDevicePort(final MidiDeviceInfo sourceDeviceInfo,
            final int sourcePortIndex, final MidiInputPort destinationInputPort,
            final OnPortsConnectedListener listener, final Handler handler) {
        mMidiManager.openDevice(sourceDeviceInfo,
                new MidiManager.OnDeviceOpenedListener() {
                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            Log.e(MidiConstants.TAG,
                                    "could not open " + sourceDeviceInfo);
                            if (listener != null) {
                                listener.onPortsConnected(null);
                            }
                        } else {
                            Log.i(MidiConstants.TAG,
                                    "connectToDevicePort opened "
                                            + sourceDeviceInfo);
                            // Device was opened so connect the ports.
                            mSourceDevice = device;
                            mConnection = device.connectPorts(
                                    destinationInputPort, sourcePortIndex);
                            if (mConnection == null) {
                                Log.e(MidiConstants.TAG, "could not connect to "
                                        + sourceDeviceInfo);
                            }
                            if (listener != null) {
                                listener.onPortsConnected(mConnection);
                            }
                        }
                    }
                }, handler);
    }

}
