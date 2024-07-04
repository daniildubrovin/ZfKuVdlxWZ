package com.dubr0vin.diary.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dubr0vin.diary.models.Settings;

@Dao
public interface SettingsDao {
    @Query("SELECT * FROM Settings")
    Settings getSettings();

    @Insert
    void setSettings(Settings settings);

    @Update
    void updateSettings(Settings settings);
}
