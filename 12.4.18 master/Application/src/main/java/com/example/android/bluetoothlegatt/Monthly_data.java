package com.example.android.bluetoothlegatt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Monthly_data extends AppCompatActivity {
    private LineChart mchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_monthly_data );

        mchart= findViewById(R.id.lineChart);
        mchart.setDragEnabled(true);
        mchart.setScaleEnabled(true);

        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0,10));
        yValues.add(new Entry(1,15));
        yValues.add(new Entry(2,5));
        yValues.add(new Entry(3,25));
        yValues.add(new Entry(4,45));
        yValues.add(new Entry(5,20));
        yValues.add(new Entry(6,30));

        final ArrayList<String> theMonths = new ArrayList<>();
        theMonths.add("Jan");
        theMonths.add("Feb");
        theMonths.add("Mar");
        theMonths.add("Apr");
        theMonths.add("May");
        theMonths.add("Jun");
        theMonths.add("Jul");
        theMonths.add("Aug");
        theMonths.add("Sep");
        theMonths.add("Oct");
        theMonths.add("Nov");
        theMonths.add("Dec");

        LineDataSet set1= new LineDataSet(yValues, "Distance Travelled");


        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData Data = new LineData( dataSets );

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return theMonths.get((int) value);
            }
        };
        YAxis rightAxis  = mchart.getAxisRight();
        XAxis xAxis = mchart.getXAxis();
        xAxis.setGranularity( 1f );
        xAxis.setValueFormatter( formatter );
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        rightAxis.setEnabled(false);
        LineData data =  new LineData(dataSets);

        mchart.getAxisLeft().setDrawGridLines(false);
        mchart.getAxisRight().setDrawGridLines( false );
        mchart.getXAxis().setDrawGridLines(false);
        mchart.getDescription().setEnabled(false);

        mchart.setData(data);

    }
}
