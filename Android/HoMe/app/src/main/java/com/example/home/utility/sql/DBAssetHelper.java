package com.example.home.utility.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/***
 * This database helper class is used to access the database (assets/databases/county_details.db)
 * with tables: county, county_colours, figures & services by
 *
 *      ContactsFragment.java: to get homeless service contact details per county in the Republic of Ireland.
 *      tables: county, services
 *
 *      StatisticsFragments.java to retrieve the name of a county from its colour selected in the coloured map of
 *      Ireland then return the corresponding homeless figures for that county.
 *      tables: county_colours, figures
 * */
public class DBAssetHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "county_details";

    // SQLite commands
    private String [] SELECT = {"*"};
    private String INNER_JOIN = "services INNER JOIN county ON (services.County = county.County_id)";
    private String SORT_COUNTY = "services.County";

    private static SQLiteDatabase db;
    private static SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    private Cursor cursor;
    private String currentCounty;   // last county selected in statistics


    /**Constructor calls base class constructor then instantiates db **/
    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getReadableDatabase();
    }

    /**@param userCounty a county selected by the user from a spinner or set by location
     *                    default spinner value "Select county…" will show all counties
     * @return Cursor containing table information of selected county/counties **/
    public Cursor getColumns(String userCounty) {
        qb = new SQLiteQueryBuilder();

        if(userCounty.equals("Select county…")){
            setAllCounties();
        }
        else if(userCounty.equals("Dublin")){
            setDublin();
        }
        else {
            setCounty(userCounty);
        }
        cursor.moveToFirst();
        return cursor;
    }

    /** Assign cursor the set of services for all counties in descending order **/
    private void setAllCounties(){
        qb.setTables(INNER_JOIN);
        cursor = qb.query(db, SELECT, null, null, null, null, SORT_COUNTY);
    }
    /** Assign cursor the set of services for the 4 Dublin sub-regions
     * Dublin City, Dún Laoghaire, Fingal & South Dublin
     * in descending order **/
    private void setDublin(){
        String WHERE_DUBLIN = "services.County='FING' OR services.County='SOUDUB' OR services.County='DUBCITY' OR services.County='DUNRATH'";

        qb.setTables(INNER_JOIN);
        cursor = qb.query(db, SELECT, WHERE_DUBLIN, null, null, null, SORT_COUNTY);
    }
    /**
     * The county id (county.County_id) is first obtained from the county table to be used to get all services for
     * @param county
     * from the services table which are assigned to cursor
     *
     * **/
    private void setCounty(String county){
        String sqlWhere = "county_name = '" +county+"'";
        String TABLES = "county";

        qb.setTables(TABLES);
        cursor = qb.query(db, SELECT, sqlWhere, null, null, null, null);
        cursor.moveToFirst();

        sqlWhere = "County_id = '" + cursor.getString(cursor.getColumnIndex("County_id")) + "'";
        qb.setTables(INNER_JOIN);
        cursor = qb.query(db, SELECT, sqlWhere, null, null, null, SORT_COUNTY);
    }

    /** This method queries the county_colours table for the corresponding county by colour.
     * @param hexValue contains the hexadecimal digits corresponding to a colour
     * @return String containing the name of the county colour with hexValue
     * If white or black are passed (regions outside map) or no examples of hexValue are in
     * county_colours we return the previously selected county**/
    public String getCountyByColour(String hexValue){
        if(!hexValue.equals("000000") && !hexValue.equals("ffffff")) {
            qb.setTables("county_colours");
            String[] SELECT_COUNTY = {"County"};
            String WHERE = "county_colours.Colour='" + hexValue + "'";
            cursor = qb.query(db, SELECT_COUNTY, WHERE, null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount()>0) {
                currentCounty = cursor.getString(cursor.getColumnIndex("County"));
            }
        }
        return currentCounty;
    }
    /** For a given county we return the homeless figures from table figures
     * @param county county to get homeless figures for
     * @return cursor containing table of homeless figures**/
    public Cursor getCountyFigures(String county){
        qb.setTables("figures");
        String WHERE = "figures.County='" + county +"'";
        cursor = qb.query(db, SELECT, WHERE, null, null, null, null);
        cursor.moveToFirst();
        return cursor;

    }
}
