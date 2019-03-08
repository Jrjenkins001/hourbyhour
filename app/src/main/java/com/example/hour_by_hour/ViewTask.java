package com.example.hour_by_hour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTask extends AppCompatActivity {
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Bundle extras = getIntent().getExtras();

        try {
            task = extras.getParcelable("TASK");
        } catch (Exception e) {
            Intent intent = new Intent(this, MainActivity.class);
            Toast toast = Toast.makeText(this, "Error loading task", Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
        }
        TextView name = findViewById(R.id.task_name);
        TextView location = findViewById(R.id.task_location);
        TextView startTime = findViewById(R.id.task_start_time);
        TextView endTime = findViewById(R.id.task_end_time);
        TextView description = findViewById(R.id.task_description);

        name.setText(task.name);
        location.setText(task.location);
        startTime.setText(task.startTime);
        endTime.setText(task.endTime);
        description.setText(task.description);
    }
}
