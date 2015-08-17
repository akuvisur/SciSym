package com.akuvisuri.scisym;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.controller.Schema;
import com.akuvisuri.scisym.controller.SchemaBuilder;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;
import com.akuvisuri.scisym.view.FactorSelector;
import com.akuvisuri.scisym.view.SymptomSelector;
import com.akuvisuri.scisym.view.adapters.TrackingListAdapter;
import com.akuvisuri.scisym.view.tracking.SymptomNumber;
import com.akuvisuri.scisym.view.tracking.SymptomScale;
import com.akuvisuri.scisym.view.tracking.SymptomText;
import com.akuvisuri.scisym.view.tracking.Tracking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.AdapterView.*;

public class Launch extends Activity {
    protected final static String LOG = "Launch.java";

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // IMPORTANT
        MainUtils.setContext(getApplicationContext());
        MainUtils.setActivity(this);

        if (MainUtils.DEBUG) {
            //Symptoms.clear();
            //Factors.clear();
        }
        SchemaBuilder.init();
        Symptoms.init(this);
        Factors.init(this);

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
                if (SchemaBuilder.schema == null) {
                    setContentView(R.layout.no_schema);
                    currentPage = 0;
                }
                else {
                    setContentView(R.layout.tracking);
                    trackingView();
                }

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

        if (!(SchemaBuilder.schema == null)) {
            tracking_mode = item.getTitle().toString().toLowerCase();
            trackingView();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please perform setup first", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // TRACKING VIEW
    public static Calendar viewDate = Calendar.getInstance();

    public static ImageButton calendarButton;
    public static Date selectedDate;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static TextView dateView;
    public static TextView label;

    public static RelativeLayout tracking_header;
    ArrayList<Tracking> listItems;
    ListView tracking_content;
    TrackingListAdapter trackingAdapter;

    public static String tracking_mode = "symptoms";

    public void trackingView() {
        listItems = new ArrayList<Tracking>();

        View v = inflate(getApplicationContext(), R.layout.tracking, null);

        setContentView(v);

        // CALENDAR BUTTON AND DATE SELECTION BEHAVIOUR
        selectedDate = new Date();
        viewDate.setTime(selectedDate);
        dateView = (TextView) v.findViewById(R.id.tracking_header_date);
        dateView.setText(dateFormat.format(selectedDate));
        calendarButton = (ImageButton) v.findViewById(R.id.tracking_calendarButton);
        calendarButton.setImageDrawable(ContextCompat.getDrawable(MainUtils.c, R.drawable.calendar));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                viewDate.set(Calendar.YEAR, year);
                viewDate.set(Calendar.MONTH, monthOfYear);
                viewDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }};

        calendarButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {new DatePickerDialog(Launch.this, date, viewDate.get(Calendar.YEAR), viewDate.get(Calendar.MONTH),
                        viewDate.get(Calendar.DAY_OF_MONTH)).show();
            }});

        // SET GENERIC LABEL AND COLOR OPTIONS BASED ON VIEW TYPE
        tracking_header = (RelativeLayout) v.findViewById(R.id.tracking_header);
        label = (TextView) v.findViewById(R.id.tracking_header_label);

        switch (tracking_mode) {
            case "symptoms":
                label.setText(tracking_mode.toUpperCase());
                tracking_header.setBackgroundColor(getResources().getColor(R.color.scisym_blue));
                break;
            case "factors":
                label.setText(tracking_mode.toUpperCase());
                tracking_header.setBackgroundColor(getResources().getColor(R.color.scisym_green));
                break;
            case "settings":
                calendarButton.setVisibility(INVISIBLE);
                dateView.setVisibility(INVISIBLE);
                tracking_header.setBackgroundColor(getResources().getColor(R.color.scisym_grey));
                label.setText(tracking_mode.toUpperCase());
                break;
        }

        // SET UI FOR TRACKING

        if (tracking_mode.equals("symptoms")) {
            // each element can only be inflated once
            View row;
            Schema.init();
            for (Symptom s : Schema.symptoms) {

                if (s.input.equals("scale")) {
                    listItems.add(new SymptomScale(s));
                    /*
                    row = View.inflate(getApplicationContext(), R.layout.tracking_symptom_scale, null);
                    row.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT));

                    TextView label = (TextView) row.findViewById(R.id.label);
                    label.setText(s.label);
                    tracking_content.addView(row);*/
                }
                else if (s.input.equals("text")) {
                    listItems.add(new SymptomText(s));
                    /*row = View.inflate(getApplicationContext(), R.layout.tracking_symptom_text, null);
                    row.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT));

                    TextView label = (TextView) row.findViewById(R.id.label);
                    label.setText(s.label);
                    tracking_content.addView(row);*/
                }
                else if (s.input.equals("numeric")) {
                    listItems.add(new SymptomNumber(s));
                    /*row = View.inflate(getApplicationContext(), R.layout.tracking_symptom_numeric, null);
                    row.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT));

                    TextView label = (TextView) row.findViewById(R.id.label);
                    label.setText(s.label);
                    tracking_content.addView(row);*/
                }

            }
        }

        else if (tracking_mode.equals("factors")) {
            View trackedRow = inflate(getApplicationContext(), R.layout.tracking_factor_tracked, null);
            View multipleRow = inflate(getApplicationContext(), R.layout.tracking_factor_multiple, null);

            Schema.init();
            for (Factor f : Schema.factors) {
                if (f.type.equals("multiple")) {
                    /*TextView label = (TextView) multipleRow.findViewById(R.id.label);
                    label.setText(f.label);
                    tracking_content.addView(multipleRow);
                    */
                }
                else if (f.type.equals("tracked")) {
                    /*TextView label = (TextView) trackedRow.findViewById(R.id.label);
                    label.setText(f.label);
                    tracking_content.addView(trackedRow);*/
                }
            }
        }
        // SET UI FOR SETTINGS
        else if (tracking_mode.equals("settings")) {
            // lets just do this like this because i dunno
            setContentView(R.layout.settings);
        }

        trackingAdapter = new TrackingListAdapter(this, listItems);
        tracking_content = (ListView) v.findViewById(R.id.tracking_content);
        trackingAdapter.setParent(tracking_content);
        tracking_content.setAdapter(trackingAdapter);
        tracking_content.setBackgroundColor(getResources().getColor(R.color.scisym_blue_light));
        tracking_content.setDivider(new ColorDrawable(getResources().getColor(R.color.white)));
        tracking_content.setDividerHeight(20);

    }

    private void updateDate() {
        selectedDate = new Date(viewDate.getTimeInMillis());
        dateView.setText(dateFormat.format(selectedDate));
        dateView.invalidate();
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
                schemaTitle = ((EditText) findViewById(R.id.schema_title)).getText().toString();
                dbName = ((EditText) findViewById(R.id.schema_id)).getText().toString();
                schemaDesc = ((EditText) findViewById(R.id.schema_desc)).getText().toString();
                schemaAuthor = ((EditText) findViewById(R.id.schema_author)).getText().toString();
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
        SchemaBuilder.save();
        trackingView();
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
