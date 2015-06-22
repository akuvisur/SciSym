package com.akuvisuri.scisym;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by Aku on 22.6.2015.
 */
public class SchemaBuilder {
    static final String MyPREFERENCES = "SciSym";
    static SharedPreferences se;

    public static JSONArray symptoms;
    public static JSONArray factors;
    public static JSONObject schemaOptions;

    public static void init() {
        se = MainUtils.c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (se != null) {
            try {
                schemaOptions = new JSONObject(se.getString("options", ""));
                symptoms = new JSONArray(se.getString("symptoms", ""));
                factors = new JSONArray(se.getString("factors", ""));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("SCHEMA", "Could not convert JSON string");
            }
        }
    }

    public static void load() {

    }

    public static void save() {
        se = MainUtils.c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = se.edit();
        // add everything
        editor.commit();
    }

}
