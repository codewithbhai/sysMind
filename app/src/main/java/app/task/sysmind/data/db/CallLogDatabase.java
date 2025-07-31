package app.task.sysmind.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by zulfikar on 26 Jul 2025 at 23:59.
 */
@Database(entities = {CallLogEntity.class}, version = 1, exportSchema = false)
public abstract class CallLogDatabase extends RoomDatabase {
    private static final String DB_NAME = "call_log_db";
    private static volatile CallLogDatabase INSTANCE;
    public abstract CallLogDao callLogDao();
    public static CallLogDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CallLogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    CallLogDatabase.class,
                                    DB_NAME
                            ).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}