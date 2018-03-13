package com.example.android.bluetoothlegatt;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by prithvi on 10/3/2018.
 */

@Entity
public class UserDistance {
    @Override

    public String toString() {
        return "UserDistance{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", distance=" + distance +
                "}\n";
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public UserDistance(String date, double distance) {
        this.date = date;
        this.distance = distance;
    }

    private final String date;
    private final double distance;

    public String getDate() {
        return date;
    }

    public double getDistance() {
        return distance;
    }


}
