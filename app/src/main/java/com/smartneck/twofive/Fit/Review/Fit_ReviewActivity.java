package com.smartneck.twofive.Fit.Review;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;

public class Fit_ReviewActivity extends AppCompatActivity {

    ListView reviewList;
    ArrayList<Fit_ReviewItem> reviewItems;
    EditText edt_contents;
    ImageView btn_send , btn_dismiss;
    Handler handler;
    Fit_ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        init();
    }

    private void init() {
        handler = new Handler();
        reviewList = findViewById(R.id.review_listview);
        edt_contents = findViewById(R.id.review_edt);
        btn_send = findViewById(R.id.review_send);
        btn_dismiss = findViewById(R.id.review_dismiss_Btn);
        reviewItems = new ArrayList<>();
        reviewList = findViewById(R.id.review_listview);
        reviewList.setClickable(false);
        reviewAdapter = new Fit_ReviewAdapter(reviewItems, getLayoutInflater());
        reviewList.setAdapter(reviewAdapter);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_contents.getWindowToken(), 0);


        getReview();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_contents.length() == 0) {
                    Toast.makeText(Fit_ReviewActivity.this, getString(R.string.customer_suppot_input2_toast), Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                        Fit_Param param = new Fit_Param();
                        param.add("token", Fit_User.token);
                        param.add("memberNo", Fit_User.MemberNo);
                        param.add("contents", edt_contents.getText().toString());
                        param.add("name" , Fit_User.name);
                        param.add("country" , Fit_User.language);
                        Fit_Address address = new Fit_Address();
                        if (httpConnect.httpConnect(param.getParam(), address.getInsertReview()) == 200) {

                            if (httpConnect.getReceiveMessage().equals(Fit_Constants.SUCCESS)) {
                                //insert success
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Fit_ReviewActivity.this, getString(R.string.customer_suppot_insert_toast), Toast.LENGTH_SHORT).show();
                                        edt_contents.setText("");
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(edt_contents.getWindowToken(), 0);
                                        getReview();
                                    }
                                });
                            } else {
                                //insert fail
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Fit_ReviewActivity.this, Fit_Constants.FAIL, Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        }
                    }
                }).start();
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getReview() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                Fit_Address address = new Fit_Address();
                if (httpConnect.httpConnect(param.getParam(), address.getLoadReview()) == 200) {
                    if (!httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)) {
                        getReviewJson(httpConnect.getReceiveMessage());
                    }
                }
            }
        }).start();
    }

    public void getReviewJson(String JsonString) {
        reviewItems.clear();
        try {

            JSONArray jsonArray = new JSONArray(JsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String contents = jsonObject.getString("contents");
                String name = jsonObject.getString("name");
                String[] dateSplit = jsonObject.getString("date").split(" ");
                String date = dateSplit[0];
                String country = jsonObject.getString("country");

                int no = jsonObject.getInt("No");
                if (country.equals(Fit_User.language)){
                    reviewItems.add(new Fit_ReviewItem(name, no, contents, date));

                }

//                Log.d(TAG, "getUserInfoJson: MemberNo -> " + User.MemberNo);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
