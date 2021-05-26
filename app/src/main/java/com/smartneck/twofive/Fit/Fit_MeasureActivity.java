package com.smartneck.twofive.Fit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smartneck.twofive.R;
import com.ssomai.android.scalablelayout.ScalableLayout;

import com.smartneck.twofive.Fit.Main.Fit_MainActivity;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Commend;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_NoticeDialog;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.Fit_ProgressDialog;
import com.smartneck.twofive.Fit.util.Fit_StringUtils;
import com.smartneck.twofive.Fit.util.User.Fit_Preset;
import com.smartneck.twofive.Fit.util.User.Fit_User;

import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.isHeiProgress;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.isWeiProgress;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.protocolType;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.setMessage;
import static com.smartneck.twofive.Fit.util.Fit_Constants.POUND;
import static com.smartneck.twofive.Fit.util.Fit_Constants.deviceType;

public class Fit_MeasureActivity extends AppCompatActivity {
    public static boolean isHeight;
    public static Context mContext;
    Handler handler;

    TextView tv_seat;
    TextView tv_height, tv_height_max, tv_weight, tv_weight_max;

    ImageButton btn_seat_down, btn_seat_up;
    ImageButton btn_weight_up, btn_weight_down;
    Button btn_cancel, btn_prev, btn_next, btn_procedure;

    public static boolean isMeasure;

    public static boolean CFG_DEVICE_MOVING = false;

    public static int CFG_SEQ = 0;

    public static int CFG_HEIGHT[] = {0, 0, 0, 0};
    public static int CFG_HEIGHT_MAX[] = {0, 0, 0, 0};
    public static int CFG_WEIGHT[] = {0, 0, 0, 0};
    public static int CFG_WEIGHT_MAX[] = {0, 0, 0, 0};

    public static int CFG_WEIGHT_DEF[] = {0, 0, 0, 0};

    public static boolean moveProcedure;
    public static boolean moveExercise;

    static double currentWeight;
    Fit_ProgressDialog progressDialog;
    //거리변환
    float tmpDistance;
    float tmpMaxDistance;
    final int anglePerMillimeter = 3;
    public static int weightMoveCount;
    public static boolean isWeightMove = false;


    Fit_NoticeDialog noticeDialog;

    boolean isclick = false;

    ImageView gif1, gif2, gif3;

    ScalableLayout scale_seat, scale_height, scale_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        isHeight = intent.getBooleanExtra("height", false);
        mContext = this;
        handler = new Handler();
        isMeasure = true;
        currentWeight = 0;

        scale_seat = findViewById(R.id.scale_seat);
        scale_height = findViewById(R.id.scale_height);
        scale_weight = findViewById(R.id.scale_weight);


        tv_seat = (TextView) findViewById(R.id.tv_seat);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_height_max = (TextView) findViewById(R.id.tv_height_max);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_weight_max = (TextView) findViewById(R.id.tv_weight_max);
        gif1 = findViewById(R.id.measure_seat_gif);
        gif2 = findViewById(R.id.measure_height_gif);
        gif3 = findViewById(R.id.measure_weight_gif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.seat)
                .into(gif1);

        Glide.with(this)
                .asGif()
                .load(R.drawable.height)
                .into(gif2);

        Glide.with(this)
                .asGif()
                .load(R.drawable.height)
                .into(gif3);


        tv_seat.setText(Integer.toString(Fit_Preset.seat));

//        setDeviceType(Constants.deviceType);
        btn_seat_down = (ImageButton) findViewById(R.id.btn_seat_down);
        btn_seat_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("확인", "- btn_seat_down onClick");

