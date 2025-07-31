package app.task.sysmind.presentation.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import java.util.UUID;

import app.task.sysmind.databinding.ActivityTestCallSchedulerBinding;
import app.task.sysmind.receiver.IncomingCallReceiver;
import app.task.sysmind.utils.PermissionsManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestCallSchedulerActivity extends AppCompatActivity {

    private ActivityTestCallSchedulerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        mBinding = ActivityTestCallSchedulerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.buttonSchedule.setOnClickListener(v -> {
            if (!PermissionsManager.hasAllRequiredPermissions(this)) {
                PermissionsManager.requestPermissions(this);
                return;
            }

            scheduleMockIncomingCall();
        });
    }

    private void scheduleMockIncomingCall() {
        String delayStr = mBinding.editTextDelay.getText().toString();
        if (delayStr.isEmpty()) {
            Toast.makeText(this, "Please enter delay in seconds", Toast.LENGTH_SHORT).show();
            return;
        }

        long delayMillis = Long.parseLong(delayStr) * 1000;

        if (!canScheduleExactAlarms()) {
            Toast.makeText(this, "Exact alarm permission not granted. Please enable manually.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
            return;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Toast.makeText(this, "AlarmManager not available", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, IncomingCallReceiver.class);
        intent.putExtra("caller_name", "Test User");
        intent.putExtra("caller_id", UUID.randomUUID().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1001,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long triggerTime = SystemClock.elapsedRealtime() + delayMillis;

        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
            Toast.makeText(this, "Mock call scheduled in " + delayStr + " seconds", Toast.LENGTH_SHORT).show();
            mBinding.editTextDelay.getText().clear();
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission required to schedule exact alarms", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean canScheduleExactAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionsManager.hasAllRequiredPermissions(this)) {
            PermissionsManager.requestPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.handlePermissionsResult(this, requestCode, permissions, grantResults);
    }
}