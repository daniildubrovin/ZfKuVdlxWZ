package com.dubr0vin.diary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dubr0vin.diary.models.Color;
import com.dubr0vin.diary.models.Date;
import com.dubr0vin.diary.models.Day;
import com.dubr0vin.diary.models.Hour;
import com.dubr0vin.diary.models.Settings;
import com.dubr0vin.diary.models.Type;
import com.dubr0vin.diary.views.MainActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utility {
    public static List<Hour> generateEmptyHours(){
        ArrayList<Hour> hours = new ArrayList<>();
        for (int i = 1; i <= 24; i++) hours.add(new Hour(i,"", Type.Nothing));
        return hours;
    }

    public static Color getRandomUniqueColor(ArrayList<Color> otherColors){
        Random random = new Random(System.currentTimeMillis());
        ArrayList<Color> randColors = new ArrayList<>();
        int limit = 1000, i = 0;
        while(i < limit){
            Color color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            randColors.add(color);
            boolean isFind = true;
            for (Color otherColor: otherColors){
                if (otherColor.equals(color)) {
                    isFind = false;
                    break;
                }
            }
            if(isFind) return color;
            i++;
        }
        return randColors.get(random.nextInt(randColors.size()));
    }

    public static String getFragmentName(Fragment fragment){
        return fragment.getClass().getName();
    }

    public static void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(App.notificationChannelID, "diaryChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(android.graphics.Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setDescription("Diary notifications");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, int hour){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("hour", hour);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int intentFlag = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, intentFlag);

        Notification notification = new NotificationCompat.Builder(context, App.notificationChannelID)
                .setContentTitle("Diary. New hour.")
                .setContentText("It's time to make a record.")
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setLights(android.graphics.Color.RED, 1, 0)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManagerCompat.from(context).notify(new Random().nextInt(), notification);
    }

    public static void createJobTask(Context context, Settings settings){
        if(!settings.isNotifyEveryHour) {
            System.out.println("cancel job");
            cancelJobTasks(context);
        }
        else {
            System.out.println("set job");
            updateSleepHours(settings);

            JobInfo.Builder builder = new JobInfo.Builder(App.jobTaskID, new ComponentName(context, NotificationJobService.class))
                    .setMinimumLatency(getTimeUntilNextNotification(settings.beginSleepHour, settings.endSleepHour))
                    .setOverrideDeadline(getTimeUntilNextNotification(settings.beginSleepHour, settings.endSleepHour) + 3);

            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
    }

    public static long getTimeUntilNextNotification(int beginSleepHour, int endSleepHour){
        Calendar calendar = Calendar.getInstance();
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutesUntilNextHour = 60 - calendar.get(Calendar.MINUTE);
        /*if(curHour >= beginSleepHour) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            return TimeUnit.MINUTES.toMillis(minutesUntilNextHour + (24 - calendar.get(Calendar.HOUR_OF_DAY))*60 + endSleepHour* 60L + 60);
        }
        return TimeUnit.MINUTES.toMillis(minutesUntilNextHour);*/
        return TimeUnit.MINUTES.toMillis(1);
    }

    public static void cancelJobTasks(Context context){
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancelAll();
    }

    public static void updateSleepHours(Settings settings){
        App.runInDBThread(() -> {
            Calendar calendar = Calendar.getInstance();
            Day curDay = App.database.dayDao().getByDate(new Date(calendar));
            if(curDay == null) {
                curDay = new Day(new Date(calendar), generateEmptyHours());
                App.database.dayDao().insert(curDay);
            }

            for (int i = 0; i < 24; i++) {
                Hour curHour = curDay.hours.get(i);
                if(curHour.type.equals(Type.Sleep)) {
                    curHour.type = Type.Nothing;
                    curHour.record = "";
                }
            }

            for (int i = 0; i < settings.endSleepHour; i++) {
                curDay.hours.get(i).type = Type.Sleep;
                curDay.hours.get(i).record = "sleep";
            }

            for (int i = settings.beginSleepHour-1; i < 24; i++) {
                curDay.hours.get(i).type = Type.Sleep;
                curDay.hours.get(i).record = "sleep";
            }

            App.database.dayDao().update(curDay);
        });
    }
}
