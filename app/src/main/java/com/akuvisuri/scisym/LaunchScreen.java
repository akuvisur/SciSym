package com.akuvisuri.scisym;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.controller.SchemaBuilder;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;
import com.akuvisuri.scisym.view.FactorSelector;
import com.akuvisuri.scisym.view.SymptomSelector;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.widget.AdapterView.*;


public class LaunchScreen extends ActionBarActivity {
    protected final static String LOG = "LaunchScreen.java";

    private static LinearLayout symptomList;
    private static LinearLayout factorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // IMPORTANT
        MainUtils.setContext(getApplicationContext());

        if (MainUtils.DEBUG) {
            Symptoms.clear();
        }
        SchemaBuilder.init();
        Symptoms.init(this);
        Factors.init(this);
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
    }

    public void setupPage3(View view) {
        setContentView(R.layout.schema_options3);
        LinearLayout root = (LinearLayout) findViewById(R.id.schema_options3);

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

}
