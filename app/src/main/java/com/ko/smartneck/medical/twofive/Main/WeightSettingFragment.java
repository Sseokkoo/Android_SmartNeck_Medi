package com.ko.smartneck.medical.twofive.Main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.Commend;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.ko.smartneck.medical.twofive.GlobalApplication.getApllication;
import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.isClick;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.setMessage;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.tabPos;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.audioStop;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.member;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_HEIGHT;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_WEIGHT;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_WEIGHT_MAX;
import static com.ko.smartneck.medical.twofive.MeasureActivity.moveProcedure;
import static com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin;
import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class WeightSettingFragment extends Fragment {
    ViewGroup view;
    Context mContext;
    Handler handler;
    String type = "";
    static boolean isWeight;
    boolean isComplete;
    static double currentWeight;
    Button btn_home, btn_exercise_setting, btn_previous;
    ImageButton btn_up , btn_down;
    TextView tv_result , tv_result_max;
    static boolean isMove;
    double maxWeight;
    ImageView gif;

    public static int tmpSetup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_weight_setting, container, false);
        mContext = getApllication();
//        ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
        MainActivity.setAudio("weight");

        init();
        setTextThread();
        onClickUpDown();
        onClickButton();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        tabPos = 2;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        isComplete = true;
        type = "";
        if (resultMax != 0){

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s");
            String[] dateSplit = sdf.format(date).split(" ")[0].split("-");
            String year = dateSplit[0];
            int imonth = Integer.valueOf(dateSplit[1]);
            int iday = Integer.valueOf(dateSplit[2]);
            String month = "";
            String day = "";
            if (imonth < 10){
                month = "0" + imonth;
            }else{
                month = imonth + "";
            }

            if (iday < 10){
                day = "0" + iday;
            }else{
                day = iday + "";
            }
            String lately = year + "-" + month + "-" + day;
            member.setLately(lately);
            userPreference.editMember(userPreference.editMember(member));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpConnect httpConnect  =new HttpConnect();
                    Param param = new Param();
                    param.add("admin" , admin.getAccount());
                    param.add("member_no" , member.getMemberNo());
                    param.add("lately" , member.getLately());
                    param.add("uid" , member.getUid());

                    if (httpConnect.httpConnect(param.getValue() , new Address().getLatelyUpdate() , true) == 200){

                    }
                }
            }).start();


        userPreference.addWeight(member , (int) (resultMax * 10));
        preset.setMaxWeight((int) (resultMax * 10));
            userPreference.editPreset(preset);

        }

    }

    void init(){
        isWeight = true;
        CFG_WEIGHT_MAX[1] = 0;
        handler = new Handler();
        btn_up = view.findViewById(R.id.btn_f_weight_setting_up);
        btn_down = view.findViewById(R.id.btn_f_weight_setting_down);
        btn_home = view.findViewById(R.id.btn_f_weight_setting_home);
//        btn_exercise_setting = view.findViewById(R.id.btn_f_weight_setting_start);
        btn_previous = view.findViewById(R.id.btn_f_weight_setting_seat);
        btn_exercise_setting = view.findViewById(R.id.btn_exercise_setting3);

        tv_result = view.findViewById(R.id.tv_f_weight_result);
        tv_result_max = view.findViewById(R.id.tv_f_max_weight_result);
        btn_home.setVisibility(View.GONE);
        btn_up.setEnabled(false);
        btn_down.setEnabled(false);
        gif = view.findViewById(R.id.weight_setting_gif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.height)
                .into(gif);
//        tv_result.setText(String.valueOf(Preset.setup * 0.1));
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        btn_up.setEnabled(true);
                        btn_down.setEnabled(true);
                    }
                });

            }
        }).start();
        setMessage(new Commend().sendWeightMove((byte) preset.getSetup()));

        btn_exercise_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveProcedure = true;
                getActivity().finish();
            }
        });
    }

    void onClickUpDown(){
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSeatMove()){ return; }

                if ((currentWeight + 0.5) > 6.5) {
                    return;
                }else{
                    currentWeight += 0.5;
                }
                if (CFG_HEIGHT[1] > 0) return;
                BleConnectActivity.isClick = true;

                isMove = true;
                preset.setSetup(preset.getSetup() + 5);
                tv_result.setText(String.valueOf(currentWeight));
                tv_result_max.setText("0.0");
                BleConnectActivity.setMessage(new Commend().sendWeightMove((byte) preset.getSetup()));
                CFG_WEIGHT_MAX[1] = 0;
                resultMax = 0;
                tv_result_max.setText(String.valueOf(resultMax));
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSeatMove()){ return; }

                if ((currentWeight - 0.5) < 0) {
                    return;
                }else {
                    if(currentWeight > 0) {
                        currentWeight -= 0.5;
                    }else if(currentWeight < 0){
                        currentWeight = 0;
                    }
                }
                if (CFG_HEIGHT[1] > 0) return;
                BleConnectActivity.isClick = true;
                isMove = true;
                preset.setSetup(preset.getSetup() - 5);

                tv_result.setText(String.valueOf(currentWeight));
                CFG_WEIGHT_MAX[1] = 0;
                resultMax = 0;
                tv_result_max.setText(String.valueOf(resultMax));
                BleConnectActivity.setMessage(new Commend().sendWeightMove((byte) preset.getSetup()));

            }
        });

    }

    void onClickButton(){
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                type = "HOME";
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type = "SEAT";
            }
        });

    }

    double resultMax =0;
    void setTextThread(){
        isComplete = false;
        Log.d(TAG, "setTextThread: " + CFG_WEIGHT[1] + " / " + currentWeight);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_result.setText(String.valueOf(currentWeight));

                    }
                });
                while (true){
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isComplete)break;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            double result = CFG_WEIGHT[1] * 0.1;


