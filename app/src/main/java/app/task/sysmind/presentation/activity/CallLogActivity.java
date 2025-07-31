package app.task.sysmind.presentation.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.task.sysmind.data.db.CallLogDatabase;
import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.databinding.ActivityCallLogBinding;
import app.task.sysmind.presentation.adapter.CallLogAdapter;
import app.task.sysmind.utils.PermissionsManager;

public class CallLogActivity extends AppCompatActivity {
    private ActivityCallLogBinding mBinding;
    private CallLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mBinding = ActivityCallLogBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        adapter = new CallLogAdapter(new ArrayList<>());
        mBinding.recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewLogs.setAdapter(adapter);

        loadCallLogs();
    }

    private void loadCallLogs() {
        new Thread(() -> {
            List<CallLogEntity> logs = CallLogDatabase.getInstance(this).callLogDao().getAllCallLogs();

            runOnUiThread(() -> {
                if (logs.isEmpty()) {
                    mBinding.recyclerViewLogs.setVisibility(RecyclerView.GONE);
                    mBinding.emptyView.setVisibility(TextView.VISIBLE);
                } else {
                    mBinding.recyclerViewLogs.setVisibility(RecyclerView.VISIBLE);
                    mBinding.emptyView.setVisibility(TextView.GONE);
                    adapter.setLogs(logs);
                }
            });
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionsManager.hasAllRequiredPermissions(this)) {
            PermissionsManager.requestPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.handlePermissionsResult(this, requestCode, permissions, grantResults);
    }
}