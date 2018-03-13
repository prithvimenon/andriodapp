package com.example.android.bluetoothlegatt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BarGraph extends AppCompatActivity {

    public String date;
    BarChart barChart;
    public int i=0;
    //public int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bar_graph );

        date = new SimpleDateFormat( "dd-MM-yyyy", Locale.getDefault() ).format( new Date() );

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get( Calendar.DAY_OF_WEEK );

        List<Float> userdistance = new ArrayList<>( );
        for (UserDistance user:AppDatabasedistance
                .getInstance( getApplicationContext() )
                .getdistanceDAO()
                .getallUsersDistance()) {
            float a = (float) user.getDistance();
            userdistance.add(a);
            Log.d("reats*****", "--->>>>"+userdistance.get( i ))
            ;
            i++;
        }
        Log.d("reats*****", "--->>>>"+userdistance.size());

          //  barChart = findViewById(R.id.bargraph);
            ArrayList<BarEntry> barEntries = new ArrayList<>( );
            int countday = day;
        Log.d("reats*****", "--->>>>"+(countday));
            while(countday!=0) {
               i--;
                Log.d("reats***", "--->>>>"+(countday));
              barEntries.add( new BarEntry( countday-1, userdistance.get( i )));
              countday--;
          }
          BarDataSet barDataSet = new BarDataSet(barEntries,"Distance travelled");

             final ArrayList<String> theDays = new ArrayList<>();
             theDays.add("Sunday");
             theDays.add("Monday");
             theDays.add("Tuesday");
             theDays.add("Wednesday");
             theDays.add("Thursday");
             theDays.add("Friday");
             theDays.add("Saturday");

             ArrayList<IBarDataSet> dataSets = new ArrayList<>();
             dataSets.add( (IBarDataSet) barDataSet );
             BarData Data = new BarData( dataSets );

            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return theDays.get( (int) value);
                }

            };

        YAxis rightAxis  = barChart.getAxisRight();
            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity( 1f );
            xAxis.setValueFormatter( formatter );
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        rightAxis.setEnabled(false);


            barChart.setData(Data);
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines( false );
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);


    }

}
