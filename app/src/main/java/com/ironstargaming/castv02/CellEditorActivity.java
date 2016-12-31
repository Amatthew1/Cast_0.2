package com.ironstargaming.castv02;

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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.ironstargaming.castv02.data.CellContract.CellEntry;

/**
 * Created by Admin on 12/21/2016.
 */

public class CellEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_CELL_LOADER = 1;
    private EditText mCellNameEditText;
    private RadioGroup mCellVisibilityRG;
    private TextView mParentCellTextView;

    private Uri mCurrentCellUri;
    private int mParentCell;

    private Keystore store;

    private boolean mCellHasChanged = false;
    private View.OnTouchListener mTouchListner = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mCellHasChanged=true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_editor);
        Intent intent = getIntent();
        mCurrentCellUri = intent.getData();
        mParentCell = intent.getIntExtra("parent_cell", 0);
        Log.v("Parent Cell", String.valueOf(mParentCell));
        if(mCurrentCellUri == null){
            setTitle("Add New Cell");
        }else{
            setTitle("Edit Existing Cell");
            getLoaderManager().initLoader(EXISTING_CELL_LOADER,null, this);
        }
        mCellNameEditText = (EditText) findViewById(R.id.cell_name_edittext);
        mParentCellTextView = (TextView) findViewById(R.id.parent_cell_display);
        mCellVisibilityRG = (RadioGroup) findViewById(R.id.radio_vis_group);
        store = Keystore.getInstance(getApplicationContext());


        mCellNameEditText.setOnTouchListener(mTouchListner);

    }

    private void saveCell(){
        String cellNameString = mCellNameEditText.getText().toString().trim();
        Integer cellVisRG_selection = mCellVisibilityRG.getCheckedRadioButtonId();
        Integer cellVisInte;
        switch (cellVisRG_selection){
            case R.id.radio_vis_hidden:
                cellVisInte =0;
                break;
            case R.id.radio_vis_obscure:
                cellVisInte = 1;
                break;
            case R.id.radio_vis_visible:
                cellVisInte = 2;
                break;
            default:
                cellVisInte = 2;
                break;
        }
        Integer parentCellInte = mParentCell;

        if(mCurrentCellUri==null &&
                (TextUtils.isEmpty(cellNameString)||cellVisInte==null)){
            Toast.makeText(getApplicationContext(),"Fill in the rest of the cell, dick", Toast.LENGTH_SHORT);
            return;
        }
        ContentValues values = new ContentValues();

        values.put(CellEntry.COLUMN_CELL_NAME, cellNameString);

        values.put(CellEntry.COLUMN_PARENT_CELL, parentCellInte);

        values.put(CellEntry.COLUMN_CELL_VISIBILITY, cellVisInte);


        if(mCurrentCellUri == null){
            Uri newRowUri = getContentResolver().insert(CellEntry.CONTENT_URI,values);
        } else{
            int newUri = getContentResolver().update(mCurrentCellUri,values,null,null);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mCurrentCellUri==null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveCell();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_move:
                //do nothing
                return true;
            case R.id.home:
                if(!mCellHasChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(CellEditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("UNSAVED CHANGES DAWG!");
        builder.setPositiveButton("discard", discardButtonClickListener);
        builder.setNegativeButton("keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        if (!mCellHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Dude...delete this?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteCell();
            }
        });
        builder.setNegativeButton("NAH", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteCell() {
        if (mCurrentCellUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentCellUri, null, null);
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection= {
                CellEntry._ID,
                CellEntry.COLUMN_CELL_NAME,
                CellEntry.COLUMN_PARENT_CELL,
              //  CellEntry.COLUMN_CELL_SCOPE
                };
                return new CursorLoader(this, mCurrentCellUri, projection, null, null,null);
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            mCellNameEditText.setText(cursor.getString(cursor.getColumnIndex(CellEntry.COLUMN_CELL_NAME)));
            mParentCellTextView.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_PARENT_CELL))));
        //    mCellScopeEditText.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(CellEntry.COLUMN_CELL_SCOPE))));
            //TO DO: SET SELECTION ON VISBILITY RADIO BUTTON
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCellNameEditText.setText("");
     //   mCellScopeEditText.setText("");
    }
}

