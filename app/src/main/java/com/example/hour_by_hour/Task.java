package com.example.hour_by_hour;

import android.os.Parcel;
import android.os.Parcelable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import static com.prolificinteractive.materialcalendarview.CalendarDay.from;

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
        int startYear = 9999;
        int startMonth = 99;
        int startDay = 99;
        int endYear = 9999;
        int endMonth = 99;
        int endDay = 99;
        name = "No name on file";

        startDate = from(startYear, startMonth, startDay);
        endDate = from(endYear, endMonth, endDay);

        startHour = 99;
        startMinute = 99;
        endHour = 99;
        endMinute = 99;
        location = "No Location on file";
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

    int getStartHour() {
        return startHour;
    }

    int getStartMinute() {
        return startMinute;
    }

    int getEndHour() {
        return endHour;
    }

    int getEndMinute() {
        return endMinute;
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
