package com.moviegraph;

/**
 * Created by drrt on 11/5/13.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + DatabaseAllMovies.TABLE_NAME +
                "(_id INTEGER PRIMARY KEY autoincrement, " +
                "name TEXT, " +
                "mood TEXT, " +
                "dateSeen TEXT, " +
                "tag1 TEXT, " +
                "tag2 TEXT)";

        db.execSQL(createQuery);

        createQuery = "CREATE TABLE " + DatabaseSeen.TABLE_NAME +
                "(_id INTEGER PRIMARY KEY autoincrement, " +
                "movie_id TEXT, " +
                "category TEXT)";

        db.execSQL(createQuery);

        createQuery = "CREATE TABLE " + DatabasePending.TABLE_NAME +
                "(_id INTEGER PRIMARY KEY autoincrement, " +
                "movie_id TEXT, " +
                "date_received TEXT)";

        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    { /* nothing to do*/ }
}
