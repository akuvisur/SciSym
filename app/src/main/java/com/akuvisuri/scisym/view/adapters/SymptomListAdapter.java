package com.akuvisuri.scisym.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.containers.Symptoms;
import com.akuvisuri.scisym.trackables.Symptom;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Aku on 23.6.2015.
 */
public class SymptomListAdapter extends ArrayAdapter<String> {
    private final String LOG = "SymptomListAdapter.java";

    private final Context context;
    private final ArrayList<String> values;

    public static ArrayList<Symptom> selection = new ArrayList<Symptom>();

    public SymptomListAdapter(Context context, ArrayList<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    public ArrayList<Symptom> getSelection() {
        return selection;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.editor_symptom, parent, false);

        TextView label = (TextView) rowView.findViewById(R.id.symptom_label);
        TextView attr = (TextView) rowView.findViewById(R.id.symptom_attr);
        ImageView severeImage = (ImageView) rowView.findViewById(R.id.symptom_severe);
        final ImageSwitcher rightImage = (ImageSwitcher) rowView.findViewById(R.id.symptom_right_image);
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

        Symptom thisSym = Symptoms.list.get(values.get(position));
        label.setText(thisSym.toString());
        label.setTextColor(Color.BLACK);
        attr.setText(thisSym.attrToString());
        if (thisSym.isSevere()) {
            severeImage.setImageResource(R.drawable.critical);
        }

        if (MainUtils.selectedSymptoms.contains(Symptoms.list.get(thisSym.toString()))) {
            rightImage.setImageResource(R.drawable.checkbox_selected);
        } else rightImage.setImageResource(R.drawable.checkbox_unselected);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView label = (TextView) v.findViewById(R.id.symptom_label);
                if (selection.contains(Symptoms.list.get(label.getText()))) {
                    Log.d(LOG, "removed " + Symptoms.list.get(label.getText()).toString());
                    rightImage.setImageResource(R.drawable.checkbox_unselected);
                    selection.remove(selection.indexOf(Symptoms.list.get(label.getText())));
                }
                else {
                    Log.d(LOG, "selected " + Symptoms.list.get(label.getText()).toString());
                    selection.add(Symptoms.list.get(label.getText()));
                    rightImage.setImageResource(R.drawable.checkbox_selected);
                }
            }
        });
        return rowView;
    }

}
