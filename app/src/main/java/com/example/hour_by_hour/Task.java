package com.example.hour_by_hour;

import android.os.Parcel;
import android.os.Parcelable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Time;

import static com.prolificinteractive.materialcalendarview.CalendarDay.from;

public class Task implements Parcelable, Comparable<Task> {
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
        int startYear = 0;
        int startMonth = 0;
        int startDay = 0;
        int endYear = 0;
        int endMonth = 0;
        int endDay = 0;
        name = "No name";

        startDate = from(startYear, startMonth, startDay);
        endDate = from(endYear, endMonth, endDay);

        startHour = 0;
        startMinute = 0;
        endHour = 0;
        endMinute = 0;
        location = "No Location";
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

    @Override
    public int compareTo(Task o) {
        if(this.startHour > o.startHour) {
            return 1;
        } else if (this.startHour < o.startHour){
            return -1;
        } else {
            if(this.startMinute > o.startMinute) {
                return 1;
            } else if (this.startMinute < o.startMinute) {
                return -1;
            } else {
                return this.name.compareTo(o.name);
            }
        }
    }
}
