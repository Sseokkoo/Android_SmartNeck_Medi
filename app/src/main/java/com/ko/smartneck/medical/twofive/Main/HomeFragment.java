package com.ko.smartneck.medical.twofive.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ko.smartneck.medical.twofive.ErrorReport.ErrorReportActivity;
import com.ko.smartneck.medical.twofive.MeasureActivity;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.YouTube.SubExerciseActivity;
import com.ko.smartneck.medical.twofive.util.Commend;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.ProgressDialog;

import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.mConnected;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.setMessage;
import static com.ko.smartneck.medical.twofive.Main.ExerciseFragment.CFG_COUNT;
import static com.ko.smartneck.medical.twofive.Main.ExerciseFragment.CFG_SET;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.audioStop;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.isLogout;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.isMain;

import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class HomeFragment extends Fragment {

    TextView tv_ble_status , btn_exercise;
    static Context mContext;
    Button btn_ble;
    ProgressDialog progressDialog;

    Handler handler;

    ImageView btn_weight, btn_exercise_setting, btn_report, btn_alarm, btn_seat, btn_sub_exercise, btn_how_to_exercise;
    ViewGroup view;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        handler = new Handler();
        progressDialog = new ProgressDialog(mContext, getLayoutInflater());
        init();
        onClick();
        CFG_COUNT[1] = 0;
        CFG_SET[1] = 0;
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.GONE);
        // BLE
        tv_ble_status = view.findViewById(R.id.tv_ble_status);


        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (preset.getMaxWeight() == 0 || preset.getMaxHeight() == 0){
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_measure));

                        }else{
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_start));
                        }
                    }
                });
            }
        }).start();
        // Init

        return view;
    }

    void init() {
        btn_alarm = view.findViewById(R.id.home_alarm_btn);
        btn_exercise_setting = view.findViewById(R.id.home_exercise_setting_btn);
        btn_how_to_exercise = view.findViewById(R.id.home_how_to_exercise_btn);
        btn_report = view.findViewById(R.id.home_report_btn);
        btn_seat = view.findViewById(R.id.home_exercise_seat_btn);
        btn_sub_exercise = view.findViewById(R.id.home_sub_exrcise_btn);
        btn_weight = view.findViewById(R.id.home_exercise_weight_btn);
        btn_exercise = view.findViewById(R.id.home_exercise_btn);



    }

    void onClick() {
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;
                Intent intent = new Intent(getActivity().getApplicationContext() , AfterMeasureActivity.class);
                intent.putExtra("selectFrag" , "HEIGHT");
                startActivity(intent);

            }
        });
        btn_exercise_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;

                ((MainActivity) getActivity()).setBottomNavigation("PROCEDURE");
            }
        });
        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;

                Intent intent = new Intent(getActivity().getApplicationContext() , AfterMeasureActivity.class);
                intent.putExtra("selectFrag" , "WEIGHT");
                startActivityForResult(intent , Constants.REQUEST_MOVE_EXERCISE_SETTING);
            }
        });
        btn_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;
                Intent intent = new Intent(getActivity().getApplicationContext() , AfterMeasureActivity.class);
                intent.putExtra("selectFrag" , "SEAT");
                startActivity(intent);

            }
        });
        btn_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (preset.getMaxWeight() == 0 || preset.getMaxHeight() == 0){
                    Intent intent = new Intent(mContext.getApplicationContext() , MeasureActivity.class);
                    startActivity(intent);
                }else{

                    ((MainActivity) getActivity()).setBottomNavigation("EXERCISE");

                }

            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext() , ErrorReportActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(mContext.getApplicationContext() , ProtocolTestActivity.class);
//                startActivity(intent);

            }
        });

        btn_sub_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext() , SubExerciseActivity.class);
                startActivity(intent);
            }
        });
        btn_how_to_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMessage(new Commend().sendGoExercise((byte) 0 , (byte) 0 ));
                getActivity().finish();
//                Intent intent = new Intent(mContext.getApplicationContext() , HowToExerciseActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate:  homeFragment");
        MainActivity.isMain = true;


    }

    @Override
    public void onResume() {
        audioStop();
        MainActivity.currentLocation = "home";
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (preset.getMaxWeight() == 0 || preset.getMaxHeight() == 0){
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_measure));

                        }else{
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_start));
                        }
                    }
                });
            }
        }).start();

        super.onResume();
    }


    // TODO: 2019-10-24 BLE ERROR 수정
    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity.currentLocation = "";

        Log.d(TAG, "onDestroy: ");
        MainActivity.isMain = false;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(!isLogout){

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!mConnected && !isMain){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity)mContext).setBottomNavigation("HOME");
                                Toast.makeText(mContext, mContext.getString(R.string.toast_disconnect), Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }else if (isMain){
                        break;
                    }

                }

            }
        });
        // TODO: 2019-11-08 ble connect check thread start
//        .start();
        audioStop();
    }

    static boolean isConnectedBle() {
        if (mConnected) {
            return true;
        } else {
            Toast.makeText(mContext, MainActivity.mContext.getString(R.string.toast_please_connect), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    boolean isMeasure(){
        if (preset.getMaxWeight() == 0 || preset.getMaxHeight() == 0){
            Toast.makeText(mContext, getString(R.string.toast_please_measure), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    // TODO: 2019-10-24 BLE ERROR CHECK

}