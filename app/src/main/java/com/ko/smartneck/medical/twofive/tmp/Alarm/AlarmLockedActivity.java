package com.ko.smartneck.medical.twofive.tmp.Alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ko.smartneck.medical.twofive.R;

public class AlarmLockedActivity extends AppCompatActivity {
    TextView moveActivity , time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        final AlarmPlay alarmPlay = new AlarmPlay(AlarmLockedActivity.this);
        alarmPlay.play();
        moveActivity = findViewById(R.id.alarm_locked_move_activity);
        time = findViewById(R.id.alarm_locked_time);
        AlarmCurrentTime alarmCurrentTime = new AlarmCurrentTime();
        time.setText(alarmCurrentTime.getToday());
        moveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmPlay.stop();
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
