package com.lickling.mymusic.ui.setting.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.lickling.mymusic.R;

public class EditDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.setting_dialog_layout, null);

        // Set the custom layout to the dialog builder
        builder.setView(dialogView);

        // Set the dialog title
        builder.setTitle("My Custom Dialog");

        // Set the positive button (OK)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when OK button is clicked
            }
        });

        // Set the negative button (Cancel)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when Cancel button is clicked
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}