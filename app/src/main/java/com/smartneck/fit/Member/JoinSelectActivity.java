package com.smartneck.fit.Member;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.fit.R;


/**
 *
 * 회원가입 유형 선택
 *
 * 병원 or 피트니스
 *
 * 인텐트로 타입명 전송
 *
 * */

public class JoinSelectActivity extends AppCompatActivity {

    TextView tv_medical , tv_fitness;
    ImageView img_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_join_select_activity);
        init();
    }


    private void init(){
        tv_medical = findViewById(R.id.tv_join_select_medical);
        tv_fitness = findViewById(R.id.tv_join_select_fitness);
        img_back_btn = findViewById(R.id.img_join_select_back_btn);


        tv_medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , JoinActivity.class);
                intent.putExtra("type" , "M");
                startActivity(intent);
                finish();

            }
        });

        tv_fitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , JoinActivity.class);
                intent.putExtra("type" , "F");
                startActivity(intent);
                finish();

            }
        });

        img_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
