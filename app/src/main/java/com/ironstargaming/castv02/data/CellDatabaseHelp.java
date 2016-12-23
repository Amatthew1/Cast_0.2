package com.ironstargaming.castv02.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ironstargaming.castv02.data.CellContract.CellEntry;

/**
 * Created by Admin on 12/21/2016.
 */

public class CellDatabaseHelp extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cell_database.db";
    private static final int DATABASE_VERSION =1;

    public CellDatabaseHelp(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase cellDatabase) {
        String SQL_CREATE_CELLS_TABLE = "CREATE TABLE "
                + CellEntry.TABLE_NAME + "("
                + CellEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CellEntry.COLUMN_CELL_NAME + " TEXT NOT NULL,"
                + CellEntry.COLUMN_CELL_SCOPE + " INTEGER NOT NULL,"
                + CellEntry.COLUMN_PARENT_CELL + " INTEGER NOT NULL);";
        cellDatabase.execSQL(SQL_CREATE_CELLS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
