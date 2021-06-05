package com.smartneck.twofive.Fit.CustomerSupport.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartneck.twofive.Fit.CustomerSupport.Adapter.Fit_CustomerFaqAdapter;
import com.smartneck.twofive.Fit.CustomerSupport.Item.Fit_CustomerFaqItem;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fit_CustomerFAQActivity extends AppCompatActivity {
    Handler handler;
    ListView faqList;
    ArrayList<Fit_CustomerFaqItem> faqItems;
    ImageView btn_dismiss;
    Fit_CustomerFaqAdapter customerFaqAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_faq);

        handler = new Handler();
        btn_dismiss = findViewById(R.id.faq_dismiss_btn);
        faqItems = new ArrayList<>();
        faqList = findViewById(R.id.faq_listview);
        customerFaqAdapter = new Fit_CustomerFaqAdapter(faqItems , getLayoutInflater());
        faqList.setAdapter(customerFaqAdapter);

        faqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tv_anwser = view.findViewById(R.id.faq_answer);
                if (tv_anwser.getVisibility() == View.VISIBLE){
                    tv_anwser.setVisibility(View.GONE);
                }else if (tv_anwser.getVisibility() == View.GONE){
                    tv_anwser.setVisibility(View.VISIBLE);
                }


            }
        });
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFaqList();
    }

    private void getFaqList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                Fit_Param param = new Fit_Param();
                param.add("token" , Fit_User.token);
                Fit_Address address = new Fit_Address();

                if (httpConnect.httpConnect(param.getParam() , address.getLoadFAQList()) == 200){
                    if (!httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)){
                        getFAQListJson(httpConnect.getReceiveMessage());
                    }
                }
            }
        }).start();
    }

    public void getFAQListJson(String JsonString) {
        faqItems.clear();
        try {

            JSONArray jsonArray = new JSONArray(JsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String questions = jsonObject.getString("questions");
                String answer = jsonObject.getString("answer");
                String country = jsonObject.getString("country");


//    public Customer1To1Item(String state, String contents, String date) {
                if (country.equals(Fit_User.language)){
                    faqItems.add(new Fit_CustomerFaqItem(questions , answer));

                }


            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    customerFaqAdapter.notifyDataSetChanged();

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
