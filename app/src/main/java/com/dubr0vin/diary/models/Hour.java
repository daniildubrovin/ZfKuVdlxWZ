package com.dubr0vin.diary.models;

import androidx.annotation.NonNull;

public class Hour {
    public int value;
    public String record;
    public Type type;

    public Hour(int value, String record, Type type) {
        this.value = value;
        this.record = record;
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Hour{" +
                "value=" + value +
                ", record='" + record + '\'' +
                ", type=" + type +
                '}';
    }
}
