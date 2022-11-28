package com.example.mnmycycle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import io.alterac.blurkit.BlurLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity implements LocationListener {
    Button button,musicB,start,reset,addinfo;
    boolean flash_State;
    TextView textView,distance,calories,maxSpeed,avgSpeed;
    BlurLayout blurLayout;
    CardView cardCal,cardDist;
//    TextClock textClock;
    boolean running;
    float currentSpeed,mxspd=0.0f,avgspd,prev=0.0f,metph,bmr;
    public float distanceCal=0.0f;
    double cal=0.0f,totcal=0.0f;
    int wt=0,ag=18;
    Chronometer chronometer;
//    EditText editText;
    String urnamestr=null,strDist;
    DBhelper DB;
    DBhelperprofile db;
    Cursor cursor;
    Vibrator myVib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        DB=new DBhelper(this);
        db=new DBhelperprofile(this);


        maxSpeed=findViewById(R.id.maxSpeed);
        avgSpeed=findViewById(R.id.avgspeed);
        chronometer=findViewById(R.id.chronometer);
        start=findViewById(R.id.start);
        reset=findViewById(R.id.reset);
        addinfo=findViewById(R.id.addinfo);
        blurLayout = findViewById(R.id.blurLayout);
        textView = findViewById(R.id.speedometer);
        distance = findViewById(R.id.dist);
        calories=findViewById(R.id.calories);
        cardCal=(CardView)findViewById(R.id.cardView1);
        cardDist=(CardView)findViewById(R.id.cardDist);
        cursor= db.getdata();
        cursor.moveToLast();

        //adding welcoming first user animation

        if(cursor.getCount()>0)
            addinfo.setText("hello " + cursor.getString(0));
        else{
            TapTargetView.showFor(this,
                    TapTarget.forView(addinfo,"WELCOME","first add your informations..")
                            .outerCircleColor(R.color.teal_200)
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(19)
                            .titleTextColor(R.color.black)
                            .descriptionTextSize(18)
                            .descriptionTextColor(R.color.black)
                            .textColor(R.color.black)
                            .textTypeface(Typeface.SANS_SERIF)
                            .dimColor(R.color.black)
                            .drawShadow(true)
                            .cancelable(false)
                            .tintTarget(true)
                            .transparentTarget(true)
                            .targetRadius(60),
                    new TapTargetView.Listener(){
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            openDialog();
                        }
                    }
            );
        }

        //stopwatch

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                int a=Integer.parseInt(cursor.getString(2));
                int h=Integer.parseInt(cursor.getString(1));

                if(mxspd<currentSpeed){
                    mxspd=currentSpeed;
                    maxSpeed.setText("max speed: "+mxspd);
                }
                distanceCal = distanceCal + currentSpeed;
                if(((SystemClock.elapsedRealtime()-chronometer.getBase())/1000)%18==0){
                    avgspd=distanceCal-prev;
                    prev=distanceCal;
                    avgSpeed.setText("avg speed: "+Math.round(avgspd/18));
                    if(avgspd/18==0)metph=0;
                    else if(avgspd/18<9)metph=3.5f;
                    else if(avgspd/18>=9 && avgspd/18<16)metph=5;
                    else if(avgspd/18>=16 && avgspd/18<20)metph=6.8f;
                    else if(avgspd/18>=20 && avgspd/18<22)metph=8;
                    else if(avgspd/18>=22 && avgspd/18<26)metph=10;
                    else if(avgspd/18>=26 && avgspd/18<32)metph=12;
                    else metph=16;
//                    Toast.makeText(MainActivity2.this, ""+metph+"-"+bmr, Toast.LENGTH_SHORT).show();
                    cal=(bmr * metph * 0.005)/24 ;
                    totcal=cal+totcal;
                    Formatter fmt =new Formatter(new StringBuilder());
                    fmt.format(Locale.US,"%5.1f",totcal);
                    String strCal=fmt.toString();
                    calories.setText(""+strCal+" Kcal");
                }
                Formatter frmt =new Formatter(new StringBuilder());
                frmt.format(Locale.US,"%5.1f",distanceCal/3.6);
                strDist=frmt.toString();
                distance.setText(strDist+" m");
            }
        });


        addinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        //check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doStuff();
        }
        this.updateSpeed(null,distanceCal);


        // this keeps the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //horn sound
        final MediaPlayer horn_sound = MediaPlayer.create(this, R.raw.belltrim);
        CardView horn = (CardView)findViewById(R.id.button5);
        horn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(100);
                horn_sound.start();
            }
        });

        cardCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                Intent intent=new Intent(MainActivity2.this,MainActivity5.class);
                startActivity(intent);
            }
        });

        cardDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                Intent intent=new Intent(MainActivity2.this, distance.class);
                startActivity(intent);
            }
        });
        musicB= (Button) findViewById(R.id.button);
        musicB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                Intent intent= new Intent(MainActivity2.this,GoalsActivity.class);
                startActivity(intent);
            }
        });

        //flashlight
        button = findViewById(R.id.button4);
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                runflashlight();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity2.this, "camera permission needed to use flashlight", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

    //user info adding dialog
    public void openDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet);

        dialog.show();
        Button saveinfo=(Button)dialog.findViewById(R.id.saveinfo);
        EditText weight=(EditText) dialog.findViewById(R.id.weight);
        EditText age=(EditText) dialog.findViewById(R.id.age);
        EditText edittextDescription =  (EditText)dialog.findViewById(R.id.urname);
        EditText ht=(EditText) dialog.findViewById(R.id.height);
        EditText wtloss=(EditText) dialog.findViewById(R.id.wtloss);
        EditText days=(EditText) dialog.findViewById(R.id.days);




        saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(weight.getText().toString()=="" || age.getText().toString()=="" || edittextDescription.getText().toString()=="" || ht.getText().toString()=="" || wtloss.getText().toString()=="" || days.getText().toString()==""){
//                    Toast.makeText(MainActivity2.this, "profile not saved !!,Enter all values", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (edittextDescription.length() == 0) {
                    edittextDescription.setError("This field is required");
                    return ;
                }
                else if(edittextDescription.length() >=13){
                    edittextDescription.setError("should be shorter!!");
                }
                if (ht.length() == 0) {
                    ht.setError("This field is required");
                    return ;
                }
                if (weight.length() == 0) {
                    weight.setError("This field is required");
                    return ;
                }
