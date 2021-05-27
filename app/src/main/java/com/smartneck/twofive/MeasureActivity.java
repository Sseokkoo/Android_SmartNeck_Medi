package com.smartneck.twofive;

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
import com.smartneck.twofive.Main.BleConnectActivity;
import com.smartneck.twofive.Main.MainActivity;
import com.smartneck.twofive.util.Address;
import com.smartneck.twofive.util.Commend;
import com.smartneck.twofive.util.Constants;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.NoticeDialog;
import com.smartneck.twofive.util.Param;
import com.smartneck.twofive.util.ProgressDialog;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.smartneck.twofive.GlobalApplication.userPreference;
import static com.smartneck.twofive.Main.BleConnectActivity.isHeiProgress;
import static com.smartneck.twofive.Main.BleConnectActivity.isWeiProgress;
import static com.smartneck.twofive.Main.BleConnectActivity.setMessage;
import static com.smartneck.twofive.Main.BleConnectActivity.tabPos;
import static com.smartneck.twofive.Main.MainActivity.member;
import static com.smartneck.twofive.Main.MainActivity.preset;
import static com.smartneck.twofive.Member.MemberSelectActivity.admin;
import static com.smartneck.twofive.util.Constants.POUND;
import static com.smartneck.twofive.util.Constants.TAG;

public class MeasureActivity extends AppCompatActivity {
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
    ProgressDialog progressDialog;
    //거리변환
    float tmpDistance;
    float tmpMaxDistance;
//    float anglePerMillimeter = 3.6666667f; 수정9
    float anglePerMillimeter = 1f;
    public static int weightMoveCount;
    public static boolean isWeightMove = false;


    NoticeDialog noticeDialog;

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
//        if (Constants.DEVICE_TYPE.equals("FIT")){ 수정9
//            anglePerMillimeter = 3f; 수정9
//        }else if (Constants.DEVICE_TYPE.equals("MED")){
//            anglePerMillimeter = 3.6666667f; 수정9
//        }

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


        btn_seat_down = (ImageButton) findViewById(R.id.btn_seat_down);
        btn_seat_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "- btn_seat_down onClick");

                if (preset.getSeat() > 0) {

                    preset.setSeat(preset.getSeat() - 1);
                    ((BleConnectActivity) BleConnectActivity.mContext).setMessage(new Commend().sendSeatMove((byte) preset.getSeat()));

                    tv_seat.setText(Integer.toString(preset.getSeat()));
                }
            }
        });

        btn_seat_up = (ImageButton) findViewById(R.id.btn_seat_up);
        btn_seat_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "- btn_seat_up onClick");

                if (preset.getSeat() < 20) {

                    preset.setSeat(preset.getSeat() + 1);
                    ((BleConnectActivity) BleConnectActivity.mContext).setMessage(new Commend().sendSeatMove((byte) preset.getSeat()));

                    tv_seat.setText(Integer.toString(preset.getSeat()));
                }
            }
        });

        btn_weight_up = (ImageButton) findViewById(R.id.btn_weight_up);
        btn_weight_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "- btn_weight_up onClick");
                if (CFG_HEIGHT[1] > 0) return;

                if ((currentWeight + 0.5) >= 6.0) {
                    currentWeight = 6.0;
                    preset.setSetup(60);
                }else{
                    currentWeight += 0.5;
                    preset.setSetup(preset.getSetup() + 5);
                }
                if (currentWeight > 6.0) {
                    currentWeight = 6.0;
                    preset.setSetup(60);
                }
                if (preset.getSetup() > 60){
                    preset.setSetup(60);
                }else {
                    tv_weight.setText(String.valueOf(preset.getSetup() * 0.1));
                }
                Log.e("확인", "curren : "+currentWeight + "pre : "+preset.getSetup());
//                if (currentWeight >= 6.5){
//                    currentWeight = 6.5;
//                }else if (currentWeight == 6){
//                    currentWeight = 6.5;
//                }else if (currentWeight == 5.5){
//                    currentWeight = 6.0;
//                }else if (currentWeight == 5){
//                    currentWeight = 5.5;
//                }else if (currentWeight == 4.5){
//                    currentWeight = 5.0;
//                }else if (currentWeight == 4){
//                    currentWeight = 4.5;
//                }else if (currentWeight == 3.5){
//                    currentWeight = 4;
//                }else if (currentWeight == 3){
//                    currentWeight = 3.5;
//                }else if (currentWeight == 2.5){
//                    currentWeight = 3;
//                }else if (currentWeight == 2){
//                    currentWeight = 2.5;
//                }else if (currentWeight == 1.5){
//                    currentWeight = 2;
//                }else if (currentWeight == 1){
//                    currentWeight = 1.5;
//                }else if (currentWeight == 0.5){
//                    currentWeight = 1;
//                }else if (currentWeight == 0) {
//                    currentWeight = 0.5;
//                }
                weightMoveCount = 0;

