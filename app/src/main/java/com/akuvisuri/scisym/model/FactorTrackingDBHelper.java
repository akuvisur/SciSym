package com.akuvisuri.scisym.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Aku on 13.7.2015.
 */
public class FactorTrackingDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + EventEntry.TABLE_NAME + " (" +
                    EventEntry._ID + " INTEGER PRIMARY KEY," +
                    EventEntry.TIMESTAMP + NUM_TYPE + COMMA_SEP +
                    EventEntry.HOUR + NUM_TYPE + COMMA_SEP +
                    EventEntry.DAY+ NUM_TYPE + COMMA_SEP +
                    EventEntry.WEEK+ NUM_TYPE + COMMA_SEP +
                    EventEntry.MONTH + NUM_TYPE + COMMA_SEP +
                    EventEntry.LABEL+ TEXT_TYPE + COMMA_SEP +
                    EventEntry.INPUT + TEXT_TYPE + COMMA_SEP +
                    EventEntry.VALUE + TEXT_TYPE + COMMA_SEP + ")"
            ;


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME;

    public FactorTrackingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static abstract class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "factors";

        public static final String TIMESTAMP = "timestamp";
        public static final String HOUR = "hour";
        public static final String DAY = "day";
        public static final String WEEK = "week";
        public static final String MONTH = "month";
        public static final String LABEL = "label";
        public static final String INPUT = "input";
        public static final String VALUE = "value";

    }

}
