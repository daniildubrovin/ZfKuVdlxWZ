package com.dubr0vin.diary;

import android.util.Log;

import androidx.annotation.NonNull;

public class Logger {
    private static String TAG = "Diary";

    public static void d(@NonNull String msg){
        Log.d(TAG, msg);
    }
}
