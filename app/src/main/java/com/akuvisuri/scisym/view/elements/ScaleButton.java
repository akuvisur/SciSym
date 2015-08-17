package com.akuvisuri.scisym.view.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.akuvisuri.scisym.R;

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
            if (!b.label.equals(label)) b.setImage(false);
        }
    }

    public void setImage(boolean selected) {
        // TODO these things dont update (redraw) properly like a radiobutton should
        Log.d("ScaleButton", "setImage + " + label + " + " + selected);
        switch (label) {
            case "none":
                if (selected) setImageResource(R.drawable.scale_none_selected);
                else setImageResource(R.drawable.scale_none);
                redraw();
                break;
            case "mild":
                if (selected) setImageResource(R.drawable.scale_mild_selected);
                else setImageResource(R.drawable.scale_mild);
                redraw();
                break;
            case "severe":
                Log.d("ScaleButton", "uliuli");
                if (selected) setImageResource(R.drawable.scale_severe_selected);
                else setImageResource(R.drawable.scale_severe);
                redraw();
                break;
        }
    }

    private void redraw() {
        parent.invalidate();
    }

    @Override
    public void onClick(View v) {
        if (selected) setSelected(false); else setSelected(true);
    }
}