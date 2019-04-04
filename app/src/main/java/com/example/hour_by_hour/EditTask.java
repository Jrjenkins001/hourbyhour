package com.example.hour_by_hour;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Collections.sort;

/**
 * Activity used to create a new task or modify an existing one
 */
public class EditTask extends AppCompatActivity implements DeleteRepeatableTaskAlertFragment.DeleteTaskListener {
    private TextView name;
    private TextView description;
    private TextView location;
    private TimePicker startTime;
    private TimePicker endTime;
    private DatePicker startDate;
    private Task task;
    private int taskIndex;
    private Spinner repeatingSpinner;

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

        repeatingSpinner = findViewById(R.id.spinner_edit);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.repeating_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatingSpinner.setAdapter(adapter);

        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        name  = findViewById(R.id.event_name_edit);
        description  = findViewById(R.id.description_edit);
        location = findViewById(R.id.location_text_edit);
        startTime = findViewById(R.id.start_time_picker_edit);
        endTime = findViewById(R.id.end_time_picker_edit);
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
                if(task == null){
                    FragmentManager fm = getSupportFragmentManager();
                    DeletePotentialAlertFragment alertDialog = DeletePotentialAlertFragment.newInstance();
                    alertDialog.show(fm, "fragment_alert");
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    DeleteRepeatableTaskAlertFragment alertDialog = new DeleteRepeatableTaskAlertFragment();
                    alertDialog.show(fm, "fragment_alert");
                }

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

    /**
     * Will take an existing task and change the fields around allowing for the
     * item to be updated and displayed properly
     */
    private void modifyEvent() {
        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(findViewById(R.id.toolbar_edit_event));

        ArrayList<Task> tasks = days.get(task.getStartDate().toString());
        Task newTask = createTask();

        if(tasks != null) {
            tasks.remove(taskIndex);
        } else {
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
        task.setRepeating(repeatingSpinner.getSelectedItem().toString());

        if(Build.VERSION.SDK_INT >= 23) {
            task.setStartHour(startTime.getHour());
            task.setStartMinute(startTime.getMinute());
            task.setEndMinute(endTime.getMinute());
            task.setEndHour(endTime.getHour());
        } else {
            task.setStartHour(startTime.getCurrentHour());
            task.setStartMinute(startTime.getCurrentMinute());
            task.setEndHour(endTime.getCurrentHour());
            task.setEndMinute(endTime.getCurrentMinute());
        }

        task.setStartDate(CalendarDay.from(startDate.getYear(),startDate.getMonth(), startDate.getDayOfMonth()));


        return task;
    }

    /**
     * fill the newly created activity with the task given
     * @param task the information to fill out the fields
     */
    private void fillFields(@NotNull Task task){
        name.setText(task.getName());
        description.setText(task.getDescription());
        location.setText(task.getLocation());

        if(Build.VERSION.SDK_INT >= 23) {
            startTime.setHour(task.getStartHour());
            startTime.setMinute(task.getStartMinute());
            endTime.setHour(task.getEndHour());
            endTime.setMinute(task.getEndMinute());
        } else {
            startTime.setCurrentHour(task.getStartHour());
            startTime.setCurrentMinute(task.getStartMinute());
            endTime.setCurrentHour(task.getEndHour());
            endTime.setCurrentMinute(task.getEndMinute());
        }

        startDate.updateDate(task.getStartDate().getYear(), task.getStartDate().getMonth(), task.getStartDate().getDay());
    }

    @Override
    public void deleteAllRepeatableTasks() {
        if(task == null || task.getRepeating().equals(getString(R.string.none_array))){
            return;
        }

        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(new View(this));
        ArrayList<Task> tasks = days.get(task.getStartDate().toString());

        if(tasks != null) {
            while (tasks.remove(task)) {
                days.put(task.getStartDate().toString(),tasks);
                task = task.getNextRepeating(EditTask.this);
                tasks = days.get(task.getStartDate().toString());
                if(tasks == null){
                    return;
                }
            }
        }

        MainActivity.putSavedDays(new View(this), days);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteSingleTask() {
        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(new View(this));
        ArrayList<Task> tasks = days.get(task.getStartDate().toString());

        if(tasks != null){
            if(tasks.remove(task)){
                Toast.makeText(this, "Deleted the task", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Error deleting task", Toast.LENGTH_SHORT).show();
            }

            days.put(task.getStartDate().toString(), tasks);

            MainActivity.putSavedDays(new View(this), days);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getString(R.string.EXTRA_CALENDAR_DAY), task.getStartDate());
            startActivity(intent);
        }
    }

    @Override
    public void cancel(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }
}
