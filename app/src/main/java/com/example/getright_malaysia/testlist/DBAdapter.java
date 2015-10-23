package com.example.getright_malaysia.testlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
    SQLiteDatabase database_ob;
    DBHelper dbHelper_ob;
    Context context;
    double rate;

    public DBAdapter(Context c){
        context = c;
        setRate(0.21);
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

    public long insertDetails(String cname, double usage, int wattage, int units){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper_ob.COMPONENT_NAME, cname);
        contentValues.put(dbHelper_ob.USAGE, usage);
        contentValues.put(dbHelper_ob.WATTAGE, wattage);
        contentValues.put(dbHelper_ob.UNITS, units);
        opnToWrite();

        long val = database_ob.insert(dbHelper_ob.TABLE_NAME, null, contentValues);
        Close();
        return val;
    }

    public Cursor queryName(){
        String[] cols = {dbHelper_ob.KEY_ID, dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE, dbHelper_ob.WATTAGE, dbHelper_ob.UNITS};
        opnToWrite();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, cols, null, null, null, null, null);

        return c;
    }

    public Cursor queryAll(int nameId){
        String[] cols = {dbHelper_ob.KEY_ID, dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE, dbHelper_ob.WATTAGE, dbHelper_ob.UNITS};
        opnToWrite();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, cols, dbHelper_ob.KEY_ID + "=" + nameId,
                null, null, null, null);

        return c;
    }

    public Boolean checkEmpty(){

        opnToRead();
        Cursor mCursor = database_ob.rawQuery("SELECT * FROM " + dbHelper_ob.DATABASE_NAME, null);
        Boolean rowExists;

        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else {
            // I AM EMPTY
            rowExists = false;
        }

        return rowExists;
    }

    public String getRowTotal(){
        String[] column =
                new String[]{"sum(WATTAGE * USAGE * UNITS) as " + dbHelper_ob.ROW_TOTAL };
        opnToRead();
        Cursor c =
                database_ob.query(dbHelper_ob.TABLE_NAME, column, null, null, null, null, null);

        String result = "";
        int rowIndex = c.getColumnIndex(dbHelper_ob.ROW_TOTAL);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext() ){
            result = result + c.getString( rowIndex );

        }
        return result;
    }

//    public String getCompWithMostLoad(){
//        String[] column =
//                new String[]{"SELECT WATTAGE as " + dbHelper_ob.ROW_TOTAL };
//        opnToRead();
//        Cursor c =
//                database_ob.query(dbHelper_ob.TABLE_NAME, column, null, null, null, null, null);
//
//        int result = 0;
//        int rowIndex = c.getColumnIndex(dbHelper_ob.WATTAGE);
//        String compRowIndex = "";
//        int rowvalue = c.getInt(c.getColumnIndex(dbHelper_ob.WATTAGE));
//
//        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext() ){
//            if(rowvalue > result){
//                result = c.getInt(c.getColumnIndex(dbHelper_ob.WATTAGE));
//            }
//
//            compRowIndex = c.getString(c.getColumnIndex(result));
//        }
//        return compRowIndex;
//    }

    public String getTotalWattage(){
        String[] column =
                new String[]{"sum(WATTAGE) as " + dbHelper_ob.WATTAGE };
        opnToRead();
        Cursor c =
                database_ob.query( dbHelper_ob.TABLE_NAME, column, null, null, null, null, null );


        String result = "";
        int wattageIndex = c.getColumnIndex(dbHelper_ob.WATTAGE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext() ){
            result = result + c.getString( wattageIndex );
        }
        return result;
    }

    public double calculateTotalBill(){
        double sumTotal;

        double bill;
        double rate;

        sumTotal = Double.parseDouble(getRowTotal());
        rate = getRate();

        bill = (((sumTotal/1000) * rate) * 30) + 3;
        return bill;
    }

    public double calculateTotalKW(){
        int watt;
        double total;

        watt = Integer.parseInt(getTotalWattage());

        rate = getRate();

        total = (watt/1000);
        return total;
    }

    public void setRate(double rate){
        this.rate = rate;
    }

    public double getRate(){
        return rate;
    }



    public long updateldetail(int rowId, String cname, double usage, int wattage, int units){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper_ob.COMPONENT_NAME, cname);
        contentValues.put(dbHelper_ob.USAGE, usage);
        contentValues.put(dbHelper_ob.WATTAGE, wattage);
        contentValues.put(dbHelper_ob.UNITS, units);
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
