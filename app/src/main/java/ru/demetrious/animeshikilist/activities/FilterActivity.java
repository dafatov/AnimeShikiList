package ru.demetrious.animeshikilist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;

import ru.demetrious.animeshikilist.R;
import ru.demetrious.animeshikilist.settings.Settings;

public class FilterActivity extends AppCompatActivity {
    private CheckBox planned, watching, rewatching, completed, onhold, dropped,
            tv, ona, ova, movie, special, music;
    private Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        declarations();
        listeners();
    }

    @Override
    protected void onStart() {
        planned.setChecked(Settings.getData(Settings.DATA_FILTER_PLANNED, true));
        watching.setChecked(Settings.getData(Settings.DATA_FILTER_WATCHING, true));
        rewatching.setChecked(Settings.getData(Settings.DATA_FILTER_REWATCHING, true));
        completed.setChecked(Settings.getData(Settings.DATA_FILTER_COMPLETED, true));
        onhold.setChecked(Settings.getData(Settings.DATA_FILTER_ONHOLD, true));
        dropped.setChecked(Settings.getData(Settings.DATA_FILTER_DROPPED, true));

        tv.setChecked(Settings.getData(Settings.DATA_FILTER_TV, true));
        ona.setChecked(Settings.getData(Settings.DATA_FILTER_ONA, true));
        ova.setChecked(Settings.getData(Settings.DATA_FILTER_OVA, true));
        movie.setChecked(Settings.getData(Settings.DATA_FILTER_MOVIE, true));
        special.setChecked(Settings.getData(Settings.DATA_FILTER_SPECIAL, true));
        music.setChecked(Settings.getData(Settings.DATA_FILTER_MUSIC, true));
        super.onStart();
    }

    private void listeners() {
        apply.setOnClickListener(v -> {
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_PLANNED, planned.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_WATCHING, watching.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_REWATCHING, rewatching.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_COMPLETED, completed.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_ONHOLD, onhold.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_DROPPED, dropped.isChecked()).apply();

            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_TV, tv.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_ONA, ona.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_OVA, ova.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_MOVIE, movie.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_SPECIAL, special.isChecked()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_FILTER_MUSIC, music.isChecked()).apply();

            setResult(RESULT_OK);
            finish();
        });
        apply.setOnLongClickListener(v -> {
            planned.setChecked(true);
            watching.setChecked(true);
            rewatching.setChecked(true);
            completed.setChecked(true);
            onhold.setChecked(true);
            dropped.setChecked(true);

            tv.setChecked(true);
            ona.setChecked(true);
            ova.setChecked(true);
            movie.setChecked(true);
            special.setChecked(true);
            music.setChecked(true);
            return true;
        });
    }

    private void declarations() {
        planned = findViewById(R.id.filter_planned);
        watching = findViewById(R.id.filter_watching);
        rewatching = findViewById(R.id.filter_rewatching);
        completed = findViewById(R.id.filter_completed);
        onhold = findViewById(R.id.filter_onhold);
        dropped = findViewById(R.id.filter_dropped);
        tv = findViewById(R.id.filter_tv);
        ona = findViewById(R.id.filter_ona);
        ova = findViewById(R.id.filter_ova);
        movie = findViewById(R.id.filter_movie);
        special = findViewById(R.id.filter_special);
        music = findViewById(R.id.filter_music);

        apply = findViewById(R.id.filter_apply);
    }
}
