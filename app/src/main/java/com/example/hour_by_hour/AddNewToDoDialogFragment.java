package com.example.hour_by_hour;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class AddNewToDoDialogFragment extends DialogFragment {

    /**
     * Used to implement and pass data used to create the responding
     * item
     */
    public interface AddNewToDoDialogListener {
        void onDialogPositiveClick(String name);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private AddNewToDoDialogListener listener;
    private Context context;

    public void setUsedContext(Context context){
        this.context = context;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View v = View.inflate(context, R.layout.to_do_list_dialog, null);
        builder.setView(v);
        final EditText et = v.findViewById(R.id.input_name);

        builder.setPositiveButton(R.string.to_do_dialog_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(et.getText().toString());
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
