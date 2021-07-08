package com.smartneck.fit.Fit.Notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.fit.R;

public class Fit_NoticeActivity extends AppCompatActivity {
    String title;
    String contents;
    String date;
    int no;
    TextView tv_title , tv_date , tv_contents;
    ImageView btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        init();
    }
    private void init(){
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        date = intent.getStringExtra("date");
        no = intent.getIntExtra("no" , 0);

        tv_title = findViewById(R.id.notice_list_title);
        tv_contents = findViewById(R.id.notice_contents);
        tv_date = findViewById(R.id.notice_list_date);

        tv_title.setText(title);
        tv_contents.setText(contents);
        tv_date.setText(date);

        btn_dismiss = findViewById(R.id.notice_dismiss_Btn);

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
