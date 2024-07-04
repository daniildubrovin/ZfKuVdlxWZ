package com.dubr0vin.diary.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dubr0vin.diary.models.Type;

import java.util.List;

@Dao
public interface TypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Type type);

    @Query("SELECT * FROM Type WHERE id == :id")
    Type getType(int id);

    @Query("SELECT * FROM Type")
    List<Type> getAllTypes();

    @Update
    void edit(Type type);

    @Delete
    void delete(Type type);
}
