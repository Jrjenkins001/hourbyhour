package com.example.hour_by_hour;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Task implements Serializable, Parcelable, Comparable<Task> {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    String description;
    String name;
    String location;

    int startYear;
    int startMonth;
    int startDay;

    String repeating;

    // TODO add alarms to the member variable

    Task() {
        name = "No name";
        description = "No Description";
        location = "No Location";

        startYear = 0;
        startMonth = 0;
        startDay = 0;

        startHour = 0;
        startMinute = 0;
        endHour = 0;
        endMinute = 0;
        setStartDate(CalendarDay.today());

        repeating = "None";
    }

    Task (Task task) {
        this.setName(task.name);
        this.setDescription(task.getDescription());
        this.setLocation(task.getLocation());
        this.setStartDate(task.getStartDate());
        this.setStartHour(task.getStartHour());
        this.setStartMinute(task.getStartMinute());
        this.setEndHour(task.getEndHour());
        this.setEndMinute(task.getEndMinute());
        this.setStartDate(task.getStartDate());
        this.repeating = task.repeating;
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

    CalendarDay getStartDate() {
        return CalendarDay.from(startYear,startMonth,startDay);
    }

    String getRepeating() { return repeating; }

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
        this.startYear = startDate.getYear();
        this.startMonth = startDate.getMonth();
        this.startDay = startDate.getDay();
    }

    private void setStartDate(int year, int month, int day) {
        this.startYear = year;
        this.startMonth = month;
        this.startDay = day;
    }

    void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    void setRepeating(String repeating) {
        this.repeating = repeating;
    }

    Task getNextRepeating(Context context){
        Task task = this;
        int addDay = startDay;
        int addMonth = startMonth;
        int addYear = startYear;

        if (getRepeating().equals(context.getString(R.string.daily_array))){
            return determineDay(this, 1);
        } else if (getRepeating().equals((context.getString(R.string.weekly_array)))){
            return determineDay(this, 7);
        } else if (getRepeating().equals((context.getString(R.string.monthly_array)))) {
            if(addMonth == 12){
                addMonth = 1;
                addYear++;
            } else {
                addMonth++;
            }
        } else if (getRepeating().equals((context.getString(R.string.yearly_array)))) {
            addYear++;
        } else {
            return null;
        }

        task.setStartDate(addYear, addMonth, addDay);

        return task;
    }

    private static Task determineDay(Task task, int addNum){
        int maxDaysOfMonth;

        int startMonth = task.startMonth;
        int startDate = task.startDay;
        int startYear = task.startYear;

        if(startMonth == 1 || startMonth == 3 || startMonth == 5 || startMonth == 7
                || startMonth == 8 || startMonth == 10 || startMonth == 12) {
            maxDaysOfMonth = 30;
        } else if (startMonth == 2) {
            maxDaysOfMonth = 28;
        } else {
            maxDaysOfMonth = 31;
        }

        if (startDate + addNum > maxDaysOfMonth){
            startDate = startDate + addNum - maxDaysOfMonth;
            startMonth++;
            if(startMonth > 12) {
                startMonth = 1;
                startYear++;
            }
        } else {
            startDate += addNum;
        }
        task.setStartDate(startYear,startMonth,startDate);
        return task;
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
        out.writeInt(startYear);
        out.writeInt(startMonth);
        out.writeInt(startDay);
        out.writeString(repeating);
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
        startYear = in.readInt();
        startMonth = in.readInt();
        startDay = in.readInt();
        repeating = in.readString();
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
