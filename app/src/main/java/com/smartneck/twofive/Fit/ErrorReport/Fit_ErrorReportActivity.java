package com.smartneck.twofive.Fit.ErrorReport;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;

public class Fit_ErrorReportActivity extends AppCompatActivity {

    EditText edt_contents;
    Button btn_finish;
    Handler handler;
    ImageView btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);
        init();
    }

    private void init(){
        handler = new Handler();
        edt_contents = findViewById(R.id.error_contents_edt);
        btn_finish = findViewById(R.id.error_finish_btn);
        btn_dismiss = findViewById(R.id.error_dismiss_btn);

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
                    Toast.makeText(Fit_ErrorReportActivity.this, getString(R.string.customer_suppot_input2_toast), Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                        Fit_Param param = new Fit_Param();
                        param.add("token" , Fit_User.token);
                        param.add("memberNo", Fit_User.MemberNo);
                        param.add("contents" , edt_contents.getText().toString());
                        param.add("name" , Fit_User.name);
                        param.add("country" , Fit_User.language);
                        Fit_Address address = new Fit_Address();
                        if (httpConnect.httpConnect(param.getParam() , address.getInsertError()) == 200){
                            Log.d(Fit_Constants.TAG, "run: " + httpConnect.getReceiveMessage());
                            if (httpConnect.getReceiveMessage().equals(Fit_Constants.SUCCESS)){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Fit_ErrorReportActivity.this, getString(R.string.error_toast), Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Fit_ErrorReportActivity.this, httpConnect.getReceiveMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Fit_ErrorReportActivity.this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });
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
}
