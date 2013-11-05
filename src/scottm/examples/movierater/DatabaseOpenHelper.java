package scottm.examples.movierater;

/**
 * Created by drrt on 11/5/13.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "all_movies";
    private static final String SEEN_MOVIES = "seen_movies";
    private static final String PENDING_MOVIES = "pending_movies";

    public DatabaseOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " +TABLE_NAME +
                "(_id INTEGER PRIMARY KEY autoincrement, " +
                "name TEXT, " +
                "mood TEXT, " +
                "dateSeen TEXT, " +
                "tag1 TEXT, " +
                "tag2 TEXT)";

        db.execSQL(createQuery);

        createQuery = "CREATE TABLE " +SEEN_MOVIES +
                "(_id INTEGER PRIMARY KEY autoincrement, " +
                "name TEXT, " +
                "mood TEXT, " +
                "dateSeen TEXT, " +
                "tag1 TEXT, " +
                "tag2 TEXT)";

        db.execSQL(createQuery);

        createQuery = "CREATE TABLE " +PENDING_MOVIES +
                "(_id INTEGER PRIMARY KEY autoincrement, " +
                "name TEXT, " +
                "mood TEXT, " +
                "dateSeen TEXT, " +
                "tag1 TEXT, " +
                "tag2 TEXT)";

        db.execSQL(createQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion)
    { /* nothing to do*/ }
}
