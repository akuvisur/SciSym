package com.akuvisuri.scisym.view.factorcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.Launch;
import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.trackables.Factor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aku on 29.6.2015.
 */
public class FactorCreator {

    private static final String LOG = "FactorCreator.java";

    private static boolean textOk = false;
    private static boolean radioOk = false;

    private static InputVerifier iv = new InputVerifier();
    private static RadioVerifier rv = new RadioVerifier();

    private static ArrayList<EditText> editViews;
    private static ArrayList<RadioGroup> radioGroups;

    private static Dialog _this;
    private static Button okButton;

    private static View dialogLayout;

    private static LinearLayout valueLayout;
    public static JSONArray _values;
    public static JSONObject range;
    public static Object valueObj;

    private static LayoutInflater inflater;

    public static void show(Activity a, final String type) {

        editViews = new ArrayList<>();
        radioGroups = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        inflater = a.getLayoutInflater();

        dialogLayout = inflater.inflate(R.layout.factor_creator, null);

        editViews.add((EditText) dialogLayout.findViewById(R.id.factor_creator_label_value));
        editViews.add((EditText) dialogLayout.findViewById(R.id.factor_creator_desc_value));

        ((EditText) dialogLayout.findViewById(R.id.factor_creator_desc_value)).addTextChangedListener(iv);
        ((EditText) dialogLayout.findViewById(R.id.factor_creator_label_value)).addTextChangedListener(iv);

        radioGroups.add((RadioGroup) dialogLayout.findViewById(R.id.factor_creator_window_value));
        radioGroups.add((RadioGroup) dialogLayout.findViewById(R.id.factor_creator_type_value));

        ((RadioGroup) dialogLayout.findViewById(R.id.factor_creator_type_value)).setOnCheckedChangeListener(rv);
        ((RadioGroup) dialogLayout.findViewById(R.id.factor_creator_window_value)).setOnCheckedChangeListener(rv);

        if (type.equals("single")) {dialogLayout.findViewById(R.id.factor_creator_window_layout).setVisibility(View.GONE);}

        okButton = (Button) dialogLayout.findViewById(R.id.factor_creator_okbutton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedInput;
                String selectedWindow;
                int selectedId;

                selectedId = ((RadioGroup) dialogLayout.findViewById(R.id.factor_creator_type_value)).getCheckedRadioButtonId();
                selectedInput = (((RadioButton) dialogLayout.findViewById(selectedId)).getText().toString().toLowerCase());

                selectedId = ((RadioGroup) dialogLayout.findViewById(R.id.factor_creator_window_value)).getCheckedRadioButtonId();
                if (type.equals("single")) {
                    selectedWindow = "single";
                } else
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

                switch (selectedInput) {
                    case ("Boolean"):
                        break;
                    case ("Multiple"):
                        _values = new JSONArray();
                        for (String s : ((TextView) valueLayout.findViewById(R.id.factor_creator_value_text)).getText().toString().split(";")) {
                            try {
                                JSONObject row = new JSONObject();
                                row.put("value", s);
                                row.put("icon", null);
                                _values.put(row);
                            }
                            catch (JSONException e) {
                                // oops
                            }
                        }
                        valueObj = _values;
                        break;

                    case ("Tracked"):
                        try {
                            range = new JSONObject();
                            range.put("range",
                                ((TextView) valueLayout.findViewById(R.id.factor_creator_range_min)).getText().toString() + ":" +
                                ((TextView) valueLayout.findViewById(R.id.factor_creator_range_max)).getText().toString()
                            );
                            range.put("unit", ((TextView) valueLayout.findViewById(R.id.factor_creator_unit)).getText().toString());
                        }
                        catch (JSONException e) {
                            // oops
                        }
                        valueObj = range;
                        break;
                }


                Factor newF = new Factor(
                        ((EditText) dialogLayout.findViewById(R.id.factor_creator_label_value)).getText().toString(),
                        ((EditText) dialogLayout.findViewById(R.id.factor_creator_desc_value)).getText().toString(),
                        selectedInput,
                        selectedWindow,
                        valueObj
                );
                Factors.put(
                        ((EditText) dialogLayout.findViewById(R.id.factor_creator_label_value)).getText().toString(),
                        newF
                );
                MainUtils.selectedFactors.add(newF);
                Launch.refreshFactorList();
                _this.dismiss();
            }
        });

        Button cb = (Button) dialogLayout.findViewById(R.id.factor_creator_cancelbutton);
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
                    e.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ok,0);
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
                else if (r.getCheckedRadioButtonId() == R.id.type_boolean) {
                    ((LinearLayout) dialogLayout.findViewById(R.id.factor_creator_type_container)).removeAllViews();
                }
                else if (r.getCheckedRadioButtonId() == R.id.type_multiple) {
                    ((LinearLayout) dialogLayout.findViewById(R.id.factor_creator_type_container)).removeAllViews();
                    valueLayout = (LinearLayout) inflater.inflate(R.layout.factor_value_creator, null);
                    valueLayout.setBackgroundColor(Color.argb(50,153,204,255));
                    ((LinearLayout) dialogLayout.findViewById(R.id.factor_creator_type_container)).addView(valueLayout);
                    dialogLayout.invalidate();
                }
                else if (r.getCheckedRadioButtonId() == R.id.type_tracked) {
                    ((LinearLayout) dialogLayout.findViewById(R.id.factor_creator_type_container)).removeAllViews();
                    valueLayout = (LinearLayout) inflater.inflate(R.layout.factor_range_creator, null);
                    valueLayout.setBackgroundColor(Color.argb(50,153,204,255));
                    ((LinearLayout) dialogLayout.findViewById(R.id.factor_creator_type_container)).addView(valueLayout);
                    dialogLayout.invalidate();
                }
            }
            okButton.setEnabled(textOk & radioOk);
        }
    }
}
