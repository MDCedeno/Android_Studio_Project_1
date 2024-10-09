package com.example.musicplayerapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler mHandler;
    private Runnable mUpdateSeekbar;
    private String[] musicFiles = {"sample1", "sample2", "sample3"}; // Add more file names without extension
    private int currentTrackIndex = 0; // To track the current track
    private ImageView trackImageView; // ImageView for track image

    // Array for track images
    private int[] trackImages = {
            R.drawable.mindset, // Image for sample1
            R.drawable.thug, // Image for sample2
            R.drawable.awkward  // Image for sample3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playPauseButton = findViewById(R.id.playPauseButton);
        Button previousButton = findViewById(R.id.previousButton);
        Button nextButton = findViewById(R.id.nextButton);
        seekBar = findViewById(R.id.seekBar);
        trackImageView = findViewById(R.id.trackImageView); // Initialize ImageView
        mHandler = new Handler();

        // Initialize MediaPlayer with the first track
        initializeMediaPlayer();

        // Play/Pause button click listener
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Play");
                } else {
                    mediaPlayer.start();
                    playPauseButton.setText("Pause");
                    updateSeekBar();
                }
            }
        });

        // Previous button click listener
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousTrack();
            }
        });

        // Next button click listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextTrack();
            }
        });

        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    int newPosition = mediaPlayer.getDuration() * progress / 100;
                    mediaPlayer.seekTo(newPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release previous media player if exists
        }
        int resourceId = getResources().getIdentifier(musicFiles[currentTrackIndex], "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, resourceId);

        // Change the track image based on the current track
        trackImageView.setImageResource(trackImages[currentTrackIndex]);
    }

    private void playNextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % musicFiles.length; // Loop back to first track if at the end
        initializeMediaPlayer();
        mediaPlayer.start();
    }

    private void playPreviousTrack() {
        currentTrackIndex = (currentTrackIndex - 1 + musicFiles.length) % musicFiles.length; // Loop back to last track if at the start
        initializeMediaPlayer();
        mediaPlayer.start();
    }

    private void updateSeekBar() {
        mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int totalDuration = mediaPlayer.getDuration();
                    seekBar.setProgress(currentPosition * 100 / totalDuration);
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        mHandler.post(mUpdateSeekbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            mHandler.removeCallbacks(mUpdateSeekbar);
        }
    }
}