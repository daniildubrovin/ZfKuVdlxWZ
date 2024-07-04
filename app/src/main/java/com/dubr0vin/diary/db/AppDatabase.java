package com.dubr0vin.diary.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dubr0vin.diary.models.Day;
import com.dubr0vin.diary.models.Settings;
import com.dubr0vin.diary.models.Type;

@Database(entities = {Day.class, Type.class, Settings.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DayDao dayDao();
    public abstract TypeDao typeDao();
    public abstract SettingsDao settingsDao();
}
