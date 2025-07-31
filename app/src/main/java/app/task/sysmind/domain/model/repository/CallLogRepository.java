package app.task.sysmind.domain.model.repository;

import java.util.List;

import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.domain.model.CallLog;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:34.
 */
public interface CallLogRepository {

    void insertCallLog(CallLogEntity callLog);

    void deleteAllLogs();

    void deleteCallLog(CallLogEntity entity);

    void getAllCallLogs(Callback<List<CallLogEntity>> callback);

    interface Callback<T> {
        void onResult(T result);
    }
}