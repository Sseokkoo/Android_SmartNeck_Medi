package com.smartneck.twofive.Fit.CustomerSupport.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.smartneck.twofive.Fit.CustomerSupport.Adapter.Fit_Customer1To1Adapter;
import com.smartneck.twofive.Fit.CustomerSupport.Item.Fit_Customer1To1Item;
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

public class Fit_Customer1To1ListActivity extends AppCompatActivity {
    ListView _1to1List;
    ArrayList<Fit_Customer1To1Item> customer1To1Items;
    Fit_Customer1To1Adapter customer1To1Adapter;
    Handler handler;
    ImageView btn_dismiss;
    Button btn_apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer1_to1_list);

        handler = new Handler();
        _1to1List = findViewById(R.id.customer_1to1_listView);
        btn_dismiss = findViewById(R.id.customer_1to1_list_dismiss_Btn);
        btn_apply = findViewById(R.id.customer_1to1_apply_btn);
        customer1To1Items = new ArrayList<>();
        customer1To1Adapter = new Fit_Customer1To1Adapter(customer1To1Items, getLayoutInflater());
        _1to1List.setAdapter(customer1To1Adapter);

        _1to1List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Fit_Customer1To1Activity.class);
                //해당 액티비티로 정보 전송\

                intent.putExtra("state", customer1To1Items.get(i).getState());
                intent.putExtra("contents", customer1To1Items.get(i).getContents());
                intent.putExtra("date", customer1To1Items.get(i).getDate());
                intent.putExtra("no", i + 1);
                startActivity(intent);
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , Fit_Customer1To1InsertActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get1to1List();
    }

    private void get1to1List() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                param.add("memberNo", Fit_User.MemberNo);
                Fit_Address address = new Fit_Address();
                if (httpConnect.httpConnect(param.getParam(), address.getLoad1To1List()) == 200) {
                    if (!httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)) {
                        getCustomerListJson(httpConnect.getReceiveMessage());
                    }
                }

            }
        }).start();
    }

    public void getCustomerListJson(String JsonString) {
        customer1To1Items.clear();
        try {

            JSONArray jsonArray = new JSONArray(JsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String state = jsonObject.getString("state");
                String contents = jsonObject.getString("contents");
                String[] dateSplit = jsonObject.getString("date").split(" ");
                String country = jsonObject.getString("country");
                String date = dateSplit[0];
                String state2 = "";
                if (state.equals("waiting")){
                    state2 = getString(R.string._1to1_state_waiting);
                }else{
                    state2 = getString(R.string._1to1_state_complete);
                }


//    public Customer1To1Item(String state, String contents, String date) {
                    customer1To1Items.add(new Fit_Customer1To1Item(state2, contents, date));




            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    customer1To1Adapter.notifyDataSetChanged();

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
