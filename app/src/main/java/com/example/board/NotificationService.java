package com.example.board;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.time.LocalTime;
import java.util.Calendar;

public class NotificationService extends Service {


    FirstThread firstThread;

    String serviceName= NotificationService.class.getSimpleName();


    boolean isRun = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground();
        else startForeground(1, new Notification());

//        Toast.makeText(this,"service oncreate: " + serviceName,Toast.LENGTH_LONG).show();
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);

        //chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this,"service onStartCommand: " + serviceName,Toast.LENGTH_LONG).show();
        if(firstThread==null)
        {
            firstThread=new FirstThread();
            isRun = true;
            firstThread.start();

        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
//        Toast.makeText(this, "taskRemove", Toast.LENGTH_SHORT).show();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
        firstThread=null;
        Log.d("data1","destroyed");


//        Toast.makeText(this,"service onDestroy: " + serviceName,Toast.LENGTH_LONG).show();

    }

    @Override

    public IBinder onBind(Intent intent) {
        return null;

    }

    public class FirstThread extends Thread

    {

        int timeCount = 0;

        @Override
        public void run() {
            super.run();
            while (isRun) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeCount++;
                Log.d("data1", "run: " + timeCount);
                if (timeCount == 18) {

                    int icon = android.R.drawable.star_on;
                    String ticket = " this is ticket message";
                    long when = System.currentTimeMillis();
                    String title = "important message";
                    String ticker = "ticker";
                    String text = "You're already three minutes out of the game !!";
                    //phase 2
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = "YOUR_CHANNEL_ID";
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                        builder.setChannelId(channelId);
                    }
                    //phase 3
                    Notification notification = builder.setContentIntent(pendingIntent)
                            .setSmallIcon(icon).setTicker(ticker).setWhen(when)
                            .setAutoCancel(true).setContentTitle(title)
                            .setContentText(text).build();
                    notificationManager.notify(3, notification);
                    stopSelf();
                }

            }
        }

    }
}
