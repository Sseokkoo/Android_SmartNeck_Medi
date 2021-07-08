package com.smartneck.fit.Fit.Join;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.smartneck.fit.R;
import com.smartneck.fit.Fit.util.Fit_Constants;
import com.smartneck.fit.Fit.util.User.Fit_User;

import static com.smartneck.fit.Fit.Join.Fit_Join_1_Activity.activityList;

public class Fit_Join_4_Activity extends AppCompatActivity {
    Button btn_next;
    static String joinBirth, joinGender;
    RadioButton male, female;
    ImageView btn_previous;
    EditText edt_1, edt_2, edt_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_join_4_);
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

    void removeArrayitem() {
        if (activityList.size() == 4) {
            activityList.remove(activityList.size() - 1);
        }
    }

    void init() {
        btn_next = findViewById(R.id.join_4_next);
        btn_previous = findViewById(R.id.join_4_previous);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        edt_1 = findViewById(R.id.join_4_edt_1);
        edt_2 = findViewById(R.id.join_4_edt_2);
        edt_3 = findViewById(R.id.join_4_edt_3);

        if (Fit_User.country.contains("US")) {
            edt_1.setHint("DD");
            edt_2.setHint("MM");
            edt_3.setHint("YYYY");
            edt_1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
            edt_2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
            edt_3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });



        } else if (Fit_User.country.contains("KR")) {
            edt_1.setHint("YYYY");
            edt_2.setHint("MM");
            edt_3.setHint("DD");
            edt_1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
            edt_2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
            edt_3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
        } else{
            edt_1.setHint("YYYY");
            edt_2.setHint("MM");
            edt_3.setHint("DD");
            edt_1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
            edt_2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
            edt_3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
        }

    }

    void onClick() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (male.isChecked() || female.isChecked()) {
                    if (male.isChecked()) {
                        joinGender = "남자";
                    } else if (female.isChecked()) {
                        joinGender = "여자";
                    }

                } else {
                    return;
                }
                if (Fit_User.country.equals("US")) {

                    if (edt_1.getText().length() < 2 || edt_2.getText().length() < 2 || edt_3.getText().length() < 4) {
                        return;
                    }
                    if (Integer.parseInt(edt_1.getText().toString()) > 31 ||
                            Integer.parseInt(edt_2.getText().toString()) > 12 ||
                            Integer.parseInt(edt_3.getText().toString()) < 1900){
                        return;
                    }
                    String day = edt_1.getText().toString();
                    String month = edt_2.getText().toString();
                    String year = edt_3.getText().toString();
                    joinBirth = year + "-" + month + "-" + day;
                }else{
                    if (edt_1.getText().length() < 4 || edt_2.getText().length() < 2 || edt_3.getText().length() < 2) {
                        return;
                    }
                    if (Integer.parseInt(edt_3.getText().toString()) > 31 ||
                            Integer.parseInt(edt_2.getText().toString()) > 12 ||
                            Integer.parseInt(edt_1.getText().toString()) < 1900){
                        return;
                    }
                    String day = edt_3.getText().toString();
                    String month = edt_2.getText().toString();
                    String year = edt_1.getText().toString();
                    joinBirth = year + "-" + month + "-" + day;
                }

                activityList.add(Fit_Join_4_Activity.this);

                Intent intent = new Intent(getApplicationContext(), Fit_Join_5_Activity.class);
                startActivity(intent);
                Log.d(Fit_Constants.TAG, "gender:" + joinGender + "year: " + joinBirth);

            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setBtnColor();
            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setBtnColor();
            }
        });

        edt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnColor();
            }
        });

        edt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnColor();
            }
        });

        edt_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnColor();
            }
        });


    }
    void setBtnColor(){
        if (edt_1.length() != 0 && edt_2.length() != 0 && edt_3.length() != 0){
            if (male.isChecked() || female.isChecked()){
                if (Fit_User.country.equals("US")){
                    if (Integer.parseInt(edt_1.getText().toString()) > 31 ||
                            Integer.parseInt(edt_2.getText().toString()) > 12 ||
                            Integer.parseInt(edt_3.getText().toString()) < 1900){
                        btn_next.setBackgroundColor(Color.parseColor("#cccccc"));

                        return;
                    }
                }else{
                    if (Integer.parseInt(edt_3.getText().toString()) > 31 ||
                            Integer.parseInt(edt_2.getText().toString()) > 12 ||
                            Integer.parseInt(edt_1.getText().toString()) < 1900){
                        btn_next.setBackgroundColor(Color.parseColor("#cccccc"));

                        return;
                    }
                }
                btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
            }else{
                btn_next.setBackgroundColor(Color.parseColor("#cccccc"));
            }
        }else{
            btn_next.setBackgroundColor(Color.parseColor("#cccccc"));
        }
    }
}
