package com.smartneck.twofive.Fit.UserInfoEdit;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;


public class Fit_EditPhoneActivity extends AppCompatActivity {

    Spinner spn_country;
    EditText edt_pw, edt_phone1 , edt_phone2, edt_phone3;
    Button btn_save;
    Handler handler;


    ImageView btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit_phone);
        init();
    }


    private void init() {
        handler = new Handler();
        spn_country = findViewById(R.id.edit_phone_country);
        edt_pw = findViewById(R.id.edit_pw);
        btn_save = findViewById(R.id.edit_btn_save);
        edt_phone1 = findViewById(R.id.edt_phone_1);
        edt_phone2 = findViewById(R.id.edt_phone_2);
        edt_phone3 = findViewById(R.id.edt_phone_3);
        btn_dismiss = findViewById(R.id.edit_phone_dismiss_btn);

        String[] phoneSplit = Fit_User.phone.split("-");
        Log.e(Fit_Constants.TAG, "init: " + Fit_User.phone);
        if (phoneSplit.length != 0){

            if (phoneSplit.length == 3){
                edt_phone2.setText(phoneSplit[1]);
                edt_phone3.setText(phoneSplit[2]);
            }else if (phoneSplit.length == 4){
                edt_phone2.setText(phoneSplit[2]);
                edt_phone3.setText(phoneSplit[3]);
            }

        }


        setEvent();

    }

    private void setEvent() {


        spn_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_pw.length() == 0){
                    Toast.makeText(Fit_EditPhoneActivity.this, getString(R.string.customer_suppot_input_toast), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_phone2.length() == 0 || edt_phone3.length() == 0){
                    Toast.makeText(Fit_EditPhoneActivity.this, getString(R.string.customer_suppot_input_toast), Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String phone = spn_country.getSelectedItem().toString();
                        phone += "-";
                        phone += edt_phone1.getText().toString();
                        phone += "-";
                        phone += edt_phone2.getText().toString();
                        phone += "-";
                        phone += edt_phone3.getText().toString();
                        final Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                        Fit_Param param = new Fit_Param();
                        param.add("token" , Fit_User.token);
                        param.add("memberNo" , Fit_User.MemberNo);
                        param.add("pw" , edt_pw.getText().toString());
                        param.add("phone" , phone);
                        param.add("type" , "phone");

                        Fit_Address address = new Fit_Address();
                        if (httpConnect.httpConnect(param.getParam() , address.getUpdateUserInfo()) == 200){
                            if (httpConnect.getReceiveMessage().equals("success")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(Fit_EditPhoneActivity.this, getString(R.string.customer_suppot_edit_toast), Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(Fit_EditPhoneActivity.this, getString(R.string.customer_suppot_input_toast), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }


                    }
                }).start();


            }
        });
        edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonBackGroundColor();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt_phone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonBackGroundColor();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt_phone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonBackGroundColor();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setButtonBackGroundColor(){
        if (edt_pw.length() > 0 && edt_phone2.length() > 0 && edt_phone3.length() > 0){
            btn_save.setBackgroundColor(Color.parseColor("#cc1b17"));

        }else{
            btn_save.setBackgroundColor(Color.parseColor("#cccccc"));

        }
    }


}
