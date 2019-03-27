package com.example.hour_by_hour;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Creates the cards that the Recycler View will use to work with
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>
implements Serializable {

    private final List<Task> taskList;

    /**
     * Display system that is used
     */
    public class TaskViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Serializable{
        // each data item is an Event object
        final TextView taskName;
        final TextView locationName;
        final TextView startTime;
        final TextView endTime;

        /**
         * create a card that has the view items selected
         * @param itemView the layout being used
         */
        TaskViewHolder(View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.task_name_card);
            locationName = itemView.findViewById(R.id.location_name_card);
            startTime = itemView.findViewById(R.id.start_time_card);
            endTime = itemView.findViewById(R.id.end_time_card);

            itemView.setOnClickListener(this);
        }

        /**
         * When a card is clicked, you are able to view the details of the event
         * @param view card being selected
         */
        @Override
        public void onClick(View view){
            int position = getAdapterPosition();
            Log.i("TaskAdapter", "Item clicked");

            if(position != RecyclerView.NO_POSITION) {
                Task task = taskList.get(position);
                Log.d("TaskAdapter",task.getStartDate().toString());
                Intent intent = new Intent(view.getContext(), ViewTask.class);
                intent.putExtra(view.getContext().getString(R.string.EXTRA_TASK), (Parcelable) task);
                intent.putExtra(view.getContext().getString(R.string.EXTRA_TASK_INFO), position);
                view.getContext().startActivity(intent);
            }
        }
    }

    /**
     * fill the TaskAdapter with the provided list
     * @param taskList the tasks of a specific day
     */
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
        startTimeView.setText(task.getStartTime());
        endTimeView.setText(task.getEndTime());
    }

    @Override
    public int getItemCount() {
        if(taskList == null){
            return 0;
        }

        return taskList.size();
    }
}
