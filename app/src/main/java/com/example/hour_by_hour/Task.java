package com.example.hour_by_hour;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Data holder for an event. Has a name, location, description, start and end hours and minutes,
 * and a starting year month and day.
 */
public class Task implements Serializable, Parcelable, Comparable<Task> {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    private String description;
    private String name;
    private String location;

    private int startYear;
    private int startMonth;
    private int startDay;

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

    private Task(Task o){
        this.setDescription(o.getDescription());
        this.setName(o.getName());
        this.setLocation(o.getLocation());
        this.setStartDate(o.startYear, o.startMonth, o.startDay);
        this.setStartHour(o.getStartHour());
        this.setStartMinute(o.getStartMinute());
        this.setEndHour(o.getEndHour());
        this.setEndMinute(o.getEndMinute());
        this.setRepeating(o.getRepeating());
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

    /**
     * If the task is repeating, it will determine where the task's next repetition is at and return that task.
     * @param context allows for access to the repetition type
     * @return the next task in the repeating list
     */
    Task getNextRepeating(Context context){
        Task task = new Task(this);
        int addDay = startDay;
        int addMonth = startMonth;
        int addYear = startYear;

        if (getRepeating().equals(context.getString(R.string.daily_array))){
            task = determineDay(task, 1);
        } else if (getRepeating().equals((context.getString(R.string.weekly_array)))){
            task = determineDay(task, 7);
        } else if (getRepeating().equals((context.getString(R.string.monthly_array)))) {
            if(addMonth == 12){
                addMonth = 1;
                addYear++;
            } else {
                addMonth++;
            }
            task.setStartDate(addYear, addMonth, addDay);
        } else if (getRepeating().equals((context.getString(R.string.yearly_array)))) {
            addYear++;
            task.setStartDate(addYear, addMonth, addDay);
        } else {
            return null;
        }

        return task;
    }

    /**
     * This will determine where to put the next repeating day at. If the
     * day is in a different month or year, adjustments will be made as
     * necessary
     * @param task the task that is being repeated
     * @param addNum how far out is the repeated task
     * @return the next repeating task
     */
    @Contract("null, _ -> null")
    private static Task determineDay(Task task, int addNum){
        if(task == null){
            return null;
        }

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
        @NotNull
        @Contract("_ -> new")
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @NotNull
        @Contract(value = "_ -> new", pure = true)
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private Task(@NotNull Parcel in) {
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
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task))
            return false;

        Task other = (Task) obj;
        return this.getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
