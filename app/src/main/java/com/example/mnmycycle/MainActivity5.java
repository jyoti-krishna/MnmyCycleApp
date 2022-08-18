package com.example.mnmycycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {

    DBhelper DB;
    float maxC=0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        ArrayList<BarEntry> cal=new ArrayList<>();
        TextView maxCal=(TextView) findViewById(R.id.maxCal);
        BarChart barChart=findViewById(R.id.barchart);
        DB=new DBhelper(this);
        Cursor cursor= DB.getdata();

        if(cursor.getCount()>0){
            while(cursor.moveToNext()) {

                if(maxC<Float.parseFloat(cursor.getString(1))) maxC=Float.parseFloat(cursor.getString(1));
                cal.add(new BarEntry(Float.parseFloat(cursor.getString(0)), Float.parseFloat(cursor.getString(1))));
            }
            maxCal.setText("Max Calories: "+maxC);
            BarDataSet barDataSet=new BarDataSet(cal,"calories burnt");
            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            BarData barData=new BarData(barDataSet);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.animateY(800);
        }
        else maxCal.setText("NO ACTIVITIES RECORDED!!");
    }

}