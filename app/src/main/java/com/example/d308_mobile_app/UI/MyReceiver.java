package com.example.d308_mobile_app.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.d308_mobile_app.R;

// BroadcastReceiver to handle alert notifications
public class MyReceiver extends BroadcastReceiver {
    // Notification channel ID (should be unique within the app)
    private final String CHANNEL_ID = "test";
    // Counter for notification IDs to ensure each notification is unique
    private static int notificationCounter;

    // onReceive method
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context);
        // Directly build the notification inside onReceive method
        Notification n = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Vacation Alert")
                .setContentText(intent.getStringExtra("key"))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationCounter++, n);
    }


    // Method to create a notification channel; required for Android O and above
    private void createNotificationChannel(Context context) {
        // Human-readable name for the channel
        CharSequence name = "Notification Channel";
        // Description for the channel
        String description = "Notifications for vacation alerts";
        // Importance level for the channel
        int importance = NotificationManager.IMPORTANCE_HIGH;

        // Instantiate the NotificationChannel object
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        // Register the channel with the system
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