//                preset.setSetup(preset.getSetup() + 5);

                isWeightMove = true;
                CFG_DEVICE_MOVING = true;
                ((BleConnectActivity) BleConnectActivity.mContext).setMessage(new Commend().sendWeightMove((byte)preset.getSetup()));
                CFG_WEIGHT_MAX[1] = 0;
                tv_weight_max.setText("0.0");
Log.e("확인", ""+ preset.getSetup());
                tv_weight.setText(String.valueOf(preset.getSetup() * 0.1));
            }


//            }
        });

        btn_weight_down = (ImageButton) findViewById(R.id.btn_weight_down);
        btn_weight_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CFG_HEIGHT[1] > 0) return;
                Log.d(TAG, "- btn_weight_down onClick");

                if ((currentWeight - 0.5) <= 0) {
                    currentWeight = 0;
                    preset.setSetup(0);
                }else{
                    currentWeight -= 0.5;
                    preset.setSetup(preset.getSetup() - 5);
                }
                if (currentWeight < 0) {
                    currentWeight = 0;
                    preset.setSetup(0);
                }
                if ((preset.getSetup()-0.5) <= 0){
                    currentWeight = 0;
                    preset.setSetup(0);
                }
                if (preset.getSetup() < 0){
                    currentWeight = 0;
                    preset.setSetup(0);
                }else {
                    tv_weight.setText(String.valueOf(preset.getSetup() * 0.1));
                }
                    Log.e("확인", "curren : "+currentWeight + "pre : "+preset.getSetup());

//                if (currentWeight >= 6.5){
//                    currentWeight = 6;
//                }else if (currentWeight == 6){
//                    currentWeight = 5.5;
//                }else if (currentWeight == 5.5){
//                    currentWeight = 5.0;
//                }else if (currentWeight == 5){
//                    currentWeight = 4.5;
//                }else if (currentWeight == 4.5){
//                    currentWeight = 4.0;
//                }else if (currentWeight == 4){
//                    currentWeight = 3.5;
//                }else if (currentWeight == 3.5){
//                    currentWeight = 3;
//                }else if (currentWeight == 3){
//                    currentWeight = 2.5;
//                }else if (currentWeight == 2.5){
//                    currentWeight = 2;
//                }else if (currentWeight == 2){
//                    currentWeight = 1.5;
//                }else if (currentWeight == 1.5){
//                    currentWeight = 1;
//                }else if (currentWeight == 1){
//                    currentWeight = 0.5;
//                }else if (currentWeight == 0.5){
//                    currentWeight = 0;
//                }else if (currentWeight < 0){
//                    currentWeight = 0;
//                }
                weightMoveCount = 0;

