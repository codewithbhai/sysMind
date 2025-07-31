package app.task.sysmind.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import app.task.sysmind.receiver.IncomingCallReceiver;

/**
 * Created by zulfikar on 27 Jul 2025 at 01:22.
 */
public class CallSchedulerHelper {

    private static final String TAG = "CallSchedulerHelper";

    /**
     * Schedule a simulated incoming call after a delay in seconds.
     *
     * @param context        Application context
     * @param callerName     Caller name
     * @param phoneNumber    Phone number
     * @param delayInSeconds Delay in seconds before triggering
     */
    public static void scheduleIncomingCall(Context context, String callerName, String phoneNumber, int delayInSeconds) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            Log.e(TAG, "AlarmManager is null, cannot schedule call.");
            return;
        }

        // Check exact alarm permission on Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w(TAG, "App is not allowed to schedule exact alarms. Please request SCHEDULE_EXACT_ALARM permission.");
                return;
            }
        }

        Intent intent = new Intent(context, IncomingCallReceiver.class);
        intent.putExtra("callerName", callerName);
        intent.putExtra("phoneNumber", phoneNumber);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                101,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long triggerAtMillis = SystemClock.elapsedRealtime() + delayInSeconds * 1000L;

        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );
            Log.d(TAG, "Incoming call scheduled in " + delayInSeconds + " seconds.");
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException while scheduling exact alarm: " + e.getMessage());
        }
    }
}