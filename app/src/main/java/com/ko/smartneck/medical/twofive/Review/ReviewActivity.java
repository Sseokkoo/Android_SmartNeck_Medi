package com.ko.smartneck.medical.twofive.Review;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReviewActivity extends AppCompatActivity {

    ListView reviewList;
    ArrayList<ReviewItem> reviewItems;
    EditText edt_contents;
    ImageView btn_send , btn_dismiss;
    Handler handler;
    ReviewAdapter reviewAdapter;

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
        reviewAdapter = new ReviewAdapter(reviewItems, getLayoutInflater());
        reviewList.setAdapter(reviewAdapter);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_contents.getWindowToken(), 0);


        getReview();

//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (edt_contents.length() == 0) {
//                    Toast.makeText(ReviewActivity.this, getString(R.string.customer_suppot_input2_toast), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpConnect httpConnect = new HttpConnect();
//                        Param param = new Param();
////                        param.add("token", User.token);
//                        param.add("memberNo", user.getMemberNo());
//                        param.add("contents", edt_contents.getText().toString());
//                        param.add("name" , user.getName());
//                        param.add("country" , Constants.language);
//                        Address address = new Address();
//                        if (httpConnect.httpConnect(param.getValue(), address.getInsertReview(), true) == 200) {
//
//                            if (httpConnect.getReceiveMessage().equals(Constants.SUCCESS)) {
//                                //insert success
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(ReviewActivity.this, getString(R.string.customer_suppot_insert_toast), Toast.LENGTH_SHORT).show();
//                                        edt_contents.setText("");
//                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        imm.hideSoftInputFromWindow(edt_contents.getWindowToken(), 0);
//                                        getReview();
//                                    }
//                                });
//                            } else {
//                                //insert fail
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(ReviewActivity.this, Constants.FAIL, Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                            }
//
//                        }
//                    }
//                }).start();
//            }
//        });

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
                HttpConnect httpConnect = new HttpConnect();
                Param param = new Param();
//                param.add("token", User.token);
                Address address = new Address();
                if (httpConnect.httpConnect(param.getValue(), address.getLoadReview(), true) == 200) {
                    if (!httpConnect.getReceiveMessage().equals(Constants.FAIL)) {
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
                if (country.equals(Constants.language)){
                    reviewItems.add(new ReviewItem(name, no, contents, date));

                }

//                Log.d(TAG, "getUserInfoJson: memberNo -> " + User.memberNo);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
