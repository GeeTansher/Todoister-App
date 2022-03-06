package com.example.todoisterapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todoisterapp.model.Task;
import com.example.todoisterapp.util.TaskRoomDatabase;

import java.util.List;

public class TaskRepository {
    private final TaskDAO taskDAO;
    private final LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        this.taskDAO = database.taskDao();
        this.allTasks = taskDAO.getTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insertTask(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() ->
                taskDAO.insertTask(task));
    }

    public LiveData<Task> getTask(long id) {
        return taskDAO.get(id);
    }

    public void updateTask(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() ->
                taskDAO.update(task));
    }

    public void deleteTask(Task task) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() ->
                taskDAO.delete(task));
    }
}
