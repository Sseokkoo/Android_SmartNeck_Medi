package com.ko.smartneck.medical.twofive.Main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.Commend;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;
import com.ko.smartneck.medical.twofive.util.ProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;
import static com.ko.smartneck.medical.twofive.GlobalApplication.getApllication;
import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.isHeiProgress;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.setMessage;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.tabPos;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.audioStop;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.member;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_HEIGHT;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_WEIGHT;
import static com.ko.smartneck.medical.twofive.MeasureActivity.moveProcedure;
import static com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin;


public class HeightFragment extends Fragment {

    ViewGroup view;
    TextView tv_height, tv_height_max;
    ImageView img_GIF;
    float tmpDistance;
    float tmpMaxDistance;
    int height;
    int heightMax;
//    float anglePerMillimeter = 3.6666667f; 수정 1
    float anglePerMillimeter =  1f;
    public static boolean isHeight = true;
    Handler handler;
    Context mContext;
    boolean isDestroy = false;
    Button btn_exercise_setting;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (Constants.DEVICE_TYPE.equals("FIT")){
//            anglePerMillimeter = 3f; 수정2
        }else if (Constants.DEVICE_TYPE.equals("MED")){
//            anglePerMillimeter = 3.6666667f; 수정3
        }

        view = (ViewGroup) inflater.inflate(R.layout.fragment_height, container, false);
        mContext = getApllication();
//        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
        MainActivity.setAudio("height");

        init();


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioStop();
        handler = new Handler();
//        setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + "00" + " 00 00"));
        setMessage(new Commend().sendWeightMove((byte) 0));
        progressDialog = new ProgressDialog(getActivity(), getLayoutInflater());
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
                setMessage(new Commend().sendWeightMove((byte) 0));
                while (true) {
                    if (isDestroy) break;
                    if (isHeiProgress) break;
                    if (CFG_WEIGHT[1] == 0) break;

                    Log.d(TAG, "isHeiProgress: " + isHeiProgress);
                    try {
                        Thread.sleep(300);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        isHeight = true;
        tabPos = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WeightSettingFragment.tmpSetup = preset.getSetup();
        isHeight = false;
        isDestroy = true;
        setMessage(new Commend().sendGoExercise((byte) preset.getSeat(), (byte) preset.getSetup()));
        if (heightMax > 0){
            userPreference.editPreset(preset);
            userPreference.addHeight(member , heightMax);
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
        }
    }

    private void init() {
        isDestroy = false;
        tv_height = view.findViewById(R.id.tv_height_frag);
        tv_height_max = view.findViewById(R.id.tv_height_max_frag);
        img_GIF = view.findViewById(R.id.measure_height_gif_frag);
        btn_exercise_setting = view.findViewById(R.id.btn_exercise_setting2);

        Glide.with(this)
                .asGif()
                .load(R.drawable.height)
                .into(img_GIF);
        setListener();

        setHeightTextThread();

        btn_exercise_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveProcedure = true;
                getActivity().finish();
            }
        });
    }

    private void setListener() {

    }

    public void setHeightTextThread() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (isDestroy) break;
                    if (CFG_WEIGHT[1] < 5) isHeiProgress = true;
                    height = CFG_HEIGHT[1];
                    tmpDistance = 0;
                    for (int i = 0; i < height; i++) {
                        tmpDistance += anglePerMillimeter;
                    }
//                        tmpDistance *= 0.1;

                    if (Constants.language.equals("en")) {
//                        tmpDistance = tmpDistance / Constants.INCHES;//수정 4
                        tmpDistance = tmpDistance;
                    }

                    if (height > heightMax) {
                        heightMax = height;
                        preset.setMaxHeight(height);
                        tmpMaxDistance = tmpDistance;

                    }



                    handler.post(new Runnable() {
                        @Override
                        public void run() {


                            String distance = String.format("%.1f", tmpDistance);
                            tv_height.setText(distance);
                            String maxDistance = String.format("%.1f", tmpMaxDistance);
                            tv_height_max.setText(maxDistance);
                        }
                    });


                }


            }
        }).start();


    }
}
