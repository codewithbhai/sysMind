package app.task.sysmind.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import app.task.sysmind.MainApplication;
import app.task.sysmind.R;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:15.
 */
public class OngoingCallService extends Service {

    public static final int ONGOING_CALL_NOTIFICATION_ID = 202;

    @Override
    public void onCreate() {
        super.onCreate();
        // You could do setup here if needed
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String callerName = intent.getStringExtra("caller_name");
        if (callerName == null) {
            callerName = "Unknown";
        }

        // Build the ongoing call notification
        Notification notification = new NotificationCompat.Builder(this, MainApplication.CHANNEL_ID_ONGOING_CALL)
                .setContentTitle("On Call with " + callerName)
                .setContentText("Call in progress")
                .setSmallIcon(R.drawable.ic_call)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .build();

        // Start this service in the foreground
        startForeground(ONGOING_CALL_NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup if needed
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}