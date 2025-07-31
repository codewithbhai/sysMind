package app.task.sysmind.presentation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import app.task.sysmind.databinding.ActivityMainBinding;
import app.task.sysmind.presentation.activity.CallLogActivity;
import app.task.sysmind.presentation.activity.TestCallSchedulerActivity;
import app.task.sysmind.utils.PermissionsManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        requestIgnoreBatteryOptimizations();

        if (!PermissionsManager.hasAllPermissions(this)) {
            PermissionsManager.requestPermissions(this);
        }

        if (!PermissionsManager.canScheduleExactAlarms(this)) {
            PermissionsManager.requestExactAlarmPermission(this);
        }

        if (!PermissionsManager.canDrawOverlays(this)) {
            PermissionsManager.requestDrawOverlayPermission(this);
        }

        mBinding.btnScheduleCall.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestCallSchedulerActivity.class);
            startActivity(intent);
        });

        mBinding.btnViewCallLogs.setOnClickListener(v -> {
            startActivity(new Intent(this, CallLogActivity.class));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.handlePermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionsManager.hasAllRequiredPermissions(this)) {
            PermissionsManager.requestPermissions(this);
        }
    }

    private void requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

}