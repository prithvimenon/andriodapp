package com.example.android.bluetoothlegatt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Today_Distance extends AppCompatActivity {

    private TextView todayview;
    public String date = new SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault() ).format( new Date() );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_today__distance );

        todayview = findViewById( R.id.today_distance );


        for (UserDistance user : AppDatabasedistance
                .getInstance( getApplicationContext() )
                .getdistanceDAO()
                .getRowOnDate( date )){
            todayview.append(String.valueOf( user.getDistance() ));
        }
    }
}
