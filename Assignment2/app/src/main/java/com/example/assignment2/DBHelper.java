package com.example.assignment2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.session.PlaybackState;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String CITYTABLE = "CITYTABLE";
    public static final String ADDRESS = "ADDRESS";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String ID = "ID";

    public DBHelper(@Nullable Context context) {
        super(context, "CityData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CITYTABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ADDRESS + " TEXT, " + LATITUDE + " TEXT, " + LONGITUDE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    public boolean addCity(String address, String latitude, String longidute){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ADDRESS, address);
        cv.put(LATITUDE, latitude);
        cv.put(LONGITUDE, longidute);

        long insert = db.insert(CITYTABLE, null, cv);

        if(insert ==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList<String> getAllCity() {
        ArrayList<String> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CITYTABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            //loop through city
            do {
                int cityID = cursor.getInt(0);
                String Address = cursor.getString(1);
                String Latitude = cursor.getString(2);
                String Longitdue = cursor.getString(3);
                returnList.add(Address);

            }while (cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean findrow(String latitude, String longitude ){

        String queryString =
                "SELECT " + LATITUDE + ", "+ LONGITUDE+
                " FROM " + CITYTABLE +
                " WHERE "+ LATITUDE+ " = '" + latitude + "' AND "+ LONGITUDE + " = '"+ longitude+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return false;
        }
        else{
            cursor.close();
            db.close();
            return true;
        }

    }

    public boolean findAddress (String address){
        String queryString =
                "SELECT " + ADDRESS +
                        " FROM " + CITYTABLE +
                        " WHERE "+ ADDRESS+ " = '" + address + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return false;
        }
        else{
            cursor.close();
            db.close();
            return true;
        }
    }

    public void deleteEverything(){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+CITYTABLE;
        db.execSQL(queryString);
    }

    public void deleteRow(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString =
                "DELETE FROM "+ CITYTABLE+
                " WHERE "+ADDRESS+ " = '"+address + "' ";
        db.execSQL(queryString);
    }

    public void updateAddress(String oldAddress, String newAddress){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString =
                "UPDATE " + CITYTABLE +
                        " SET "+ ADDRESS + " = '" + newAddress + "' "+
                        " WHERE " + ADDRESS + " = '" + oldAddress + "' ";

        db.execSQL(queryString);
    }
}
