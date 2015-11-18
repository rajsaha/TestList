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

    //Method for Inserting Details
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

    //Method for querying Component Name
    public Cursor queryName(){
        String[] cols = {dbHelper_ob.KEY_ID, dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE, dbHelper_ob.WATTAGE, dbHelper_ob.UNITS};
        opnToWrite();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, cols, null, null, null, null, null);

        return c;
    }

    //Method for getting all data from database
    public Cursor queryAll(int nameId){
        String[] cols = {dbHelper_ob.KEY_ID, dbHelper_ob.COMPONENT_NAME,
                dbHelper_ob.USAGE, dbHelper_ob.WATTAGE, dbHelper_ob.UNITS};
        opnToWrite();
        Cursor c = database_ob.query(dbHelper_ob.TABLE_NAME, cols, dbHelper_ob.KEY_ID + "=" + nameId,
                null, null, null, null);

        return c;
    }

    //Method for checking if database is empty
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

    //Method for getting the total bill by summing wattage usage and units
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

    //Method for getting the component with Highest Power Usage
    public String getHighestLoad(){
//        opnToRead();
//        Cursor mCursor = database_ob.rawQuery("SELECT C_NAME FROM " + dbHelper_ob.TABLE_NAME + " WHERE WATTAGE = ( SELECT MAX(WATTAGE) FROM " + dbHelper_ob.TABLE_NAME + ");", null);
//        int index = mCursor.getColumnIndex(dbHelper_ob.COMPONENT_NAME);
//        String result = mCursor.getString(index);
//
//        return result;
        String[] column =
                new String[]{"C_NAME FROM COMPONENTS_TABLE WHERE WATTAGE  = (SELECT MAX (WATTAGE) " };
        opnToRead();
        Cursor c =
                database_ob.query( "COMPONENTS_TABLE)", column, null, null, null, null, null );


        String result = "";
        int wattageIndex = c.getColumnIndex(dbHelper_ob.COMPONENT_NAME);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext() ){
            result = result + c.getString( wattageIndex );
        }
        return result;
    }

    public String getCompWithHighLoad(){
        String component = getHighestLoad();
        return component;
    }

    //Method for acquiring total power consumed
    public String getTotalWattage(){
        String[] column =
                new String[]{"sum(WATTAGE * UNITS) as " + dbHelper_ob.WATTAGE };
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

    //Method for calcuting total bill using ROW_TOTAL & rate
    public double calculateTotalBill(){
        double sumTotal;

        double bill;
        double rate;

        sumTotal = Double.parseDouble(getRowTotal());
        rate = getRate();

        bill = (((sumTotal/1000) * rate) * 30) + 3;
        return bill;
    }

    //Method for returning total watt consumed
    public double calculateTotalKW(){
        double watt;
        double total;

        watt = Integer.parseInt(getTotalWattage());

        //total = (watt/1000);
        return watt;
    }

    //Getter and Setter methods for Rate
    public void setRate(double rate){
        this.rate = rate;
    }
    public double getRate(){
        return rate;
    }



    //Method for updating detail when in the Edit Page
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

    //Method for Deleting Records
    public int deleteOneRecord(int rowId){
        opnToWrite();
        int val = database_ob.delete(dbHelper_ob.TABLE_NAME, dbHelper_ob.KEY_ID
                + "=" + rowId, null);
        Close();
        return val;
    }
}
