package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private  SharedPreferences sharedPreferences;

    public PreferenceManager( Context context, String type ) {
        switch (type)
        {
            case "chatAppPreference":
                sharedPreferences = context.getSharedPreferences("chatAppPreference", Context.MODE_PRIVATE );
                break;
            case "User":
                sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE );
                break;
        }
    }

    public void putBoolean( String key, Boolean value ) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
    }

    public Boolean getBoolean( String key ) {
        return sharedPreferences.getBoolean( key, false );
    }

    public void putString( String key, String value ) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( key, value );
        editor.apply();
    }

    public String getString( String key ) {
        return sharedPreferences.getString( key, null );
    }

    public  void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
