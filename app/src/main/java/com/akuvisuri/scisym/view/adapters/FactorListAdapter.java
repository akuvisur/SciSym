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
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.containers.Factors;
import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.trackables.Factor;

import java.util.ArrayList;

/**
 * Created by Aku on 29.6.2015.
 */
public class FactorListAdapter extends ArrayAdapter<FactorListAdapter.FactorListItem> {
    private final String LOG = "FactorListAdapter.java";

    private final Context context;
    private final ArrayList<FactorListItem> values;

    public static ArrayList<Factor> selection = new ArrayList<Factor>();

    public FactorListAdapter(Context context, ArrayList<FactorListItem> v) {
        super(context, -1, v);
        this.context = context;
        values = v;
    }

    public ArrayList<Factor> getSelection() {
        return selection;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.editor_factor, parent, false);

        if (!values.get(position).visible) {
            return new LinearLayout(context);
        }

        TextView label = (TextView) rowView.findViewById(R.id.factor_label);
        TextView attr = (TextView) rowView.findViewById(R.id.factor_attr);
        final ImageSwitcher rightImage = (ImageSwitcher) rowView.findViewById(R.id.factor_right_image);
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

        Factor thisFact = Factors.list.get(values.get(position).label);
        label.setText(thisFact.toString());
        label.setTextColor(Color.BLACK);
        attr.setText(thisFact.attrToString());

        if (MainUtils.selectedFactors.contains(Factors.list.get(thisFact.toString()))
                | selection.contains(thisFact)) {
            rightImage.setImageResource(R.drawable.checkbox_selected);
        } else rightImage.setImageResource(R.drawable.checkbox_unselected);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView label = (TextView) v.findViewById(R.id.factor_label);
                if (selection.contains(Factors.list.get(label.getText()))) {
                    Log.d(LOG, "removed " + Factors.list.get(label.getText()).toString());
                    rightImage.setImageResource(R.drawable.checkbox_unselected);
                    selection.remove(selection.indexOf(Factors.list.get(label.getText())));
                }
                else {
                    Log.d(LOG, "selected " + Factors.list.get(label.getText()).toString());
                    selection.add(Factors.list.get(label.getText()));
                    rightImage.setImageResource(R.drawable.checkbox_selected);
                }
            }
        });
        return rowView;
    }

    public static class FactorListItem {
        public boolean visible;
        public String label;

        public FactorListItem(boolean v, String l) {
            visible = v;
            label = l;
        }

        public void setVisible(boolean v) {
            visible = v;
        }

        public String toString() {
            return label;
        }
    }
}
