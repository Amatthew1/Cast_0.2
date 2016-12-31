package com.ironstargaming.castv02;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ironstargaming.castv02.data.CellContract;

/**
 * Created by Admin on 12/22/2016.
 */

public class SubCellCursorAdapter extends CursorAdapter{
    protected ListView mListView;
    public SubCellCursorAdapter(Context context, Cursor cursor){super (context,cursor,0);}

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("LOOOOOOOG","DURING");
        TextView subCellTextView = (TextView) view.findViewById(R.id.sub_cell_name_text);
        if(cursor.getString(cursor.getColumnIndex(CellContract.CellEntry.COLUMN_CELL_NAME))==null){
            Log.d("IS IS NULL?","YES");
        }
        String subCellName = cursor.getString(cursor.getColumnIndex(CellContract.CellEntry.COLUMN_CELL_NAME));
        subCellTextView.setText(subCellName);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("LOOOOOOOG","DURING");
        return LayoutInflater.from(context).inflate(R.layout.sub_cell_list,parent,false);

    }
}
