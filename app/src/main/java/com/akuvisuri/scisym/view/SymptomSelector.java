package com.akuvisuri.scisym.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.view.adapters.SymptomListAdapter;
import com.akuvisuri.scisym.view.watchers.SymptomSearchWatcher;

import java.util.ArrayList;

/**
 * Created by Aku on 23.6.2015.
 */
public class SymptomSelector {
    private static final String LOG = "SymptomSelector.java";

    private static ListView symptomList;
    private static EditText searchFld;

    public static Dialog getInstance(final Activity a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.symptom_selector, null);

        symptomList = (ListView) dialogLayout.findViewById(R.id.add_symptom_list);
        final ArrayList<SymptomListAdapter.SymptomListItem> symptoms = new ArrayList<SymptomListAdapter.SymptomListItem>();
        for (String key : Symptoms.list.keySet()) {
            symptoms.add(new SymptomListAdapter.SymptomListItem(true, key));
        }
        final SymptomListAdapter adapter = new SymptomListAdapter(MainUtils.c, symptoms);
        symptomList.setAdapter(adapter);

        searchFld = (EditText) dialogLayout.findViewById(R.id.add_symptom_search);
        searchFld.addTextChangedListener(new SymptomSearchWatcher(symptomList));

        Button addNew = (Button) dialogLayout.findViewById(R.id.add_symptom_create);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SymptomCreator.show(a, LaunchScreen.schemaType);
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainUtils.selectedSymptoms = adapter.getSelection();
                        LaunchScreen.refreshSymptomList();
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
