package com.example.hour_by_hour;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Day implements Serializable {
    ArrayList<Task> dayTasks;

    private Day() { dayTasks = new ArrayList<>(); }

    Day(ArrayList<Task> tasks) {
        dayTasks = tasks;
    }

    static ArrayList<Day> createDays(int num){
        ArrayList<Day> days = new ArrayList<>();
        days.add(new Day());
        for(int i = 0; i < num; i++){
            days.get(0).dayTasks.add(new Task());
        }
        return days;
    }
}