//                            if (isMove){
//                                tv_result.setText(String.valueOf(currentWeight));
//                                return;
//                            }

                            if (CFG_HEIGHT[1] > 0){

//                                if (CFG_WEIGHT[1] > 0){
//                                    if (CFG_WEIGHT[1] >= 70){
//                                        result += 0.5;
//                                    }else if (CFG_WEIGHT[1] >= 45 && CFG_WEIGHT[1] <= 65){
//                                        result += 1;
//                                    }else if (CFG_WEIGHT[1] >= 5 && CFG_WEIGHT[1] <= 40){
//                                        result += 1.5;
//                                    }
//                                } //수정13


//                                if (Constants.language.equals("en")){
//                                    result *= Constants.POUND;
//                                }


                                tv_result.setText(String.format("%.1f" , result));
                                if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1]) resultMax = result;

                                if (Constants.language.equals("en")){
                                    tv_result_max.setText(String.format("%.1f" , resultMax * Constants.POUND));

                                }else{
                                    tv_result_max.setText(String.format("%.1f" , resultMax));
//                                    preset.setMaxWeight((int) resultMax * 10);
                                }
                            }else{
                                if (!isClick){
                                    currentWeight = (double) preset.getSetup() * 0.1;

                                }
                                tv_result.setText(String.valueOf(currentWeight));

                            }



                        }
                    });

                }
            }
        }).start();
    }
//    void updateWeight(){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final double measureSetup =  currentWeight * 10;
//                double max =  resultMax * 10;
//                int tmpmax = (int) max;
//                if (tmpmax != 0){
//                    pastPreset.MaxWeight = tmpmax;
//
//                }
//                Param param = new Param();
//
//                param.add("maxWeight" , tmpmax);
////                param.add("token" , User.token);
//                param.add("setup" , (int)measureSetup);
//                param.add("type" , "weight");
//                param.add("productNo" , pastPreset.DEVICE_NAME);
//                Address address = new Address();
//
//                Log.d(TAG, "param: " + param.getValue() + "  url: " + address.getUpdatePreset());
//
//                HttpConnect httpConnect = new HttpConnect();
//                if (httpConnect.httpConnect(param.getValue() , address.getUpdatePreset(), true) == 200){
//                    Log.d(TAG, "receive message: " + httpConnect.getReceiveMessage());
//                    if (httpConnect.getReceiveMessage().equals("success")){
//                        pastPreset.setup = (int) measureSetup;
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
////                                if (!type.equals("")) ((MainActivity) getActivity()).setBottomNavigation(type);
//
//                            }
//                        });
//
//                    }
//                }else{
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//            }
//        }).start();
//
//
//    }
    boolean isSeatMove(){
        if (BleConnectActivity.isSeatMove && tabPos == 2){
            Toast.makeText(mContext, getString(R.string.toast_chair_control), Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }

}