                if (Fit_Preset.seat > 0) {

                    Fit_Preset.seat = Fit_Preset.seat - 1;
                    tv_seat.setText(Integer.toString(Fit_Preset.seat));

                    if (protocolType.equals("3a")){
                        ((Fit_MainActivity) Fit_MainActivity.mContext).setMessage(Fit_StringUtils.getCommand("44 3A 03 01 02"));

                    }else if (protocolType.equals("3b")){
                        setMessage(new Fit_Commend().sendSeatMove((byte) Fit_Preset.seat));
                    }

                }
            }
        });

        btn_seat_up = (ImageButton) findViewById(R.id.btn_seat_up);
        btn_seat_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("확인", "- btn_seat_up onClick");

                if (Fit_Preset.seat < 20) {

                    Fit_Preset.seat = Fit_Preset.seat + 1;
                    tv_seat.setText(Integer.toString(Fit_Preset.seat));

                    if (protocolType.equals("3a")){
                        ((Fit_MainActivity) Fit_MainActivity.mContext).setMessage(Fit_StringUtils.getCommand("44 3A 03 01 01"));

                    }else if (protocolType.equals("3b")){
                        setMessage(new Fit_Commend().sendSeatMove((byte) Fit_Preset.seat));
                    }
                }
            }
        });

        btn_weight_up = (ImageButton) findViewById(R.id.btn_weight_up);
        btn_weight_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CFG_HEIGHT[1] > 0) return;
                if ((currentWeight + 0.5) > 6.5) {
                    return;
                }
                weightMoveCount = 0;

                isWeightMove = true;
                CFG_DEVICE_MOVING = true;
                CFG_WEIGHT_MAX[1] = 0;
                tv_weight_max.setText("0.0");

                currentWeight += 0.5;
                tv_weight.setText(String.valueOf(currentWeight));


                if (protocolType.equals("3a")){
                    ((Fit_MainActivity) Fit_MainActivity.mContext).setMessage(Fit_StringUtils.getCommand("44 3A 04 02 01"));

                }else if (protocolType.equals("3b")){
                    setMessage(new Fit_Commend().sendWeightMove((byte)(currentWeight * 10)));
                }

            }


//            }
        });

        btn_weight_down = (ImageButton) findViewById(R.id.btn_weight_down);
        btn_weight_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CFG_HEIGHT[1] > 0) return;
                if ((currentWeight - 0.5) < 0) {
                    return;
                }
                weightMoveCount = 0;

                isWeightMove = true;
                CFG_DEVICE_MOVING = true;
                CFG_WEIGHT_MAX[1] = 0;
                tv_weight_max.setText("0.0");

                currentWeight -= 0.5;

                tv_weight.setText(String.valueOf(currentWeight));


                if (protocolType.equals("3a")){
                    ((Fit_MainActivity) Fit_MainActivity.mContext).setMessage(Fit_StringUtils.getCommand("44 3A 04 02 02"));

                }else if (protocolType.equals("3b")){
                    setMessage(new Fit_Commend().sendWeightMove((byte)(currentWeight * 10)));
                }
            }
        });


        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("확인", "- btn_cancel onClick");

                finish();
            }
        });

        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("확인", "- btn_prev onClick");



                if (CFG_SEQ == 2 && Fit_Constants.deviceType.equals("HOM")){
                    finish();
                    return;
                }

                if (CFG_SEQ > 1) {

                    CFG_SEQ = CFG_SEQ - 1;

                    setMeasureSeq(CFG_SEQ);
                }
            }
        });

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("확인", "- btn_next onClick");

                if (isHeight) {
                    if (CFG_HEIGHT_MAX[1] == 0) {
                        finish();
                    } else {
                        updateHeight();
                        finish();
                    }
                    if (protocolType.equals("3a")){
                        setMessage(Fit_StringUtils.getCommand("44 3A 07 00 03 " + Fit_StringUtils.getHexStringCode(Fit_Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + Fit_StringUtils.getHexStringCode(Fit_Preset.setup) + " 00 00"));

                    }else if (protocolType.equals("3b")){
                        setMessage(new Fit_Commend().sendGoExercise((byte) Fit_Preset.seat , (byte) Fit_Preset.setup));
                    }

                    return;
                }

                if (CFG_SEQ < 3) {
                    CFG_SEQ = CFG_SEQ + 1;
                    if (CFG_SEQ == 3) {
                        if (CFG_HEIGHT_MAX[1] == 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_measure_height), Toast.LENGTH_SHORT).show();
                            CFG_SEQ -= 1;
                            return;
                        }
                    }

                    setMeasureSeq(CFG_SEQ);
                }


            }
        });

//        btn_finish = (Button) findViewById(R.id.btn_finish);
//        btn_finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("확인", "- btn_finish onClick");
//                if (CFG_WEIGHT_MAX[1] == 0) {
//                    Toast.makeText(getApplicationContext(), getString(R.string.toast_measure_weight), Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    moveExercise = true;
//                    moveProcedure = false;
//                    double tmpWeight = currentWeight * 10;
//                    Preset.setup = (int)tmpWeight;
//                    Preset.MaxWeight = CFG_WEIGHT_MAX[1];
//                    updateMeasure();
//                }
//
//            }
//        });
        btn_procedure = findViewById(R.id.btn_procedure);
        btn_procedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CFG_WEIGHT_MAX[1] == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_measure_weight), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    moveExercise = false;
                    moveProcedure = true;
                    double tmpWeight = currentWeight * 10;
                    Fit_Preset.setup = (int) tmpWeight;
                    Fit_Preset.MaxWeight = CFG_WEIGHT_MAX[1];
                    updateMeasure();

                }
            }
        });

        // Init
        init();
    }

    @Override
    public void onResume() {
        Log.d("확인", "----- MeasureActivity onResume");


        Log.d("확인", "onResume: " + CFG_SEQ);


        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isHeiProgress = false;
        isWeiProgress = false;
        isHeight = false;
        if (progressDialog != null) {
            progressDialog.dismiss();

        }

    }

