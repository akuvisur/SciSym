package com.akuvisuri.scisym.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.controller.SchemaBuilder;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;

import java.util.ArrayList;

import static android.widget.AdapterView.*;


public class LaunchScreen extends Activity {
    protected final static String LOG = "LaunchScreen.java";

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // IMPORTANT
        MainUtils.setContext(getApplicationContext());
        MainUtils.setActivity(this);

        if (MainUtils.DEBUG) {
            Symptoms.clear();
        }
        SchemaBuilder.init();
        Symptoms.init(this);
        Factors.init(this);
        if (SchemaBuilder.schemaOptions == null) {
            setContentView(R.layout.no_schema);
            currentPage = 0;
        }
        else setContentView(R.layout.activity_mainview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainUtils.setContext(getApplicationContext());
        MainUtils.setActivity(this);
        switch (currentPage) {
            case 1:
                setupPage1(null);
                break;
            case 2:
                setupPage2(null);
                break;
            case 3:
                setupPage3(null);
                break;
            default:
                if (SchemaBuilder.schemaOptions == null) {
                    setContentView(R.layout.no_schema);
                    currentPage = 0;
                }
                else setContentView(R.layout.activity_mainview);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("OPTIONS", item.getTitle().toString());

        return super.onOptionsItemSelected(item);
    }

    // NEW SCHEMA CREATOR
    public static String schemaType = "repeating";
    public static String schemaTitle;
    public static String dbName;
    public static String schemaDesc;
    public static String schemaAuthor;

    private static LinearLayout symptomList;
    private static LinearLayout factorList;

    private static boolean textOk;
    private static InputVerifier iv;
    private static ArrayList<EditText> editViews;

    private static boolean radioOk;
    private static RadioVerifier rv;
    private static RadioGroup type;

    private static Button okButton;

    public void setupPage1(View view) {
        setContentView(R.layout.schema_options1);

        editViews = new ArrayList<>();

        iv = new InputVerifier();
        rv = new RadioVerifier();

        okButton = (Button) findViewById(R.id.contbutton);

        editViews.add((EditText) findViewById(R.id.schema_title));
        editViews.add((EditText) findViewById(R.id.schema_desc));
        editViews.add((EditText) findViewById(R.id.schema_author));
        editViews.add((EditText) findViewById(R.id.schema_id));

        ((EditText) findViewById(R.id.schema_title)).addTextChangedListener(iv);
        ((EditText) findViewById(R.id.schema_desc)).addTextChangedListener(iv);
        ((EditText) findViewById(R.id.schema_author)).addTextChangedListener(iv);
        ((EditText) findViewById(R.id.schema_id)).addTextChangedListener(iv);

        type = (RadioGroup) findViewById(R.id.schema_type);
        type.setOnCheckedChangeListener(rv);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupPage2(v);
            }
        });

        currentPage = 1;
    }

    public void setupPage2(View view) {
        setContentView(R.layout.schema_options2);

        symptomList = (LinearLayout) findViewById(R.id.schema_options2_list);

        ProgressBar pb = (ProgressBar) findViewById(R.id.setupProgress);
        pb.setProgress(33);

        View addRow = findViewById(R.id.new_symptom);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSymptoms();
            }
        });

        currentPage = 2;
    }

    public void setupPage3(View view) {
        setContentView(R.layout.schema_options3);

        factorList = (LinearLayout) findViewById(R.id.schema_options3_list);

        ProgressBar pb = (ProgressBar) findViewById(R.id.setupProgress);
        pb.setProgress(66);

        View addRow = findViewById(R.id.new_factor);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFactors();
            }
        });

        currentPage = 3;
    }

    public static void refreshSymptomList() {
        symptomList.removeAllViews();
        for (final Symptom s : MainUtils.selectedSymptoms) {
            Log.d(LOG, s.toString());
            LayoutInflater inflater = (LayoutInflater) MainUtils.c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View symptomInfo = inflater.inflate(R.layout.editor_symptom, null);
            TextView label = (TextView) symptomInfo.findViewById(R.id.symptom_label);
            TextView attr = (TextView) symptomInfo.findViewById(R.id.symptom_attr);
            label.setText(s.toString());
            attr.setText(s.attrToString());
            ImageView severeImage = (ImageView) symptomInfo.findViewById(R.id.symptom_severe);
            final ImageSwitcher rightImage = (ImageSwitcher) symptomInfo.findViewById(R.id.symptom_right_image);
            rightImage.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView myView = new ImageView(MainUtils.c);
                    myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    myView.setLayoutParams(new ImageSwitcher.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    return myView;
                }
            });
            rightImage.setImageResource(R.drawable.delete);
            rightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG, "clicked delete symptom");
                    symptomList.removeView(symptomInfo);
                    MainUtils.selectedSymptoms.remove(s);
                }
            });
            if (s.isSevere()) {
                severeImage.setImageResource(R.drawable.critical);
            }
            symptomList.addView(symptomInfo);
        }
        symptomList.invalidate();
    }

    public static void refreshFactorList() {
        factorList.removeAllViews();
        for (final Factor f : MainUtils.selectedFactors) {
            Log.d(LOG, f.toString());
            LayoutInflater inflater = (LayoutInflater) MainUtils.c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View factorInfo = inflater.inflate(R.layout.editor_factor, null);
            TextView label = (TextView) factorInfo.findViewById(R.id.factor_label);
            TextView attr = (TextView) factorInfo.findViewById(R.id.factor_attr);
            label.setText(f.toString());
            attr.setText(f.attrToString());
            final ImageSwitcher rightImage = (ImageSwitcher) factorInfo.findViewById(R.id.factor_right_image);
            rightImage.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView myView = new ImageView(MainUtils.c);
                    myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    myView.setLayoutParams(new ImageSwitcher.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    return myView;
                }
            });
            rightImage.setImageResource(R.drawable.delete);
            rightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG, "clicked delete factor");
                    factorList.removeView(factorInfo);
                    MainUtils.selectedFactors.remove(f);
                }
            });

            factorList.addView(factorInfo);
        }
        factorList.invalidate();
    }

    public void setupDone(View view) {
        // do stuff
        setContentView(R.layout.activity_mainview);
    }

    public void importSchema(View view) {

    }

    public void useExisting(View view) {

    }

    public void addSymptoms() {
        Dialog addDialog = SymptomSelector.getInstance(this);
        addDialog.show();
    }

    public void addFactors() {
        Dialog addDialog = FactorSelector.getInstance(this);
        addDialog.show();
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
