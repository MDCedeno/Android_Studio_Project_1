package com.example.musicplayerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    private static final int SELECT_TRACK_REQUEST = 1; // Request code for selection
    private MediaPlayer mediaPlayer;

    // Track titles
    private final String[] trackTitles = {
            "Chance", "Choose", "Rockstar", "Back", "Conviction",
            "Dreaming", "Free", "Go", "Grateful", "Immortal",
            "Kill", "King", "Legendary", "Life", "Me",
            "Never", "Purpose", "Rumors", "War", "Way"
    };

    // Music files (reference IDs)
    private final int[] musicFiles = {
            R.raw.chance, R.raw.choose, R.raw.rockstar, R.raw.back, R.raw.conviction,
            R.raw.dreaming, R.raw.free, R.raw.go, R.raw.grateful, R.raw.immortal,
            R.raw.kill, R.raw.king, R.raw.legendary, R.raw.life, R.raw.me,
            R.raw.never, R.raw.purpose, R.raw.rumors, R.raw.war, R.raw.way
    };

    // Track images
    private final int[] trackImages = {
            R.drawable.mindset, R.drawable.thug, R.drawable.awkward,
            R.drawable.peace, R.drawable.bayani, R.drawable.bossing,
            R.drawable.daddy, R.drawable.gavin, R.drawable.gwenchana,
            R.drawable.huh, R.drawable.nani, R.drawable.omg,
            R.drawable.sad, R.drawable.slap, R.drawable.sus,
            R.drawable.thinker, R.drawable.titikman, R.drawable.women,
            R.drawable.wut, R.drawable.zesty
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        LinearLayout trackList = findViewById(R.id.trackList);
        ImageButton backButton = findViewById(R.id.imageButton); // Find the back button

        // Set the back button click listener
        backButton.setOnClickListener(v -> {
            finish(); // Close SelectionActivity and return to MainActivity
        });

        // Add track items dynamically
        for (int i = 0; i < trackTitles.length; i++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.track_item, trackList, false);

            TextView itemTrackTitle = itemView.findViewById(R.id.itemTrackTitle);
            ImageView itemTrackImage = itemView.findViewById(R.id.itemTrackImage);

            itemTrackTitle.setText(trackTitles[i]);
            itemTrackImage.setImageResource(trackImages[i]); // Assuming you have track images

            // Click listener to return selected track index
            final int index = i; // Final variable for use in inner class
            itemView.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTrackIndex", index); // Send back selected track index
                setResult(RESULT_OK, resultIntent); // Set result to OK
                finish(); // Close SelectionActivity and return to MainActivity
            });

            trackList.addView(itemView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_TRACK_REQUEST && resultCode == RESULT_OK) {
            int selectedTrackIndex = data.getIntExtra("selectedTrackIndex", -1); // Changed default value
            if (selectedTrackIndex != -1) { // Check if a valid index is received
                playTrack(selectedTrackIndex); // Call to playTrack
            }
        }
    }

    private void playTrack(int trackIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release previous media player if it exists
        }
        mediaPlayer = MediaPlayer.create(this, musicFiles[trackIndex]);
        if (mediaPlayer != null) { // Check if media player was created successfully
            mediaPlayer.start(); // Start playing the selected track
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release media player when the activity is destroyed
            mediaPlayer = null; // Nullify to avoid memory leaks
        }
    }
}
