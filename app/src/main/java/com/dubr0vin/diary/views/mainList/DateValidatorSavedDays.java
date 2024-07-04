package com.dubr0vin.diary.views.mainList;

import android.os.Parcel;

import com.dubr0vin.diary.models.Date;
import com.dubr0vin.diary.models.Day;
import com.google.android.material.datepicker.CalendarConstraints;

import java.util.Calendar;
import java.util.List;

public class DateValidatorSavedDays implements CalendarConstraints.DateValidator {

    private final List<Day> days;
    private final Calendar calendar;

    public DateValidatorSavedDays(List<Day> days) {
        this.days = days;
        this.calendar = Calendar.getInstance();
    }

    public static final Creator<DateValidatorSavedDays> CREATOR =
            new Creator<DateValidatorSavedDays>() {
                @Override
                public DateValidatorSavedDays createFromParcel(Parcel source) {
                    return new DateValidatorSavedDays(null);
                }

                @Override
                public DateValidatorSavedDays[] newArray(int size) {
                    return new DateValidatorSavedDays[size];
                }
            };

    @Override
    public boolean isValid(long date) {
        calendar.setTimeInMillis(date);
        Date _date = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        for(Day day: days){
            if(_date.equals(day.date)) return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
