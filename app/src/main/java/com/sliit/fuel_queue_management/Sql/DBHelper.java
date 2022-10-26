package com.sliit.fuel_queue_management.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The type Db helper.
 */
public class DBHelper extends SQLiteOpenHelper {
    /**
     * Instantiates a new Db helper.
     *
     * @param context the context
     */
    public DBHelper(Context context ) {
        super(context,"UserData",null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table UserDetails(userID TEXT primary key,name TEXT,password PASSWORD,number NUMBER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists UserDetails");
    }

    /**
     * Inset user data boolean.
     *
     * @param name     the name
     * @param number   the number
     * @param email    the email
     * @param password the password
     * @return the boolean
     */
    public Boolean insetUserData(String name,String number,String email,String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",email);
        contentValues.put("name",name);
        contentValues.put("password",password);
        contentValues.put("number",number);
        long result= DB.insert("UserDetails",null,contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    /**
     * Get data cursor.
     *
     * @return the cursor
     */
    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails ",null);
        return cursor;
    }
}