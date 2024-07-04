package com.dubr0vin.diary.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dubr0vin.diary.db.ColorConverter;

import java.util.Objects;

@Entity
public class Type {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public long id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    @TypeConverters({ColorConverter.class})
    public Color color;

    public Type(){
        name = "Nothing";
        color = Color.White;
    }

    @Ignore
    public Type(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return Objects.equals(name, type.name) && Objects.equals(color, type.color);
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                '}';
    }

    public static Type Sleep = new Type("Sleep", new Color(0, 0, 255));
    public static Type Nothing = new Type("Nothing", Color.White);
}
