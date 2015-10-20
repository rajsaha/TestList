package com.example.getright_malaysia.testlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "COMPONENTS_DB";
    public static final String TABLE_NAME = "COMPONENTS_TABLE";

    //Database Version
    public static final int VERSION = 5;

    public static final String KEY_ID = "_id";
    public static final String COMPONENT_NAME = "C_NAME";
    public static final String USAGE = "USAGE";
    public static final String WATTAGE = "WATTAGE";
    public static final String UNITS = "UNITS";
    public static final String ROW_TOTAL = "ROW_TOTAL";

    public static final String SCRIPT = "create table " + TABLE_NAME
            + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + COMPONENT_NAME + " text not null, "
                + USAGE + " integer not null,"
                + WATTAGE + " integer not null,"
                + UNITS + " integer not null,"
                + ROW_TOTAL + " integer " +
            ");";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
