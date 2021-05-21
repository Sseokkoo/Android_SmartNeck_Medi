package com.smartneck.twofive.UserInfoEdit;

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
import android.widget.Toast;

import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Address;
import com.smartneck.twofive.util.Constants;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.Param;


public class EditPasswordActivity extends AppCompatActivity {

    EditText edt_pw_current , edt_pw_new , edt_pw_confirm;
    Button save;
    Handler handler;
    ImageView btn_dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_password);
        init();
    }

    private void init(){
        handler = new Handler();
        edt_pw_current = findViewById(R.id.edit_current_pw);
        edt_pw_new = findViewById(R.id.edit_new_pw);
        edt_pw_confirm = findViewById(R.id.edit_confirm);
        save = findViewById(R.id.btn_save_pw);
        btn_dismiss = findViewById(R.id.edit_pw_dismiss_btn);

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String newPw = edt_pw_new.getText().toString();
//                String confirmPw = edt_pw_confirm.getText().toString();
//                if (edt_pw_confirm.length() < 0 || edt_pw_current.length() < 0 || edt_pw_new.length() < 0){
//                    Toast.makeText(EditPasswordActivity.this, getString(R.string.customer_suppot_input_toast), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (!newPw.equals(confirmPw)){
//                    Toast.makeText(EditPasswordActivity.this, getString(R.string.customer_suppot_input_toast), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpConnect httpConnect = new HttpConnect();
//                        Param param = new Param();
////                        param.add("token" , User.token);
//                        param.add("memberNo" , member.getMemberNo());
//                        param.add("type" , "pw");
//                        param.add("pw" , edt_pw_current.getText().toString());
//                        param.add("newPw" , edt_pw_new.getText().toString());
//                        Address address = new Address();
//                        if (httpConnect.httpConnect(param.getValue() ,address.getUpdateUserInfo(), true) == 200){
//                            if (httpConnect.getReceiveMessage().equals("success")){
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(EditPasswordActivity.this, getString(R.string.customer_suppot_edit_toast), Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//                                });
//
//                            }else{
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(EditPasswordActivity.this, getString(R.string.customer_suppot_input_toast), Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//
//                            }
//                        }
//
//                    }
//                }).start();
//            }
//        });
        edt_pw_confirm.addTextChangedListener(new TextWatcher() {
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
        edt_pw_new.addTextChangedListener(new TextWatcher() {
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
        edt_pw_current.addTextChangedListener(new TextWatcher() {
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
    }

    private void setButtonBackGroundColor(){
        if (edt_pw_current.length() > 0  && edt_pw_confirm.length() > 0 && edt_pw_new.length() > 0){
            Log.d(Constants.TAG, "setButtonBackGroundColor: red");
            save.setBackgroundColor(Color.parseColor("#cc1b17"));

        }else{
            Log.d(Constants.TAG, "setButtonBackGroundColor: grey");
            save.setBackgroundColor(Color.parseColor("#cccccc"));

        }
    }
}
