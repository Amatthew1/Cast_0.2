package com.ironstargaming.castv02;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ironstargaming.castv02.data.CellContract.CellEntry;
import com.ironstargaming.castv02.data.CellDatabaseHelp;

import java.util.ArrayList;

public class CellDisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Keystore store;
    private static final int CELL_LOADER_ID = 0;
    CellCursorAdapter mCursorAdapter;
    private int mParentCell;
    private String mParentCellName;
    private int mCurrentCell;
    private String mCurrentCellName;
    TextView cell_ID_Text;
    TextView cell_Name_Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_display);

        ////////////////////initialization/////////////////////
        store = Keystore.getInstance(getApplicationContext());



        //////////////////////////////////////
        Intent intent = getIntent();
        Log.v("THIS IS WHERE LOGS ARE", "LOG");
        mCurrentCell = intent.getIntExtra("parent_cell", 0);
        mCurrentCellName = intent.getStringExtra("parent_cell_name");
        cell_ID_Text=(TextView)findViewById(R.id.cell_id);
        cell_ID_Text.setText(String.valueOf(mCurrentCell));
        cell_Name_Text =(TextView) findViewById(R.id.cell_name);
        cell_Name_Text.setText(mCurrentCellName);
        if(mCurrentCell==0){
            cell_Name_Text.setText("This is the Root Cell");
        }

        ////////////////////////////////add cell fab///////////////////////////////////////////////////////////////
        FloatingActionButton add_cell_fab = (FloatingActionButton) findViewById(R.id.add_cell_fab);
        add_cell_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellDisplayActivity.this, CellEditorActivity.class);
                intent.putExtra("parent_cell", mCurrentCell);
                Toast.makeText(getApplicationContext(),String.valueOf(mCurrentCell),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        /////////////////////////////////edit cell fab////////////////////////////////////////////////////////////
        FloatingActionButton edit_cell_fab = (FloatingActionButton) findViewById(R.id.edit_cell_fab);
        edit_cell_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellDisplayActivity.this, CellEditorActivity.class);
                intent.setData(ContentUris.withAppendedId(CellEntry.CONTENT_URI, mCurrentCell));
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////
        if (store.getBool("isStoryTeller")) {
            add_cell_fab.setVisibility(View.VISIBLE);
            edit_cell_fab.setVisibility(View.VISIBLE);
        } else{/*show player specific items*/}
        /////////////////////////////////////
        ListView CellListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        CellListView.setEmptyView(emptyView);

        mCursorAdapter = new CellCursorAdapter(this, null);
        CellListView.setAdapter(mCursorAdapter);
        CellListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("long id", String.valueOf(id));
                String[] projection= {
                        CellEntry._ID,
                        CellEntry.COLUMN_CELL_NAME
                };
                String selection = CellEntry._ID + "=?";
                String[]  selectionArgs = new String[]{String.valueOf(id)};

                CellDatabaseHelp mDBHelper;
                mDBHelper = new CellDatabaseHelp(getApplicationContext());
                SQLiteDatabase database = mDBHelper.getReadableDatabase();
                Cursor subCursor = database.query(CellEntry.TABLE_NAME, projection,selection,selectionArgs,null,null, null);
                subCursor.moveToFirst();
                mParentCell=subCursor.getInt(subCursor.getColumnIndex(CellEntry._ID));
                mParentCellName=subCursor.getString(subCursor.getColumnIndex(CellEntry.COLUMN_CELL_NAME));
                subCursor.close();
                Intent intent = new Intent(CellDisplayActivity.this, CellDisplayActivity.class);
                intent.putExtra("parent_cell", mParentCell);
                intent.putExtra("parent_cell_name",mParentCellName);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(CELL_LOADER_ID, null, this);
    }

    private void insertTestData() {/*to do*/}

    private void deleteAllCells() {
        int rowsDeleted = getContentResolver().delete(CellEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_test_data:
                insertTestData();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllCells();
                return true;
            case R.id.action_launch_options_menu:
                Intent intent = new Intent(CellDisplayActivity.this, OptionsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CellEntry._ID,
                CellEntry.COLUMN_CELL_NAME,
                CellEntry.COLUMN_PARENT_CELL,
                CellEntry.COLUMN_CELL_VISIBILITY
        };
        String selection = CellEntry.COLUMN_PARENT_CELL + "=?";
        ArrayList<String> selectionArgsList= new ArrayList<>();
        selectionArgsList.add(String.valueOf(mCurrentCell));
        if(!store.getBool("isStoryTeller")){
            Toast.makeText(getApplicationContext(), String.valueOf(store.getBool("isStoryTeller")),Toast.LENGTH_SHORT).show();
            selection = selection + " AND (" + CellEntry.COLUMN_CELL_VISIBILITY + "=+1 OR " + CellEntry.COLUMN_CELL_VISIBILITY + "=2)";
        }
         String[] selectionArgs= new String[selectionArgsList.size()];
        selectionArgsList.toArray(selectionArgs);


        return new CursorLoader(this, CellEntry.CONTENT_URI, projection, selection, selectionArgs, null);
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
