package app.task.sysmind.domain.model.usecases;

import javax.inject.Inject;

import app.task.sysmind.data.db.CallLogEntity;
import app.task.sysmind.domain.model.CallLog;
import app.task.sysmind.domain.model.repository.CallLogRepository;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:37.
 */
public class AddCallLogUseCase {

    private final CallLogRepository callLogRepository;

    public AddCallLogUseCase(CallLogRepository callLogRepository) {
        this.callLogRepository = callLogRepository;
    }

    public void execute(CallLogEntity callLogEntity) {
        if (callLogEntity != null) {
            callLogRepository.insertCallLog(callLogEntity);
        }
    }
}