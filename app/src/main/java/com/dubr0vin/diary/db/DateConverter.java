package com.dubr0vin.diary.db;

import androidx.room.TypeConverter;

import com.dubr0vin.diary.models.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DateConverter {
    @TypeConverter
    public String fromDate(Date date){
        return new Gson().toJson(date);
    }

    @TypeConverter
    public Date toDate(String jsonDate){
        return new Gson().fromJson(jsonDate, Date.class);
    }
}
