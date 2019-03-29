package com.example.hour_by_hour;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Collections.sort;

public class EditTask extends AppCompatActivity {
    private TextView name;
    private TextView description;
    private TextView location;
    private TimePicker startTime;
    private DatePicker startDate;
    private Task task;
    private int taskIndex;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ActionBar ab = getSupportActionBar();

        Spinner spinner = findViewById(R.id.spinner_edit);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.repeating_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        name  = findViewById(R.id.event_name_edit);
        description  = findViewById(R.id.description_edit);
        location = findViewById(R.id.location_text_edit);
        startTime = findViewById(R.id.start_time_picker_edit);
        startDate = findViewById(R.id.start_date_picker_edit);

        Toolbar myToolbar = findViewById(R.id.toolbar_edit_event);
        setSupportActionBar(myToolbar);

        CalendarDay day;
        if((task = getIntent().getParcelableExtra(getString(R.string.EXTRA_TASK)))!= null){
            fillFields(task);
        } else if((day = getIntent().getParcelableExtra(getString(R.string.EXTRA_CALENDAR_DAY))) != null){
            startDate.updateDate(day.getYear(), day.getMonth(), day.getDay());
        }

        taskIndex = getIntent().getIntExtra(getString(R.string.EXTRA_TASK_INFO), -100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                // User clicked on adding the selected event information
                if(task == null) {
                    createEvent();
                } else {
                    modifyEvent();
                }
                return true;

            case R.id.action_delete_event:
                // User clicked on deleting the event currently being worked on
                deleteEvent();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * create a new event with the information provided in the inputs and returns
     * to the main activity afterward.
     */
    private void createEvent() {
        CharSequence message = "Event Created";
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();

        Task task = createTask();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.EXTRA_TASK), (Parcelable) task);
        intent.putExtra(getString(R.string.EXTRA_CALENDAR_DAY), task.getStartDate());
        startActivity(intent);

    }

    private void modifyEvent() {
        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(findViewById(R.id.toolbar_edit_event));

        ArrayList<Task> tasks = days.get(task.getStartDate().toString());
        Task newTask = createTask();

        Log.i("EditTask", new Gson().toJson(tasks));

        if(tasks != null) {
            tasks.remove(taskIndex);
        } else {
            Log.e("EditTask", "ERROR: tasks is null");
            return;
        }

        if(task.getStartDate() != newTask.getStartDate()){
            days.put(task.getStartDate().toString(), tasks);
            tasks = days.get(newTask.getStartDate().toString());

            if(tasks == null){
                tasks = new ArrayList<>();
            }
        }

        tasks.add(newTask);
        sort(tasks);

        days.put(newTask.getStartDate().toString(), tasks);

        MainActivity.putSavedDays(new View(this), days);

        Toast toast = Toast.makeText(getApplicationContext(), "Event modified", Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.EXTRA_CALENDAR_DAY), newTask.getStartDate());
        startActivity(intent);
    }

    /**
     * create the task object that will be displayed
     * @return task that was made from the inputs
     */
    private Task createTask() {
        Task task = new Task();

        task.setName(name.getText().toString());
        task.setDescription(description.getText().toString());
        task.setLocation(location.getText().toString());

        if(Build.VERSION.SDK_INT >= 23) {
            task.setStartHour(startTime.getHour());
            task.setStartMinute(startTime.getMinute());
        } else {
            task.setStartHour(startTime.getCurrentHour());
            task.setStartMinute(startTime.getCurrentMinute());
        }

        task.setStartDate(CalendarDay.from(startDate.getYear(),startDate.getMonth(), startDate.getDayOfMonth()));


        return task;
    }

    /**
     * fill the newly created activity with the task given
     * @param task the information to fill out the fields
     */
    private void fillFields(Task task){
        name.setText(task.getName());
        description.setText(task.getDescription());
        location.setText(task.getLocation());

        if(Build.VERSION.SDK_INT >= 23) {
            startTime.setHour(task.getStartHour());
            startTime.setMinute(task.getStartMinute());
        } else {
            startTime.setCurrentHour(task.getStartHour());
            startTime.setCurrentMinute(task.getStartMinute());
        }

        startDate.updateDate(task.getStartDate().getYear(), task.getStartDate().getMonth(), task.getStartDate().getDay());
    }

    /**
     * delete the event that is currently being edited, if there this is an existing
     * event, delete from the list and return to the main activity. Otherwise, just
     * return to the main activity.
     */
    private void deleteEvent() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DeleteAlertFragment alertDialog = DeleteAlertFragment.newInstance();
        alertDialog.show(fm, "fragment_alert");
    }
}
