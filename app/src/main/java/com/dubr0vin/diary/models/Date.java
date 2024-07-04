package com.dubr0vin.diary.models;

import java.util.Calendar;
import java.util.Objects;

public class Date {
    public int year;
    public int month;
    public int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date(Calendar calendar){
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return year == date.year && month == date.month && day == date.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        String strDay = day < 10 ? "0" + day : "" + day;
        String strMonth = month < 10 ? "0" + month : "" + month;
        return strDay + "." + strMonth + "." + year;
    }

    public Calendar getCalendarInstance(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1,day);
        return calendar;
    }
}