//                preset.setSetup(preset.getSetup() - 5);

                isWeightMove = true;
                CFG_DEVICE_MOVING = true;
                ((BleConnectActivity) BleConnectActivity.mContext).setMessage(new Commend().sendWeightMove((byte)preset.getSetup()));
                CFG_WEIGHT_MAX[1] = 0;
                tv_weight_max.setText("0.0");


                tv_weight.setText(String.valueOf(preset.getSetup() * 0.1));

            }
        });


        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "- btn_cancel onClick");

                finish();
            }
        });

        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "- btn_prev onClick");

                if (isHeight) {
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
//                Log.d(TAG, "- btn_next onClick");
//
//                if (isHeight) {
//                    if (CFG_HEIGHT_MAX[1] == 0) {
//                        finish();
//                    } else {
//                        updateHeight();
//                        finish();
//                    }
//                    setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(pastPreset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(pastPreset.setup) + " 00 00"));
//
//                    return;
//                }

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
//                Log.d(TAG, "- btn_finish onClick");
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
                    preset.setSetup((int) tmpWeight);
                    preset.setMaxWeight((int) (result_max * 10) );

//                    preset.setMaxWeight(CFG_WEIGHT_MAX[1]);
                    userPreference.editPreset(preset);
                    userPreference.addHeight(member , CFG_HEIGHT_MAX[1]);
                    userPreference.addWeight(member , preset.getMaxWeight());

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
                    finish();

                }
            }
        });

        // Init
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tv_seat.setText(String.valueOf(preset.getSeat()));

    }

    @Override
    public void onResume() {
        Log.d(TAG, "----- MeasureActivity onResume");


        Log.d(TAG, "onResume: " + CFG_SEQ);


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

    void init() {
        setMeasureSeq(1);
    }


    public void setMeasureSeq(int seq) {
        Log.d(TAG, "----- setMeasureSeq: " + seq);

        CFG_SEQ = seq;

        scale_seat.setVisibility(View.GONE);
        scale_height.setVisibility(View.GONE);
        scale_weight.setVisibility(View.GONE);

        btn_cancel.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);

        switch (CFG_SEQ) {
            case 1:
                tabPos = 0;


                MainActivity.setAudio("chair");
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

                MainActivity.setAudio("height");
                isWeiProgress = false;
                isHeiProgress = false;

                progressDialog = new ProgressDialog(this, getLayoutInflater());
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
                })
                        .start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        float count = 0;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        setMessage(new Commend().sendWeightMove((byte) 0));

                        while (true) {
                            if (isHeiProgress) break;
                            Log.d(TAG, "isHeiProgress: " + isHeiProgress);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count += 0.5f;
                            if (count == 5) {
                                setMessage(new Commend().sendWeightMove((byte) 0));

                                count = 0;
                            }
                        }
                        progressDialog.dismiss();
                    }
                }).start();


                break;

            case 3:
                tabPos = 1;
                CFG_WEIGHT_MAX[1] = 0;
                tv_weight_max.setText(String.valueOf(CFG_WEIGHT_MAX[1]));
                scale_weight.setVisibility(View.VISIBLE);
                btn_procedure.setVisibility(View.VISIBLE);
                btn_prev.setVisibility(View.VISIBLE);

                MainActivity.setAudio("weight");
                isWeiProgress = false;
                isHeiProgress = false;
                break;
        }
    }

    public void setMeasure(String[] CFG_RECEIVED) {
        Log.d(TAG, "----- setMeasure: " + CFG_RECEIVED[2]);

        String CFG_RECEIVE_CODE = CFG_RECEIVED[2];

        switch (CFG_RECEIVE_CODE) {


            case "82": // EXERCISE_DATA
                switch (CFG_SEQ) {
                    case 2:

                        CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
                        tmpDistance = 0;
                        for (int i = 0; i < CFG_HEIGHT[1]; i++) {
                            tmpDistance += anglePerMillimeter;
                        }
                        if (Constants.language.equals("en")) {
//                            tmpDistance = tmpDistance / Constants.INCHES; 수정11
                            tmpDistance = tmpDistance;
                        }
                        if (CFG_HEIGHT[1] > CFG_HEIGHT_MAX[1]) {
                            CFG_HEIGHT_MAX[1] = CFG_HEIGHT[1];
                            preset.setMaxHeight(CFG_HEIGHT[1]);
                            tmpMaxDistance = tmpDistance;

                        }
                        if (!isHeiProgress && Integer.parseInt(CFG_RECEIVED[8], 16) <= 5 && isMeasure) {
                            isHeiProgress = true;
                        }

                        break;

                    case 3:

                        CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
                        CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[5], 16);

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
                break;


            case 2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

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

//                                if (isWeightMove) {
//                                    weightMoveCount++;
//                                    if (weightMoveCount == 20) {
//                                        isWeightMove = false;
//                                    }
//                                    return;
//                                }
                                double result_def = CFG_WEIGHT_DEF[1] * 0.1;
                                double result = CFG_WEIGHT[1] * 0.1;
                                float cur = ((float) preset.getSetup()) / 10;

                                Log.i(TAG, "weight: " + CFG_WEIGHT[1] + "  def: " + CFG_WEIGHT_DEF[1] + "  current: " + currentWeight);

                                if (result == currentWeight) {

                                    CFG_DEVICE_MOVING = false;
                                } else if (result != currentWeight) {
                                    currentWeight = result;
                                }

                                if (CFG_DEVICE_MOVING == true) {

//                                    tv_weight.setText(String.valueOf(preset.getSetup() * 0.1)); 수정 5
                                    tv_weight_max.setText(String.valueOf(preset.getSetup() * 0.1));
                                } else {
                                    if (CFG_HEIGHT[1] > 0) {
//                                        if (CFG_WEIGHT[1] >= 70) {
//                                            result += 0.5;
//                                        } else if (CFG_WEIGHT[1] >= 45 && CFG_WEIGHT[1] <= 65) {
//                                            result += 1;
//                                        } else if (CFG_WEIGHT[1] >= 5 && CFG_WEIGHT[1] <= 40) {
//                                            result += 1.5;
//                                        }
                                        if (Constants.language.equals("ko")) {
//                                            tv_weight.setText(String.format("%.1f", result));

                                        } else if (Constants.language.equals("en")) {
//                                            tv_weight.setText(String.format("%.1f", result * POUND));

                                        } else {
//                                            tv_weight.setText(String.format("%.1f", result));

                                        }
                                    } else {
                                        tv_weight.setText(String.valueOf(cur));

                                    }


                                }
                                result_max = 0;
                                if (CFG_WEIGHT_MAX[1] >= 70) {
                                    result_max = (CFG_WEIGHT_MAX[1] + 5) * 0.1;
                                } else if (CFG_WEIGHT_MAX[1] >= 45 && CFG_WEIGHT_MAX[1] <= 65) {
                                    result_max = (CFG_WEIGHT_MAX[1] + 10) * 0.1;
                                } else if (CFG_WEIGHT_MAX[1] >= 5 && CFG_WEIGHT_MAX[1] <= 40) {
                                    result_max = (CFG_WEIGHT_MAX[1] + 15) * 0.1;
                                }
                                double tmpMaxd = result_max * 10;
                                preset.setMaxWeight((int) (result_max * 10) );
                                int tmpMax = (int) tmpMaxd;
                                if (Constants.language.equals("ko")) {
                                    tv_weight_max.setText(String.format("%.1f", result_max));

                                } else if (Constants.language.equals("en")) {
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
    double result_max = 0;


}