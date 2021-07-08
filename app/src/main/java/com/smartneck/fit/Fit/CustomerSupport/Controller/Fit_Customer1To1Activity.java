package com.smartneck.fit.Fit.CustomerSupport.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.fit.R;

public class Fit_Customer1To1Activity extends AppCompatActivity {
    //1to1 list에서 받아온 정보 출력

    TextView tv_state , tv_date , tv_contents;
    ImageView btn_dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer1_to1);
//                    public Customer1To1Item(String state, String contents, String date) {

        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        String contents = intent.getStringExtra("contents");
        String date = intent.getStringExtra("date");
        int no = intent.getIntExtra("no" , 0);

        tv_state = findViewById(R.id.customer_1to1_state);
        tv_date = findViewById(R.id.customer_1to1_date);
        tv_contents = findViewById(R.id.customer_1to1_contents);
        btn_dismiss = findViewById(R.id.customer_1to1_dismiss_Btn);

        tv_state.setText(state);
        tv_date.setText(date);
        tv_contents.setText(contents);

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
