package com.akuvisuri.scisym.view.factorcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.akuvisuri.scisym.R;

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

    public static void show(Activity a, final String type) {

        editViews = new ArrayList<>();
        radioGroups = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();

        final View dialogLayout = inflater.inflate(R.layout.factor_creator, null);

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
            }
            okButton.setEnabled(textOk & radioOk);
        }
    }
}
