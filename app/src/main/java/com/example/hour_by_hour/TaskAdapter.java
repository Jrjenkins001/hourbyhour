package com.example.hour_by_hour;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private String [] mDataset;

    // TODO reference the JSON file with the information

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        // each data item is an Event object
        TextView textView;
        TaskViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public TaskAdapter(String [] myDataset){
        mDataset = myDataset;
        // TODO set file to be read from
    }

    @Override
    @NonNull
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view, parent, false);

        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // TODO get element from data set and this position
        // TODO replace the contents of view with that element
        holder.textView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return 1;

        // for some reason gives me an error saying "expects method call"
        //return mDataset.length();
    }
}
