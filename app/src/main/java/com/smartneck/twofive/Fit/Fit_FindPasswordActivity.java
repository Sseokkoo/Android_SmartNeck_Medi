package com.smartneck.twofive.Fit;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;

public class Fit_FindPasswordActivity extends AppCompatActivity {
    Spinner phone1;
    EditText account, name1, name2, name3, phone2, phone3;
    Button btn_find_password, btn_tmp_password;
    Handler handler;
    ImageView btn_dismiss;
    private Spinner spn_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_find_password);
        init();
        setEvent();
    }

    private void init() {
        handler = new Handler();
        phone1 = findViewById(R.id.find_account_phone1);
        phone2 = findViewById(R.id.find_account_phone2);
        phone3 = findViewById(R.id.find_account_phone3);
        name1 = findViewById(R.id.find_password_name1);
        name2 = findViewById(R.id.find_password_name2);
        name3 = findViewById(R.id.find_password_name3);
        account = findViewById(R.id.find_password_account);
        btn_find_password = findViewById(R.id.find_password_find_btn);
        btn_tmp_password = findViewById(R.id.find_password_tmp_password);
        btn_dismiss = findViewById(R.id.find_password_dismiss);
        spn_country = findViewById(R.id.spn_country);
        if (!Fit_User.language.equals("en")) {
            name3.setVisibility(View.GONE);
            name2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            name3.setVisibility(View.VISIBLE);
            name2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
        spn_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    Fit_User.country = Fit_Constants.KR;
                }else if (i == 1){
                    Fit_User.country = Fit_Constants.US;
                }else if (i == 2){
                    Fit_User.country = Fit_Constants.CN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setEvent() {
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = "";
                String phone = "";
                if (Fit_User.language.equals("en")) {
//                    if (name1.length() == 0 || name3.length() == 0) {
//                        Toast.makeText(FindPasswordActivity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    String firstName = name1.getText().toString().trim();
                    String middleName = name2.getText().toString().trim();
                    String lastName = name3.getText().toString().trim();

                    fullName = firstName + " " + middleName + " " + lastName;
                } else {
//                    if (name1.length() == 0 || name2.length() == 0) {
//                        Toast.makeText(FindPasswordActivity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    String firstName = name1.getText().toString().trim();
                    String middleName = name2.getText().toString().trim();
                    fullName = firstName + " " + middleName;

                }
                String countryCode = spn_country.getSelectedItem().toString();
                phone = countryCode +"-"+ phone1.getSelectedItem().toString() +"-"+ phone2.getText().toString() + "-"+phone3.getText().toString();

                Log.e("확인",fullName+phone);

                getPassword(fullName, phone, account.getText().toString(), "password");
            }
        });
        btn_tmp_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = "";
                String phone = "";
                if (Fit_User.language.equals("en")) {
//                    if (name1.length() == 0 || name3.length() == 0) {
//                        Toast.makeText(FindPasswordActivity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    String firstName = name1.getText().toString().trim();
                    String middleName = name2.getText().toString().trim();
                    String lastName = name3.getText().toString().trim();

                    fullName = firstName + " " + middleName + " " + lastName;
                } else {
//                    if (name1.length() == 0 || name2.length() == 0) {
//                        Toast.makeText(FindPasswordActivity.this, getString(R.string.toast_insert_name), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    String firstName = name1.getText().toString().trim();
                    String middleName = name2.getText().toString().trim();
                    fullName = firstName + " " + middleName;

                }

                String countryCode = spn_country.getSelectedItem().toString();
                phone = countryCode +"-"+ phone1.getSelectedItem().toString() +"-"+ phone2.getText().toString() + "-"+phone3.getText().toString();

                Log.e("확인",fullName+phone);

                getPassword(fullName, phone, account.getText().toString(), "tmpPassword");

            }
        });

    }

    private void getPassword(final String fullName, final String phone, final String account, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();
                param.add("name", fullName);
                param.add("phone", phone);
                param.add("account", account);
                param.add("type", type);
                Fit_Address address = new Fit_Address();
                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam(), address.getFindAccount()) == 200) {
//                    String account[] = httpConnect.getReceiveMessage().split("@");
//                    int accountLength = account[0].length();
//                    int accountLength2 = account[1].length();
//
//                    String privateAccount = account[0].substring(0 , accountLength-2);
//                    String privateAccount2 = account[1].substring(0 ,accountLength2-4);
//                    privateAccount += "**";
//                    privateAccount2 += "****";

                    final String result = httpConnect.getReceiveMessage();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (type.equals("password")) {
                                if (httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)){
                                    Toast.makeText(Fit_FindPasswordActivity.this, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show();
                                }else{
                                    showAlertDialog(result);

                                }

                            } else if (type.equals("tmpPassword")) {
                                if (httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)) {
                                    Toast.makeText(Fit_FindPasswordActivity.this, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Fit_FindPasswordActivity.this, getString(R.string.send_to_mail), Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }

                        }
                    });
                    Log.d(Fit_Constants.TAG, "run: " + httpConnect.getReceiveMessage());
                }
            }
        }).start();
    }

    private void showAlertDialog(String result) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_account, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dismiss = view.findViewById(R.id.find_account_ok);
        TextView contents = view.findViewById(R.id.find_account_contents);
        SpannableStringBuilder sps = null;


        if (Fit_User.language.equals("ko")){
            sps = new SpannableStringBuilder("회원님의 비밀번호는 " + result + "입니다.\n\n 비밀번호가 기억나지 않는 경우에는 임시 비밀번호 발송 기능을 이해주세요.");
            sps.setSpan(new StyleSpan(Typeface.BOLD), 11, 11 + result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if (Fit_User.language.equals("en")){
            sps = new SpannableStringBuilder("Your password is " + result + "\n\n If you have forgotten your password, please use the temporary password sending function.");
            sps.setSpan(new StyleSpan(Typeface.BOLD), 16, 16 + result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        contents.setText(sps);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        dialog.show();
    }
}
