package com.example.hour_by_hour;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
    CalendarDay startDate;
    CalendarDay endDate;
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;
    String description;
    String name;
    String location;

    // TODO add alarms to the member variable

    Task() {
        int startYear = 2018;
        int startMonth = 3;
        int startDay = 5;
        int endYear = 2019;
        int endMonth = 5;
        int endDay = 21;

        startDate = new com.prolificinteractive.materialcalendarview.CalendarDay(startYear, startMonth, startDay);
        endDate = new com.prolificinteractive.materialcalendarview.CalendarDay(endYear, endMonth, endDay);

        startHour = 8;
        startMinute = 00;
        int endHour = 12;
        int endMinute = 30;
        location = "Here";
    }
}
