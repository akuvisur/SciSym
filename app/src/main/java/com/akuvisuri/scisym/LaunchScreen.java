package com.akuvisuri.scisym;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;


public class LaunchScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainUtils.setContext(getApplicationContext());
        SchemaBuilder.init();
        if (SchemaBuilder.schemaOptions == null) {
            setContentView(R.layout.no_schema);
        }
        else setContentView(R.layout.activity_mainview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainUtils.setContext(getApplicationContext());
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

    public void setupMenu(View view) {
        setContentView(R.layout.schema_options1);
    }

    public void setupPage2(View view) {
        setContentView(R.layout.schema_options2);
        LinearLayout root = (LinearLayout) findViewById(R.id.schema_options2);

        final LinearLayout symptomList = (LinearLayout) findViewById(R.id.schema_options2_list);

        ProgressBar pb = (ProgressBar) findViewById(R.id.setupProgress);
        pb.setProgress(33);

        View addRow = findViewById(R.id.new_symptom);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UI", "sym click");
                View symptomInfo = getLayoutInflater().inflate(R.layout.editor_symptom, null);
                symptomList.addView(symptomInfo);
            }
        });
    }

    public void setupPage3(View view) {
        setContentView(R.layout.schema_options3);
        LinearLayout root = (LinearLayout) findViewById(R.id.schema_options3);

        final LinearLayout factorList = (LinearLayout) findViewById(R.id.schema_options3_list);

        ProgressBar pb = (ProgressBar) findViewById(R.id.setupProgress);
        pb.setProgress(66);

        View addRow = findViewById(R.id.new_factor);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UI", " factor click");
                View factorInfo = getLayoutInflater().inflate(R.layout.editor_factor, null);
                factorList.addView(factorInfo);
            }
        });

    }

    public void setupDone(View view) {
        // do stuff
        setContentView(R.layout.activity_mainview);
    }

    public void importSchema(View view) {

    }

    public void useExisting(View view) {

    }

    public Symptom addSymptom() {
        Log.d("TEST", "addSymptom");
        return new Symptom();
    }

    public Factor addFactor() {

        return new Factor();
    }
}
