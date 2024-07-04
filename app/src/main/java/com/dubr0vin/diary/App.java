package com.dubr0vin.diary;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.dubr0vin.diary.db.AppDatabase;
import com.dubr0vin.diary.models.Settings;
import com.dubr0vin.diary.models.Type;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    public static String notificationChannelID = "diaryNotification";
    public static int jobTaskID = 12345;

    public static AppDatabase database;
    public static ExecutorService dbThreadPool;
    public static Handler uiThread;

    @Override
    public void onCreate() {
        super.onCreate();
        dbThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        uiThread = new Handler(Looper.getMainLooper());
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();

        setInitialTypes();
        setInitialSettings();
        Utility.createNotificationChannel(this);
    }

    public static void runInDBThread(Runnable runnable){
        dbThreadPool.execute(runnable);
    }

    public static void runInUI(Runnable runnable){
        uiThread.post(runnable);
    }

    public void setInitialTypes(){
        runInDBThread(() -> {
            if(database.typeDao().getAllTypes().isEmpty()){
                Type[] types = new Type[]{Type.Sleep, Type.Nothing};
                for (Type type: types) database.typeDao().insert(type);
            }
        });
    }

    public void setInitialSettings(){
        runInDBThread(() -> {
            if(database.settingsDao().getSettings() == null){
                Settings settings = new Settings(false, 22, 8);
                database.settingsDao().setSettings(settings);
            }
        });
    }
}
