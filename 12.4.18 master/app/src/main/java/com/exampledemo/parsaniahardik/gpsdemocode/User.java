package com.exampledemo.parsaniahardik.gpsdemocode;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by prithvi on 1/3/2018.
 */
@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public final int id;

    public String getDate() {
        return date;
    }

    public String getLatLng() {
        return latLng;
    }

    private final String date;
    private final String latLng;

    public User(int id, String date, String latLng) {
        this.id = id;
        this.date = date;
        this.latLng = latLng;
    }
}


