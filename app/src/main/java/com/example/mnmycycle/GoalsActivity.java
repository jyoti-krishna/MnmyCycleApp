package com.example.mnmycycle;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GoalsActivity extends AppCompatActivity {
    DBhelperprofile db;
    Cursor cursor;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        db=new DBhelperprofile(this);
        cursor=db.getdata();
        cursor.moveToLast();
        imageView=findViewById(R.id.imageView7);
        TextView show=(TextView) findViewById(R.id.textView6);
        TextView height=(TextView) findViewById(R.id.textView11);
        TextView weight=(TextView) findViewById(R.id.textView12);
        TextView tip=(TextView) findViewById(R.id.textView10);
        TextView goal=(TextView) findViewById(R.id.textView7);
        TextView bmi=(TextView) findViewById(R.id.textView2);
//        TextView day=(TextView) findViewById(R.id.textView7);
        if(cursor.getCount()>0) {
            float wt=Float.parseFloat(cursor.getString(1));
            float ht=Float.parseFloat(cursor.getString(3));
            int wtl=Integer.parseInt(cursor.getString(4));
            int days=Integer.parseInt(cursor.getString(5));
            height.setText("height: "+ht);
            weight.setText("weight: "+wt);
            ht=ht*ht;
            bmi.setText("BMI: "+(wt/ht*10000));
            if((wt/ht*10000)<=18.5){
                imageView.setImageResource(R.drawable.undr);
            }
            else if((wt/ht*10000)>18.5 && (wt/ht*10000)<=24.9){
                imageView.setImageResource(R.drawable.nor);
            }
            else if((wt/ht*10000)>25 && (wt/ht*10000)<=29.9){
                imageView.setImageResource(R.drawable.mor);
            }
            else if((wt/ht*10000)>30 && (wt/ht*10000)<=34.9){
                imageView.setImageResource(R.drawable.fat);
            }
            else if((wt/ht*10000)>=35){
                imageView.setImageResource(R.drawable.obs);
            }
            show.setText("You need to intake "+wt*24+" calories daily to maintain your current weight.");
            goal.setText("Daily target cal burn: "+(7000*wtl/days));
        }
        tip.setText(generateString());
    }
    private String generateString(){
        String[] tips={"Never try to loose weight aggressively, it may lead severe health consequences.",
                "the more faster you ride, more calories you will burn.",
                "attach your phone to cycle using phone holder while riding.",
                "never use phone while riding in traffic.",
                "if your bmi is good don't try to loose weight, try to maintain it.",
                "Don’t move your upper body too much. Let your back serve as a fulcrum, with your bike swaying from side to side beneath it.",
                "If you’re leading a paceline up a hill, keep your frequency  and pedal pressure constant by shifting to a easier gear.",
                "As your effort becomes harder, increase the force of your breaths rather than the frequency.",
                "On descents, your bike is much more stable when you’re pedaling than seating ideal."};
        int max = 8;
        int min = 0;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        return tips[rand];
    }
}