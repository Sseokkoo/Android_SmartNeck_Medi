package com.smartneck.fit.Fit.YouTube;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.smartneck.fit.Fit.util.Fit_Address;
import com.smartneck.fit.R;

public class Fit_HowToExerciseActivity extends YouTubeBaseActivity {
    YouTubePlayerView youTubeView;
    YouTubePlayer.OnInitializedListener listener;
    ImageView btn_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_exercise);
        youTubeView = findViewById(R.id.how_to_exercise_youtube_player);
        btn_finish = findViewById(R.id.how_to_exercise_dismiss);
        listener = new YouTubePlayer.OnInitializedListener(){

            //초기화 성공시
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(Fit_Address.YOUTUBE_HOW_TO_EXERCISE);//url의 맨 뒷부분 ID값만 넣으면 됨
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(Fit_HowToExerciseActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        };
        youTubeView.initialize(getString(R.string.google_key), listener);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
