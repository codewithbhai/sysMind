package app.task.sysmind.domain.model.usecases;

import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.domain.model.repository.CallLogRepository;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:38.
 */
public class DeleteCallLogUseCase {

    private final CallLogRepository callLogRepository;

    public DeleteCallLogUseCase(CallLogRepository callLogRepository) {
        this.callLogRepository = callLogRepository;
    }

    public void execute(CallLogEntity entity) {
        if (entity != null) {
            callLogRepository.deleteCallLog(entity);
        }
    }
}