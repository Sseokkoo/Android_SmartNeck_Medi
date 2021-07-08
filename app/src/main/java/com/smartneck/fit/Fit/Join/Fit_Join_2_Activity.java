package com.smartneck.fit.Fit.Join;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.smartneck.fit.R;

import static com.smartneck.fit.Fit.Join.Fit_Join_1_Activity.activityList;

public class Fit_Join_2_Activity extends AppCompatActivity {
    Button btn_next;
    static String joinPassword;
    EditText edt_pw , edt_conf;
    ImageView btn_previous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_join_2_);
        init();
        onClick();
    }
    protected void onResume() {
        super.onResume();
        removeArrayitem();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeArrayitem();

    }
    void removeArrayitem(){
        if (activityList.size() == 2) {
            activityList.remove(activityList.size() - 1);
        }
    }
    void init(){
        btn_next = findViewById(R.id.join_2_next);
        btn_previous = findViewById(R.id.join_2_previous);
        edt_pw= findViewById(R.id.join_2_edt);
        edt_conf = findViewById(R.id.join_2_edt2);
    }
    void onClick(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_pw.length() < 8 || edt_conf.length() < 8){
                    Toast.makeText(Fit_Join_2_Activity.this, getString(R.string.toast_pw), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!edt_pw.getText().toString().equals(edt_conf.getText().toString())){
                    Toast.makeText(Fit_Join_2_Activity.this, getString(R.string.toast_discord_password), Toast.LENGTH_SHORT).show();
                }
                if (edt_pw.getText().toString().equals(edt_conf.getText().toString())){
                    joinPassword = edt_pw.getText().toString();
                    activityList.add(Fit_Join_2_Activity.this);

                    Intent intent = new Intent(getApplicationContext() , Fit_Join_3_Activity.class);
                    startActivity(intent);
                }


            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_pw.length() == 0 || edt_conf.length() == 0){
                    btn_next.setBackgroundColor(Color.parseColor("#cccccc"));
                }else if(edt_pw.length() > 0 && edt_conf.length() > 0){
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));

                }
            }
        });
        edt_conf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_pw.length() == 0 || edt_conf.length() == 0){
                    btn_next.setBackgroundColor(Color.parseColor("#cccccc"));
                }else if(edt_pw.length() >= 8 && edt_conf.length() >= 8){
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));

                }
            }
        });
    }
}
