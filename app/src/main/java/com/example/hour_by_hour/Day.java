package com.example.hour_by_hour;

import java.io.Serializable;
import java.util.ArrayList;

class Day implements Serializable {
    final ArrayList<Task> dayTasks;

    private Day() { dayTasks = new ArrayList<>(); }

    Day(ArrayList<Task> tasks) {
        dayTasks = tasks;
    }

    // TODO remove after testing
    static ArrayList<Day> createDays(int num){
        ArrayList<Day> days = new ArrayList<>();
        days.add(new Day());
        for(int i = 0; i < num; i++){
            days.get(0).dayTasks.add(new Task());
        }
        return days;
    }
}
