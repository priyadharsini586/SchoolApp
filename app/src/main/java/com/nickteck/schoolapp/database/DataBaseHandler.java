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
    private static final String DEVICE_ID  = "DEVICE_ID";

    //parent Deatils
    private static final String TABLE_PARENT_CHILD= "TABLE_PARENT_CHILD";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String PARENT_CHILD_DETAILS = "PARENT_CHILD_DETAILS";

    public DataBaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_CHECK + "("
                + Id + " TEXT ," + DEVICE_ID + " TEXT ,"
                + MOBILE_NUMBER + " TEXT " + ")";

        String CREATE_PARENT_CHILD_TABLE = "CREATE TABLE " + TABLE_PARENT_CHILD + "("
                + PARENT_ID + " TEXT ,"
                + PARENT_CHILD_DETAILS + " TEXT " + ")";

        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_PARENT_CHILD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOGIN_CHECK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT_CHILD);
    }

    public void insertLoginTable(String id,String mobileNumber,String device_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Id, id);
        values.put(MOBILE_NUMBER,mobileNumber);
        values.put(DEVICE_ID,device_id);

        // Inserting Row
        db.insert(LOGIN_CHECK, null, values);
        db.close();
    }

    public void insertParentDetails(String ParentID,String parentChildDetails){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARENT_ID, ParentID);
        values.put(PARENT_CHILD_DETAILS,parentChildDetails);

        // Inserting Row
        db.insert(TABLE_PARENT_CHILD, null, values);
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
        }else {
            cursor.close();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              return true;
        }
    }

    public String getMobileNumber(){
        String mobileNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + LOGIN_CHECK;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

         mobileNumber = cursor.getString(2);
        db.close();

        return mobileNumber;
    }

    public String getDeviceId(){
        String deviceId = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + LOGIN_CHECK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        deviceId = cursor.getString(1);
        db.close();

        return deviceId;
    }
    public void dropParentDetails(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "DELETE  FROM " + TABLE_PARENT_CHILD;
        db.execSQL(selectQuery);
    }

    public String getParentChildDetails(){
        String parentDetails = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PARENT_CHILD;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        parentDetails = cursor.getString(1);
        db.close();

        return parentDetails;
    }
}
