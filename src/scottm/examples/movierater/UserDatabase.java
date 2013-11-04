package scottm.examples.movierater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDatabase {

    private static final String TAG = "DatabaseConnector";

    private static final String DATABASE_NAME = "MovieGraph";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;


    public UserDatabase(Context context) {
        databaseOpenHelper = 
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }


    public void open() throws SQLException {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } 


    public void close() {
        if (database != null)
            database.close();
    } 


    // inserts a new rating into the database
    public void insertProfile(String email, String password) {

        ContentValues newRating = new ContentValues();
        newRating.put("email", email);
        newRating.put("password", password);

        open();
        database.insert("profiles", null, newRating);
        close();
    }


    // updates a rating in the database
    public void updateRating(long id, String email, String password) {

        ContentValues editRating = new ContentValues();
        editRating.put("email", email);
        editRating.put("password", password);
//        editRating.put("genre", genre);
//        editRating.put("dateSeen", dateSeen);
//        editRating.put("tag1", tag1);
//        editRating.put("tag2", tag2);

        open(); 
        database.update("profiles", editRating, "_id=" + id, null);
        close(); 
    } 

    public Cursor getAllRatings() {
        return database.query("profiles", new String[] {"_id", "email"},
                null, null, null, null, "email");
        // query(String table, 
        // String[] columns, String selection, String[] selectionArgs, 
        // String groupBy, String having, String orderBy)
    }


    // get a Cursor containing all information about the movie specified
    // by the given id
    public Cursor getOneRating(long id) {
        return database.query(
                "profiles", null, "_id=" + id, null, null, null, null);

        // public Cursor query (String table, String[] columns, 
        // String selection, String[] selectionArgs, String groupBy, 
        // String having, String orderBy, String limit)
    }


    // delete the rating specified by the given id
    public void deleteRating(long id) {
        open(); 
        database.delete("profiles", "_id=" + id, null);
        close();
    } 


    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
            Log.d(TAG, "in constructor");
        } 


        // creates the ratings table when the database is created
        @Override   
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "in on create");
            // query to create a new table named ratings
            String createQuery = "CREATE TABLE profiles" +
                    "(_id INTEGER PRIMARY KEY autoincrement, " +
                    "email TEXT, " +
                    "password TEXT);";

            db.execSQL(createQuery);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                int newVersion) 
        { /* nothing to do*/ }
    } 
} 

