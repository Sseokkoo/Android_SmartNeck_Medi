//package com.ko.smartneck.medical.twofive.Join;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.ko.smartneck.medical.twofive.R;
//import com.ko.smartneck.medical.twofive.util.Constants;
//import com.ko.smartneck.medical.twofive.Member.User;
//
//import static com.ko.smartneck.medical.twofive.Join.Join_1_Activity.activityList;
//
//public class Join_5_Activity extends AppCompatActivity {
//    Button btn_next;
//    static String joinPhone;
//    ImageView btn_previous;
//    Spinner spn_country;
//    EditText edt_phone , edt_second , edt_third;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_join_5_);
//        init();
//        onClick();
//
//    }
//    protected void onResume() {
//        super.onResume();
//        removeArrayitem();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        removeArrayitem();
//
//    }
//    void removeArrayitem(){
//        if (activityList.size() == 5) {
//            activityList.remove(activityList.size() - 1);
//        }
//    }
//    void init() {
//        btn_next = findViewById(R.id.join_5_next);
//        btn_previous = findViewById(R.id.join_5_previous);
//        edt_phone = findViewById(R.id.join_5_edt_phone);
//        edt_second = findViewById(R.id.join_5_edt);
//        edt_third = findViewById(R.id.join_5_edt2);
//        spn_country = findViewById(R.id.spn_country);
//
//    }
//
//    void onClick() {
//
//        spn_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i == 0){
//                    User.country = Constants.KR;
//                }else if (i == 1){
//                    User.country = Constants.US;
//                }else if (i == 2){
//                    User.country = Constants.CN;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (edt_second.length() == 0 || edt_third.length() == 0){
//                    Toast.makeText(Join_5_Activity.this, getString(R.string.toast_insert_phone_number), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String countryCode = spn_country.getSelectedItem().toString();
////                int firstNum = Integer.parseInt(edt_phone.getText().toString());
//                joinPhone = countryCode + "-" + edt_phone.getText().toString() + "-" + edt_second.getText().toString() + "-" + edt_third.getText().toString();
//                Log.d(Constants.TAG, "onClick: " + joinPhone);
//                activityList.add(Join_5_Activity.this);
//
//                Intent intent = new Intent(getApplicationContext() , Join_6_Activity.class);
//                startActivity(intent);
//
//            }
//        });
//        btn_previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        edt_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (edt_phone.length() > 0 && edt_second.length() > 0 && edt_third.length() > 0){
//                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
//                }
//            }
//        });
//
//
//        edt_second.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (edt_phone.length() > 0 && edt_second.length() > 0 && edt_third.length() > 0){
//                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
//                }
//            }
//        });
//        edt_third.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (edt_phone.length() > 0 && edt_second.length() > 0 && edt_third.length() > 0){
//                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
//                }
//            }
//        });
//    }
//}
