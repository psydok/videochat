package ru.csu.videochat.model.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.csu.videochat.R;

public class DialogCreateLink extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }
    private DialogListener mListener;
    public static DialogCreateLink newInstance(){
        DialogCreateLink dialogCreateTask = new DialogCreateLink();
        return dialogCreateTask;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        builder.setCancelable(false)
                .setView(inflater.inflate(R.layout.dialog_create_link, null))
                .setPositiveButton(R.string.done, (dialog, id) -> getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, getActivity().getIntent()))
                .setNegativeButton(R.string.cancel, (dialog, id) -> getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_CANCELED, getActivity().getIntent()));
        return builder.create();
    }
}
