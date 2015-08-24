package com.akuvisuri.scisym.view.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.akuvisuri.scisym.Launch;
import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.view.adapters.TrackingListAdapter;

import java.util.ArrayList;

public class ScaleButton extends ImageButton implements View.OnClickListener {
    private boolean selected = false;
    private ArrayList<ScaleButton> buttonGroup;
    public String label;
    private ListView parent;

    public ScaleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);
    }

    public void setRequired(ArrayList<ScaleButton> a, String l, ListView p) {
        buttonGroup = a;
        label = l;
        parent = p;
    }

    public void setSelected(boolean s) {
        selected = s;
        setImage(s);
        for (ScaleButton b : buttonGroup) {
            Log.d("sc", b.label + " : " + label);
            if (!label.equals(b.label)) {
                Log.d("sc", "hit");
                b.setImage(false);
            }
        }

        for (int i = 0; i < Launch.tracking_content.getChildCount(); i++) {
            Log.d("sc", "get view at " + i);
            ((TrackingListAdapter) Launch.tracking_content.getAdapter()).getView(i, Launch.tracking_content.getChildAt(i), Launch.tracking_content);
        }

    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public void setImage(boolean _selected) {
        selected = _selected;
        // TODO these things dont update (redraw) properly like a radiobutton should
        switch (label) {
            case "none":
                if (selected) setImageResource(R.drawable.scale_none_selected);
                else setImageResource(R.drawable.scale_none);
                break;
            case "mild":
                if (selected) setImageResource(R.drawable.scale_mild_selected);
                else setImageResource(R.drawable.scale_mild);
                break;
            case "severe":
                if (selected) {setImageResource(R.drawable.scale_severe_selected);}
                else {setImageResource(R.drawable.scale_severe);}
                break;
        }

        //Launch.tracking_content.invalidate();
    }

    @Override
    public void onClick(View v) {
        if (selected) setSelected(false); else setSelected(true);
        //Launch.tracking_content.invalidateViews();
    }
}