package ru.csu.videochat.model.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;

import java.util.ArrayList;

import ru.csu.videochat.R;
import ru.csu.videochat.model.utilities.Constants;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private final Context context;

    public PreferenceManager(Context context) {
        this.context = context;
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
        editor.putString(key, getKeyIfAge(value));
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    private String getKeyIfAge(String key) {
        if (key.equals(context.getString(R.string.age_17))) {
            return "age17";
        }
        if (key.equals(context.getString(R.string.age_18_21))) {
            return "age21";
        }
        if (key.equals(context.getString(R.string.age_22_25))) {
            return "age25";
        }
        if (key.equals(context.getString(R.string.age_26_30))) {
            return "age30";
        }
        if (key.equals(context.getString(R.string.age_31))) {
            return "age31";
        }

        return key;
    }

    public static String getValueIfAge(Context context, String key) {
        if (key == null)
            return null;

        if (key.equals("age17")) {
            return context.getString(R.string.age_17);
        }
        if (key.equals("age21")) {
            return context.getString(R.string.age_18_21);
        }
        if (key.equals("age25")) {
            return context.getString(R.string.age_22_25);
        }
        if (key.equals("age30")) {
            return context.getString(R.string.age_26_30);
        }
        if (key.equals("age31")) {
            return context.getString(R.string.age_31);
        }

        return key;
    }

    public void putStringArray(String key, String[] array) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putString(key + "_" + i, getKeyIfAge(array[i]));
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

    public static void putServerToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME_TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_TOKEN, null);
    }

    public static void putAvatar(Context context, String uriAvatar) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME_AVATAR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_AVATAR, uriAvatar);
        editor.apply();
    }

    public static Uri getAvatar(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME_AVATAR, Context.MODE_PRIVATE);
        String avatar = sharedPreferences.getString(Constants.KEY_AVATAR, null);
        if (avatar != null) {
            return Uri.parse(avatar);
        }
        return null;
    }

}
