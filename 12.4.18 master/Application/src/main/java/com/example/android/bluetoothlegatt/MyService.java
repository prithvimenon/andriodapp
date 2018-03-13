package com.example.android.bluetoothlegatt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by prithvi on 7/3/2018.
 */

public class MyService extends Service {



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // return super.onStartCommand( intent, flags, startId );
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

