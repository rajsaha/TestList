package com.example.getright_malaysia.testlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Raj on 10/6/2015.
 */
public class DBAdapter {
    SQLiteDatabase database_ob;
    DBHelper dbHelper_ob;
    Context context;

    public DBAdapter(Context c){
        context = c;
    }

    public DBAdapter opnToRead(){
        dbHelper_ob = new DBHelper(context, dbHelper_ob.DATABASE_NAME, null, dbHelper_ob.VERSION);
        database_ob = dbHelper_ob.getReadableDatabase();
        return this;
    }

    public DBAdapter opnToWrite(){
        dbHelper_ob = new DBHelper(context, dbHelper_ob.DATABASE_NAME, null, dbHelper_ob.VERSION);
        database_ob = dbHelper_ob.getWritableDatabase();
        return this;
    }

    public void Close(){
        database_ob.close();
    }

    public long insertDetails(String cname, int usage, int wattage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper_ob.COMPONENT_NAME, cname);
        contentValues.put(dbHelper_ob.USAGE, usage);
        contentValues.put(dbHelper_ob.WATTAGE, wattage);
        opnToWrite();

        long val = database_ob.insert(dbHelper_ob.TABLE_NAME, null, contentValues);
        Close();
        return val;
    }

    public Cursor queryName(){
        String[] cols = {dbHelper_ob.KEY_ID, dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE, dbHelper_ob.WATTAGE};
        opnToWrite();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, cols, null, null, null, null, null);

        return c;
    }

    public Cursor queryAll(int nameId){
        String[] cols = {dbHelper_ob.KEY_ID, dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE, dbHelper_ob.WATTAGE};
        opnToWrite();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, cols, dbHelper_ob.KEY_ID + "=" + nameId,
                null, null, null, null);

        return c;
    }

    public int getTotalWattage(){
        String[] columns = new String[] {dbHelper_ob.KEY_ID,
                dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE,
                dbHelper_ob.WATTAGE};
        opnToRead();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, columns,
                null, null, null, null, null);
        int total = 0;

        int wattage = c.getInt(Integer.parseInt(dbHelper_ob.WATTAGE));

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            total = total + wattage;
        }

        return total;
    }



    public long updateldetail(int rowId, String cname, int usage, int wattage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper_ob.COMPONENT_NAME, cname);
        contentValues.put(dbHelper_ob.USAGE, usage);
        contentValues.put(dbHelper_ob.WATTAGE, wattage);
        opnToWrite();
        long val = database_ob.update(dbHelper_ob.TABLE_NAME, contentValues,
                dbHelper_ob.KEY_ID + "=" + rowId, null);
        Close();
        return val;
    }

    public int deleteOneRecord(int rowId){
        opnToWrite();
        int val = database_ob.delete(dbHelper_ob.TABLE_NAME, dbHelper_ob.KEY_ID
                + "=" + rowId, null);
        Close();
        return val;
    }
}
