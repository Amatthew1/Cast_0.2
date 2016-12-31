package com.ironstargaming.castv02.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;

import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.util.Log;
import android.widget.Toast;

import com.ironstargaming.castv02.data.CellContract.CellEntry;
/**
 * Created by Admin on 12/21/2016.
 */

public class CellProvider extends ContentProvider{

    private static final int CELLS = 001;
    private static final int CELL_ID= 010;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CellContract.CONTENT_AUTHORITY, CellContract.PATH_CELLS,CELLS);
        sUriMatcher.addURI(CellContract.CONTENT_AUTHORITY, CellContract.PATH_CELLS + "/#", CELL_ID);
    }
    private CellDatabaseHelp mDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDBHelper = new CellDatabaseHelp(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case CELLS:
               // selection = CellEntry.COLUMN_PARENT_CELL + "=?";
              //  selectionArgs = new String[]{String.valueOf(0)};
                cursor = database.query(CellEntry.TABLE_NAME, projection,selection,selectionArgs,null,null, sortOrder);
                        break;
            case CELL_ID:
                selection = CellEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(CellEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query URI: "+ uri +", It may be malformed");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CELLS:
                return  CellEntry.CONTENT_LIST_TYPE;
            case CELL_ID:
                return CellEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI: " + uri + ", with match: " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CELLS:
                return insertCell(uri,values);
            default:
                throw new IllegalArgumentException("This uri: " + uri + " cannot be inserted");
        }
    }

    private Uri insertCell(Uri uri, ContentValues values){
        String cell_name = values.getAsString(CellEntry.COLUMN_CELL_NAME);
        if(cell_name==null){throw new IllegalArgumentException("Cell requires a name");}

        Integer parent_cell = values.getAsInteger(CellEntry.COLUMN_PARENT_CELL);
        Log.v("Provider", "parent_cell: " + parent_cell);
        if (parent_cell==null){throw new IllegalArgumentException("Cell requires a parent");}

        Integer cell_visibility = values.getAsInteger(CellEntry.COLUMN_CELL_VISIBILITY);
        if((cell_visibility<0 || cell_visibility>2)&& cell_visibility!=null){throw new IllegalArgumentException("Cell visibility incorrect @provider @insert");}

       // Integer cell_scope = values.getAsInteger(CellEntry.COLUMN_CELL_SCOPE);
       // Log.v("Provider", "cell_scope: " + cell_scope);
      //  if(cell_scope==null){throw new IllegalArgumentException("Cell scope incorrect @ provider");}

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(CellEntry.TABLE_NAME, null, values);
        if (id==-1){Toast.makeText(getContext(),"Error inserting cell @insertCell",Toast.LENGTH_LONG).show();}

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CELLS:
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(CellEntry.TABLE_NAME, selection, selectionArgs);
            case CELL_ID:
                selection = CellEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(CellEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("This uri:" + uri + ", cannot be deleted @ delete");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CELLS:
                return updateCell(uri, values, selection,selectionArgs);
            case CELL_ID:
                selection = CellEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCell(uri, values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update cannot be complete with this uri: " + uri + ", @update @provider");
        }
    }

    private int updateCell(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if(values.size()==0){return 0;}
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        if(values.containsKey(CellEntry.COLUMN_CELL_NAME)){
            String cell_name =values.getAsString(CellEntry.COLUMN_CELL_NAME);
            if(cell_name==null){throw new IllegalArgumentException("cell requires a name @update");}
        }
        if (values.containsKey(CellEntry.COLUMN_PARENT_CELL)){
            Integer parent_cell=values.getAsInteger(CellEntry.COLUMN_PARENT_CELL);
            if(parent_cell==null){throw new IllegalArgumentException("cell requires a parent @update");}
        }
     //   if(values.containsKey(CellEntry.COLUMN_CELL_VISIBILITY)){
     //       Integer cell_visibility=values.getAsInteger(CellEntry.COLUMN_CELL_VISIBILITY);
     //       if((cell_visibility<0 || cell_visibility>2)&& cell_visibility!=null){throw new IllegalArgumentException("Cell visibility incorrect @provider @update");}
      //  }

       // if (values.containsKey(CellEntry.COLUMN_CELL_SCOPE)){
      //      Integer cell_scope = values.getAsInteger(CellEntry.COLUMN_CELL_SCOPE);
      //      if(cell_scope==null||cell_scope<-2||cell_scope>5){throw new IllegalArgumentException("error with cell scope @update @provider");}
      //  }
    getContext().getContentResolver().notifyChange(uri,null);
        return database.update(CellEntry.TABLE_NAME, values,selection,selectionArgs);

    }
}
