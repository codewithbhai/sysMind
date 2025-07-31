package app.task.sysmind.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by zulfikar on 26 Jul 2025 at 23:59.
 */
@Dao
public interface CallLogDao {

    @Insert
    void insert(CallLogEntity callLog);

    @Query("SELECT * FROM call_logs ORDER BY start_time DESC")
    List<CallLogEntity> getAllCallLogs();

    @Delete
    void deleteCallLog(CallLogEntity callLog);

    @Query("DELETE FROM call_logs")
    void deleteAllLogs();

    @Query("DELETE FROM call_logs")
    void deleteAll();

    @Delete
    void delete(CallLogEntity callLog);
}