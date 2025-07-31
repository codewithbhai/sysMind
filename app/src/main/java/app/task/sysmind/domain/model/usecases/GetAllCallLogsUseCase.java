package app.task.sysmind.domain.model.usecases;

import java.util.List;

import javax.inject.Inject;

import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.domain.model.CallLog;
import app.task.sysmind.domain.model.repository.CallLogRepository;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:39.
 */
public class GetAllCallLogsUseCase {

    private final CallLogRepository callLogRepository;

    public GetAllCallLogsUseCase(CallLogRepository callLogRepository) {
        this.callLogRepository = callLogRepository;
    }

    public void execute(Callback callback) {
        callLogRepository.getAllCallLogs(callback::onResult);
    }

    public interface Callback {
        void onResult(List<CallLogEntity> logs);
    }
}