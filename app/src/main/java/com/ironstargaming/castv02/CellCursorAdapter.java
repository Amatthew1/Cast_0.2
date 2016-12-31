package com.ironstargaming.castv02;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.ironstargaming.castv02.data.CellContract.CellEntry;
import com.ironstargaming.castv02.data.CellDatabaseHelp;
import com.ironstargaming.castv02.data.CellProvider;
//import com.ironstargaming.castv02.data.CellProvider.query;


/**
 * Created by Admin on 12/21/2016.
 */

public class CellCursorAdapter extends CursorAdapter /* implements LoaderManager.LoaderCallbacks<Cursor> */ {

    Context cellContext; // declared to resolve a matter of scope
    int mParentCell; // declared to resolve a matter of scope
    private Keystore store;
    TextView subCell1;
    TextView subCell2;
    TextView subCell3;
    TextView subCell4;
    TextView subCell5;
    TextView subCell6;
    TextView subCell7;


    public CellCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        //cellContext = context;

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        store = Keystore.getInstance(context);
        TextView cellNameTextView = (TextView) view.findViewById(R.id.cell_name_text);
        //TextView parentCellTextView = (TextView) view.findViewById(R.id.parent_cell_text);
        //TextView cellIDText = (TextView) view.findViewById(R.id.cell_id);

        subCell1 = (TextView) view.findViewById(R.id.sub_cell_1);
        subCell2 = (TextView) view.findViewById(R.id.sub_cell_2);
        subCell3 = (TextView) view.findViewById(R.id.sub_cell_3);
        subCell4 = (TextView) view.findViewById(R.id.sub_cell_4);
        subCell5 = (TextView) view.findViewById(R.id.sub_cell_5);
        subCell6 = (TextView) view.findViewById(R.id.sub_cell_6);
        subCell7 = (TextView) view.findViewById(R.id.sub_cell_7);
        final int parentCell = cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_PARENT_CELL));
        final int cellID = cursor.getInt(cursor.getColumnIndex(CellEntry._ID));

        // take current cell cellID to use as selection for subCells
        mParentCell = cellID;


        String cellName = cursor.getString(cursor.getColumnIndex(CellEntry.COLUMN_CELL_NAME));
        int cellIDint = cursor.getInt(cursor.getColumnIndex(CellEntry._ID));
        String cellIDstring = String.valueOf(cellIDint);
        //cellIDText.setText(cellIDstring);
        cellNameTextView.setText(cellName);
        //parentCellTextView.setText(String.valueOf(parentCell));

        Cursor subCursor;

        String[] projection = {
                CellEntry._ID
                , CellEntry.COLUMN_CELL_NAME
                , CellEntry.COLUMN_CELL_VISIBILITY

        };
        String selection = CellEntry.COLUMN_PARENT_CELL + "=?";
        String[] selectionArgs = new String[]{String.valueOf(mParentCell)};

        CellDatabaseHelp mDBHelper;
        mDBHelper = new CellDatabaseHelp(context);
        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        subCursor = database.query(CellEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if(cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_CELL_VISIBILITY))!=CellEntry.OBSCURE
                ||store.getBool("isStoryteller")) {
            tryIt(subCell1, subCursor);
            tryIt(subCell2, subCursor);
            tryIt(subCell3, subCursor);
            tryIt(subCell4, subCursor);
            tryIt(subCell5, subCursor);
            tryIt(subCell6, subCursor);
            tryIt(subCell7, subCursor);
        }
        subCursor.close();

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.cell_list, parent, false);
    }

    private void tryIt(TextView textView, Cursor cursor) {

        if (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_CELL_VISIBILITY)) != CellEntry.HIDDEN
                    || store.getBool("isStoryTeller"))

            {
                textView.setText(cursor.getString(cursor.getColumnIndex(CellEntry.COLUMN_CELL_NAME)));
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }

        }

    }
}