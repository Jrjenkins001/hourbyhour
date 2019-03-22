package com.example.hour_by_hour;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;


/**
 * Creates the cards that the Recycler View will use to work with
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>
        implements Serializable {

    private final List<ToDo> toDoList;

    /**
     * Display system that is used
     */
    public class ToDoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Serializable{

        final TextView toDoName;
        final CheckBox complete;

        /**
         * create a card that has the view items selected
         * @param itemView the layout being used
         */
        ToDoViewHolder(View itemView) {
            super(itemView);

            toDoName = itemView.findViewById(R.id.to_do_name);
            complete = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(this);

            complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    complete.setChecked(isChecked);
                }
            });
        }

        /**
         * When a card is clicked, you are able to view the details of the event
         * @param view card being selected
         */
        @Override
        public void onClick(View view){
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION) {
                ToDo task = toDoList.get(position);
                task.set_completed(!task.get_completed());

                CheckBox cb = view.findViewById(R.id.checkBox);
                cb.setChecked(task.get_completed());
            }
        }

    }

    /**
     * fill the TaskAdapter with the provided list
     * @param toDoList the tasks of a specific day
     */
    ToDoAdapter(List<ToDo> toDoList){
        this.toDoList = toDoList;
    }

    @Override
    @NonNull
    public ToDoAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View toDoView = inflater.inflate(R.layout.to_do_view_card, parent, false);

        return new ToDoViewHolder(toDoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo toDo = toDoList.get(position);

        TextView name = holder.toDoName;
        CheckBox complete = holder.complete;

        name.setText(toDo.get_name());
        complete.setChecked(toDo.get_completed());
    }

    @Override
    public int getItemCount() {
        if(toDoList == null){
            return 0;
        }

        return toDoList.size();
    }
}
