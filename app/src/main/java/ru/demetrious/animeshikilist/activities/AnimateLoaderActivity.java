package ru.demetrious.animeshikilist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import ru.demetrious.animeshikilist.R;

public class AnimateLoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_loader);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.animate();
    }
}
