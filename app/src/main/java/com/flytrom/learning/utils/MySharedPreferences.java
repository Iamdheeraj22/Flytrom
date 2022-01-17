package com.flytrom.learning.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    public static  String PREF_NAME = "catanco_database";
    private static volatile MySharedPreferences newInstance = new MySharedPreferences();

    //private constructor.
    private MySharedPreferences(){}

    public static MySharedPreferences getInstance() {
        if (newInstance == null){
            newInstance = new MySharedPreferences();
        }
        return newInstance;
    }

    public void saveString(Context context,String key,String value){
        SharedPreferences sharedPref = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(Context context,String key){
        SharedPreferences sharedPref = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        String res = sharedPref.getString(key, "");
        return res;
    }

    public void clearData(Context context){
        SharedPreferences preferences =context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

    }

}
