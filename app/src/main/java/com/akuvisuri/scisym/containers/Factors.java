package com.akuvisuri.scisym.containers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.akuvisuri.scisym.R;
import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aku on 29.6.2015.
 */
public class Factors {

    private static final String LOG = "Factors.java";

    public static HashMap<String, Symptom> list = new HashMap<String, Symptom>();
    public static SharedPreferences se;

    public static void init(Activity a) {
        se = MainUtils.c.getSharedPreferences(MainUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        if (se != null) {
            try {
                Log.d(LOG, "factors: " + se.getString("factors", null));
                JSONArray symArr = new JSONObject(se.getString("factors", null)).getJSONArray("list");
                Log.d(LOG, "array: " + symArr.toString());
                for (int i = 0; i < symArr.length(); i++) {
                    JSONObject jobj = symArr.getJSONObject(i);
                    list.put(jobj.getString("label"),
                            new Symptom(
                                    jobj.getString("label"),
                                    jobj.getString("desc"),
                                    jobj.getString("class"),
                                    jobj.getInt("severity")
                            ));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.d(LOG, "Could not convert JSON string");
            }
            // if starting for the first time
            catch (NullPointerException e2) {
                InputStream inputStream = a.getResources().openRawResource(R.raw.symptoms);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int ctr;
                try {
                    ctr = inputStream.read();
                    while (ctr != -1) {
                        byteArrayOutputStream.write(ctr);
                        ctr = inputStream.read();
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v(LOG, inputStream.toString());
                try {
                    JSONArray symArr = new JSONObject(byteArrayOutputStream.toString()).getJSONArray("list");
                    JSONObject _label;
                    JSONObject _desc;
                    JSONObject _class;
                    JSONObject _severity;
                    String cat_Id = "";
                    String cat_name = "";
                    ArrayList<String[]> data = new ArrayList<String[]>();
                    for (int i = 0; i < symArr.length(); i++) {
                        JSONObject jobj = symArr.getJSONObject(i);
                        list.put(jobj.getString("label"),
                                new Symptom(
                                        jobj.getString("label"),
                                        jobj.getString("desc"),
                                        jobj.getString("class"),
                                        jobj.getInt("severity")
                                ));
                    }
                    SharedPreferences.Editor editor = se.edit();
                    JSONObject saved = new JSONObject();
                    saved.put("list", symArr.toString());
                    editor.putString("factors", saved.toString());
                    editor.commit();
                } catch (Exception se) {
                    se.printStackTrace();
                }
            }
        }
        Log.d(LOG, list.toString());
    }

    public static void put(Factor s) {

    }

    public static void remove(Factor s) {

    }

    public static void clear() {
        se = MainUtils.c.getSharedPreferences(MainUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = se.edit();
        editor.remove("symptoms");
        editor.remove("options");
        editor.remove("factors");
        editor.commit();
    }
}
