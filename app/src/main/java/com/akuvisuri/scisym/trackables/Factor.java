package com.akuvisuri.scisym.trackables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aku on 22.6.2015.
 */
public class Factor {
    public enum Type {
        BOOL, TRACKED, MULTIPLE
    }

    protected String label;
    protected String desc;
    protected Type type;
    protected String rep_window;
    protected JSONArray values;
    protected JSONObject range;

    // obj contains values OR range depending on type
    public Factor(
        String label,
        String desc,
        String _type,
        String rep_window,
        Object obj
    ) {
        this.label = label;
        this.desc = desc;
        this.rep_window = rep_window;
        switch(_type) {
            case ("bool"):
                type = Type.BOOL;
                break;
            case ("tracked"):
                type = Type.TRACKED;
                range = (JSONObject) obj;
                break;
            case ("multiple"):
                type = Type.MULTIPLE;
                values = (JSONArray) obj;
                break;
        }
    }

    public String toString() {
        return this.label;
    }

    public String attrToString() {
        return this.desc;
    }

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();

        try {
            o.put("label", label);
            o.put("desc", desc);
            o.put("rep_window", rep_window);
            switch(type) {
                case TRACKED:
                    JSONObject obj = range;
                    o.put("range", obj);
                    o.put("type", "tracked");
                    break;
                case MULTIPLE:
                    JSONArray arr = values;
                    o.put("values", arr);
                    o.put("type", "multiple");
                    break;
                case BOOL:
                    o.put("type", "bool");
                    break;
            }
        } catch (JSONException e) {e.printStackTrace();}

        return o;
    }

}
