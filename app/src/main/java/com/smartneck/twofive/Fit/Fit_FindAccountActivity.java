package com.smartneck.twofive.Fit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;


public class Fit_FindAccountActivity extends AppCompatActivity {
    ImageView btn_dismiss;
    Button btn_find;
    Spinner phone1;
    EditText phone2 , phone3 , name1 , name2 , name3;
    Handler handler;
    static String ACCOUNT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_find_account);
        init();
        setEvent();
    }

    private void init(){
        handler = new Handler();

        btn_dismiss = findViewById(R.id.find_account_dismiss);
        btn_find = findViewById(R.id.find_account_find_btn);
        phone1 = findViewById(R.id.find_account_phone1);
        phone2 = findViewById(R.id.find_account_phone2);
        phone3 = findViewById(R.id.find_account_phone3);
        name1 = findViewById(R.id.find_account_name1);
        name2 = findViewById(R.id.find_account_name2);
        name3 = findViewById(R.id.find_account_name3);
        if (!Fit_User.language.equals("en")){
            name3.setVisibility(View.GONE);
            name2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }else{
            name3.setVisibility(View.VISIBLE);
            name2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
    }


    private void setEvent(){
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName = "";
                String phone = "";
                if (Fit_User.language.equals("en")){
//                    if (name1.length() == 0 || name3.length() == 0){
//                        Toast.makeText(FindAccountActivity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    String firstName = name1.getText().toString().trim();
                    String middleName = name2.getText().toString().trim();
                    String lastName = name3.getText().toString().trim();

                    fullName = firstName + " " + middleName + " " + lastName;
                }else{
//                    if (name1.length() == 0 || name2.length() == 0){
//                        Toast.makeText(FindAccountActivity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    String firstName = name1.getText().toString().trim();
                    String middleName = name2.getText().toString().trim();
                    fullName = firstName + " " + middleName;

                }

                phone = phone1.getSelectedItem().toString() + "-" + phone2.getText().toString() + "-" +phone3.getText().toString();
                getAccount(fullName , phone);

//                Intent intent = new Intent(getApplicationContext() , Join_4_Activity.class);
//                startActivity(intent);
            }
        });
    }

    private void getAccount(final String fullName , final String phone){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();
                param.add("name" , fullName);
                param.add("phone" , phone);
                param.add("type" , "account");
                Fit_Address address = new Fit_Address();
                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam() , address.getFindAccount()) == 200 ){
                    String account[] = httpConnect.getReceiveMessage().split("@");
                    int accountLength = account[0].length();
                    int accountLength2 = account[1].length();

                    String privateAccount = account[0].substring(0 , accountLength-2);
                    String privateAccount2 = account[1].substring(0 ,accountLength2-4);
                    privateAccount += "**";
                    privateAccount2 += "****";

//                    final String result = privateAccount + "@" + privateAccount2;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)){
                                Toast.makeText(Fit_FindAccountActivity.this, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final String result = httpConnect.getReceiveMessage();
                            Intent intent = new Intent();

                            intent.putExtra("account", result);

                            setResult(Activity.RESULT_OK, intent);

                            finish();
                            ACCOUNT = httpConnect.getReceiveMessage();


//                            Toast.makeText(FindAccountActivity.this, result, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(FindAccountActivity.this, httpConnect.getReceiveMessage(), Toast.LENGTH_SHORT).show();
//                            showAlertDialog(result);
                        }
                    });
                }
            }
        }).start();
    }
}
