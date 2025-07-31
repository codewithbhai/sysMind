package app.task.sysmind.domain.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:33.
 */
@Entity(tableName = "call_logs")
public class CallLog {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String phoneNumber;
    private long timestamp;

    public CallLog() {
        // Empty constructor required by Room
    }

    public CallLog(String name, String phoneNumber, long timestamp) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.timestamp = timestamp;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : "";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
