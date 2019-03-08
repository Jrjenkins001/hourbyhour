package com.example.hour_by_hour;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>
implements Serializable {
    private List<Task> taskList;
    Context context;

    public class TaskViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Serializable{
        // each data item is an Event object
        TextView taskName;
        TextView locationName;
        TextView startTime;
        TextView endTime;

        TaskViewHolder(View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.task_name_card);
            locationName = itemView.findViewById(R.id.location_name_card);
            startTime = itemView.findViewById(R.id.start_time_card);
            endTime = itemView.findViewById(R.id.end_time_card);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Task task = taskList.get(position);
                Intent intent = new Intent(view.getContext(), ViewTask.class);
                intent.putExtra("TASK", task);
                view.getContext().startActivity(intent);
            }
        }
    }

    TaskAdapter(List<Task> taskList){
        this.taskList = taskList;
    }

    @Override
    @NonNull
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.task_view_card, parent, false);

        return new TaskViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        TextView nameView = holder.taskName;
        TextView locationView = holder.locationName;
        TextView startTimeView = holder.startTime;
        TextView endTimeView = holder.endTime;

        nameView.setText(task.name);
        locationView.setText(task.location);
        startTimeView.setText(task.startTime);
        endTimeView.setText(task.endTime);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
