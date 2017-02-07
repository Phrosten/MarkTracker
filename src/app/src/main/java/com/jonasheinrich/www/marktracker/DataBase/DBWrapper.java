package com.jonasheinrich.www.marktracker.DataBase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBWrapper extends SQLiteOpenHelper
{
    public static final String DBNAME = "marktracker.db";
    public static final int VERSION = 1;

    public DBWrapper(Context context)
    {
        super(context, DBNAME, null, VERSION);
    }
    public void onCreate(SQLiteDatabase db)
    {

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}