//    public void setDeviceType(String DeviceType) {
//        Log.d("확인", "----- MeasureActivity setDeviceType: " + DeviceType);
//
//        Log.d("확인", "setDeviceType: " + CFG_SEQ);
//
//        switch (DeviceType) {
//            case "HOM":
//                CFG_SEQ = 2;
//                scale_seat.setVisibility(View.GONE);
//                break;
//
//            case "FIT":
//                scale_seat.setVisibility(View.VISIBLE);
//
//                break;
//        }
//    }

    void init() {


        if (Fit_Constants.deviceType.equals("HOM")) {
            setMeasureSeq(2);
//            btn_prev.setVisibility(View.GONE);
//            btn_next.setText(getString(R.string.btn_complete));
        } else {
            setMeasureSeq(1);

        }
    }


    public void setMeasureSeq(int seq) {
        Log.d("확인", "----- setMeasureSeq: " + seq);

        CFG_SEQ = seq;

        scale_seat.setVisibility(View.GONE);
        scale_height.setVisibility(View.GONE);
        scale_weight.setVisibility(View.GONE);

        btn_cancel.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);

        switch (CFG_SEQ) {
            case 1:
                Fit_MainActivity.tabPos = 0;


                Fit_MainActivity.setAudio("chair");
                scale_seat.setVisibility(View.VISIBLE);

                btn_cancel.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                isHeiProgress = false;
                isWeiProgress = false;


                break;


            case 2:
                tmpMaxDistance = 0;
                CFG_HEIGHT_MAX[1] = 0;
                scale_height.setVisibility(View.VISIBLE);
                btn_procedure.setVisibility(View.GONE);

                btn_prev.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);

                Fit_MainActivity.setAudio("height");
                isWeiProgress = false;
                isHeiProgress = false;

                progressDialog = new Fit_ProgressDialog(this, getLayoutInflater());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.show(getString(R.string.dialog_height));
                            }
                        });
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        float count = 0;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (protocolType.equals("3a")){
                            ((Fit_MainActivity) Fit_MainActivity.mContext).setMessage(Fit_StringUtils.getCommand("44 3A 07 00 03 " + Fit_StringUtils.getHexStringCode(Fit_Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 00 00 00"));

                        }else if (protocolType.equals("3b")){
                            setMessage(new Fit_Commend().sendWeightMove((byte) 0));
                        }
                        while (true) {
                            if (isHeiProgress) break;
                            Log.d("확인", "isHeiProgress: " + isHeiProgress);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count += 0.5f;
                            if (count == 5) {

                                if (protocolType.equals("3a")){
                                    ((Fit_MainActivity) Fit_MainActivity.mContext).setMessage(Fit_StringUtils.getCommand("44 3A 07 00 03 " + Fit_StringUtils.getHexStringCode(Fit_Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 00 00 00"));

                                }else if (protocolType.equals("3b")){
                                    setMessage(new Fit_Commend().sendWeightMove((byte) 0));
                                }                                count = 0;
                            }
                        }
                        progressDialog.dismiss();
                    }
                }).start();


                break;

            case 3:
                Fit_MainActivity.tabPos = 1;
                CFG_WEIGHT_MAX[1] = 0;
                tv_weight_max.setText(String.valueOf(CFG_WEIGHT_MAX[1]));
                scale_weight.setVisibility(View.VISIBLE);
                btn_procedure.setVisibility(View.VISIBLE);
                btn_prev.setVisibility(View.VISIBLE);

                Fit_MainActivity.setAudio("weight");
//                isWeiProgress = false;
//                isHeiProgress = false;
//
//
//                progressDialog = new ProgressDialog(this, getLayoutInflater());
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressDialog.show(getString(R.string.dialog_weight));
//                            }
//                        });
//                    }
//                }).start();
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        float count = 0;
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        ((MainActivity) MainActivity.mContext).setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(CFG_WEIGHT_DEF[1]) + " 00 00"));
//
//                        while (!isWeiProgress) {
//                            Log.d("확인", "isWeiProgress: " + isWeiProgress);
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            count += 0.5f;
//                            if (count == 5) {
//
//                                ((MainActivity) MainActivity.mContext).setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(CFG_WEIGHT_DEF[1]) + " 00 00"));
//                                count = 0;
//                            }
//                        }
//                        progressDialog.dismiss();
//                    }
//                }).start();


                break;
        }
    }

    public void setMeasure(String[] CFG_RECEIVED) {
        Log.d("확인", "----- setMeasure: " + CFG_RECEIVED[2]);

        String CFG_RECEIVE_CODE = CFG_RECEIVED[2];

        switch (CFG_RECEIVE_CODE) {
            case "81": // RSP_PP_DATA
                switch (CFG_SEQ) {
                    case 1:
                        Fit_Preset.seat = Integer.parseInt(CFG_RECEIVED[3], 16);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_seat.setText(String.valueOf(Fit_Preset.seat));
                                    }
                                });
                            }
                        }).start();
                        break;

                    case 2:
                        CFG_WEIGHT_DEF[1] = Integer.parseInt(CFG_RECEIVED[14], 16);
                        Log.d("확인", "- CFG_WEIGHT_DEF[1]: " + CFG_WEIGHT_DEF[1]);
                        break;

                    case 3:
                        CFG_WEIGHT_DEF[1] = Integer.parseInt(CFG_RECEIVED[14], 16);
                        Log.d("확인", "- CFG_WEIGHT_DEF[1]: " + CFG_WEIGHT_DEF[1]);
                        break;
                }
                break;

            case "82": // EXERCISE_DATA
                switch (CFG_SEQ) {
                    case 2:
//                        CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);
//
//                        if (CFG_HEIGHT[1] > CFG_HEIGHT_MAX[1]) {
//                            CFG_HEIGHT_MAX[1] = CFG_HEIGHT[1];
//                        }

                        if (protocolType.equals("3a")){
                            CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);

                        }else if (protocolType.equals("3b")){

                            if (deviceType.equals("HOM")){
                                CFG_HEIGHT[1] = Fit_Preset.getHomeHeightValue(Integer.parseInt(CFG_RECEIVED[8], 16), Integer.parseInt(CFG_RECEIVED[5], 16));
                            }else{
                                CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
                            }


                        }
                        tmpDistance = 0;
                        for (int i = 0; i < CFG_HEIGHT[1]; i++) {
                            tmpDistance += anglePerMillimeter;
                        }
