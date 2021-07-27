package ru.demetrious.animeshikilist.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import ru.demetrious.animeshikilist.R;
import ru.demetrious.animeshikilist.animes.Anime;
import ru.demetrious.animeshikilist.animes.AnimeElement;
import ru.demetrious.animeshikilist.animes.AnimeElementAdapter;
import ru.demetrious.animeshikilist.settings.Settings;

public class MainActivity extends AppCompatActivity {
    public final static int ACTIVITY_SORT = 0;
    public final static int ACTIVITY_FILTER = 1;
    public final static int ACTIVITY_SETTINGS = 2;
    private String[] permissions = {Manifest.permission.INTERNET};
    private ArrayList<AnimeElement> animeElements = new ArrayList<>();
    private ListView animeList;
    private Settings settings;
    private Anime anime;
    private AnimeElementAdapter animeElementAdapter;
    private ImageButton roll;

    public ListView getAnimeList() {
        return animeList;
    }

    public ArrayList<AnimeElement> getAnimeElements() {
        return animeElements;
    }

    public AnimeElementAdapter getAnimeElementAdapter() {
        return animeElementAdapter;
    }

    public Anime getAnime() {
        return anime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions();
        declarations();
        listeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ERROR", String.valueOf(resultCode));

        switch (requestCode) {
            case ACTIVITY_SORT:
                if (resultCode == RESULT_OK)
                    anime.sort();
                break;
            case ACTIVITY_FILTER:
                if (resultCode == RESULT_OK)
                    anime.filter();
                break;
            case ACTIVITY_SETTINGS:
                if (resultCode == RESULT_OK)
                    anime.loadAnimes(true);
                break;
            default:
                throw new ExceptionInInitializerError();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bar_sort:
                Intent intentSort = new Intent(MainActivity.this, SortActivity.class);
                startActivityForResult(intentSort, ACTIVITY_SORT);
                return true;
            case R.id.bar_filter:
                Intent intentFilter = new Intent(MainActivity.this, FilterActivity.class);
                startActivityForResult(intentFilter, ACTIVITY_FILTER);
                return true;
            case R.id.bar_settings:
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intentSettings, ACTIVITY_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        Settings.loadData();
        super.onStart();
        anime.anime();
    }

    private void listeners() {
        roll.setOnClickListener(v -> {
            anime.roll();
            /*Intent intent = new Intent(MainActivity.this, AnimateLoaderActivity.class);
            startActivity(intent);*/
        });
    }

    private void declarations() {
        anime = new Anime(this);
        settings = new Settings(this);

        animeList = findViewById(R.id.anime_list);
        animeElementAdapter = new AnimeElementAdapter(this, R.layout.advanced_list_anime, animeElements);
        animeList.setAdapter(animeElementAdapter);

        roll = findViewById(R.id.anime_roll);
    }

    private void permissions(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
    }

    private void permissions() {
        for (String perm : permissions)
            permissions(perm);
    }
}
