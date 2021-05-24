package com.smartneck.twofive.Fit.Main;

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

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Address;
import com.smartneck.twofive.Fit.util.Commend;
import com.smartneck.twofive.Fit.util.Constants;
import com.smartneck.twofive.Fit.util.HttpConnect;
import com.smartneck.twofive.Fit.util.Param;
import com.smartneck.twofive.Fit.util.StringUtils;
import com.smartneck.twofive.Fit.util.User.Preset;
import com.smartneck.twofive.Fit.util.User.User;

import static com.smartneck.twofive.Fit.GlobalApplication.getApllication;
import static com.smartneck.twofive.Fit.Main.MainActivity.audioStop;
import static com.smartneck.twofive.Fit.Main.MainActivity.isClick;
import static com.smartneck.twofive.Fit.Main.MainActivity.protocolType;
import static com.smartneck.twofive.Fit.Main.MainActivity.setMessage;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_HEIGHT;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_WEIGHT;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_WEIGHT_MAX;
import static com.smartneck.twofive.Fit.util.Constants.TAG;

public class WeightSettingFragment extends Fragment {
    ViewGroup view;
    Context mContext;
    Handler handler;
    String type = "";
    static boolean isWeight;
    boolean isComplete;
    static double currentWeight;
    Button btn_home, btn_exercise_setting, btn_previous;
    ImageButton btn_up, btn_down;
    TextView tv_result, tv_result_max;
    static boolean isMove;
    boolean isDestroy = false;
    public static int tmpSetup;
    ImageView gif;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_weight_setting, container, false);
        mContext = getApllication();
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
    public void onStart() {
        super.onStart();
        if (protocolType.equals("3a")){
            setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(Preset.measureSetup) + " 00 00"));

        }else if (protocolType.equals("3b")){
            setMessage(new Commend().sendWeightMove((byte)Preset.measureSetup));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        updateWeight();

        tmpSetup = Preset.measureSetup;
        isComplete = true;
        isDestroy =true;
        type = "";
        isWeight = false;

    }

    void init() {
        isWeight = true;
        isDestroy = false;
        CFG_WEIGHT_MAX[1] = 0;
        currentWeight = 0;
        handler = new Handler();
        btn_up = view.findViewById(R.id.btn_f_weight_setting_up);
        btn_down = view.findViewById(R.id.btn_f_weight_setting_down);
        btn_home = view.findViewById(R.id.btn_f_weight_setting_home);
        btn_exercise_setting = view.findViewById(R.id.btn_f_weight_setting_start);
        btn_previous = view.findViewById(R.id.btn_f_weight_setting_seat);

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
                MainActivity.tabPos = 1;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        btn_up.setEnabled(true);
                        btn_down.setEnabled(true);
                    }
                });

            }
        }).start();


    }




    void onClickUpDown() {
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSeatMove()) {
                    return;
                }

                if (currentWeight == 6.5) return;
                if (CFG_HEIGHT[1] > 0) return;
                MainActivity.isClick = true;

                isMove = true;
                Preset.measureSetup += 5;
                currentWeight += 0.5;
                tv_result.setText(String.valueOf(currentWeight));
                tv_result_max.setText("0.0");
                CFG_WEIGHT_MAX[1] = 0;
                resultMax = 0;
                tv_result_max.setText(String.valueOf(resultMax));

                if (protocolType.equals("3a")){
                    ((MainActivity) MainActivity.mContext).setMessage(StringUtils.getCommand("44 3A 04 02 01"));

                }else if (protocolType.equals("3b")){
                    setMessage(new Commend().sendWeightMove((byte)Preset.measureSetup));
                }
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSeatMove()) {
                    return;
                }

                if (currentWeight == 0) return;
                if (CFG_HEIGHT[1] > 0) return;
                MainActivity.isClick = true;
                isMove = true;
                Preset.measureSetup -= 5;
                currentWeight -= 0.5;
                tv_result.setText(String.valueOf(currentWeight));
                CFG_WEIGHT_MAX[1] = 0;
                resultMax = 0;
                tv_result_max.setText(String.valueOf(resultMax));
                if (protocolType.equals("3a")){
                    ((MainActivity) MainActivity.mContext).setMessage(StringUtils.getCommand("44 3A 04 02 02"));

                }else if (protocolType.equals("3b")){
                    setMessage(new Commend().sendWeightMove((byte)Preset.measureSetup));
                }
            }
        });

    }

    void onClickButton() {
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                type = "HOME";
                updateWeight();
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type = "SEAT";
                updateWeight();
            }
        });
        btn_exercise_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type = "PROCEDURE_DEFAULT";
                updateWeight();
            }
        });
    }

    double resultMax = 0;

    void setTextThread() {
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
                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isComplete) break;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            double result = CFG_WEIGHT[1] * 0.1;


//                            if (isMove){
//                                tv_result.setText(String.valueOf(currentWeight));
//                                return;
//                            }
                            if (CFG_HEIGHT[1] > 0) {
                                if (CFG_WEIGHT[1] > 0) {
                                    if (CFG_WEIGHT[1] >= 70) {
                                        result += 0.5;
                                    } else if (CFG_WEIGHT[1] >= 45 && CFG_WEIGHT[1] <= 65) {
                                        result += 1;
                                    } else if (CFG_WEIGHT[1] >= 5 && CFG_WEIGHT[1] <= 40) {
                                        result += 1.5;
                                    }
                                }
//                                if (User.language.equals("en")) {
//                                    result *= Constants.POUND;
//                                }


                                if (User.language.equals("en")){
                                    tv_result.setText(String.format("%.1f", result * Constants.POUND));
                                }else{
                                    tv_result.setText(String.format("%.1f", result));
                                }

                                if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1]) resultMax = result;

                                if (User.language.equals("en")){
                                    tv_result_max.setText(String.format("%.1f", resultMax * Constants.POUND));

                                }else{
                                    tv_result_max.setText(String.format("%.1f", resultMax));

                                }
                            } else {
                                if (!isClick) {
                                    currentWeight = (double) Preset.measureSetup * 0.1;

                                }
                                tv_result.setText(String.valueOf(currentWeight));

                            }


                        }
                    });

                }
            }
        }).start();
    }

    private void updateWeight() {

        new Thread(new Runnable() {
            @Override
            public void run() {


//                final double measureSetup = currentWeight * 10;
                double max = resultMax * 10;
                int tmpmax = (int) max;
                if (tmpmax != 0) {
                    Preset.MaxWeight = tmpmax;

                }
                Param param = new Param();
                HttpConnect httpConnect = new HttpConnect();

                param.add("maxWeight", tmpmax);
                param.add("token", User.token);
                param.add("measureSetup", Preset.measureSetup);
                param.add("memberNo", User.MemberNo);
                param.add("type", "measureSetup");
                param.add("productNo", Preset.DEVICE_NAME);
                Address address = new Address();

                Log.d(TAG, "run: Measure setup : " + Preset.measureSetup);
                Log.d(TAG, "run: Measure setup : " + Preset.measureSetup);
                Log.d(TAG, "run: Measure setup : " + Preset.measureSetup);
                Log.d(TAG, "param: " + param.getParam() + "  url: " + address.getUpdatePreset());

                if (httpConnect.httpConnect(param.getParam(), address.getUpdatePreset()) == 200) {
                    Log.d(TAG, "receive message: " + httpConnect.getReceiveMessage());
                    if (httpConnect.getReceiveMessage().equals("success")) {

                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).start();


    }

    boolean isSeatMove() {
        if (MainActivity.isSeatMove) {
            Toast.makeText(mContext, getString(R.string.toast_chair_control), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

}
