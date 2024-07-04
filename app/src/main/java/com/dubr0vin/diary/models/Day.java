package com.dubr0vin.diary.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dubr0vin.diary.db.DateConverter;
import com.dubr0vin.diary.db.HoursConverter;
import java.util.List;

@Entity
public class Day {
    @PrimaryKey
    @ColumnInfo
    @TypeConverters({DateConverter.class})
    @NonNull
    public Date date;

    @ColumnInfo
    @TypeConverters({HoursConverter.class})
    public List<Hour> hours;

    public Day(){}

    @Ignore
    public Day(@NonNull Date date, List<Hour> hours) {
        this.date = date;
        this.hours = hours;
    }
}