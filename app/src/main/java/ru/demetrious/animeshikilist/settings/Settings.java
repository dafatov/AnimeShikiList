package ru.demetrious.animeshikilist.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Map;

import ru.demetrious.animeshikilist.activities.MainActivity;

public class Settings {
    public static final String DATA_SORT_CATEGORY = "ru.demetrious.animeshikilist.data.sort.category";
    public static final String DATA_SORT_DIRECTION = "ru.demetrious.animeshikilist.data.sort.direction";
    public static final String DATA_SORT_SEPARATE = "ru.demetrious.animeshikilist.data.sort.separate";

    public static final String DATA_FILTER_PLANNED = "ru.demetrious.animeshikilist.data.filter.planned";
    public static final String DATA_FILTER_WATCHING = "ru.demetrious.animeshikilist.data.filter.watching";
    public static final String DATA_FILTER_REWATCHING = "ru.demetrious.animeshikilist.data.filter.rewatching";
    public static final String DATA_FILTER_COMPLETED = "ru.demetrious.animeshikilist.data.filter.completed";
    public static final String DATA_FILTER_ONHOLD = "ru.demetrious.animeshikilist.data.filter.onhold";
    public static final String DATA_FILTER_DROPPED = "ru.demetrious.animeshikilist.data.filter.dropped";
    public static final String DATA_FILTER_TV = "ru.demetrious.animeshikilist.data.filter.tv";
    public static final String DATA_FILTER_ONA = "ru.demetrious.animeshikilist.data.filter.ona";
    public static final String DATA_FILTER_OVA = "ru.demetrious.animeshikilist.data.filter.ova";
    public static final String DATA_FILTER_MOVIE = "ru.demetrious.animeshikilist.data.filter.movie";
    public static final String DATA_FILTER_SPECIAL = "ru.demetrious.animeshikilist.data.filter.special";
    public static final String DATA_FILTER_MUSIC = "ru.demetrious.animeshikilist.data.filter.music";

    public static final String DATA_SETTINGS_NICKNAME = "ru.demetrious.animeshikilist.data.settings.nickname";

    private static SharedPreferences sharedPreferences;
    private static Map<String, ?> data;
    private static HashSet<String> incorrectKeys = new HashSet<>();
    private MainActivity mainActivity;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    public Settings(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                incorrectKeys.add(key);
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        //sharedPreferences.edit().clear().apply();
    }

    public static SharedPreferences.Editor getPreferencesEditor() {
        return sharedPreferences.edit();
    }

    public static <T> T getData(String key, T defaultValue) {
        if (incorrectKeys.contains(key)) loadData();
        Object value = data.get(key);
        if (value != null && !value.getClass().equals(defaultValue.getClass())) {
            value = null;
            getPreferencesEditor().remove(key).apply();
        }
        if (value == null) return defaultValue;
        return (T) value;
    }

    public static void loadData() {
        incorrectKeys.clear();
        data = sharedPreferences.getAll();
    }
}