//                        tmpDistance *= 0.1;
                        if (Fit_User.language.equals("en")) {
                            tmpDistance = tmpDistance / Fit_Constants.INCHES;
                        }
                        if (CFG_HEIGHT[1] > CFG_HEIGHT_MAX[1]) {
                            CFG_HEIGHT_MAX[1] = CFG_HEIGHT[1];
                            Fit_Preset.MaxHeight = CFG_HEIGHT[1];
                            tmpMaxDistance = tmpDistance;

                        }
                        if (!isHeiProgress && Integer.parseInt(CFG_RECEIVED[8], 16) <= 5 && isMeasure) {
                            isHeiProgress = true;
                        }

                        break;

                    case 3:

                        if (protocolType.equals("3a")){
                            CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);
                            CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
                        }else if (protocolType.equals("3b")){
                            CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
                            CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[5], 16);
                        }


                        //double height_limit = CFG_HEIGHT_MAX[1] * 0.8;

                        if (CFG_WEIGHT[1] > CFG_WEIGHT_MAX[1] && CFG_HEIGHT[1] > 0) {

                            CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];


                        }
                        break;
                }
                break;

            case "83": // MOTOR COMPLETE
                switch (CFG_SEQ) {

                    case 3:

                        CFG_DEVICE_MOVING = false;
                        break;
                }
                break;
        }

        switch (CFG_SEQ) {
            case 1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tv_seat.setText(Integer.toString(Fit_Preset.seat));
                            }
                        }, 1000);
                    }
                }).start();
                break;


            case 2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                tv_height.setText(Integer.toString(CFG_HEIGHT[1]));
