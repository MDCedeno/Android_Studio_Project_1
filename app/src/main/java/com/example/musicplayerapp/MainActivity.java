package com.example.musicplayerapp;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler mHandler;
    private Runnable mUpdateSeekbar;
    private TextView elapsedTimeView, remainingTimeView;
    private final String[] musicFiles = {
            "chance", "choose", "rockstar", "back", "conviction",
            "dreaming", "free", "go", "grateful", "immortal",
            "kill", "king", "legendary", "life", "me",
            "never", "purpose", "rumors", "war", "way"
    };
    private final String[] trackTitles = {
            "Chance", "Choose", "Rockstar", "Back", "Conviction",
            "Dreaming", "Free", "Go", "Grateful", "Immortal",
            "Kill", "King", "Legendary", "Life", "Me",
            "Never", "Purpose", "Rumors", "War", "Way"
    };
    private int currentTrackIndex = 0;
    private ImageView trackImageView;
    private TextView titleView;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private final Random random = new Random();

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
        setContentView(R.layout.activity_main);

        ImageButton playPauseButton = findViewById(R.id.playPauseButton);
        ImageButton previousButton = findViewById(R.id.previousButton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton shuffleButton = findViewById(R.id.shuffleButton);
        ImageButton repeatButton = findViewById(R.id.repeatButton);
        ImageButton dropdownButton = findViewById(R.id.dropdownButton);
        seekBar = findViewById(R.id.seekBar);
        trackImageView = findViewById(R.id.trackImageView);
        titleView = findViewById(R.id.titleView);
        elapsedTimeView = findViewById(R.id.elapsedTime); // Elapsed time TextView
        remainingTimeView = findViewById(R.id.remainingTime); // Remaining time TextView
        mHandler = new Handler();

        // Initialize MediaPlayer with the first track
        initializeMediaPlayer();

        // Play/Pause button click listener
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause);
                updateSeekBar();
            }
        });

        // Previous button click listener
        previousButton.setOnClickListener(v -> playPreviousTrack());

        // Next button click listener
        nextButton.setOnClickListener(v -> playNextTrack());

        // Shuffle button click listener
        shuffleButton.setOnClickListener(v -> {
            isShuffle = !isShuffle; // Toggle shuffle state
            shuffleButton.setImageResource(isShuffle ? R.drawable.shuffleon : R.drawable.shuffle);
        });

        // Repeat button click listener
        repeatButton.setOnClickListener(v -> {
            isRepeat = !isRepeat; // Toggle repeat state
            repeatButton.setImageResource(isRepeat ? R.drawable.repeaton : R.drawable.repeat);
        });

        // Dropdown button click listener (to minimize the entire application)
        dropdownButton.setOnClickListener(v -> {
            moveTaskToBack(true); // Minimize the entire application
        });

        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    int newPosition = mediaPlayer.getDuration() * progress / 100;
                    mediaPlayer.seekTo(newPosition);
                    updateTimerViews();
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
                    updateTimerViews();
                }
            }
        });
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(musicFiles[currentTrackIndex], "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, resourceId);

        // Change track image and title
        trackImageView.setImageResource(trackImages[currentTrackIndex]);
        titleView.setText(trackTitles[currentTrackIndex]);
        seekBar.setProgress(0);
        updateTimerViews();

        // Set up completion listener to handle repeat and shuffle
        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeat) {
                mediaPlayer.start(); // Repeat the same track
            } else if (isShuffle) {
                currentTrackIndex = random.nextInt(musicFiles.length);
                initializeMediaPlayer();
                mediaPlayer.start();
            } else {
                playNextTrack();
            }
        });
    }

    private void playNextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % musicFiles.length;
        initializeMediaPlayer();
        mediaPlayer.start();
        updateTimerViews();
    }

    private void playPreviousTrack() {
        currentTrackIndex = (currentTrackIndex - 1 + musicFiles.length) % musicFiles.length;
        initializeMediaPlayer();
        mediaPlayer.start();
        updateTimerViews();
    }

    private void updateSeekBar() {
        mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int totalDuration = mediaPlayer.getDuration();
                    seekBar.setProgress(currentPosition * 100 / totalDuration);
                    updateTimerViews();
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        mHandler.post(mUpdateSeekbar);
    }

    private void updateTimerViews() {
        if (mediaPlayer != null) {
            int elapsedTime = mediaPlayer.getCurrentPosition();
            int remainingTime = mediaPlayer.getDuration() - elapsedTime;

            elapsedTimeView.setText(formatTime(elapsedTime)); // Update elapsed time
            remainingTimeView.setText(formatTime(remainingTime)); // Update remaining time
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
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
