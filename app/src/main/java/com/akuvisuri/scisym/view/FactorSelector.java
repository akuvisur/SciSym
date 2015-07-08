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

import com.akuvisuri.scisym.LaunchScreen;
import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.view.adapters.FactorListAdapter;
import com.akuvisuri.scisym.view.factorcreator.FactorCreator;
import com.akuvisuri.scisym.view.watchers.FactorSearchWatcher;

import java.util.ArrayList;

/**
 * Created by Aku on 29.6.2015.
 */
public class FactorSelector {
    private static final String LOG = "FactorSelector.java";

    private static ListView factorList;
    private static EditText searchFld;

    public static Dialog getInstance(final Activity a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.factor_selector, null);
        factorList = (ListView) dialogLayout.findViewById(R.id.add_factor_list);
        final ArrayList<FactorListAdapter.FactorListItem> factors = new ArrayList<FactorListAdapter.FactorListItem>();
        for (String key : Factors.list.keySet()) {
            factors.add(new FactorListAdapter.FactorListItem(true, key));
        }
        final FactorListAdapter adapter = new FactorListAdapter(MainUtils.c, factors);
        factorList.setAdapter(adapter);

        searchFld = (EditText) dialogLayout.findViewById(R.id.add_factor_search);
        searchFld.addTextChangedListener(new FactorSearchWatcher(factorList));

        Button addNew = (Button) dialogLayout.findViewById(R.id.add_factor_create);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FactorCreator.show(a, LaunchScreen.schemaType);
            }
        });

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
