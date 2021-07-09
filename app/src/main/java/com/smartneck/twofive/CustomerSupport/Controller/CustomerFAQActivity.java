package com.smartneck.twofive.CustomerSupport.Controller;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.smartneck.twofive.CustomerSupport.Adapter.CustomerFaqAdapter;
import com.smartneck.twofive.CustomerSupport.Item.CustomerFaqItem;
import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Address;
import com.smartneck.twofive.util.Constants;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.Param;

import static com.smartneck.twofive.util.Constants.language;

public class CustomerFAQActivity extends AppCompatActivity {
    Handler handler;
    ListView faqList;
    ArrayList<CustomerFaqItem> faqItems;
    ImageView btn_dismiss;
    CustomerFaqAdapter customerFaqAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_faq);

        handler = new Handler();
        btn_dismiss = findViewById(R.id.faq_dismiss_btn);
        faqItems = new ArrayList<>();
        faqList = findViewById(R.id.faq_listview);
        customerFaqAdapter = new CustomerFaqAdapter(faqItems , getLayoutInflater());
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
                HttpConnect httpConnect = new HttpConnect();
                Param param = new Param();

                param.add("country" , language);
                Address address = new Address();

                if (httpConnect.httpConnect(param.getValue() , address.getLoadFAQList() , true) == 200){
                    if (!httpConnect.getReceiveMessage().equals(Constants.FAIL)){
                        getFAQListJson(httpConnect.getReceiveMessage());
                    }else{
                        Log.d(Constants.TAG, "run: FAQ failed");
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
                if (country.equals(language)){
                    faqItems.add(new CustomerFaqItem(questions , answer));

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
