package com.smartneck.twofive.CustomerSupport.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Address;
import com.smartneck.twofive.util.Constants;
import com.smartneck.twofive.util.DialogProgress;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.Param;
import com.smartneck.twofive.util.ProgressDialog;

import static com.smartneck.twofive.Member.MemberSelectActivity.admin;


public class Customer1To1InsertActivity extends AppCompatActivity {

    EditText edt_contents;
    Button btn_finish;
    Handler handler;
    ImageView btn_dismiss;
    Spinner spinner;
    Activity activity;
    Context context;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer1_to1_insert);
        context = this;
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
                DialogProgress progressDialog = new DialogProgress(context);
                progressDialog.setMessage("전송중입니다.");
                progressDialog.show();
                if (edt_contents.length() == 0){
                    Toast.makeText(Customer1To1InsertActivity.this, getString(R.string.customer_suppot_input2_toast) , Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String type = spinner.getSelectedItem().toString();

                        HttpConnect httpConnect = new HttpConnect();
                        Param param = new Param();
//                        param.add("token" , User.token);
                        param.add("admin" , admin.getAccount());
                        param.add("contents" , edt_contents.getText().toString());
                        param.add("type" ,type );
                        param.add("country" , Constants.language);
                        Address address = new Address();
                        if (httpConnect.httpConnect(param.getValue() , address.get_1to1Receipt(), true) == 200){
                            Log.e(Constants.TAG, "1to1: " + httpConnect.getReceiveMessage() );
                            progressDialog.dismiss();
                            if (httpConnect.getReceiveMessage().equals(Constants.SUCCESS)){

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Customer1To1InsertActivity.this, getString(R.string.customer_suppot_insert_toast), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }else{
//                                Toast.makeText(Customer1To1InsertActivity.this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
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
