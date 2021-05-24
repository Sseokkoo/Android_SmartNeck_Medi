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
import static com.smartneck.twofive.Fit.util.Constants.TAG;

public class SeatSettingFragment extends Fragment {
    ViewGroup view;
    Context mContext;
    Button btn_home, btn_exercise, btn_weight;
    ImageButton btn_up, btn_down;
    TextView tv_seat;
    Handler handler;
    String type = "";
    boolean isActivitiy;
    ImageView gif;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_seat_setting, container, false);
        mContext = getApllication();
        MainActivity.setAudio("chair");
        init();
        onClickBotton();
        onClickPosition();
        setSeatTextThraed();
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
        MainActivity.tabPos = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        type = "";
        updateSeat();
        isActivitiy = false;
    }

    void init() {

        handler = new Handler();
        btn_home = view.findViewById(R.id.btn_f_seat_setting_home);
        btn_exercise = view.findViewById(R.id.btn_f_seat_setting_exercise);
        btn_weight = view.findViewById(R.id.btn_f_seat_setting_weight);
        btn_up = view.findViewById(R.id.btn_f_seat_setting_up);
        btn_down = view.findViewById(R.id.btn_f_seat_setting_down);
        tv_seat = view.findViewById(R.id.tv_f_seat_setting_result);
        tv_seat.setText(String.valueOf(Preset.seat));

        btn_home.setText(getString(R.string.btn_close));
        btn_home.setVisibility(View.GONE);
        btn_exercise.setVisibility(View.GONE);

        gif = view.findViewById(R.id.seat_setting_gif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.seat)
                .into(gif);


    }

    void setSeatTextThraed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isActivitiy = true;
                while (isActivitiy) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_seat.setText(String.valueOf(Preset.seat));
                        }
                    });
                }
            }
        }).start();
    }

    void onClickPosition() {
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWeightMove()) {
                    return;
                }
                if (Preset.seat < 20) {
                    isClick = true;

                    Preset.seat = Preset.seat + 1;
                    tv_seat.setText(String.valueOf(Preset.seat));
//                    if (protocolType.equals("3a")){
//                        ((MainActivity) MainActivity.mContext).setMessage(StringUtils.getCommand("44 3A 03 01 01"));
//
//                    }else if (protocolType.equals("3b")){
                        setMessage(new Commend().sendSeatMove((byte)Preset.seat));
//                    }
                }
            }
        });

        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWeightMove()) {
                    return;
                }
                isClick = true;

                if (Preset.seat > 0) {

                    Preset.seat = Preset.seat - 1;
                    tv_seat.setText(String.valueOf(Preset.seat));
                    if (protocolType.equals("3a")){
                        MainActivity.setMessage(StringUtils.getCommand("44 3a 03 01 02"));

                    }else if (protocolType.equals("3b")){
                        setMessage(new Commend().sendSeatMove((byte)Preset.seat));
                    }
                }
            }
        });
    }

    void onClickBotton() {
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "HOME";
                updateSeat();
            }
        });
        btn_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                type = "EXERCISE";
                updateSeat();

            }
        });
        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                type = "WEIGHT";

                updateSeat();

            }
        });
    }

    void updateSeat() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Param param = new Param();
                param.add("token", User.token);
                param.add("seat", Preset.seat);
                param.add("memberNo" , User.MemberNo);
                param.add("type" , "seat");
                Address address = new Address();

                Log.d(TAG, "param: " + param.getParam() + "  url: " + address.getUpdatePreset());

                HttpConnect httpConnect = new HttpConnect();
                if (httpConnect.httpConnect(param.getParam(), address.getUpdatePreset()) == 200) {
                    Log.d(TAG, "receive message: " + httpConnect.getReceiveMessage());
                    if (httpConnect.getReceiveMessage().equals("success")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (!type.equals(""))
                                    ((MainActivity) getActivity()).setBottomNavigation(type);

                            }
                        });

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

    boolean isWeightMove() {
        if (MainActivity.isWeightMove) {
            Toast.makeText(mContext, getString(R.string.toast_weight_control), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
}
