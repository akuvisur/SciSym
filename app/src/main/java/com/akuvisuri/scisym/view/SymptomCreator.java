package com.akuvisuri.scisym.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.trackables.Symptom;

/**
 * Created by Aku on 29.6.2015.
 */
public class SymptomCreator {

    private static Symptom s;
    private static Dialog _this;

    public static void show(Activity a) {
        s = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.symptom_creator, null);

        Button okb = (Button) dialogLayout.findViewById(R.id.symptom_creator_ok);
        okb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _this.dismiss();
            }
        });

        Button cb = (Button) dialogLayout.findViewById(R.id.symptom_creator_cancel);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _this.dismiss();
            }
        });

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogLayout);
        _this = builder.create();
        _this.show();

    }

}
