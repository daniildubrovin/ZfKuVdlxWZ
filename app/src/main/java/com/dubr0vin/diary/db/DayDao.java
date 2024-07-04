package com.dubr0vin.diary.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.dubr0vin.diary.models.Date;
import com.dubr0vin.diary.models.Day;

import java.util.List;

@Dao
public interface DayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Day day);

    @Query("SELECT * FROM Day WHERE date == :date")
    @TypeConverters({DateConverter.class})
    Day getByDate(Date date);

    @Query("SELECT * FROM Day")
    List<Day>  getAllDays();

    @Update
    void update(Day day);

    @Delete
    void delete(Day day);
}