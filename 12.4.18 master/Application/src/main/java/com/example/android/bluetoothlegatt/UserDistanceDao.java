package com.example.android.bluetoothlegatt;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by prithvi on 10/3/2018.
 */

@Dao
public interface UserDistanceDao {

    @Query("SELECT * FROM UserDistance" )
    List<UserDistance> getallUsersDistance();


    @Insert
    void insertdistance(UserDistance... userDistances);

    @Query( "UPDATE UserDistance SET distance = :distancenew"  )
    int updatedistance(double distancenew);

    @Query( "UPDATE UserDistance SET distance = distance + :distancenew WHERE date=:date")
    int incrementDistanceonDate(String date, double distancenew);

    @Query("SELECT * from UserDistance where date = :date")
    List<UserDistance> getRowOnDate(String date);


    //@Update
    //void update (UserDistance...userDistances);


}
