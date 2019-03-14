package com.example.hour_by_hour;

import android.os.Parcel;
import android.os.Parcelable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Task implements Parcelable {
    private CalendarDay startDate;
    private CalendarDay endDate;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

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
        name = "This appointment";

        startDate = new com.prolificinteractive.materialcalendarview.CalendarDay(startYear, startMonth, startDay);
        endDate = new com.prolificinteractive.materialcalendarview.CalendarDay(endYear, endMonth, endDay);

        startHour = 8;
        startMinute = 0;
        endHour = 12;
        endMinute = 30;
        location = "Here";
    }

    String getLocation() {
        return location;
    }

    String getDescription() {
        return description;
    }

    String getName() {
        return name;
    }

    String getStartTime() {
        return startHour + ":" + (startMinute < 10 ? "0" : "" ) + startMinute;
    }

    String getEndTime() {
        return endHour + ":" + (endMinute < 10 ? "0" : "") + endMinute;
    }

    CalendarDay getEndDate() {
        return endDate;
    }

    CalendarDay getStartDate() {
        return startDate;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setName(String name) {
        this.name = name;
    }

    void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    void setStartDate(CalendarDay startDate) {
        this.startDate = startDate;
    }

    void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    void setEndDate(CalendarDay endDate) {
        this.endDate = endDate;
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
    }
}
