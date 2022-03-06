package com.example.todoisterapp.util;

import androidx.room.TypeConverter;

import com.example.todoisterapp.model.Priority;

import java.util.Date;


public class Convertor {

    @TypeConverter
    public static Date fromTimeStamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date value){
        return value == null ? null : value.getTime();
    }

    @TypeConverter
    public static String fromPriority(Priority priority){
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String priority){
        return priority == null ? null : Priority.valueOf(priority);
    }
}
