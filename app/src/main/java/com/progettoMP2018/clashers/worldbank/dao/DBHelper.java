package com.progettoMP2018.clashers.worldbank.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static String NAME = "worldbankDB.db";
    private static int VERSION = 1;
    private String[] columns = {
            "url",
            "json"
    };
    private String[] columns1 = {
            "json",
            "topic",
            "indicator",
            "country",
            "indicator_id",
            "country_iso2code"
    };
    private SQLiteDatabase db;
    private static final String TABLE_URL_NAME = "json_url";
    private static final String TABLE_REQUEST_NAME = "requests";

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_URL_NAME + " (\n" +
                "url TEXT NOT NULL PRIMARY KEY,\n" +
                "json TEXT\n" +
                ");";

        String query1 = "CREATE TABLE IF NOT EXISTS " + TABLE_REQUEST_NAME + " (\n" +
                "json TEXT,\n" +
                "topic TEXT,\n" +
                "indicator TEXT,\n" +
                "country TEXT,\n" +
                "indicator_id TEXT,\n" +
                "country_iso2code TEXT,\n" +
                "PRIMARY KEY (topic, indicator_id, country_iso2code)\n" +
                ");";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version_from, int version_to) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_URL_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void open() {
        db = getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public Cursor getURL(String url) {
        Cursor cur = db
                .query("json_url", columns,
                        "url = '" + url + "'", null,
                        null, null, null);
        return cur;
    }

    public void addURL(String url, String json) {
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("json", json);
        db.insert(TABLE_URL_NAME, null,
                values);
    }

    public void saveRequestIntoDatabase(String json, String topic, String indicator, String country, String indicatorId, String country_iso2code) {
        ContentValues values = new ContentValues();
        values.put("json", json);
        values.put("topic", topic);
        values.put("indicator", indicator);
        values.put("country", country);
        values.put("indicator_id", indicatorId);
        values.put("country_iso2code", country_iso2code);
        db.insert(TABLE_REQUEST_NAME, null,
                values);
    }

    public Cursor getSavedRequests() {
        Cursor cur = db
                .query("requests", columns1,
                        null, null,
                        null, null, null);
        return cur;
    }

    public void deleteAllJson() {
        db.delete(TABLE_URL_NAME, null, null);
    }

    public void deleteSavedRequests() {
        db.delete(TABLE_REQUEST_NAME, null, null);
    }

    public void deleteTable() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_URL_NAME);
    }
}
