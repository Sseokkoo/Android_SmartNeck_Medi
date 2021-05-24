package com.smartneck.twofive.Fit.Join;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.smartneck.twofive.R;

import static com.smartneck.twofive.Fit.Join.Fit_Join_1_Activity.activityList;
import static com.smartneck.twofive.Fit.Join.Fit_Join_7_Activity.survey;
import static com.smartneck.twofive.Fit.Join.Fit_Join_7_Activity.survey2;

public class Fit_Join_6_Activity extends AppCompatActivity {
    RadioButton radio_yes , radio_no , radio_1 , radio_2 , radio_3 , radio_4;
    RadioGroup radioGroup;
    boolean isCheckd;
    int checkNum;
    Button btn_next;
    ImageView btn_previous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_6_);

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
        if (activityList.size() == 6){
            activityList.remove(activityList.size()-1);
        }
    }
    void init(){
        radio_1 = findViewById(R.id.radio_1);
        radio_2 = findViewById(R.id.radio_2);
        radio_3 = findViewById(R.id.radio_3);
        radio_4 = findViewById(R.id.radio_4);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        radioGroup = findViewById(R.id.radio_yes_select);
        radioGroup.setVisibility(View.GONE);
        btn_next = findViewById(R.id.join_6_next);
        btn_previous = findViewById(R.id.join_6_previous);
    }

    void onClick(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (radio_yes.isChecked() || radio_no.isChecked()){
                    if (radio_yes.isChecked()) {
                        if (radio_1.isChecked() || radio_2.isChecked() || radio_3.isChecked() || radio_4.isChecked()){

                        }else{
                            Toast.makeText(Fit_Join_6_Activity.this, getString(R.string.toast_please_check), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }else if(radio_no.isChecked()){

                    }else{
                        Toast.makeText(Fit_Join_6_Activity.this, getString(R.string.toast_please_check), Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else{
                    Toast.makeText(Fit_Join_6_Activity.this, getString(R.string.toast_please_check), Toast.LENGTH_SHORT).show();
                    return;
                }
                activityList.add(Fit_Join_6_Activity.this);

                Intent intent = new Intent(getApplicationContext() , Fit_Join_7_Activity.class);
                startActivity(intent);


            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        radio_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    radioGroup.setVisibility(View.VISIBLE);
                    survey = "yes";
                }
            }
        });
        radio_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    radioGroup.setVisibility(View.GONE);
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
                    survey = "no";

                }
            }
        });

        radio_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkNum = 1;
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
                    survey2 = "3m";

                }
            }
        });
        radio_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkNum = 2;
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
                    survey2 = "6m";

                }
            }
        });
        radio_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkNum = 3;
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
                    survey2 = "1y";

                }
            }
        });
        radio_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkNum = 4;
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
                    survey2 = "1y ago";

                }
            }
        });
    }

}
