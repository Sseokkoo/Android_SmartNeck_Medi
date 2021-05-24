package com.smartneck.twofive.Fit.Join;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.User.Fit_User;

import static com.smartneck.twofive.Fit.Join.Fit_Join_1_Activity.activityList;

public class Fit_Join_3_Activity extends AppCompatActivity {
    Button btn_next;
    static String joinName;
    ImageView btn_previous;
    EditText edt_name, edt_name2 , edt_name3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_3_);
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
        if (activityList.size() == 3) {
            activityList.remove(activityList.size() - 1);
        }
    }
    void init(){
        btn_next = findViewById(R.id.join_3_next);
        edt_name = findViewById(R.id.join_3_edt);
        edt_name2 = findViewById(R.id.join_3_edt2);
        edt_name3 = findViewById(R.id.join_3_edt3);
        btn_previous = findViewById(R.id.join_3_previous);

        if (!Fit_User.language.equals("en")){
            edt_name3.setVisibility(View.GONE);
            edt_name2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }else{
            edt_name3.setVisibility(View.VISIBLE);
            edt_name2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
    }
    void onClick(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNumeric(edt_name.getText().toString()) || isNumeric(edt_name2.getText().toString()) || isNumeric(edt_name3.getText().toString())){
                    Toast.makeText(Fit_Join_3_Activity.this, getString(R.string.toast_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Fit_User.language.equals("en")){
                    if (edt_name.length() == 0 || edt_name3.length() == 0){
                        Toast.makeText(Fit_Join_3_Activity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String firstName = edt_name.getText().toString().trim();
                    String middleName = edt_name2.getText().toString().trim();
                    String lastName = edt_name3.getText().toString().trim();

                    joinName = firstName + " " + middleName + " " + lastName;
                }else{
                    if (edt_name.length() == 0 || edt_name2.length() == 0){
                        Toast.makeText(Fit_Join_3_Activity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String firstName = edt_name.getText().toString().trim();
                    String middleName = edt_name2.getText().toString().trim();
                    joinName = firstName + " " + middleName;

                }

                activityList.add(Fit_Join_3_Activity.this);

                Intent intent = new Intent(getApplicationContext() , Fit_Join_4_Activity.class);
                startActivity(intent);
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt_name.addTextChangedListener(new TextWatcher() {
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
        edt_name2.addTextChangedListener(new TextWatcher() {
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
        edt_name3.addTextChangedListener(new TextWatcher() {
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
        if (Fit_User.country == "US") {
            if (edt_name.length() != 0 && edt_name3.length() != 0){

                btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));

            }else{

                btn_next.setBackgroundColor(Color.parseColor("#cccccc"));

            }
        }else{
            if (edt_name.length() != 0 && edt_name2.length() != 0){

                btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));

            }else{

                btn_next.setBackgroundColor(Color.parseColor("#cccccc"));

            }
        }

    }
    public static boolean isNumeric(String s)
    {
        return s.matches(".*\\d.*");
    }
}
