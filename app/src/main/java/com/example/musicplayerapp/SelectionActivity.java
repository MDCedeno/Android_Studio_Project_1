package com.example.musicplayerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    private final String[] trackTitles = {
            "Chance", "Choose", "Rockstar", "Back", "Conviction",
            "Dreaming", "Free", "Go", "Grateful", "Immortal",
            "Kill", "King", "Legendary", "Life", "Me",
            "Never", "Purpose", "Rumors", "War", "Way"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        LinearLayout trackList = findViewById(R.id.trackList);

        for (int i = 0; i < trackTitles.length; i++) {
            final int index = i;
            TextView trackView = new TextView(this);
            trackView.setText(trackTitles[i]);
            trackView.setPadding(16, 16, 16, 16);
            trackView.setTextSize(18);
            trackView.setOnClickListener(v -> {
                // Return selected track to MainActivity
                Intent intent = new Intent();
                intent.putExtra("selectedTrackIndex", index);
                setResult(RESULT_OK, intent);
                finish(); // Close the activity and return to the main interface
            });
            trackList.addView(trackView);
        }
    }
}