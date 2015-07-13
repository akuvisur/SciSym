package com.akuvisuri.scisym.controller;

import android.util.Log;

import com.akuvisuri.scisym.model.SciSymGeneralModel;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aku on 13.7.2015.
 */
public class Schema {
    private static final String LOG = "Schema.java";

    public static ArrayList<Symptom> symptoms;
    public static ArrayList<Factor> factors;

    public static String title;
    public static String desc;
    public static String author;
    public static String type;
    public static String database;

    public static void init() {
        symptoms = new ArrayList<>();
        factors = new ArrayList<>();
        try {
            Log.d(LOG, "Schema:\\n" + SchemaBuilder.schema.toString());
            title = SchemaBuilder.schema.getString("title");
            desc = SchemaBuilder.schema.getString("desc");
            author = SchemaBuilder.schema.getString("author");
            type = SchemaBuilder.schema.getString("type");
            database = SchemaBuilder.schema.getString("dbname");
            SciSymGeneralModel.setDatabase(database);
        }
        catch (JSONException e) {
            Log.d(LOG, "Schema initialization - JSON error");
        }
        try {
            JSONArray s = SchemaBuilder.schema.getJSONArray("symptoms");
            for (int i = 0; i < s.length(); i++) {
                JSONObject obj = s.getJSONObject(i);
                symptoms.add(new Symptom(
                        obj.getString("label"),
                        obj.getString("desc"),
                        obj.getString("class"),
                        obj.getInt("severity"),
                        obj.getString("type"),
                        obj.getString("input"),
                        obj.getString("rep_window")
                ));
            }
        }
        catch (JSONException e) {
            Log.d(LOG, "Schema initialization - JSON error parsing symptoms");
        }

        try {
            JSONArray s = SchemaBuilder.schema.getJSONArray("factors");
            for (int i = 0; i < s.length(); i++) {
                JSONObject obj = s.getJSONObject(i);
                switch (obj.getString("type")) {
                    case "bool":
                        factors.add(new Factor(
                                obj.getString("label"),
                                obj.getString("desc"),
                                obj.getString("type"),
                                obj.getString("rep_window"),
                                null
                        ));
                        break;
                    case "tracked":
                        factors.add(new Factor(
                                obj.getString("label"),
                                obj.getString("desc"),
                                obj.getString("type"),
                                obj.getString("rep_window"),
                                obj.get("range")
                        ));
                        break;
                    case "multiple":
                        factors.add(new Factor(
                                obj.getString("label"),
                                obj.getString("desc"),
                                obj.getString("type"),
                                obj.getString("rep_window"),
                                obj.get("values")
                        ));
                        break;
                }

            }
        }
        catch (JSONException e) {
            Log.d(LOG, "Schema initialization - JSON error parsing factors");
        }

        Log.d(LOG, "Schema initialization COMPLETE");
    }

}
