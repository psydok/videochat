package ru.csu.videochat.model.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ru.csu.videochat.model.utilities.Constants;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putStringArray(String key, String[] array) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putString(key + "_" + i, array[i]);
        editor.apply();
    }

    public String[] getStringArray(String key) {
        int size = sharedPreferences.getInt(key + "_size", 0);
        String[] array = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = sharedPreferences.getString(key + "_" + i, null);
        return array;
    }

    public void cleanPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
