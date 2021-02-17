package com.ko.smartneck.medical.twofive.Main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.ko.smartneck.medical.twofive.util.Commend;

import static android.app.Activity.RESULT_OK;
import static com.ko.smartneck.medical.twofive.GlobalApplication.context;
import static com.ko.smartneck.medical.twofive.GlobalApplication.getApllication;
import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.isClick;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.tabPos;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.audioStop;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.MeasureActivity.moveProcedure;


public class SeatSettingFragment extends Fragment {
    ViewGroup view;
    Context mContext;
    Button btn_home, btn_exercise, btn_weight , btn_exercise_setting;
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
        tabPos = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        type = "";
        isActivitiy = false;
        userPreference.editPreset(preset);
    }

    void init() {

        handler = new Handler();
        btn_home = view.findViewById(R.id.btn_f_seat_setting_home);
        btn_exercise = view.findViewById(R.id.btn_f_seat_setting_exercise);
        btn_weight = view.findViewById(R.id.btn_f_seat_setting_weight);
        btn_up = view.findViewById(R.id.btn_f_seat_setting_up);
        btn_down = view.findViewById(R.id.btn_f_seat_setting_down);
        tv_seat = view.findViewById(R.id.tv_f_seat_setting_result);
        tv_seat.setText(String.valueOf(preset.getSeat()));

        btn_exercise_setting = view.findViewById(R.id.btn_exercise_setting1);
        btn_home.setText(getString(R.string.btn_close));
        btn_exercise.setVisibility(View.GONE);
        gif = view.findViewById(R.id.seat_setting_gif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.seat)
                .into(gif);


        btn_exercise_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveProcedure = true;
                getActivity().finish();
            }
        });

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
                            tv_seat.setText(String.valueOf(preset.getSeat()));
                        }
                    });
                }
            }
        }).start();
    }

    private void onClickPosition() {
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWeightMove()) {
                    return;
                }
                if (preset.getSeat() < 20) {
                    isClick = true;

                    preset.setSeat(preset.getSeat() + 1);
                    moveSeat();
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

                if (preset.getSeat() > 0) {

                    preset.setSeat(preset.getSeat() - 1);
                    moveSeat();

                }
            }
        });
    }

    private void moveSeat(){
        tv_seat.setText(String.valueOf(preset.getSeat()));
        BleConnectActivity.setMessage(new Commend().sendSeatMove((byte) preset.getSeat()));
    }

    void onClickBotton() {
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "HOME";
//                updateSeat();
            }
        });
        btn_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                type = "EXERCISE";
//                updateSeat();

            }
        });
        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                type = "WEIGHT";

//                updateSeat();

            }
        });
    }



    boolean isWeightMove() {
        if (BleConnectActivity.isWeightMove && tabPos == 1) {
            Toast.makeText(mContext, getString(R.string.toast_weight_control), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
}
