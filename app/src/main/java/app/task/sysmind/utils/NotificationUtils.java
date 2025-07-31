package app.task.sysmind.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.task.sysmind.MainApplication;
import app.task.sysmind.R;
import app.task.sysmind.presentation.activity.CallActivity;
import app.task.sysmind.presentation.activity.CallLogActivity;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:00.
 */
public class NotificationUtils {

    private static final int MISSED_CALL_NOTIFICATION_ID = 101;

    public static void showMissedCallNotification(Context context, String callerName, @Nullable String phoneNumber) {

        // 1️⃣ Intent to open Call Log screen
        Intent logIntent = new Intent(context, CallLogActivity.class);
        logIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent logPendingIntent = PendingIntent.getActivity(
                context, 0, logIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // 2️⃣ Intent to directly call back (if phoneNumber is available)
        Intent callBackIntent;
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            callBackIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        } else {
            // fallback: open CallActivity if number is unavailable
            callBackIntent = new Intent(context, CallActivity.class);
        }

        PendingIntent callBackPendingIntent = PendingIntent.getActivity(
                context, 1, callBackIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // 3️⃣ Create notification with timestamp
        String timestamp = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainApplication.CHANNEL_ID_MISSED_CALL)
                .setSmallIcon(R.drawable.ic_missed_call)
                .setContentTitle("Missed call from " + callerName)
                .setContentText("Missed at " + timestamp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(logPendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_call, "Call Back", callBackPendingIntent);

        // 4️⃣ Ensure notification channel exists (for Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    MainApplication.CHANNEL_ID_MISSED_CALL,
                    "Missed Call Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for missed calls");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // 5️⃣ Show only if permission is granted (Android 13+)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat.from(context).notify(MISSED_CALL_NOTIFICATION_ID, builder.build());
        }
    }

    // 🔋 Optional: Prompt user to exclude app from battery optimizations
    public static void promptBatteryOptimizationExclusion(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(activity.getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            }
        }
    }
}
