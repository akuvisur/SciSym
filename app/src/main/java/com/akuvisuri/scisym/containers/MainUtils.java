package com.akuvisuri.scisym.containers;

import android.app.Activity;
import android.content.Context;

import com.akuvisuri.scisym.trackables.Factor;
import com.akuvisuri.scisym.trackables.Symptom;

import java.util.ArrayList;

/**
 * Created by Aku on 22.6.2015.
 */
public class MainUtils {
    public static final boolean DEBUG = true;

    public static final String MyPREFERENCES = "SciSym";

    public static ArrayList<Symptom> selectedSymptoms = new ArrayList<>();
    public static ArrayList<Factor> selectedFactors = new ArrayList<>();

    public static Context c;
    public static void setContext(Context nn) {
        c = nn;
    }
    public static Activity a;
    public static void setActivity(Activity aa) { a = aa;}
}
