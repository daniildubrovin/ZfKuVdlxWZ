package com.dubr0vin.diary;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.dubr0vin.diary.models.Settings;

import java.util.Calendar;

public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Logger.d("onStartJob");
        Utility.showNotification(getApplicationContext(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        App.runInDBThread(() -> {
            Settings settings = App.database.settingsDao().getSettings();
            App.runInUI(() -> Utility.createJobTask(getApplicationContext(), settings));
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Logger.d("onStopJob");
        return false;
    }
}
