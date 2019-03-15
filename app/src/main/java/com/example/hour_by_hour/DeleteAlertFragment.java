package com.example.hour_by_hour;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteAlertFragment extends DialogFragment {

    public DeleteAlertFragment() {}

    public static DeleteAlertFragment newInstance() {
        return new DeleteAlertFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Do you want to delete this task?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence message = "Event Deleted";
                Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }
}