//                else if(Integer.parseInt(weight.getText().toString())<20){
//                    wtloss.setError("ur weight is very less, eat something");
//                    return ;
//                }
                if (age.length() == 0) {
                    age.setError("This field is required");
                    return ;
                }
                else if(Integer.parseInt(age.getText().toString())<5 || Integer.parseInt(age.getText().toString())>110 ){
                    age.setError("u are either too young ur too old ");
                    return;
                }
                if (wtloss.length() == 0) {
                    wtloss.setError("This field is required");
                    return ;
                }
                else if(Integer.parseInt(wtloss.getText().toString())+25 > Integer.parseInt(weight.getText().toString())){
                    wtloss.setError("u will die after weight loss");
                    return ;
                }
                if (days.length() == 0) {
                    days.setError("This field is required");
                    return ;
                }else if(Integer.parseInt(days.getText().toString())<5) {
                    days.setError("no. of days must be greater than 5");
                    return;
                }
                wt=Integer.parseInt(weight.getText().toString());
                ag=Integer.parseInt(age.getText().toString());

                urnamestr=edittextDescription.getText().toString();

                if(cursor.getCount()>0) {
                    cursor = db.getdata();
                    cursor.moveToFirst();
                    String name = cursor.getString(0);
                    db.deletedata(name);
                }

                Boolean chkIns=db.insertdata(urnamestr,""+wt,""+ag,""+Float.parseFloat(ht.getText().toString()),""+Float.parseFloat(wtloss.getText().toString()),""+Float.parseFloat(days.getText().toString()));

                if(chkIns==true) {
                    Toast.makeText(MainActivity2.this, "profile saved..", Toast.LENGTH_SHORT).show();
                    cursor= db.getdata();
                    cursor.moveToLast();
                    addinfo.setText("Hi!! " + cursor.getString(0));
                }
                else{
                    Toast.makeText(MainActivity2.this, "profile not saved!!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    //    stopwatch
    public void startChronometer(View v){
        final MediaPlayer count_sound = MediaPlayer.create(this, R.raw.countdown);
        if(cursor.getCount()==0){
            Toast.makeText(this, "Add your profile first!!", Toast.LENGTH_SHORT).show();
            return;
        }
        bmr=(float)(88.36+(13.4*Integer.parseInt(cursor.getString(1)))+(4.8*Integer.parseInt(cursor.getString(3)))-(5.6*Integer.parseInt(cursor.getString(2))));
        distanceCal=0;
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running=true;
            if((SystemClock.elapsedRealtime()- chronometer.getBase())<5000){
                count_sound.start();
            }
        }

    }
    public void resetChronometer(View v){
        if(running){
            final MediaPlayer stop_sound = MediaPlayer.create(this, R.raw.wrong);
            final MediaPlayer applause = MediaPlayer.create(this, R.raw.applause8);
            final MediaPlayer stop=MediaPlayer.create(this,R.raw.stopsound);
            if(((SystemClock.elapsedRealtime()- chronometer.getBase())<120000) ){
                Toast.makeText(this, "activity is too short to get recorded!!", Toast.LENGTH_SHORT).show();
                stop_sound.start();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
                running=false;
                return;
            }
            else if((SystemClock.elapsedRealtime()- chronometer.getBase())>=1.8e+6){
                applause.start();
            }
            else if((SystemClock.elapsedRealtime()- chronometer.getBase())<600000 && (SystemClock.elapsedRealtime()- chronometer.getBase())>=120000){
                Toast.makeText(this, "bhai itni zaldi!!", Toast.LENGTH_SHORT).show();
                stop.start();
            }
            else{
                stop.start();
            }
            Formatter fmt =new Formatter(new StringBuilder());
            fmt.format(Locale.US,"%5.2f",totcal);
            String strCal=fmt.toString();
            calories.setText(strCal+"kcal");
            if(totcal>0 || Float.parseFloat(strDist)>0) {
                Boolean chkIns = DB.insertdata(strCal, strDist);
                if (chkIns == true) {
                    Toast.makeText(this, "Activity saved..", Toast.LENGTH_SHORT).show();
                }
            }
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            running=false;
        }
    }

    private void runflashlight() {
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                myVib.vibrate(150);
                if (!flash_State) {
                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, true);
                        flash_State = true;
                    } catch (CameraAccessException e) {

                    }
                } else {
                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, false);
                        flash_State = false;
                    } catch (CameraAccessException e) {

                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            CLocation myLocation = new CLocation(location, true);
            this.updateSpeed(myLocation,distanceCal);
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            forecast(latLng);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    void forecast(LatLng latLng){
        String url="https://aerisweather1.p.rapidapi.com/forecasts/"+latLng.latitude + "," + latLng.longitude;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "b936137b11msh7ba9f065f8e5a8ep1693ebjsn272f402ffcfb")
                .addHeader("X-RapidAPI-Host", "aerisweather1.p.rapidapi.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity2.this, "Hauni bhai :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    MainActivity2.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(res);
                                JSONArray arr= jsonObject.getJSONArray("response");
                                JSONArray arr1= arr.getJSONObject(0).getJSONArray("periods");
                                String Val=arr1.getJSONObject(0).getString("weather");
                                TextView textView=(TextView) findViewById(R.id.ans);
                                textView.setText(Val);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doStuff() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
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
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this, "Waiting for GPS connection...", Toast.LENGTH_SHORT).show();
    }

    private void updateSpeed(CLocation location, float distanceCal){
        currentSpeed=0;

        if(location!=null){
            location.setUseMetric(true);
            currentSpeed=location.getSpeed();
            currentSpeed=currentSpeed * 3.6f;
        }

        Formatter fmt =new Formatter(new StringBuilder());
        fmt.format(Locale.US,"%5.1f",currentSpeed);
        String strCurrentspeed=fmt.toString();
        strCurrentspeed=strCurrentspeed.replace(" ","0");
        textView.setText(strCurrentspeed);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                doStuff();
            }
            else finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }
}