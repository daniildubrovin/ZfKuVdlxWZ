package com.dubr0vin.diary.db;

import androidx.room.TypeConverter;

import com.dubr0vin.diary.models.Hour;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoursConverter {

    @TypeConverter
    public String fromHours(List<Hour> hours){
        return new Gson().toJson(hours);
    }

    @TypeConverter
    public List<Hour> toHours(String data){
        Type itemsListType = new TypeToken<List<Hour>>() {}.getType();
        return new Gson().fromJson(data, itemsListType);
    }
}
