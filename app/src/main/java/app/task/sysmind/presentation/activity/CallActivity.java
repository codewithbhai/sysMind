package app.task.sysmind.presentation.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import app.task.sysmind.R;
import app.task.sysmind.data.db.CallLogDatabase;
import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.databinding.ActivityCallBinding;
import app.task.sysmind.utils.NotificationUtils;

public class CallActivity extends AppCompatActivity {
    private ActivityCallBinding mBinding;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private Handler timeoutHandler;
    private boolean callAnswered = false;

    private static final long TIMEOUT = 10000; // 10 seconds
    private static final String DUMMY_CALLER_NAME = "ZULFIKAR.";
    private static final String DUMMY_CALLER_NUMBER = "9971805350";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mBinding = ActivityCallBinding.inflate(getLayoutInflater());
        setShowWhenLocked(true);
        setTurnScreenOn(true);
        unlockScreen();

        setContentView(mBinding.getRoot());

        mBinding.txtCallerName.setText(DUMMY_CALLER_NAME);
        mBinding.txtCallerNumber.setText(DUMMY_CALLER_NUMBER);
        playRingtone();
        vibratePhone();

        mBinding.btnAnswer.setOnClickListener(v -> answerCall());
        mBinding.btnReject.setOnClickListener(v -> rejectCall());

        timeoutHandler = new Handler();
        timeoutHandler.postDelayed(() -> {
            if (!callAnswered) {
                markMissedCall();
                finish();
            }
        }, TIMEOUT);
    }

    private void answerCall() {
        callAnswered = true;
        stopRingtoneAndVibration();
        timeoutHandler.removeCallbacksAndMessages(null);

        long startTime = System.currentTimeMillis();

        // Save answered call log
        new Thread(() -> {
            CallLogEntity log = new CallLogEntity(DUMMY_CALLER_NAME, startTime, 0, "Answered");
            CallLogDatabase.getInstance(this).callLogDao().insert(log);
        }).start();

        Intent intent = new Intent(this, OngoingCallActivity.class);
        intent.putExtra("caller_name", DUMMY_CALLER_NAME);
        startActivity(intent);
        finish();
    }

    private void rejectCall() {
        stopRingtoneAndVibration();
        timeoutHandler.removeCallbacksAndMessages(null);
        markMissedCall();
        finish();
    }

    private void markMissedCall() {
        long time = System.currentTimeMillis();

        new Thread(() -> {
            CallLogEntity log = new CallLogEntity(DUMMY_CALLER_NAME, time, time, "Missed");
            CallLogDatabase.getInstance(this).callLogDao().insert(log);
        }).start();

        NotificationUtils.showMissedCallNotification(this, DUMMY_CALLER_NAME, DUMMY_CALLER_NUMBER);
    }

    private void playRingtone() {
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void vibratePhone() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 500, 500};
        if (vibrator != null) {
            vibrator.vibrate(pattern, 0);
        }
    }

    private void stopRingtoneAndVibration() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace(); // Optional: Log it or ignore
        }

        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
    }

    private void unlockScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager != null) {
            keyguardManager.requestDismissKeyguard(this, null);
        }

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null && !powerManager.isInteractive()) {
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE,
                    "voip:CALL_WAKELOCK");
            wakeLock.acquire(5 * 1000L); // wake up for 5 seconds
        }
    }

    @Override
    protected void onDestroy() {
        stopRingtoneAndVibration();
        timeoutHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}