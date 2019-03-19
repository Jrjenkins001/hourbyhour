package com.example.hour_by_hour;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

public class AddNewToDoDialogFragment extends DialogFragment {

    public interface AddNewToDoDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String name);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    AddNewToDoDialogListener listener;
    EditText nameText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.to_do_list_dialog, null, false));
        builder.setMessage(R.string.create_to_do_dialog);
        builder.setPositiveButton(R.string.to_do_dialog_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(AddNewToDoDialogFragment.this, "Thing");
                    }
                })
                .setNegativeButton(R.string.to_do_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(AddNewToDoDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddNewToDoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    "must implement AddNewToDoDialogListener");
        }
    }
}
