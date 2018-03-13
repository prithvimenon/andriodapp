package com.example.android.bluetoothlegatt;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MyActivity extends Activity {
    private final static String TAG = MyActivity.class.getSimpleName();
    private ProgressBar pbar;
    private ProgressBar pbarr;
    private ProgressBar pbarp;
    private ProgressBar pbarb;
    private ImageView bike;
    private ImageView biker;
    private ImageView bikep;
    private ImageView bikeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        pbar = (ProgressBar) findViewById(R.id.pbar);
        pbarr = (ProgressBar) findViewById(R.id.pbar2);
        pbarp = (ProgressBar) findViewById(R.id.pbar3);
        pbarb = (ProgressBar) findViewById(R.id.pbar4);
        bike = (ImageView) findViewById(R.id.bike);
        biker = (ImageView) findViewById(R.id.bikered);
        bikep = (ImageView) findViewById(R.id.bikepurple);
        bikeb = (ImageView) findViewById(R.id.bikeblack);
        Log.d(TAG, "Bike x position: " + bike.getX() );
        Log.d(TAG, "pbar x position: " + pbar.getX() );
        bike.setY(pbar.getTop());
        bike.setX(-75+pbar.getProgress()*1);
        biker.setY(pbarr.getTop());
        biker.setX(-450+pbar.getProgress()*3);
        bikep.setY(pbarp.getTop());
        bikep.setX(-450+pbar.getProgress()*3/2);
        bikeb.setY(pbarb.getTop());
        bikeb.setX(-450+pbar.getProgress()*3/4);
        Log.d(TAG, "Bike x position after: " + bike.getX() );
        while (ExampleBluetoothCharacteristic.progress>0){
            pbar.incrementProgressBy(ExampleBluetoothCharacteristic.progress);
            bike.setX(bike.getX()+(ExampleBluetoothCharacteristic.progress*1));
            pbarr.incrementProgressBy(ExampleBluetoothCharacteristic.progress);
            biker.setX(biker.getX()+(ExampleBluetoothCharacteristic.progress*3));
            pbarp.incrementProgressBy(ExampleBluetoothCharacteristic.progress);
            bikep.setX(bikep.getX()+(ExampleBluetoothCharacteristic.progress*3/2));
            pbarb.incrementProgressBy(ExampleBluetoothCharacteristic.progress);
            bikeb.setX(bikeb.getX()+(ExampleBluetoothCharacteristic.progress*3/4));
        }
    }

}
