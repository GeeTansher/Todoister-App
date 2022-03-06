package com.example.todoisterapp.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoisterapp.data.TaskDAO;
import com.example.todoisterapp.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Convertor.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    public static final int NUMBER_OF_THREAD = 4;
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
    private static final String DATABASE_NAME = "todoister_database";
    private static volatile TaskRoomDatabase INSTANCE;
    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(() -> {
                        // invoke DAO, and write);
                        TaskDAO taskDAO = INSTANCE.taskDao();
                        taskDAO.deleteAll(); // clean

                        // can add some values to table
                    });
                }
            };

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TaskDAO taskDao();
}
