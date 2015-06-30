package com.akuvisuri.scisym.view.setup;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.view.LaunchScreen;

import java.util.ArrayList;

/**
 * Created by Aku on 30.6.2015.
 */
public class SetupPage1 {
    private final String LOG = "SetupPage1.java";

    private LinearLayout v;

    private boolean textOk;
    private InputVerifier iv;
    private ArrayList<EditText> editViews;

    private boolean radioOk;
    private RadioVerifier rv;
    private RadioGroup type;

    private Button okButton;

    public SetupPage1(final Activity a, View _view) {
        editViews = new ArrayList<>();

        iv = new InputVerifier();
        rv = new RadioVerifier();
        v = (LinearLayout) _view;

        okButton = (Button) v.findViewById(R.id.contbutton);

        editViews.add((EditText) v.findViewById(R.id.schema_title));
        editViews.add((EditText) v.findViewById(R.id.schema_desc));
        editViews.add((EditText) v.findViewById(R.id.schema_author));
        editViews.add((EditText) v.findViewById(R.id.schema_id));

        ((EditText) v.findViewById(R.id.schema_title)).addTextChangedListener(iv);
        ((EditText) v.findViewById(R.id.schema_desc)).addTextChangedListener(iv);
        ((EditText) v.findViewById(R.id.schema_author)).addTextChangedListener(iv);
        ((EditText) v.findViewById(R.id.schema_id)).addTextChangedListener(iv);

        type = (RadioGroup) v.findViewById(R.id.schema_type);
        type.setOnCheckedChangeListener(rv);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LaunchScreen) a).setupPage2(v);
            }
        });

    }

    public View getInstance() {
        return v;
    }

    private class InputVerifier implements TextWatcher {
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

    private class RadioVerifier implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            radioOk = true;
            okButton.setEnabled(textOk & radioOk);
        }
    }
}
