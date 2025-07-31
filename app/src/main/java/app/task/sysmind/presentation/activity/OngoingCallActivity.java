package app.task.sysmind.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

import app.task.sysmind.R;
import app.task.sysmind.databinding.ActivityOngoingCallBinding;
import app.task.sysmind.service.OngoingCallService;

public class OngoingCallActivity extends AppCompatActivity {
    private ActivityOngoingCallBinding mBinding;
    private long callStartTime;
    private final Handler handler = new Handler();
    private final Runnable updateDurationRunnable = new Runnable() {
        @Override
        public void run() {
            updateCallDuration();
            handler.postDelayed(this, 1000);
        }
    };

    private String callerName = "Unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mBinding  = ActivityOngoingCallBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (getIntent() != null) {
            callerName = getIntent().getStringExtra("caller_name");
            callStartTime = getIntent().getLongExtra("call_start_time", System.currentTimeMillis());
        }

        mBinding.callerNameTextView.setText(callerName);
        updateCallDuration();

        // Start foreground service
        Intent serviceIntent = new Intent(this, OngoingCallService.class);
        serviceIntent.putExtra("caller_name", callerName);
        startService(serviceIntent);

        handler.post(updateDurationRunnable);

        mBinding.endCallButton.setOnClickListener(v -> endCall());
    }

    private void updateCallDuration() {
        long elapsed = (System.currentTimeMillis() - callStartTime) / 1000;

        long minutes = elapsed / 60;
        long seconds = elapsed % 60;

        mBinding.callDurationTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void endCall() {
        stopService(new Intent(this, OngoingCallService.class));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateDurationRunnable);
    }
}