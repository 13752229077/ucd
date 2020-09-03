package com.example.home.utility.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * A helper class that deals with storing and accessing users reports in a local database
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * An entry class which describes an entry of the database
     */
    public class Entry{

        private String latitude;
        private String longitude;
        private int people;
        private String sheltered;
        private String description;

        Entry(String location, int people, String sheltered, String description){
            this.latitude = location.split(",")[0];
            this.longitude = location.split(",")[1];
            this.people = people;
            this.sheltered = sheltered;
            this.description = description;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude(){
            return longitude;
        }

        public int getPeople() {
            return people;
        }

        public String getSheltered() {
            return sheltered;
        }

        public String getDescription() {
            return description;
        }

    }

    private static final String DATABASE_NAME = "HOME";
    private static final String REPORTS_TABLE_NAME = "REPORTS";
    private static final String REPORTS_COLUMN_ID = "ID";
    private static final String REPORTS_COLUMN_LOCATION = "LOCATION";
    private static final String REPORTS_COLUMN_PEOPLE = "PEOPLE";
    private static final String REPORTS_COLUMN_SHELTERED = "SHELTERED";
    private static final String REPORTS_COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String REPORTS_COLUMN_IMAGE = "IMAGE";

    private boolean captured = false;
    private ArrayList<Entry> entries = new ArrayList<>();

    /**
     * Constructor calling superclass
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    /**
     * Creating the the database table
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + REPORTS_TABLE_NAME +
                        " (" + REPORTS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        REPORTS_COLUMN_LOCATION + " TEXT, " +
                        REPORTS_COLUMN_PEOPLE + " INT, " +
                        REPORTS_COLUMN_SHELTERED + " TEXT, " +
                        REPORTS_COLUMN_DESCRIPTION + " TEXT, " +
                        REPORTS_COLUMN_IMAGE + " TEXT)"
        );
    }

    /**
     * replacing old table with new table
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + REPORTS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Insert a report entry into the database, due to lack of a remote database and the significant amount of space
     * we chose to not store a captured image in the local database
     * @param location
     * @param people
     * @param sheltered
     * @param description
     * @param image
     * @return
     */
    public boolean insertReport(String location, int people, String sheltered, String description, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(REPORTS_COLUMN_LOCATION, location);
        cv.put(REPORTS_COLUMN_PEOPLE, people);
        cv.put(REPORTS_COLUMN_SHELTERED, sheltered);
        cv.put(REPORTS_COLUMN_DESCRIPTION, description);

        try{
            int check = image.getWidth(); // if no image was captured, this should throw an exception
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] b = baos.toByteArray();
            captured = true;
            cv.put(REPORTS_COLUMN_IMAGE, Base64.encode(b, Base64.DEFAULT));
            Log.i("SQL", "Detected that an image was captured...");
        }catch(Exception e){
            Log.i("SQL", "Detected that no image was captured...");
            captured = false;
            cv.put(REPORTS_COLUMN_IMAGE, "-");
        }

        db.insert(REPORTS_TABLE_NAME, null, cv);
        entries.add(new Entry(location, people, sheltered, description));

        return true;
    }

    /**
     * Method to retrieve all entries stored within the database
     * @return
     */
    public ArrayList<Entry> getAllEntries(){
        SQLiteDatabase db = this.getReadableDatabase();
        entries = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM " + REPORTS_TABLE_NAME,null);
        CursorWindow cw = new CursorWindow("test", 10000000);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) cur;
        ac.setWindow(cw);

        //using a cursor we request information form the database an place it into a list of Entry objects
        try {
            ac.moveToFirst();
            while(!ac.isAfterLast()){
                entries.add(new Entry(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_LOCATION)),
                                      ac.getInt(ac.getColumnIndex(REPORTS_COLUMN_PEOPLE)),
                                      ac.getString(ac.getColumnIndex(REPORTS_COLUMN_SHELTERED)),
                                      ac.getString(ac.getColumnIndex(REPORTS_COLUMN_DESCRIPTION))));
                ac.moveToNext();
            }
        } catch (SQLiteBlobTooBigException expected) {
            Log.v("SQL", expected.getMessage());
        }
        return entries;
    }

    /**
     * Get the latest entry in the database in string format
     * @return
     */
    public String getLastEntry(){
        AbstractWindowedCursor ac = (AbstractWindowedCursor) getReadableDatabase().rawQuery(
                "SELECT * FROM " + REPORTS_TABLE_NAME +
                        " WHERE " + REPORTS_COLUMN_ID +
                        "= (SELECT MAX(" + REPORTS_COLUMN_ID +
                        ") FROM " + REPORTS_TABLE_NAME +
                        ");",
                null);
        ac.setWindow(new CursorWindow("test", 5000000));

        try {
            ac.moveToNext();
        } catch (SQLiteBlobTooBigException expected) {
            Log.v("SQL", expected.getMessage());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_ID)));
        sb.append("  ");
        sb.append(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_LOCATION)));
        sb.append("  ");
        sb.append(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_PEOPLE)));
        sb.append("  ");
        sb.append(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_SHELTERED)));
        sb.append("  ");
        sb.append(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_DESCRIPTION)));
        sb.append("  ");

        if(captured) {
            sb.append(Base64.encodeToString(ac.getBlob(ac.getColumnIndex(REPORTS_COLUMN_IMAGE)), Base64.DEFAULT));
        }else{
            sb.append(ac.getString(ac.getColumnIndex(REPORTS_COLUMN_IMAGE)));
        }
        return sb.toString();
    }

    /**
     * method checking if a table is empty or not
     * @return
     */
    public boolean hasEntries(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + REPORTS_TABLE_NAME,null);
        CursorWindow cw = new CursorWindow("test", 10000000);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) cursor;
        ac.setWindow(cw);
        try {
            ac.moveToNext();
        } catch (SQLiteBlobTooBigException expected) {
            Log.v("SQL", expected.getMessage());
        }
        return ac.getInt(0) > 0;
    }
}
