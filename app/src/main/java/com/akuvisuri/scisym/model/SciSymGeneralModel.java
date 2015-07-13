package com.akuvisuri.scisym.model;

/**
 * Created by Aku on 13.7.2015.
 */
public class SciSymGeneralModel {


    public static void setDatabase(String s) {
        FactorTrackingDBHelper.DATABASE_NAME = s;
        SymptomTrackingDBHelper.DATABASE_NAME = s;
    }
}
