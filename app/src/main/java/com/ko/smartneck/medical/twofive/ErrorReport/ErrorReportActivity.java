package com.ko.smartneck.medical.twofive.ErrorReport;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;

import static com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin;


public class ErrorReportActivity extends AppCompatActivity {

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
                    Toast.makeText(ErrorReportActivity.this, getString(R.string.customer_suppot_input2_toast), Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final HttpConnect httpConnect = new HttpConnect();
                        Param param = new Param();
//                        param.add("token" , User.token);
                        param.add("contents" , edt_contents.getText().toString());
                        param.add("admin" , admin.getAccount());
                        param.add("country" , Constants.language);
                        Address address = new Address();
                        if (httpConnect.httpConnect(param.getValue() , address.getErrorReceipt(), true) == 200){
                            if (httpConnect.getReceiveMessage().equals(Constants.SUCCESS)){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ErrorReportActivity.this, getString(R.string.error_toast), Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ErrorReportActivity.this, httpConnect.getReceiveMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(ErrorReportActivity.this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
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
