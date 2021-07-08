package com.smartneck.fit.Fit.UserInfoEdit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.fit.R;

public class Fit_UserInfoEditListActivity extends AppCompatActivity {

    TextView tv_pw , tv_phone;
    ImageView btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit_list);
        tv_pw = findViewById(R.id.tv_pw);
        tv_phone = findViewById(R.id.tv_phone);
        btn_dismiss = findViewById(R.id.edit_list_dismiss_btn);
        tv_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , Fit_EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , Fit_EditPhoneActivity.class);
                startActivity(intent);
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
