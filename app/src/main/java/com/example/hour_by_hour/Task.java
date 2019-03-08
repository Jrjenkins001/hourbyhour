package com.example.hour_by_hour;

import android.os.Parcel;
import android.os.Parcelable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Task implements Parcelable {
    CalendarDay startDate;
    CalendarDay endDate;
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;

    String description;
    String name;
    String location;
    String startTime;
    String endTime;

    // TODO add alarms to the member variable

    Task() {
        int startYear = 2018;
        int startMonth = 3;
        int startDay = 5;
        int endYear = 2019;
        int endMonth = 5;
        int endDay = 21;
        name = "This appointment";

        startDate = new com.prolificinteractive.materialcalendarview.CalendarDay(startYear, startMonth, startDay);
        endDate = new com.prolificinteractive.materialcalendarview.CalendarDay(endYear, endMonth, endDay);

        startHour = 8;
        startMinute = 0;
        endHour = 12;
        endMinute = 30;
        location = "Here";

        startTime = startHour + ":" + (startMinute < 10 ? "0" : "" ) + startMinute;
        endTime = endHour + ":" + endMinute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(location);
        out.writeString(description);
        out.writeInt(startHour);
        out.writeInt(endHour);
        out.writeInt(startMinute);
        out.writeInt(endMinute);
        out.writeString(startTime);
        out.writeString(endTime);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private Task(Parcel in) {
        name = in.readString();
        location = in.readString();
        description = in.readString();
        startHour = in.readInt();
        endHour = in.readInt();
        startMinute = in.readInt();
        endMinute = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
    }
}
