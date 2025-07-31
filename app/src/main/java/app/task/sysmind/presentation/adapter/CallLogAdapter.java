package app.task.sysmind.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import app.task.sysmind.R;
import app.task.sysmind.data.db.CallLogEntity;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:09.
 */
public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.LogViewHolder> {

    private List<CallLogEntity> callLogs;

    public CallLogAdapter(List<CallLogEntity> callLogs) {
        this.callLogs = callLogs;
    }

    public void setLogs(List<CallLogEntity> logs) {
        this.callLogs = logs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_log, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        CallLogEntity log = callLogs.get(position);

        holder.txtCallerName.setText(log.getCallerName());
        holder.txtCallType.setText(log.getCallType());

        String formattedTime = DateFormat.getDateTimeInstance().format(new Date(log.getStartTime()));
        holder.txtTime.setText(formattedTime);

        if ("Answered".equals(log.getCallType())) {
            long duration = (log.getEndTime() - log.getStartTime()) / 1000;
            holder.txtDuration.setText(duration + " sec");
        } else {
            holder.txtDuration.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView txtCallerName, txtCallType, txtTime, txtDuration;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCallerName = itemView.findViewById(R.id.txtCallerName);
            txtCallType = itemView.findViewById(R.id.txtCallType);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDuration = itemView.findViewById(R.id.txtDuration);
        }
    }
}