//                                tv_height_max.setText(Integer.toString(CFG_HEIGHT_MAX[1]));

                                String distance = String.format("%.1f", tmpDistance);
                                tv_height.setText(distance);
                                String maxDistance = String.format("%.1f", tmpMaxDistance);
                                tv_height_max.setText(maxDistance);
                            }
                        });
                    }
                }).start();
                break;

            case 3:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (isWeightMove) {
                                    weightMoveCount++;
                                    if (weightMoveCount == 20) {
                                        isWeightMove = false;
                                    }
                                    return;
                                }
                                double result_def = CFG_WEIGHT_DEF[1] * 0.1;
                                double result = CFG_WEIGHT[1] * 0.1;

                                Log.i("확인", "weight: " + CFG_WEIGHT[1] + "  def: " + CFG_WEIGHT_DEF[1] + "  current: " + currentWeight);

                                if (result == currentWeight) {

                                    CFG_DEVICE_MOVING = false;
                                } else if (result != currentWeight) {
                                    currentWeight = result;
                                }

                                if (CFG_DEVICE_MOVING == true) {


                                    tv_weight.setText(String.valueOf(currentWeight));
                                } else {
                                    if (CFG_HEIGHT[1] > 0) {
                                        if (CFG_WEIGHT[1] >= 70) {
                                            result += 0.5;
                                        } else if (CFG_WEIGHT[1] >= 45 && CFG_WEIGHT[1] <= 65) {
                                            result += 1;
                                        } else if (CFG_WEIGHT[1] >= 5 && CFG_WEIGHT[1] <= 40) {
                                            result += 1.5;
                                        }
                                        if (Fit_User.language.equals("ko")) {
                                            tv_weight.setText(String.format("%.1f", result));

                                        } else if (Fit_User.language.equals("en")) {
                                            tv_weight.setText(String.format("%.1f", result * POUND));

                                        } else {
                                            tv_weight.setText(String.format("%.1f", result));

                                        }
                                    } else {
                                        tv_weight.setText(String.valueOf(currentWeight));

                                    }


                                }
                                double result_max = 0;
                                if (CFG_WEIGHT_MAX[1] >= 70) {
                                    result_max = (CFG_WEIGHT_MAX[1] + 5) * 0.1;
                                } else if (CFG_WEIGHT_MAX[1] >= 45 && CFG_WEIGHT_MAX[1] <= 65) {
                                    result_max = (CFG_WEIGHT_MAX[1] + 10) * 0.1;
                                } else if (CFG_WEIGHT_MAX[1] >= 5 && CFG_WEIGHT_MAX[1] <= 40) {
                                    result_max = (CFG_WEIGHT_MAX[1] + 15) * 0.1;
                                }
                                double tmpMaxd = result_max * 10;
                                int tmpMax = (int) tmpMaxd;
                                Fit_Preset.MaxWeight = tmpMax;
                                if (Fit_User.language.equals("ko")) {
                                    tv_weight_max.setText(String.format("%.1f", result_max));

                                } else if (Fit_User.language.equals("en")) {
                                    tv_weight_max.setText(String.format("%.1f", result_max * POUND));

                                } else {
                                    tv_weight_max.setText(String.format("%.1f", result_max));

                                }

                            }
                        });
                    }
                }).start();
                break;
        }
    }

    private void updateHeight() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Fit_Param param = new Fit_Param();

                param.add("maxHeight", Fit_Preset.MaxHeight);
                param.add("token", Fit_User.token);
                param.add("memberNo", Fit_User.MemberNo);
                param.add("type", "height");
                param.add("productNo", Fit_Preset.DEVICE_NAME);

                Fit_Address address = new Fit_Address();

                Log.d("확인", "param: " + param.getParam() + "  url: " + address.getUpdatePreset());

                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                if (httpConnect.httpConnect(param.getParam(), address.getUpdatePreset()) == 200) {
                    Log.d("확인", "receive message: " + httpConnect.getReceiveMessage());
                    if (httpConnect.getReceiveMessage().equals("success")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                finish();

                            }
                        });

                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {


//                            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).start();


    }


    private void updateMeasure() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();

                param.add("memberNo", Fit_User.MemberNo);
                param.add("token", Fit_User.token);
                param.add("maxWeight", Fit_Preset.MaxWeight);
                param.add("maxHeight", Fit_Preset.MaxHeight);
                param.add("seat", Fit_Preset.seat);
                param.add("setup", Fit_Preset.setup);
                param.add("productNo", Fit_Preset.DEVICE_NAME);
                Fit_Preset.measureSetup = Fit_Preset.setup;
                Fit_Address address = new Fit_Address();
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                if (httpConnect.httpConnect(param.getParam(), address.getMeasureUpdate()) == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
                Log.d("확인", "updateMeasure: " + httpConnect.getResponseCode());
                Log.d("확인", "updateMeasure: " + httpConnect.getReceiveMessage());
            }
        }).start();
    }


}