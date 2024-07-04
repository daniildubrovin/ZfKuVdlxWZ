package com.dubr0vin.diary.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Settings {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public int id;
    @ColumnInfo
    public boolean isNotifyEveryHour;
    @ColumnInfo
    public int beginSleepHour;
    @ColumnInfo
    public int endSleepHour;

    public Settings(boolean isNotifyEveryHour, int beginSleepHour, int endSleepHour) {
        this.isNotifyEveryHour = isNotifyEveryHour;
        this.beginSleepHour = beginSleepHour;
        this.endSleepHour = endSleepHour;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", isNotifyEveryHour=" + isNotifyEveryHour +
                ", beginSleepHour=" + beginSleepHour +
                ", endSleepHour=" + endSleepHour +
                '}';
    }
}
