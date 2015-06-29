package com.akuvisuri.scisym.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.akuvisuri.scisym.containers.MainUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aku on 22.6.2015.
 */
public class SchemaBuilder {
    protected static final String LOG = "SchemaBuilder.java";
    static SharedPreferences se;

    public static JSONObject schema;
    public static JSONArray symptoms;
    public static JSONArray factors;
    public static JSONObject schemaOptions;

    public static void init() {
        se = MainUtils.c.getSharedPreferences(MainUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        if (se != null) {
            try {
                schema = new JSONObject(se.getString("schema", ""));
                schemaOptions = new JSONObject(se.getString("options", ""));
                symptoms = new JSONObject(se.getString("symptoms", "")).getJSONArray("list");
                factors = new JSONObject(se.getString("factors", "")).getJSONArray("list");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(LOG, "Could not convert JSON string");
            }
        }
    }

    public static void load() {

    }

    public static void save() {
        se = MainUtils.c.getSharedPreferences(MainUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = se.edit();
        // add everything
        editor.commit();
    }

}
