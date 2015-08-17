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
                    /*
                    image = (ImageSwitcher) v.findViewById(R.id.value);
                    image.setFactory(new ViewSwitcher.ViewFactory() {
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
                    image.setImageResource(R.drawable.delete);
                    */

                    none = (ScaleButton) v.findViewById(R.id.scale_none);
                    none.setImageResource(R.drawable.scale_none);
                    none.setRequired(buttonGroup, "none", parentView);

                    mild = (ScaleButton) v.findViewById(R.id.scale_mild);
                    mild.setImageResource(R.drawable.scale_mild);
                    mild.setRequired(buttonGroup, "mild", parentView);

                    severe = (ScaleButton) v.findViewById(R.id.scale_severe);
                    severe.setImageResource(R.drawable.scale_severe);
                    severe.setRequired(buttonGroup, "severe", parentView);

                    buttonGroup.add(none);
                    buttonGroup.add(mild);
                    buttonGroup.add(severe);

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