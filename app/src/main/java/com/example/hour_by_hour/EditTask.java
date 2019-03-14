package com.example.hour_by_hour;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class EditTask extends AppCompatActivity {
    TextView name;
    TextView description;
    TextView location;
    TimePicker startTime;
    TimePicker endTime;
    DatePicker startDate;
    DatePicker endDate;



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

        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        name  = findViewById(R.id.task_name);
        description  = findViewById(R.id.task_description);
        location = findViewById(R.id.location_text_edit);
        startTime = findViewById(R.id.start_time_picker_edit);
        endTime = findViewById(R.id.end_time_picker_edit);
        startDate = findViewById(R.id.start_date_picker_edit);
        endDate = findViewById(R.id.end_date_picker_edit);

        Toolbar myToolbar = findViewById(R.id.toolbar_edit_event);
        setSupportActionBar(myToolbar);

        if((getIntent().getExtras() != null) && !getIntent().getExtras().isEmpty()){
            fillFields((Task) getIntent().getExtras().getParcelable(getString(R.string.EXTRA_TASK_INFO)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                // User clicked on adding the selected event information
                createEvent();
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
    void createEvent() {
        CharSequence message = "Event Created";
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.EXTRA_TASK_INFO), createTask());
        startActivity(intent);
    }

    /**
     * create the task object that will be displayed
     * @return task that was made from the inputs
     */
    Task createTask() {
        Task task = new Task();

        task.setName(name.getText().toString());
        task.setDescription(description.getText().toString());
        task.setLocation(location.getText().toString());

        if(Build.VERSION.SDK_INT >= 23) {
            task.setStartHour(startTime.getHour());
            task.setStartMinute(startTime.getMinute());
            task.setEndHour(endTime.getHour());
            task.setEndMinute(endTime.getMinute());
        } else {
            task.setStartHour(startTime.getCurrentHour());
            task.setStartMinute(startTime.getCurrentMinute());
            task.setEndHour(endTime.getCurrentHour());
            task.setEndMinute(endTime.getCurrentMinute());
        }

        task.setStartDate(CalendarDay.from(startDate.getYear(),startDate.getMonth(), startDate.getDayOfMonth()));
        task.setEndDate(CalendarDay.from(endDate.getYear(),endDate.getMonth(), endDate.getDayOfMonth()));


        return task;
    }

    /**
     * fill the newly created activity with the task given
     * @param task the information to fill out the fields
     */
    void fillFields(Task task){
        name.setText(task.getName());
        description.setText(task.getDescription());
        location.setText(task.getLocation());

        if(Build.VERSION.SDK_INT >= 23) {
            endTime.setHour(task.getEndHour());
            endTime.setMinute(task.getEndMinute());
            startTime.setHour(task.getStartHour());
            startTime.setMinute(task.getStartMinute());
        } else {
            endTime.setCurrentHour(task.getEndHour());
            endTime.setCurrentMinute(task.getEndMinute());
            startTime.setCurrentHour(task.getStartHour());
            startTime.setCurrentMinute(task.getStartMinute());
        }

        startDate.updateDate(task.getStartDate().getYear(), task.getStartDate().getMonth(), task.getStartDate().getDay());
        endDate.updateDate(task.getEndDate().getYear(), task.getEndDate().getMonth(), task.getEndDate().getDay());
    }

    /**
     * delete the event that is currently being edited, if there this is an existing
     * event, delete from the list and return to the main activity. Otherwise, just
     * return to the main activity.
     */
    void deleteEvent() {
        CharSequence message = "Event Deleted";
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
