package com.smartneck.twofive.Fit.Main;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Address;
import com.smartneck.twofive.Fit.util.Commend;
import com.smartneck.twofive.Fit.util.Constants;
import com.smartneck.twofive.Fit.util.HttpConnect;
import com.smartneck.twofive.Fit.util.Param;
import com.smartneck.twofive.Fit.util.ProgressDialog;
import com.smartneck.twofive.Fit.util.StringUtils;
import com.smartneck.twofive.Fit.util.User.Preset;
import com.smartneck.twofive.Fit.util.User.User;

import static com.smartneck.twofive.Fit.GlobalApplication.getApllication;
import static com.smartneck.twofive.Fit.Main.MainActivity.audioStop;
import static com.smartneck.twofive.Fit.Main.MainActivity.isHeiProgress;
import static com.smartneck.twofive.Fit.Main.MainActivity.protocolType;
import static com.smartneck.twofive.Fit.Main.MainActivity.setMessage;
import static com.smartneck.twofive.Fit.Main.MainActivity.tabPos;
import static com.smartneck.twofive.Fit.Main.WeightSettingFragment.currentWeight;
import static com.smartneck.twofive.Fit.Main.WeightSettingFragment.isWeight;
import static com.smartneck.twofive.Fit.Main.WeightSettingFragment.tmpSetup;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_WEIGHT;
import static com.smartneck.twofive.Fit.util.Constants.TAG;
import static com.smartneck.twofive.Fit.util.Constants.deviceType;

public class HeightFragment extends Fragment {

    ViewGroup view;
    TextView tv_height, tv_height_max;
    ImageView img_GIF;
    float tmpDistance;
    float tmpMaxDistance;
    int height;
    int heightMax;
    final int anglePerMillimeter = 3;
    public static boolean isHeight = true;
    Handler handler;
    Context mContext;
    boolean isDestroy = false;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_height, container, false);
        mContext = getApllication();
        MainActivity.setAudio("height");

        init();


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioStop();
        isWeight= false;
        handler = new Handler();
        if (protocolType.equals("3a")) {
            setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + "00" + " 00 00"));

        } else if (protocolType.equals("3b")) {
            setMessage(new Commend().sendWeightMove((byte) 0));
        }

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
                if (protocolType.equals("3a")) {
                    setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + "00" + " 00 00"));

                } else if (protocolType.equals("3b")) {
                    setMessage(new Commend().sendWeightMove((byte) 0));
                }
                while (true) {
                    if (isDestroy) break;
                    if (isHeiProgress) break;
                    if (CFG_WEIGHT[1] == 0) break;

                    Log.d(TAG, "isHeiProgress: " + isHeiProgress);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count += 0.5f;
                    if (count == 5) {

                        if (protocolType.equals("3a")) {
                            setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + "00" + " 00 00"));

                        } else if (protocolType.equals("3b")) {
                            setMessage(new Commend().sendWeightMove((byte) 0));
                        }
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
        tabPos = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tmpSetup = Preset.setup;
        isHeight = false;
        isDestroy = true;
        if (protocolType.equals("3a")) {
            setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(Preset.setup) + " 00 00"));

        } else if (protocolType.equals("3b")) {
            setMessage(new Commend().sendWeightMove((byte) Preset.setup));
        }
        setWeightThread();
        if (heightMax > 0)
            updateHeight();
    }

    private void setWeightThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "run: "+currentWeight + " / " + Preset.setup);
                    if (currentWeight != tmpSetup * 0.1){
                        if (protocolType.equals("3a")){
                            setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(tmpSetup) + " 00 00"));

                        }else if (protocolType.equals("3b")){
                            setMessage(new Commend().sendWeightMove((byte)tmpSetup));
                        }

                    }else if (currentWeight == tmpSetup){
                        Log.d(TAG, "GO_EXERCISE_BREAK: ");
                        Preset.setup = tmpSetup;
                        break;
                    }
                    if (count > 5){
                        break;
                    }
                    count ++;
                }


            }
        }).start();
    }

    private void init() {
        isDestroy = false;
        tv_height = view.findViewById(R.id.tv_height_frag);
        tv_height_max = view.findViewById(R.id.tv_height_max_frag);
        img_GIF = view.findViewById(R.id.measure_height_gif_frag);
        Glide.with(this)
                .asGif()
                .load(R.drawable.height)
                .into(img_GIF);
        setListener();

    }

    private void setListener() {

    }

    public void setHeight(String[] CFG_RECEIVED) {
        Log.d(TAG, "----- setMeasure: " + CFG_RECEIVED[2]);

        String CFG_RECEIVE_CODE = CFG_RECEIVED[2];

        if ("82".equals(CFG_RECEIVE_CODE)) { // EXERCISE_DATA
//                        CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);
//
//                        if (CFG_HEIGHT[1] > CFG_HEIGHT_MAX[1]) {
//                            CFG_HEIGHT_MAX[1] = CFG_HEIGHT[1];
//                        }
//                if (CFG_RECEIVED[8].equals("00")) isHeiProgress = true;
            if (protocolType.equals("3a")) {
                height = Integer.parseInt(CFG_RECEIVED[4], 16);
                if (!isHeiProgress && Integer.parseInt(CFG_RECEIVED[8], 16) <= 0) {
                    isHeiProgress = true;
                }
            } else if (protocolType.equals("3b")) {
                if (deviceType.equals("HOM")) {
                    height = Preset.getHomeHeightValue(Integer.parseInt(CFG_RECEIVED[8], 16) , Integer.parseInt(CFG_RECEIVED[5], 16));
                    Log.d(TAG, "setHeight: " + height);
                } else {

                    height = Integer.parseInt(CFG_RECEIVED[8], 16);
                }
                if (!isHeiProgress && Integer.parseInt(CFG_RECEIVED[5], 16) <= 0) {
                    isHeiProgress = true;
                }
            }
            tmpDistance = 0;
            for (int i = 0; i < height; i++) {
                tmpDistance += anglePerMillimeter;
            }
//                        tmpDistance *= 0.1;
            if (User.language.equals("en")) {
                tmpDistance = tmpDistance / Constants.INCHES;
            }
            if (height > heightMax) {
                heightMax = height;
                Preset.MaxHeight = heightMax;
                tmpMaxDistance = tmpDistance;

            }


            new Thread(new Runnable() {
                @Override
                public void run() {
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
            }).start();
        }

    }

    private void updateHeight() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Param param = new Param();

                param.add("maxHeight", Preset.MaxHeight);
                param.add("token", User.token);
                param.add("memberNo", User.MemberNo);
                param.add("type", "height");
                param.add("productNo", Preset.DEVICE_NAME);

                Address address = new Address();

                Log.d(TAG, "param: " + param.getParam() + "  url: " + address.getUpdatePreset());

                HttpConnect httpConnect = new HttpConnect();
                if (httpConnect.httpConnect(param.getParam(), address.getUpdatePreset()) == 200) {
                    Log.d(TAG, "receive message: " + httpConnect.getReceiveMessage());
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
}
