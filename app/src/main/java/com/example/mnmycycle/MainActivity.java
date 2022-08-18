package com.example.mnmycycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MediaPlayer cycle_sound = MediaPlayer.create(this,R.raw.bell);


        Thread td = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    cycle_sound.start();
                    sleep(2500);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            }
        } ;td.start();
    }
}