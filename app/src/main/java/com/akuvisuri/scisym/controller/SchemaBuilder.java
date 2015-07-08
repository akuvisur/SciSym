package com.akuvisuri.scisym.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.akuvisuri.scisym.containers.MainUtils;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;
import com.akuvisuri.scisym.LaunchScreen;

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
        JSONObject schema = new JSONObject();
        try {
            schema.put("type", LaunchScreen.schemaType);
            schema.put("author", LaunchScreen.schemaAuthor);
            schema.put("title", LaunchScreen.schemaTitle);
            schema.put("desc", LaunchScreen.schemaDesc);
            schema.put("dbname", LaunchScreen.dbName);
            JSONArray symptoms = new JSONArray();
            for (Symptom s : MainUtils.selectedSymptoms) {
                symptoms.put(s.toJSON());
            }
            schema.put("symptoms", symptoms);
            JSONArray factors = new JSONArray();
            for (Factor f : MainUtils.selectedFactors) {
                factors.put(f.toJSON());
            }
            schema.put("factors", factors);
        }
        catch (JSONException e) {
            Log.d(LOG, "JSONException saving schema");
        }
        editor.putString("schema", schema.toString());

        editor.commit();

        Log.d(LOG, "Schema saved\n" + schema.toString());
    }

}
