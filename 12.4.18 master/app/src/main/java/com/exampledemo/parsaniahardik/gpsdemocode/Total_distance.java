package com.exampledemo.parsaniahardik.gpsdemocode;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Total_distance extends AppCompatActivity {

    public TextView totalview;
    public double totaldistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_total_distance );

        totalview = findViewById( R.id.total_distance );
        for(UserDistance user :AppDatabasedistance
            .getInstance( getApplicationContext() )
            .getdistanceDAO()
            .getallUsersDistance()){
            totaldistance = user.getDistance()+totaldistance;

        }
        totalview.append( String.valueOf( totaldistance ) );
    }

}
