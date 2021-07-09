package com.smartneck.twofive.Fit.Notice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fit_NoticeListActivity extends AppCompatActivity {
    ListView noticeList;
    ArrayList<Fit_NoticeItem> noticeItems;
    Fit_NoticeAdapter noticeAdapter;
    Handler handler;
    ImageView btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotice();
    }

    private void init(){
        handler = new Handler();
        noticeItems = new ArrayList<>();
        noticeList = findViewById(R.id.notice_listview);
        btn_dismiss = findViewById(R.id.notice_list_dismiss_Btn);
        noticeAdapter = new Fit_NoticeAdapter(noticeItems , getLayoutInflater());
        noticeList.setAdapter(noticeAdapter);

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext() , Fit_NoticeActivity.class);
                intent.putExtra("no",noticeItems.get(i).getNo());
                intent.putExtra("title" , noticeItems.get(i).getTitle());
                intent.putExtra("contents" , noticeItems.get(i).getContents());
                intent.putExtra("date" , noticeItems.get(i).getDate());
                startActivity(intent);
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getNotice(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                Fit_Param param = new Fit_Param();
                param.add("token" , Fit_User.token);
                Fit_Address address = new Fit_Address();
                if (httpConnect.httpConnect(param.getParam() , address.getLoadNotice()) == 200){
                    if (!httpConnect.getReceiveMessage().equals(Constants.FAIL)){
                        getNoticeJson(httpConnect.getReceiveMessage());
                    }
                }
            }
        }).start();
    }
    public void getNoticeJson(String JsonString) {
        noticeItems.clear();
        try {

            JSONArray jsonArray = new JSONArray(JsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String contents = jsonObject.getString("contents");
                String[] dateSplit = jsonObject.getString("date").split(" ");
                String date = dateSplit[0];
                String country = jsonObject.getString("country");
//                int no = jsonObject.getInt("No");
                if (Fit_User.language.equals(country)){
                    noticeItems.add(new Fit_NoticeItem(title, contents, 0, date));

                }


            }

            for (int i = noticeItems.size()-1; 0 <= i; i--){
                noticeItems.get(i).setNo(i+1);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    noticeAdapter.notifyDataSetChanged();

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
