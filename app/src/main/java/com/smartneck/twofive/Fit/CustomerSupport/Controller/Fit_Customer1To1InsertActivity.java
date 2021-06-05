package com.smartneck.twofive.Fit.CustomerSupport.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Constants;


public class Fit_Customer1To1InsertActivity extends AppCompatActivity {

    EditText edt_contents;
    Button btn_finish;
    Handler handler;
    ImageView btn_dismiss;
    Spinner spinner;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer1_to1_insert);

        handler = new Handler();

        spinner = findViewById(R.id.customer_1to1_spinner);
        btn_dismiss = findViewById(R.id.customer_1to1_insert_dismiss_Btn);
        edt_contents = findViewById(R.id.customer_1to1_edt);
        btn_finish = findViewById(R.id.customer_1to1_finish_btn);

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_contents.length() == 0){
                    Toast.makeText(Fit_Customer1To1InsertActivity.this, getString(R.string.customer_suppot_input2_toast) , Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String type = spinner.getSelectedItem().toString();

                        Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                        Fit_Param param = new Fit_Param();
                        param.add("token" , Fit_User.token);
                        param.add("memberNo" , Fit_User.MemberNo);
                        param.add("contents" , edt_contents.getText().toString());
                        param.add("type" ,type );
                        param.add("country" , Fit_User.language);
                        Fit_Address address = new Fit_Address();
                        if (httpConnect.httpConnect(param.getParam() , address.getInsert1To1()) == 200){
                            if (httpConnect.getReceiveMessage().equals(Constants.SUCCESS)){

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Fit_Customer1To1InsertActivity.this, getString(R.string.customer_suppot_insert_toast), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }else{
                                Toast.makeText(Fit_Customer1To1InsertActivity.this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    }
                }).start();


            }
        });
        edt_contents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_contents.length() > 0) {
                    btn_finish.setBackgroundColor(Color.parseColor("#cc1b17"));
                }else{
                    btn_finish.setBackgroundColor(Color.parseColor("#cccccc"));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


//    private String getRadioChecked(){
//        String type = "";
//        if (radio_1.isChecked()){
//            type = "App";
//        }else if (radio_2.isChecked()){
//            type = "Product";
//        }else if (radio_3.isChecked()){
//            type = "suggestion";
//        }else if (radio_4.isChecked()){
//            type = "Etc";
//        }
//        return type;
//    }
}
