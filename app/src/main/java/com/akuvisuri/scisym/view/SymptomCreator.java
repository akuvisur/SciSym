package com.akuvisuri.scisym.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.akuvisuri.scisym.LaunchScreen;
import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.trackables.Symptom;

import java.util.ArrayList;

/**
 * Created by Aku on 29.6.2015.
 */
public class SymptomCreator {
    private static final String LOG = "SymptomCreator.java";

    private static boolean textOk = false;
    private static boolean radioOk = false;

    private static InputVerifier iv = new InputVerifier();
    private static RadioVerifier rv = new RadioVerifier();

    private static ArrayList<EditText> editViews;
    private static ArrayList<RadioGroup> radioGroups;

    private static Dialog _this;
    private static Button okButton;

    public static void show(Activity a, final String type) {

        editViews = new ArrayList<>();
        radioGroups = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        final View dialogLayout = inflater.inflate(R.layout.symptom_creator, null);
        editViews.add((EditText) dialogLayout.findViewById(R.id.symptom_creator_label_value));
        editViews.add((EditText) dialogLayout.findViewById(R.id.symptom_creator_desc_value));
        editViews.add((EditText) dialogLayout.findViewById(R.id.symptom_creator_class_value));

        ((EditText) dialogLayout.findViewById(R.id.symptom_creator_desc_value)).addTextChangedListener(iv);
        ((EditText) dialogLayout.findViewById(R.id.symptom_creator_label_value)).addTextChangedListener(iv);
        ((EditText) dialogLayout.findViewById(R.id.symptom_creator_class_value)).addTextChangedListener(iv);

        radioGroups.add((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_rep_window));
        radioGroups.add((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_input));
        radioGroups.add((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_severity));

        ((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_severity)).setOnCheckedChangeListener(rv);
        ((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_input)).setOnCheckedChangeListener(rv);
        ((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_rep_window)).setOnCheckedChangeListener(rv);

        if (type.equals("single")) {
            dialogLayout.findViewById(R.id.symptom_creator_rep_window).setVisibility(View.GONE);
            dialogLayout.findViewById(R.id.symptom_creator_window_text).setVisibility(View.GONE);
        }

        okButton = (Button) dialogLayout.findViewById(R.id.symptom_creator_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer selectedSeverity;
                String selectedInput;
                String selectedWindow;
                int selectedId = ((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_severity)).getCheckedRadioButtonId();
                if (((RadioButton) dialogLayout.findViewById(selectedId)).getText().equals("Severe")) {
                    selectedSeverity = 1;
                }
                else selectedSeverity = 0;

                selectedId = ((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_input)).getCheckedRadioButtonId();
                selectedInput = (((RadioButton) dialogLayout.findViewById(selectedId)).getText().toString().toLowerCase());

                selectedId = ((RadioGroup) dialogLayout.findViewById(R.id.symptom_creator_rep_window)).getCheckedRadioButtonId();
                if (type.equals("single")) {
                    selectedWindow = "single";
                }
                else
                switch (((RadioButton) dialogLayout.findViewById(selectedId)).getText().toString()) {
                    case "1 / Day":
                        selectedWindow = "daily:1";
                        break;
                    case "2 / Day":
                        selectedWindow = "daily:2";
                        break;
                    case "3 / Day":
                        selectedWindow = "daily:3";
                        break;
                    case "4 / Day":
                        selectedWindow = "daily:4";
                        break;
                    case "Weekly":
                        selectedWindow = "week";
                        break;
                    case "Monthly":
                        selectedWindow = "month";
                        break;
                    default:
                        selectedWindow = "error";
                }

                Symptom newS = new Symptom(
                        ((EditText)dialogLayout.findViewById(R.id.symptom_creator_label_value)).getText().toString(),
                        ((EditText)dialogLayout.findViewById(R.id.symptom_creator_desc_value)).getText().toString(),
                        ((EditText)dialogLayout.findViewById(R.id.symptom_creator_class_value)).getText().toString(),
                        selectedSeverity,
                        type,
                        selectedInput,
                        selectedWindow
                );
                Symptoms.put(
                        ((EditText)dialogLayout.findViewById(R.id.symptom_creator_label_value)).getText().toString(),
                        newS
                );
                MainUtils.selectedSymptoms.add(newS);
                LaunchScreen.refreshSymptomList();
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

        builder.setView(dialogLayout);
        _this = builder.create();
        _this.show();

    }

    private static class InputVerifier implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textOk = true;
            for (EditText e : editViews) {
                if (e.getText().length() > 2) {
                    e.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ok,0);
                }
                else {
                    e.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.warning,0);
                    textOk = false;
                }
            }
            okButton.setEnabled(textOk & radioOk);
        }

        @Override public void afterTextChanged(Editable s) {}
    }

    private static class RadioVerifier implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            radioOk = true;
            for (RadioGroup r : radioGroups) {
                if (r.getCheckedRadioButtonId() == -1) {
                    radioOk = false;
                }
            }
            okButton.setEnabled(textOk & radioOk);
        }
    }
}
