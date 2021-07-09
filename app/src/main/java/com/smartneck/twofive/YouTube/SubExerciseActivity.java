package com.smartneck.twofive.YouTube;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.smartneck.twofive.R;

public class SubExerciseActivity extends YouTubeBaseActivity {
    YouTubePlayerView youTubeView;
    YouTubePlayer.OnInitializedListener listener;
    ImageView btn_finish;
    ImageView img_sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_exercise);
        youTubeView = findViewById(R.id.sub_exercise_youtube_player);
        btn_finish = findViewById(R.id.sub_exercise_dismiss);
        img_sub = findViewById(R.id.img_sub);
        Glide.with(this)
                .asGif()
                .load(R.drawable.back)
                .into(img_sub);
//        listener = new YouTubePlayer.OnInitializedListener(){
//
//            //초기화 성공시
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.loadVideo(Address.YOUTUBE_SUB_EXERCISE);//url의 맨 뒷부분 ID값만 넣으면 됨
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                Toast.makeText(SubExerciseActivity.this, "동영상 주소가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
//            }
//        };
//        youTubeView.initialize(getString(R.string.google_key), listener);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
