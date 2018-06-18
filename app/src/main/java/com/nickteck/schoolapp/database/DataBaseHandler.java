package com.nickteck.schoolapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 6/18/2018.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SCHOOLDB   ";
    private static final int DATABASE_VERSION = 1;

    //parent table
    private static final String LOGIN_CHECK = "LOGIN_CHECK";
    private static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    private static final String Id = "ID";
    public DataBaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_CHECK + "("
                + Id + " TEXT ,"
                + MOBILE_NUMBER + " TEXT " + ")";

        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOGIN_CHECK);
    }

    public void insertLoginTable(String id,String mobileNumber){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Id, id);
        values.put(MOBILE_NUMBER,mobileNumber);

        // Inserting Row
        db.insert(LOGIN_CHECK, null, values);
        db.close();
    }

    public boolean  checkTableIsEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " +LOGIN_CHECK, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if(count > 0){
            cursor.close();
            return false;
        }else
        {
            cursor.close();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              return true;
        }



    }
}
