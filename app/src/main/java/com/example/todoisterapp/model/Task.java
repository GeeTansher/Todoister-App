package com.example.todoisterapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Task_Table")
public class Task {

    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true)
    public long taskId;

    public String task;
    public Priority priority;

    @ColumnInfo(name = "due_date")
    public Date dueDate;
    public int chipNo;
    @ColumnInfo(name = "created_date")
    public Date dateCreated;
    @ColumnInfo(name = "is_done")
    public boolean isdone;

    public Task(String task, Priority priority, Date dueDate, Date dateCreated, boolean isdone,int chipNo) {
        this.task = task;
        this.priority = priority;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
        this.isdone = isdone;
        this.chipNo = chipNo;
    }

    public int getChipNo() {
        return chipNo;
    }

    public void setChipNo(int chipNo) {
        this.chipNo = chipNo;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isIsdone() {
        return isdone;
    }

    public void setIsdone(boolean isdone) {
        this.isdone = isdone;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", task='" + task + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", dateCreated=" + dateCreated +
                ", isdone=" + isdone +
                '}';
    }
}
