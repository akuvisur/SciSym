package com.akuvisuri.scisym.trackables;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aku on 22.6.2015.
 */
public class Symptom {
    public enum Type {
        REPEATING, SINGLE
    }

    protected String label;
    protected String desc;
    protected String _class;
    protected Integer severity;
    protected Type type;
    protected String input;
    protected String rep_window;


    public Symptom(
            String label,
            String desc,
            String _class,
            Integer severity,
            String _type,
            String input,
            String rep_window
    ) {
        this.label = label;
        this.desc = desc;
        this._class = _class;
        this.severity = severity;
        this.input = input;
        this.rep_window = rep_window;
        if (_type.equals("repeating")) {type = Type.REPEATING;} else type = Type.SINGLE;
    }

    public String toString() {
        return this.label;
    }

    public String attrToString() {
        return this._class + ", " + this.desc;
    }

    public boolean isSevere() {
        return severity.equals(1);
    }

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        try {
            o.put("label", label);
            o.put("desc", desc);
            o.put("class", _class);
            o.put("severity", severity);
            o.put("type", type);
            o.put("input", input);
            o.put("rep_window", rep_window);
        }
        catch (JSONException e) {e.printStackTrace();}

        return o;
    }
}
