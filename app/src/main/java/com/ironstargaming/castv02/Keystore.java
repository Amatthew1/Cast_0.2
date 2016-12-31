package com.ironstargaming.castv02;

/**
 * Created by Admin on 12/29/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Keystore {
    private static Keystore store;
    private SharedPreferences SP;
    private static String filename="Keys";

    private Keystore(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename,0);
    }

    public static Keystore getInstance(Context context) {
        if (store == null) {
            Log.v("Keystore","NEW STORE");
            store = new Keystore(context);
        }
        return store;
    }

    public void put(String key, String value) {//Log.v("Keystore","PUT "+key+" "+value);
        Editor editor;

        editor = SP.edit();
        editor.putString(key, value);
        editor.commit(); // Stop everything and do an immediate save!
        // editor.apply();//Keep going and save when you are not busy - Available only in APIs 9 and above.  This is the preferred way of saving.
    }

    public String get(String key) {//Log.v("Keystore","GET from "+key);
        return SP.getString(key, null);

    }

    public int getInt(String key) {//Log.v("Keystore","GET INT from "+key);
        return SP.getInt(key, 0);
    }

    public void putInt(String key, int num) {//Log.v("Keystore","PUT INT "+key+" "+String.valueOf(num));
        Editor editor;
        editor = SP.edit();

        editor.putInt(key, num);
        editor.commit();
    }

    public boolean getBool(String key) {//Log.v("Keystore","GET INT from "+key);
        return SP.getBoolean(key, false);
    }

    public void putBool(String key, Boolean bool) {//Log.v("Keystore","PUT INT "+key+" "+String.valueOf(num));
        Editor editor;
        editor = SP.edit();

        editor.putBoolean(key, bool);
        editor.commit();
    }

    public void clear(){
        Editor editor;
        editor = SP.edit();

        editor.clear();
        editor.commit();
    }

    public void remove(){
        Editor editor;
        editor = SP.edit();

        editor.remove(filename);
        editor.commit();
    }
}