package com.example.hour_by_hour;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Displays the task and the information related to it
 */
public class ViewTask extends AppCompatActivity implements DeleteRepeatableTaskAlertFragment.DeleteTaskListener {
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

        TextView name = findViewById(R.id.task_name);
        TextView location = findViewById(R.id.task_location);
        TextView startTime = findViewById(R.id.task_start_time);
        TextView endTime = findViewById(R.id.task_end_time);
        TextView description = findViewById(R.id.task_description);

        if(task == null) {
            task = new Task();
        }

        calendarDay = task.getStartDate();

        name.setText(task.getName());
        location.setText(task.getLocation());
        startTime.setText(task.getStartTime());
        endTime.setText(task.getEndTime());
        description.setText(task.getDescription());
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
                intent.putExtra(getString(R.string.EXTRA_TASK_INFO), taskIndex);
                startActivity(intent);
                return true;

            case(R.id.delete_event_menu_button):
                if(task.getRepeating().equals(getString(R.string.none_array))){
                    deleteEventClick();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    DialogFragment alertDialog = DeleteRepeatableTaskAlertFragment.newInstance(true);
                    alertDialog.show(fm, "fragment_alert");
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void deleteSingleTask() {
        deleteEventClick();
    }

    @Override
    public void deleteAllRepeatableTasks() {
        if(task == null || task.getRepeating().equals(getString(R.string.none_array))){
            return;
        }

        HashMap<String, ArrayList<Task>> days = MainActivity.getSavedDays(this);
        ArrayList<Task> tasks = days.get(task.getStartDate().toString());

        if(tasks != null) {
            while (tasks != null && tasks.remove(task)) {
                days.put(task.getStartDate().toString(),tasks);
                task = task.getNextRepeating(ViewTask.this);
                tasks = days.get(task.getStartDate().toString());
            }
        } else {
            Log.i("ViewTask", "COuldn't Delete Items");
        }

        Toast.makeText(this,"Deleted All Items", Toast.LENGTH_SHORT).show();

        MainActivity.putSavedDays(this, days);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void cancel(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    /**
     * Remove a single item from one day's list
     */
    private void deleteEventClick() {
        HashMap<String, ArrayList<Task>> daysList = MainActivity.getSavedDays(this);

        ArrayList<Task> taskList = daysList.get(task.getStartDate().toString());
        String toastText = "Deleted Task Successfully";

        try {
            if (taskList != null) {
                /*
                if(!taskList.remove(task){
                    toastText = "Error Deleting Task";
                }*/
                taskList.remove(taskIndex);
                daysList.put(task.getStartDate().toString(), taskList);
            } else {
                Log.i("ViewTask", "List is null");
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d("ViewTask", e.getMessage());
        }

        MainActivity.putSavedDays(this, daysList);

        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
