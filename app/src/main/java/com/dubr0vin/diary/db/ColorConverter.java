package com.dubr0vin.diary.db;

import androidx.room.TypeConverter;

import com.dubr0vin.diary.models.Color;
import com.google.gson.Gson;

public class ColorConverter {
    @TypeConverter
    public String fromColor(Color color){
        return new Gson().toJson(color);
    }

    @TypeConverter
    public Color toColor(String jsonColor){
        return new Gson().fromJson(jsonColor, Color.class);
    }
}
