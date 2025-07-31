package app.task.sysmind;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Created by zulfikar on 26 Jul 2025 at 23:44.
 */
public class MainApplication extends Application {
    public static final String CHANNEL_ID_ONGOING_CALL = "channel_ongoing_call";
    public static final String CHANNEL_ID_INCOMING_CALL = "channel_incoming_call";
    public static final String CHANNEL_ID_MISSED_CALL = "channel_missed_call";

    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);

            NotificationChannel ongoingCallChannel = new NotificationChannel(
                    CHANNEL_ID_ONGOING_CALL,
                    "Ongoing Call Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            ongoingCallChannel.setDescription("Used for ongoing VoIP call");

            NotificationChannel incomingCallChannel = new NotificationChannel(
                    CHANNEL_ID_INCOMING_CALL,
                    "Incoming Call Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            incomingCallChannel.setDescription("Used for incoming call alerts");
            incomingCallChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);

            NotificationChannel missedCallChannel = new NotificationChannel(
                    CHANNEL_ID_MISSED_CALL,
                    "Missed Call Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            missedCallChannel.setDescription("Used for missed call notifications");

            manager.createNotificationChannel(ongoingCallChannel);
            manager.createNotificationChannel(incomingCallChannel);
            manager.createNotificationChannel(missedCallChannel);
        }
    }
}
