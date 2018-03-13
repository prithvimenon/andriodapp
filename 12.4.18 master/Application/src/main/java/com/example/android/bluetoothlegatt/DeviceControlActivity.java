/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.android.bluetoothlegatt.BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED;
import static com.example.android.bluetoothlegatt.BluetoothLeService.hexStringToByteArray;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity{

    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private ToggleButton toggle1;
    private ToggleButton toggle2;
    private ToggleButton toggle3;
    private ImageView toggle4;
    private ImageButton but;
    private BluetoothGatt mBluetoothGatt;
    private SeekBar seekBar;
    private TextView seekText;

    private TextView cstate;


    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(SampleGattAttributes.macaddress);
            if(mConnected) {
                cstate.setText(R.string.connected);
            }

           // ExampleBluetoothCharacteristic.gattrxx.readCharacteristic(ExampleBluetoothCharacteristic.readx);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };





    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                cstate.setText(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
          /*  if (mGattCharacteristics != null) {
                final BluetoothGattCharacteristic characteristic =
                        ExampleBluetoothCharacteristic.readx;
                final int charaProp = characteristic.getProperties();
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    // If there is an active notification on a characteristic, clear
                    // it first so it doesn't update the data field on the user interface.
                    if (mNotifyCharacteristic != null) {
                        mBluetoothLeService.setCharacteristicNotification(
                                mNotifyCharacteristic, false);
                        mNotifyCharacteristic = null;
                    }
                    mBluetoothLeService.readCharacteristic(ExampleBluetoothCharacteristic.readx);
                }
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    mNotifyCharacteristic = characteristic;
                    mBluetoothLeService.setCharacteristicNotification(
                            characteristic, true);
                }
            } */
        }
    };

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
     private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                            final BluetoothGattCharacteristic characteristic =
                                    ExampleBluetoothCharacteristic.readx;
                            final int charaProp = characteristic.getProperties();
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                                // If there is an active notification on a characteristic, clear
                                // it first so it doesn't update the data field on the user interface.
                                if (mNotifyCharacteristic != null) {
                                    mBluetoothLeService.setCharacteristicNotification(
                                            mNotifyCharacteristic, false);
                                    mNotifyCharacteristic = null;
                                }
                                mBluetoothLeService.readCharacteristic(characteristic);
                            }
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                mNotifyCharacteristic = characteristic;
                                mBluetoothLeService.setCharacteristicNotification(
                                        characteristic, true);
                            }
                        return true;
                    }
                    return false;
                }
    };

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = "mybt";
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);
        toggle1 = (ToggleButton) findViewById(R.id.toggleButton10);
        toggle2 = (ToggleButton) findViewById(R.id.toggleButton11);
        toggle3 = (ToggleButton) findViewById(R.id.toggleButton12);
        toggle4 = (ImageView) findViewById(R.id.iv_toggle);
        but = (ImageButton) findViewById(R.id.ImageButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        seekText = (TextView) findViewById(R.id.textView5);

        cstate = (TextView) findViewById(R.id.connection);

        //BluetoothGatt gatt.BluetoothGatt ;

        //BluetoothGattCharacteristic characteristic = getCharacteristicById("mybt", handle);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceControlActivity.this, MyActivity.class));
            }
        });

        /*if(mConnected)
            toggle4.setEnabled(true);
        else
            toggle4.setEnabled(false);
        */
        if(toggle4.isActivated())
            seekBar.setEnabled(true);
        else
            seekBar.setEnabled(false);

        toggle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle4.setActivated(!toggle4.isActivated());
                if (toggle4.isActivated()) {
                    String originalString = "\\x31\\x0A";
                    byte[] b = hexStringToByteArray(originalString);
                    //String s = "some text here";
                    // byte[] b = s.getBytes("UTF-8");
                    //byte[] b = 1;

                    ExampleBluetoothCharacteristic.Charx.setValue(b); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                    Log.d(TAG, "BUTTON IS ON" );
                    toggle1.setChecked(false);
                    toggle3.setChecked(false);
                    seekBar.setEnabled(true);
                } else {
                    Log.d(TAG, "BUTTON IS OFF" );
                    seekBar.setEnabled(false);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekText.setTextSize(20);

                if (progress == 0) {
                    seekText.setText("LOW");
                    String originalString1 = "\\x32";
                    byte[] a = hexStringToByteArray(originalString1);
                    ExampleBluetoothCharacteristic.Charx.setValue(a); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                }
                if (progress == 1) {
                    seekText.setText("MEDIUM");
                    String originalString2 = "\\x33";
                    byte[] b = hexStringToByteArray(originalString2);
                    ExampleBluetoothCharacteristic.Charx.setValue(b); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                }
                if (progress == 2) {
                    seekText.setText("HIGH");
                    String originalString3 = "\\x34";
                    byte[] c = hexStringToByteArray(originalString3);
                    ExampleBluetoothCharacteristic.Charx.setValue(c); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(DeviceControlActivity.this, "started", Toast.LENGTH_SHORT);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mGattCharacteristics != null) {
                    final BluetoothGattCharacteristic characteristic =
                            ExampleBluetoothCharacteristic.readx;
                    final int charaProp = characteristic.getProperties();
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                        // If there is an active notification on a characteristic, clear
                        // it first so it doesn't update the data field on the user interface.
                        if (mNotifyCharacteristic != null) {
                            mBluetoothLeService.setCharacteristicNotification(
                                    mNotifyCharacteristic, true);
                            mNotifyCharacteristic = null;
                        }
                        mBluetoothLeService.readCharacteristic(characteristic);
                    }
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        mNotifyCharacteristic = characteristic;
                        mBluetoothLeService.setCharacteristicNotification(
                                characteristic, true);
                    }

                }
                if (isChecked) {
                    String originalString = "\\x32\\x0A";
                    byte[] b = hexStringToByteArray(originalString);
                    //String s = "some text here";
                    // byte[] b = s.getBytes("UTF-8");
                    //byte[] b = 1;

                    ExampleBluetoothCharacteristic.Charx.setValue(b); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                    Log.d(TAG, "BUTTON IS ON" );
                    toggle2.setChecked(false);
                    toggle3.setChecked(false);
                } else {
                    Log.d(TAG, "BUTTON IS OFF" );
                }
            }
        });

        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "onServicesDiscovered: found sender" + ExampleBluetoothCharacteristic.readx);
                if (isChecked) {
                    String originalString = "\\x33\\x0A";
                    byte[] b = hexStringToByteArray(originalString);
                    //String s = "some text here";
                    // byte[] b = s.getBytes("UTF-8");
                    //byte[] b = 1;

                    ExampleBluetoothCharacteristic.Charx.setValue(b); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                    Log.d(TAG, "BUTTON IS ON" );
                    toggle1.setChecked(false);
                    toggle3.setChecked(false);
                } else {
                    Log.d(TAG, "BUTTON IS OFF" );
                }
            }
        });

        toggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    String originalString = "\\x34\\x0A";
                    byte[] b = hexStringToByteArray(originalString);
                    //String s = "some text here";
                    // byte[] b = s.getBytes("UTF-8");
                    //byte[] b = 1;

                    ExampleBluetoothCharacteristic.Charx.setValue(b); // call this BEFORE(!) you 'write' any stuff to the server
                    ExampleBluetoothCharacteristic.gattx.writeCharacteristic(ExampleBluetoothCharacteristic.Charx);
                    Log.d(TAG, "BUTTON IS ON" );
                    toggle1.setChecked(false);
                    toggle2.setChecked(false);
                } else {
                    Log.d(TAG, "BUTTON IS OFF" );
                }
            }
        });

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        Log.d(TAG, "Display gatt services started and read is  " + ExampleBluetoothCharacteristic.readx.getUuid().toString());
        if (mGattCharacteristics != null) {
            final BluetoothGattCharacteristic characteristic =
                    ExampleBluetoothCharacteristic.readx;
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, true);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }

        }
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        cstate.setText(R.string.connected);
        Log.d(TAG, "We are at connected and read is " + ExampleBluetoothCharacteristic.readx.getUuid().toString());
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
        Log.d(TAG, "We are here and read is " + ExampleBluetoothCharacteristic.readx.getUuid().toString());
        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}