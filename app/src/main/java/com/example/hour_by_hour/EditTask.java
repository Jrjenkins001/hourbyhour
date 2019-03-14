package com.example.hour_by_hour;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.time.LocalDate;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.time.LocalDate;
import java.util.GregorianCalendar;

public class EditTask extends AppCompatActivity {

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
        Toolbar myToolbar = findViewById(R.id.toolbar_edit_event);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();

        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
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

        Task task = createTask();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    Task createTask() {
        Task task = new Task();

        GregorianCalendar gc = new GregorianCalendar(2019, 12, 7);
        LocalDate ld = new LocalDate(gc);

        TextView name = findViewById(R.id.task_name);
        TextView description = findViewById(R.id.task_name);
        TextView location = findViewById(R.id.location_text_edit);
        //Spinner startTime = findViewById(R.id.start_time_picker_edit);
        //Spinner endTime = findViewById(R.id.end_time_picker_edit);
        //Spinner startDate = findViewById(R.id.start_date_picker_edit);
        //Spinner endDate = findViewById(R.id.end_date_picker_edit);

        task.setName(name.getText().toString());
        task.setDescription(description.getText().toString());
        task.setLocation(location.getText().toString());
        //task.setStartHour(startTime);


        return task;
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
