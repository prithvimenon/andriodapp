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

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public static String macaddress = "E1:3E:DD:CA:4F:8F";
    static {
        // Sample Services.
        attributes.put("6e400003-b5a3-f393-e0a9-e50e24dccA9e", "Heart Rate Service");
        attributes.put("6e400002-b5a3-f393-e0a9-e50e24dcca9e", "Device Information Service");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("6E400002-B5A3-F393-E0A9-E50E24DCCA9E", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
