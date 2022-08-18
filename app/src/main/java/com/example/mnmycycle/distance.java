package com.example.mnmycycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class distance extends AppCompatActivity {

    DBhelper DB;
    float maxD=0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        ArrayList<BarEntry> dist=new ArrayList<>();
        TextView maxDist=(TextView) findViewById(R.id.maxDist);
        BarChart barChart=findViewById(R.id.barchart1);
        DB=new DBhelper(this);
        Cursor cursor= DB.getdata();
        if(cursor.getCount()>0){
            while(cursor.moveToNext()) {
                if(maxD<Float.parseFloat(cursor.getString(2))) maxD=Float.parseFloat(cursor.getString(2));
                dist.add(new BarEntry(Float.parseFloat(cursor.getString(0)), Float.parseFloat(cursor.getString(2))));
            }
            maxDist.setText("Max distance: "+maxD);

            BarDataSet barDataSet=new BarDataSet(dist,"Distance travelled");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            BarData barData=new BarData(barDataSet);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.animateY(800);
        }
        else maxDist.setText("NO ACTIVITIES RECORDED!!");
    }
}