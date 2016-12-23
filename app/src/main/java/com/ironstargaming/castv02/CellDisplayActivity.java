package com.ironstargaming.castv02;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ironstargaming.castv02.data.CellContract.CellEntry;

public class CellDisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int CELL_LOADER_ID =0;
    CellCursorAdapter mCursorAdapter;
    private Uri mCurrentCellUri;
    private int mParentCell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_display);

        //////////////////////////////////////
        Intent intent = getIntent();
        mParentCell = intent.getIntExtra("parent_cell", 0);
        FloatingActionButton add_cell_fab = (FloatingActionButton) findViewById(R.id.cell_fab);
        add_cell_fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellDisplayActivity.this, CellEditorActivity.class);
                intent.putExtra("parent_cell", mParentCell);
                startActivity(intent);
            }
        });
        /////////////////////////////////////
            ListView CellListView = (ListView) findViewById(R.id.list);
            View emptyView = findViewById(R.id.empty_view);
            CellListView.setEmptyView(emptyView);

        mCursorAdapter= new CellCursorAdapter(this,null);
        CellListView.setAdapter(mCursorAdapter);
        CellListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CellDisplayActivity.this, CellDisplayActivity.class);
                Uri currentCellUri = ContentUris.withAppendedId(CellEntry.CONTENT_URI,id);
                intent.setData(currentCellUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(CELL_LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CellEntry._ID,
                CellEntry.COLUMN_CELL_NAME,
                CellEntry.COLUMN_PARENT_CELL,
                CellEntry.COLUMN_CELL_SCOPE
        };
        String selection = CellEntry.COLUMN_PARENT_CELL + "=?";
        String[]  selectionArgs = new String[]{String.valueOf(mParentCell)};
        return new CursorLoader(this, CellEntry.CONTENT_URI, projection, selection, selectionArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    mCursorAdapter.swapCursor(null);
    }
}
