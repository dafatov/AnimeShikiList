package ru.demetrious.animeshikilist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import ru.demetrious.animeshikilist.R;
import ru.demetrious.animeshikilist.settings.Settings;

public class SortActivity extends AppCompatActivity {
    private RadioGroup category, direction;
    private CheckBox separate;
    private Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        declarations();
        listeners();
    }

    @Override
    protected void onStart() {
        category.check(Settings.getData(Settings.DATA_SORT_CATEGORY, R.id.sort_name));
        direction.check(Settings.getData(Settings.DATA_SORT_DIRECTION, R.id.sort_asc));
        separate.setChecked(Settings.getData(Settings.DATA_SORT_SEPARATE, true));
        super.onStart();
    }

    private void listeners() {
        apply.setOnClickListener(v -> {
            Settings.getPreferencesEditor().putInt(Settings.DATA_SORT_CATEGORY, category.getCheckedRadioButtonId()).apply();
            Settings.getPreferencesEditor().putInt(Settings.DATA_SORT_DIRECTION, direction.getCheckedRadioButtonId()).apply();
            Settings.getPreferencesEditor().putBoolean(Settings.DATA_SORT_SEPARATE, separate.isChecked()).apply();
            setResult(RESULT_OK);
            finish();
        });
    }

    private void declarations() {
        category = findViewById(R.id.group_sort_category);
        direction = findViewById(R.id.group_sort_direction);
        separate = findViewById(R.id.sort_separate);
        apply = findViewById(R.id.sort_apply);
    }
}
