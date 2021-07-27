package ru.demetrious.animeshikilist.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import ru.demetrious.animeshikilist.R;
import ru.demetrious.animeshikilist.settings.Settings;

public class SettingsActivity extends AppCompatActivity {
    private EditText nicknameInput;
    private Button nicknameApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        declarations();
        listeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_settings_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onStart() {
        nicknameInput.setText(Settings.getData(Settings.DATA_SETTINGS_NICKNAME, getString(R.id.settings_nickname_input)));
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bar_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void listeners() {
        nicknameApply.setOnClickListener(v -> {
            Settings.getPreferencesEditor().putString(Settings.DATA_SETTINGS_NICKNAME, nicknameInput.getText().toString()).apply();
            setResult(RESULT_OK);
        });
    }

    private void declarations() {
        nicknameInput = findViewById(R.id.settings_nickname_input);
        nicknameApply = findViewById(R.id.settings_nickname_apply);
    }
}
