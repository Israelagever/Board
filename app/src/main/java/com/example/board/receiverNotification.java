package com.example.board;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class receiverNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int icon = android.R.drawable.star_on;

        long when = System.currentTimeMillis();
        String title = "important message";
        String ticker = "ticker";
        int random = new Random().nextInt(5)+1;
        String message = "";
        switch (random) {
            case 1:
                message = context.getResources().getString(R.string.message1);
                break;
            case 2:
                message = context.getResources().getString(R.string.message2);
                break;
            case 3:
            message = context.getResources().getString(R.string.message3);
                break;
            case 4:
                message = context.getResources().getString(R.string.message4);
                break;
            case 5:
                message = context.getResources().getString(R.string.message5);
                break;
        }




        //phase 2
        Intent intent1 = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "M_CH_ID");
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
                .setContentText(message).build();
        notificationManager.notify(3, notification);

        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
    }
}
