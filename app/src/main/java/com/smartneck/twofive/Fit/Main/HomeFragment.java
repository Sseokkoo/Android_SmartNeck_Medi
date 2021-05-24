package com.smartneck.twofive.Fit.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.smartneck.twofive.Fit.ErrorReport.ErrorReportActivity;
import com.smartneck.twofive.Fit.MeasureActivity;
import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.YouTube.HowToExerciseActivity;
import com.smartneck.twofive.Fit.YouTube.SubExerciseActivity;
import com.smartneck.twofive.Fit.util.BluetoothUtils;
import com.smartneck.twofive.Fit.util.ProgressDialog;
import com.smartneck.twofive.Fit.util.User.Preset;

import static com.smartneck.twofive.Fit.Main.ExerciseFragment.CFG_COUNT;
import static com.smartneck.twofive.Fit.Main.ExerciseFragment.CFG_SET;
import static com.smartneck.twofive.Fit.Main.MainActivity.audioStop;
import static com.smartneck.twofive.Fit.Main.MainActivity.isLogout;
import static com.smartneck.twofive.Fit.Main.MainActivity.isMain;
import static com.smartneck.twofive.Fit.Main.MainActivity.mConnected;
import static com.smartneck.twofive.Fit.util.Constants.TAG;
import static com.smartneck.twofive.Fit.util.User.User.isHomeDevice;

public class HomeFragment extends Fragment {

    TextView tv_ble_status , btn_exercise;
    static Context mContext;
    Button btn_ble;
    ProgressDialog progressDialog;

    Handler handler;
    ImageView btn_weight, btn_exercise_setting, btn_report, btn_height, btn_seat, btn_sub_exercise, btn_how_to_exercise;
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
        btn_ble = view.findViewById(R.id.btn_ble);
        btn_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) MainActivity.mContext).setEnable();
                if (!mConnected){
                    ((MainActivity) MainActivity.mContext).setScanStart(false);

                }else{
                    ((MainActivity) MainActivity.mContext).setDisconnect();

                }

//                }

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (Preset.MaxWeight == 0 || Preset.MaxHeight == 0){
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_measure));

                        }else{
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_start));
                        }
                    }
                });
            }
        }).start();
        // Init

        // Init

        return view;
    }

    void init() {
        btn_height = view.findViewById(R.id.home_height_btn);
        btn_exercise_setting = view.findViewById(R.id.home_exercise_setting_btn);
        btn_how_to_exercise = view.findViewById(R.id.home_how_to_exercise_btn);
        btn_report = view.findViewById(R.id.home_report_btn);
        btn_seat = view.findViewById(R.id.home_exercise_seat_btn);
        btn_sub_exercise = view.findViewById(R.id.home_sub_exrcise_btn);
        btn_weight = view.findViewById(R.id.home_exercise_weight_btn);
        btn_exercise = view.findViewById(R.id.home_exercise_btn);

        btn_exercise.setText("");

    }

    void onClick() {

        btn_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;
//                Intent intent = new Intent(getActivity().getApplicationContext(), MeasureActivity.class);
//                intent.putExtra("height", true);
//
//                startActivity(intent);

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

                ((MainActivity) getActivity()).setBottomNavigation("PROCEDURE_DEFAULT");
            }
        });
        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;

                Intent intent = new Intent(getActivity().getApplicationContext() , AfterMeasureActivity.class);
                intent.putExtra("selectFrag" , "WEIGHT");
                startActivity(intent);
            }
        });
        btn_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (!isMeasure()) return;
                if (isHomeDevice()){
                    Toast.makeText(getContext(), "Fitness 제품만 의자 조절이 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getActivity().getApplicationContext() , AfterMeasureActivity.class);
                intent.putExtra("selectFrag" , "SEAT");
                startActivity(intent);
            }
        });
        btn_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectedBle()) return;
                if (Preset.MaxWeight == 0 || Preset.MaxHeight == 0){
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
                Intent intent = new Intent(mContext.getApplicationContext() , HowToExerciseActivity.class);
                startActivity(intent);
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

        if (mConnected){
                    setBleStatus(5);

        }else{
            setBleStatus(3);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Preset.MaxWeight == 0 || Preset.MaxHeight == 0){
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_measure));
                        }else{
                            btn_exercise.setText(MainActivity.mContext.getString(R.string.btn_start));
                        }
                    }
                },1500);
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
        }).start();
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
        if (Preset.MaxWeight == 0 || Preset.MaxHeight == 0){
            Toast.makeText(mContext, getString(R.string.toast_please_measure), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }


    // TODO: 2019-10-24 BLE ERROR CHECK
    String BLE_STATE_MSG;
    public void setBleStatus(final int Code) {
        Log.d(TAG, "----- setBleStatus: " + Code);

        BluetoothUtils.BLE_STATE_CODE = Code;

        switch (Code) {
            case 0:
                break;

            case 1:
                BLE_STATE_MSG = MainActivity.mContext.getString(R.string.ble_01);
                break;

            case 2:
                BLE_STATE_MSG = MainActivity.mContext.getString(R.string.ble_02);
                break;

            case 3:
                BLE_STATE_MSG = MainActivity.mContext.getString(R.string.ble_03);
                break;

            case 4:
                BLE_STATE_MSG = MainActivity.mContext.getString(R.string.ble_04);
                break;

            case 5:
                BLE_STATE_MSG = MainActivity.mContext.getString(R.string.ble_05);

                break;

            case 6:
                BLE_STATE_MSG = MainActivity.mContext.getString(R.string.ble_06);
                break;
        }

        Log.d(TAG, "- Constants.CFG_BLE_STATUS_MSG: " + BLE_STATE_MSG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_ble_status.setText(BLE_STATE_MSG);

                        switch (Code) {
                            case 0:
                                btn_ble.setVisibility(View.GONE);
                                break;

                            case 1:
                                btn_ble.setVisibility(View.GONE);
                                break;

                            case 2:
                                btn_ble.setText(R.string.btn_ble_enable);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));
                                break;

                            case 3:
                                btn_ble.setText(R.string.btn_ble_connect);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));
                                break;

                            case 4:
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));
                                break;

                            case 5:
                                btn_ble.setText(R.string.btn_ble_disconnect);

                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));

                                break;

                            case 6:
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));

                                break;
                        }
                    }
                });
            }
        }).start();
    }
}