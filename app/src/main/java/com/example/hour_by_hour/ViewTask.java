package com.example.hour_by_hour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewTask extends AppCompatActivity {
    private CalendarDay calendarDay;
    private Task task;
    private int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Toolbar myToolbar = findViewById(R.id.toolbar_task_view);
        setSupportActionBar(myToolbar);

        task = getIntent().getParcelableExtra(getString(R.string.EXTRA_TASK));
        taskIndex = getIntent().getIntExtra(getString(R.string.EXTRA_TASK_INFO), -100);
        if(task == null) {
            task = new Task();
        }

        TextView name = findViewById(R.id.task_name);
        TextView location = findViewById(R.id.task_location);
        TextView startTime = findViewById(R.id.task_start_time);
        TextView endTime = findViewById(R.id.task_end_time);
        TextView description = findViewById(R.id.task_description);

        calendarDay = task.getStartDate();

        name.setText(task.name);
        location.setText(task.location);
        startTime.setText(task.getStartTime());
        endTime.setText(task.getEndTime());
        description.setText(task.description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case(R.id.edit_event_menu_button):
                Intent intent = new Intent(this, EditTask.class);
                intent.putExtra(getString(R.string.EXTRA_TASK), (Parcelable) task);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void deleteEventClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.saved_data_info), Context.MODE_PRIVATE);
        String daysListJSON = sharedPreferences.getString(getString(R.string.saved_preferences_json), "NULL");

        if(daysListJSON != null && !daysListJSON.equals("NULL")){
            Gson g = new Gson();
            Type type = new TypeToken<HashMap<String, ArrayList<Task>>>(){}.getType();
            HashMap<String, ArrayList<Task>> daysList = g.fromJson(daysListJSON, type);

            ArrayList<Task> taskList = daysList.get(calendarDay.toString());


            try {
                if (taskList != null) {
                    taskList.remove(taskIndex);

                    daysList.put(calendarDay.toString(), taskList);
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d("ViewTask", e.getMessage());
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.saved_preferences_json), g.toJson(daysList))
                    .apply();

            Toast toast = Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
