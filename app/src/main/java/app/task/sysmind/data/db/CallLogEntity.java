package app.task.sysmind.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by zulfikar on 26 Jul 2025 at 23:56.
 */
@Entity(tableName = "call_logs")
public class CallLogEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "caller_name")
    private String callerName;

    @ColumnInfo(name = "start_time")
    private long startTime;

    @ColumnInfo(name = "end_time")
    private long endTime;

    @ColumnInfo(name = "call_type") // Answered / Missed
    private String callType;

    // Constructor
    public CallLogEntity(String callerName, long startTime, long endTime, String callType) {
        this.callerName = callerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.callType = callType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}