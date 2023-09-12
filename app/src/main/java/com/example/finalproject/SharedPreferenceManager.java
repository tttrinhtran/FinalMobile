package com.example.finalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreferenceManager<T> {
    final private Class<T> type;
    Context context;
    SharedPreferences sharedPreference;

    public SharedPreferenceManager(Class<T> type, Context context) {
        this.type = type;
        this.context = context;
    }

    public boolean storeSerializableObjectToSharedPreference(T object, String key){
        sharedPreference = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(key, json);
//        editor.commit();
//        editor.clear();
        editor.apply();
        return true;
    }

    public T retrieveSerializableObjectFromSharedPreference(String key){
        sharedPreference = context.getSharedPreferences(key, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreference.getString(key, null);
        if (json == null) return null;
        return gson.fromJson(json, type);
    }


}
