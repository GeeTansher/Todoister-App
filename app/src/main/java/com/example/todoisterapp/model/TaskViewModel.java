package com.example.todoisterapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todoisterapp.data.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    public static TaskRepository repository;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public static void insertTask(Task task) {
        repository.insertTask(task);
    }

    public static void updateTask(Task task) {
        repository.updateTask(task);
    }

    public static void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public static LiveData<Task> getTask(long id) {
        return repository.getTask(id);
    }
}
