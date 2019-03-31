package com.example.hour_by_hour;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notification extends Application {
    public static final String CHANNEL_1_ID = "notify";
    public static final String CHANNEL_2_ID = "alarm";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID, "Notification", NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("Notify Event");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Alarm Event");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
