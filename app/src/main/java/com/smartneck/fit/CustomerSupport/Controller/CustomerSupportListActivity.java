package com.smartneck.fit.CustomerSupport.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartneck.fit.R;

import static com.smartneck.fit.Member.MemberSelectActivity.admin;

public class CustomerSupportListActivity extends AppCompatActivity {
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
                if (!admin.isBackup()){
                    Toast.makeText(CustomerSupportListActivity.this, "백업기능을 사용하지 않는 계정은 사용할수 없습니다. 본사로 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext() , Customer1To1ListActivity.class);
                startActivity(intent);
            }
        });

        tv_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , CustomerFAQActivity.class);
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
