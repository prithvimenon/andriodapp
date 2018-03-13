package com.example.android.bluetoothlegatt;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by prithvi on 10/3/2018.
 */

@Database( entities = {UserDistance.class},version =1, exportSchema = false)
public abstract class AppDatabasedistance extends RoomDatabase {

    private static volatile AppDatabasedistance instance;

    public static synchronized AppDatabasedistance getInstance(Context context){
        if(instance==null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabasedistance create(final Context context) {
        return Room.databaseBuilder(context, AppDatabasedistance.class,"app_database_distance").allowMainThreadQueries().build();
    }
    public abstract UserDistanceDao getdistanceDAO();
}
