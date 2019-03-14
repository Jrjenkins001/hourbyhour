package com.example.hour_by_hour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ViewTask extends AppCompatActivity {
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        task = getIntent().getParcelableExtra("TASK");

        TextView name = findViewById(R.id.task_name);
        TextView location = findViewById(R.id.task_location);
        TextView startTime = findViewById(R.id.task_start_time);
        TextView endTime = findViewById(R.id.task_end_time);
        TextView description = findViewById(R.id.task_description);

        name.setText(task.name);
        location.setText(task.location);
        startTime.setText(task.getStartTime());
        endTime.setText(task.getEndTime());
        description.setText(task.description);
    }
}
