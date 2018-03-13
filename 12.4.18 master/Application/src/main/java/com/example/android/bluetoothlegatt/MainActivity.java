package com.example.android.bluetoothlegatt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public String date = new SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault() ).format( new Date() );
   // public List<Double> distacecummulative = new ArrayList<>(  );
    public String distime;
    private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private double distance;
    private TextView mDistanceTextView;
    private TextView mdatabaseview;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 10000;  /* 10 secs */
    private long FASTEST_INTERVAL = 10000; /* 2 sec */
    private AtomicInteger distanceID = new AtomicInteger(0);
    Button button;
    Button today_distance;
    Button monthly_distance;
    Button total_distance;


    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));
        mdatabaseview = (TextView) findViewById( R.id.database );
        mDistanceTextView = (TextView) findViewById((R.id.distance_textview));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //location_updateclass.mGoogleApiClientx=mGoogleApiClient;
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);



        checkLocation(); //check whether location service is enable or not in your  phone
        UserDistance user2 = new UserDistance(date,0 );  // bascially need this code when installing app for
        AppDatabasedistance
               .getInstance( getApplicationContext() )
                .getdistanceDAO()
                .insertdistance( user2 );

        Button button = (Button) findViewById( R.id.graphbutton );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this, BarGraph.class ) );
            }
        } );

        Button today_button = (Button) findViewById( R.id.today_distance );
        today_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, Today_Distance.class) );
            }
        } );

         monthly_distance = (Button) findViewById( R.id.monthly_distance );
         monthly_distance.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity( new Intent( MainActivity.this , Monthly_data.class ) );
             }
         } );

         total_distance = (Button) findViewById( R.id.total_distance );
         total_distance.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity( new Intent( MainActivity.this, Total_distance.class ) );
             }
         } );
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
       mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

        Log.d("reque", "--->>>>");
    }

    double  longtitudebefore = 51.469697;
    double latitudebefore = 0.225500;

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString( location.getLatitude() ) + "," +
                Double.toString( location.getLongitude() );
        distance = (location.getLatitude() + location.getLongitude());
        mDistanceTextView.setText( String.valueOf( distance ) );
        mLatitudeTextView.setText( String.valueOf( location.getLatitude() ) );
        mLongitudeTextView.setText( String.valueOf( location.getLongitude() ) );
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng( location.getLatitude(), location.getLongitude() );
        distime = String.valueOf( latLng );

        User user = new User( 0,date, distime );

            AppDatabase
                    .getInstance( getApplicationContext() )
                    .getAppDatabseDAO()
                    .insert( user );

        for (User user2 : AppDatabase
                .getInstance( getApplicationContext() )
                .getAppDatabseDAO()
                .getAllUsers());

        String longitude = user.getLatLng().substring( 10,19 );
        String latitude = user.getLatLng().substring( 22,29);

        Log.d("reque**", "--->>>>"+(longitude));
        Log.d("reque**", "--->>>>"+(latitude));
        Log.d("reque***", "--->>>>"+(longtitudebefore));
        Log.d("reque****", "--->>>>"+(latitudebefore));


        double  latitudeint = Double.parseDouble( longitude );
        double  longtidueint = Double.parseDouble( latitude );

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(latitudeint-latitudebefore);
        double lngDiff = Math.toRadians(longtidueint-longtitudebefore);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(latitudebefore)) * Math.cos(Math.toRadians(latitudeint)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = Math.abs((earthRadius * c)-4619.35089);
        System.out.println("Distance:" +  distance);

        if(AppDatabasedistance.getInstance( getApplicationContext() ).getdistanceDAO().getRowOnDate( date ).size() == 0) {
            // no row entry for given date --> date has changed
            AppDatabasedistance.getInstance( getApplicationContext() )
                    .getdistanceDAO()
                    .insertdistance(new UserDistance( date,0 ));
        } else {
            // date has not changed hence update current date's distance
            AppDatabasedistance.getInstance( getApplicationContext() )
                    .getdistanceDAO()
                    .incrementDistanceonDate( date, distance);
        }

        System.out.println(  AppDatabasedistance.getInstance( getApplicationContext() )
                .getdistanceDAO().getallUsersDistance());

        for (UserDistance userDistance:AppDatabasedistance
                .getInstance( getApplicationContext() )
                .getdistanceDAO()
                .getallUsersDistance()){
            mdatabaseview.append( String.valueOf(userDistance.getDistance()));
        }


         longtitudebefore = latitudeint;
         latitudebefore = longtidueint;

    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }


    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
