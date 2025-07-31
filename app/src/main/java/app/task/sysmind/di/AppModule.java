package app.task.sysmind.di;

import android.content.Context;

import javax.inject.Singleton;

import app.task.sysmind.data.db.CallLogDatabase;
import app.task.sysmind.data.repository.CallLogRepositoryImpl;
import app.task.sysmind.domain.model.repository.CallLogRepository;
import app.task.sysmind.domain.model.usecases.AddCallLogUseCase;
import app.task.sysmind.domain.model.usecases.DeleteCallLogUseCase;
import app.task.sysmind.domain.model.usecases.GetAllCallLogsUseCase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Created by zulfikar on 27 Jul 2025 at 00:40.
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public CallLogDatabase provideDatabase(@ApplicationContext Context context) {
        return CallLogDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public CallLogRepository provideCallLogRepository(@ApplicationContext Context context) {
        return new CallLogRepositoryImpl(context);
    }

    @Provides
    public AddCallLogUseCase provideAddCallLogUseCase(CallLogRepository repository) {
        return new AddCallLogUseCase(repository);
    }

    @Provides
    public GetAllCallLogsUseCase provideGetAllCallLogsUseCase(CallLogRepository repository) {
        return new GetAllCallLogsUseCase(repository);
    }

    @Provides
    public DeleteCallLogUseCase provideDeleteCallLogUseCase(CallLogRepository repository) {
        return new DeleteCallLogUseCase(repository);
    }
}