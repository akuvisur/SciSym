package com.akuvisuri.scisym.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.trackables.Symptom;
import com.akuvisuri.scisym.view.adapters.SymptomListAdapter;

import java.util.ArrayList;

/**
 * Created by Aku on 23.6.2015.
 */
public class SymptomSelector {
    private static final String LOG = "SymptomSelector.java";

    public static Dialog getInstance(Activity a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.symptom_selector, null);
        ListView l = (ListView) dialogLayout.findViewById(R.id.add_symptom_list);
        final ArrayList<String> symptoms = new ArrayList<String>();
        for (String key : Symptoms.list.keySet()) {
            symptoms.add(key);
        }
        final SymptomListAdapter adapter = new SymptomListAdapter(MainUtils.c, symptoms);
        l.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        l.setAdapter(adapter);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainUtils.selectedSymptoms = adapter.getSelection();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // do nada
                    }
                });
        return builder.create();
    }

}
