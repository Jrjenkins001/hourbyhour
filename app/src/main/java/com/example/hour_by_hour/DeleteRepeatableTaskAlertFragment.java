package com.example.hour_by_hour;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DeleteRepeatableTaskAlertFragment extends DialogFragment {
    boolean isRepeating;

    /**
     * Used to implement buttons
     */
    public interface DeleteTaskListener{
        void deleteAllRepeatableTasks();
        void deleteSingleTask();
        void cancel(DialogFragment dialogFragment);
    }

    static public DialogFragment newInstance(boolean isRepeating){
        DeleteRepeatableTaskAlertFragment df = new DeleteRepeatableTaskAlertFragment();
        df.isRepeating = isRepeating;
        return df;
    }

    private DeleteTaskListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteTaskListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String deleteSingleItem = "Delete";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        if(isRepeating) {
            alertDialogBuilder.setMessage("Do you want to just this task or all repeating tasks too?");
            alertDialogBuilder.setPositiveButton("Delete all items", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.deleteAllRepeatableTasks();
                }
            });
            deleteSingleItem = "Delete only this item";
        } else {
            alertDialogBuilder.setMessage("Are you sure you want to delete this task?");
        }

        alertDialogBuilder.setNeutralButton(deleteSingleItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.deleteSingleTask();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.cancel(DeleteRepeatableTaskAlertFragment.this);
            }
        });

        return alertDialogBuilder.create();
    }
}
