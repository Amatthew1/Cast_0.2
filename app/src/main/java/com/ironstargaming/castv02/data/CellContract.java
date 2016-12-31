package com.ironstargaming.castv02.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Admin on 12/21/2016.
 */

public class CellContract {
    public static final String CONTENT_AUTHORITY = "com." + "ironstargaming.castv0.2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CELLS = "cells";

    private CellContract() {//no implementation in 0.2 build as nothing should be calling this class}
    }

    public static final class CellEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CELLS);

        //MIME TYPE for full list of cells
        // GONNA NEED TO DO SOME WORK HERE
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CELLS;
        //MIME TYPE for a single cell
        // is this suitable if child of table is a table itself? i doubt it

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CELLS;

        public static final String TABLE_NAME = "cells";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CELL_NAME= "cell_name";
        public final static String COLUMN_PARENT_CELL= "parent_cell";
        public final static String COLUMN_CELL_VISIBILITY= "cell_visibility";
        //public static final String COLUMN_CELL_SCOPE= "cell_scope";

        public static final int SCALE_M_TWO = -2; //items
        public static final int SCALE_M_ONE = -1; //body parts, armor, backpacks
        public static final int BASE_SCALE = 0; //characters, containers, NPCs, Shops
        public static final int SCALE_P_ONE = 1; // rooms, small terrain features, vehicles, large creatures
        public static final int SCALE_P_TWO = 2; //buildings, large terrain features, large vehicles, massive creatures
        public static final int SCALE_P_THREE = 3; //neighborhoods, collection of similar large terrain features
        public static final int SCALE_P_FOUR = 4;// cities, small lakes and forests
        public static final int SCALE_P_FIVE = 5;// geographic areas, the Nth scope of this build

        public static final int VISIBLE =2;
        public static final int OBSCURE =1;
        public static final int HIDDEN =0;




    }
}
