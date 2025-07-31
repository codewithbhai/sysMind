package app.task.sysmind.data.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.data.db.CallLogDao;
import app.task.sysmind.data.db.CallLogDatabase;
import app.task.sysmind.domain.model.CallLog;
import app.task.sysmind.domain.model.repository.CallLogRepository;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:29.
 */
public class CallLogRepositoryImpl implements CallLogRepository {

    private final CallLogDao callLogDao;
    private final ExecutorService executorService;

    public CallLogRepositoryImpl(Context context) {
        CallLogDatabase db = CallLogDatabase.getInstance(context);
        callLogDao = db.callLogDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void insertCallLog(CallLogEntity callLog) {
        executorService.execute(() -> callLogDao.insert(callLog));
    }

    @Override
    public void deleteAllLogs() {
        executorService.execute(callLogDao::deleteAll);
    }

    @Override
    public void deleteCallLog(CallLogEntity entity) {
        executorService.execute(() -> callLogDao.delete(entity));
    }

    @Override
    public void getAllCallLogs(Callback<List<CallLogEntity>> callback) {
        executorService.execute(() -> {
            List<CallLogEntity> logs = callLogDao.getAllCallLogs();
            callback.onResult(logs);
        });
    }
}