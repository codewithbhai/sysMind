package app.task.sysmind.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import app.task.sysmind.presentation.activity.CallActivity;

/**
 * Created by zulfikar on 27 Jul 2025 at 01:15.
 */
public class IncomingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("IncomingCallReceiver", "Incoming call broadcast received");

        // Wake the screen
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE,
                    "voip:IncomingCallWakeLock"
            );
            wakeLock.acquire(3000); // 3 seconds
        }

        // Start the CallActivity in full-screen mode
        Intent callIntent = new Intent(context, CallActivity.class);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        callIntent.putExtra("caller_name", intent.getStringExtra("caller_name"));
        callIntent.putExtra("caller_number", intent.getStringExtra("caller_number"));

        context.startActivity(callIntent);
    }
}