package com.ironstargaming.castv02;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ironstargaming.castv02.data.CellContract.CellEntry;

/**
 * Created by Admin on 12/21/2016.
 */

public class CellCursorAdapter extends CursorAdapter {

    protected ListView mListView;//I think this is for the viewholer

    SubCellCursorAdapter mSubCursorAdapter;

    public CellCursorAdapter(Context context, Cursor cursor){super(context,cursor,0);}

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView cellNameTextView = (TextView) view.findViewById(R.id.cell_name_text);
        TextView parentCellTextView = (TextView) view.findViewById(R.id.parent_cell_text);
        TextView cellScopeTextView = (TextView) view.findViewById(R.id.cell_scope_text);
        TextView cellIDTextView = (TextView) view.findViewById(R.id.cell_id_text);

        final int parentCell = cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_PARENT_CELL));
        final int cellID = cursor.getInt(cursor.getColumnIndex(CellEntry._ID));

        Button enterCellbutton = (Button) view.findViewById(R.id.enter_cell_button);
        enterCellbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CellDisplayActivity.class);
                // parent cell is current cell
                intent.putExtra("parent_cell", cellID);
                context.startActivity(intent);
            }
        });



        String cellName = cursor.getString(cursor.getColumnIndex(CellEntry.COLUMN_CELL_NAME));

        int cellScope = cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_CELL_SCOPE));

        cellNameTextView.setText(cellName);
        cellIDTextView.setText(String.valueOf(cellID));
        cellScopeTextView.setText(String.valueOf(cellScope));
        parentCellTextView.setText(String.valueOf(parentCell));

        Log.d("LOOOOOOOOOOOOOG", "BEFORE");
        ListView subCellListView =(ListView) view.findViewById(R.id.sub_list);
        mSubCursorAdapter = new SubCellCursorAdapter(context,null);
        if(mSubCursorAdapter==null){Log.d("LOOOOOOOOOOOOOG", "AFTER");}
        subCellListView.setAdapter(mSubCursorAdapter);
        Log.d("LOOOOOOOOOOOOOG", "AFTER");



    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.cell_list,parent,false);
    }
}
