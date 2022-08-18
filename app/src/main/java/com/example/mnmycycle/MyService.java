//package com.example.mnmycycle;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Build;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//
//public class MyService extends Service {
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        createNotificationChannel();
//
//        Intent intent1=new Intent(this,MainActivity2.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent1,0);
//        Notification notification=new NotificationCompat.Builder(this,"ChannelId1").setContentTitle("Mnmy cycle").setContentText("your ride is on").setOngoing(true).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent).build();
//        startForeground(1,notification);
//        return START_STICKY;
//    }
//
//    private void createNotificationChannel() {
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel=new NotificationChannel("ChannelId1","Foreground notification", NotificationManager.IMPORTANCE_LOW);
//            NotificationManager manager=getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        stopSelf();
//        stopForeground(true);
//        super.onDestroy();
//    }
//}
