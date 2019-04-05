package com.example.hour_by_hour;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.hour_by_hour.Notification.CHANNEL_1_ID;
import static com.example.hour_by_hour.Notification.CHANNEL_2_ID;
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
    private NotificationManagerCompat notificationManger;

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


        notificationManger = NotificationManagerCompat.from(this);
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
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment alertDialog;
                if(task == null){
                    alertDialog = DeletePotentialAlertFragment.newInstance();
                } else if(task.getRepeating().equals(getString(R.string.none_array))){
                    alertDialog = DeleteRepeatableTaskAlertFragment
                            .newInstance(false);
                } else {
                    alertDialog = DeleteRepeatableTaskAlertFragment
                            .newInstance(true);
                }

                alertDialog.show(fm, "fragment_alert");
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
        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(findViewById(R.id.toolbar_edit_event).getContext());

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

        MainActivity.putSavedDays(this, days);

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
            this.deleteSingleTask();
            return;
        }

        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(this);
        ArrayList<Task> tasks = days.get(task.getStartDate().toString());

        String toastText = "Successfully removed Task";

        if(tasks != null) {
            while (tasks != null && tasks.remove(task)) {
                days.put(task.getStartDate().toString(),tasks);
                task = task.getNextRepeating(EditTask.this);
                tasks = days.get(task.getStartDate().toString());
            }
        } else {
            toastText = "Unable to remove Task";
        }

        Toast.makeText(this,toastText, Toast.LENGTH_SHORT).show();

        MainActivity.putSavedDays(this, days);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteSingleTask() {
        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(this);
        ArrayList<Task> tasks = days.get(task.getStartDate().toString());

        if(tasks != null){
            tasks.remove(taskIndex);
            days.put(task.getStartDate().toString(), tasks);

            MainActivity.putSavedDays(this, days);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getString(R.string.EXTRA_CALENDAR_DAY), task.getStartDate());
            startActivity(intent);
        }
    }

    @Override
    public void cancel(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    public void sendOnChannel1(View view) {
        String title = "Event beginning soon";//editTextTitle.getText().toString(); event_name_edit start_time_picker_edit name.getText().toString()
        String message;
        if(Build.VERSION.SDK_INT >= 23) {
            message = startTime.getHour() + ":" + startTime.getMinute() + " " + name.getText().toString();
        } else {
            message = name.getText().toString() + "begins soon";
        }

        Intent notifyIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notifyIntent,0);

        Intent broadcastIntent = new Intent (this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();

        notificationManger.notify(1,notification);
    }

    public void sendOnChannel2(View view) {
        String title = "Event beginning soon";//editTextTitle.getText().toString(); event_name_edit start_time_picker_edit name.getText().toString()
        String message;
        String dateTime;

        //I created this if to try and convert the start time to milliseconds but I couldn't do it
        if(Build.VERSION.SDK_INT >= 23) {
            dateTime = startDate.getYear() + "/" + startDate.getMonth() +
                    "/" + startDate.getDayOfMonth() + " " + startTime.getHour() +
                    ":" + startTime.getMinute() + ":00";
        }

        if(Build.VERSION.SDK_INT >= 23) {
            message = startTime.getHour() + ":" + startTime.getMinute() + " " + name.getText().toString();
        } else {
            message = name.getText().toString() + "begins soon";
        }

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        int delay = 1000000;
        //notificationManger.notify(2,notification);
        scheduleNotification(notification, delay);
    }

    public void scheduleNotification(android.app.Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID,1);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureTimeInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureTimeInMillis, pendingIntent);

    }
}
