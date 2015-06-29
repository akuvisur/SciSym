package com.akuvisuri.scisym.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.akuvisuri.scisym.LaunchScreen;
import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.view.adapters.FactorListAdapter;

import java.util.ArrayList;

/**
 * Created by Aku on 29.6.2015.
 */
public class FactorSelector {
    private static final String LOG = "FactorSelector.java";

    public static Dialog getInstance(Activity a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.factor_selector, null);
        ListView l = (ListView) dialogLayout.findViewById(R.id.add_factor_list);
        final ArrayList<String> factors = new ArrayList<String>();
        for (String key : Factors.list.keySet()) {
            factors.add(key);
        }
        final FactorListAdapter adapter = new FactorListAdapter(MainUtils.c, factors);
        l.setAdapter(adapter);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainUtils.selectedFactors = adapter.getSelection();
                        LaunchScreen.refreshFactorList();
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
