package com.akuvisuri.scisym.view.adapters;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;
import com.akuvisuri.scisym.view.elements.ScaleButton;
import com.akuvisuri.scisym.view.tracking.Tracking;

import java.util.ArrayList;

/**
 * Created by Aku on 20.7.2015.
 */
public class TrackingListAdapter extends ArrayAdapter<Tracking> {
    private Context c;
    private ArrayList<Tracking> items;

    Symptom s;
    Factor f;

    View v;

    TextView label;
    ImageSwitcher image;
    ScaleButton none;
    ScaleButton mild;
    ScaleButton severe;
    ArrayList<ScaleButton> buttonGroup;

    private ListView parentView;

    public void setParent(ListView v) {
        parentView = v;
    }

    public TrackingListAdapter(Context _c, ArrayList<Tracking> _items) {
        super(_c, -1, _items);
        c = _c;
        items = _items;
        buttonGroup = new ArrayList<ScaleButton>();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        buttonGroup.clear();
        if (items.get(position).getTrackable() instanceof Symptom) {
            s = (Symptom) items.get(position).getTrackable();
            switch (s.input) {
                case "scale":
                    v = MainUtils.a.getLayoutInflater().inflate(R.layout.tracking_symptom_scale, null);
                    label = (TextView) v.findViewById(R.id.label);
                    label.setText(s.label);

                    none = (ScaleButton) v.findViewById(R.id.scale_none);
                    mild = (ScaleButton) v.findViewById(R.id.scale_mild);
                    severe = (ScaleButton) v.findViewById(R.id.scale_severe);


                    buttonGroup.add(none);
                    buttonGroup.add(mild);
                    buttonGroup.add(severe);

                    none.setRequired(buttonGroup, "none", parentView);
                    mild.setRequired(buttonGroup, "mild", parentView);
                    severe.setRequired(buttonGroup, "severe", parentView);

                    if (none.isSelected()) {
                        Log.d("adapter", "none was selected");
                        none.setImageResource(R.drawable.scale_none_selected);
                    }
                    else none.setImageResource(R.drawable.scale_none);

                    if (mild.isSelected()) {
                        Log.d("adapter", "mild was selected");
                        mild.setImageResource(R.drawable.scale_mild_selected);
                    }
                    else mild.setImageResource(R.drawable.scale_mild);

                    if (severe.isSelected()) {
                        Log.d("adapter", "severe was selected");
                        severe.setImageResource(R.drawable.scale_severe_selected);
                    }
                    else severe.setImageResource(R.drawable.scale_severe);

                    return v;
                case "text":
                    v = MainUtils.a.getLayoutInflater().inflate(R.layout.tracking_symptom_text, null);
                    label = (TextView) v.findViewById(R.id.label);
                    label.setText(s.label);

                    return v;
                case "numeric":
                    v = MainUtils.a.getLayoutInflater().inflate(R.layout.tracking_symptom_numeric, null);
                    label = (TextView) v.findViewById(R.id.label);
                    label.setText(s.label);

                    return v;

            }
        }
        else if (items.get(position).getTrackable() instanceof Factor) {
            f = (Factor) items.get(position).getTrackable();
            switch (f.type) {

            }
        }
        return v;
    }

}