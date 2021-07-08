package com.smartneck.fit.Fit.CustomerSupport.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.fit.R;

public class Fit_CustomerSupportListActivity extends AppCompatActivity {
    TextView tv_1to1 , tv_faq , tv_1to1_list;
    ImageView btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support_list);

        btn_dismiss = findViewById(R.id.customer_support_dismiss_btn);
        tv_1to1 = findViewById(R.id.customer_1to1);
        tv_faq = findViewById(R.id.customer_faq);
        tv_1to1_list = findViewById(R.id.customer_1to1_list);
        tv_1to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , Fit_Customer1To1ListActivity.class);
                startActivity(intent);
            }
        });

        tv_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , Fit_CustomerFAQActivity.class);
                startActivity(intent);
            }
        });

//        tv_1to1_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext() , Customer1To1ListActivity.class);
//                startActivity(intent);
//            }
//        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
