package com.smartneck.twofive.Fit.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import com.smartneck.twofive.GlobalApplication;
import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_BreakTimeDialog;
import com.smartneck.twofive.Fit.util.Fit_Commend;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_DialogSeatControl;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.Fit_StringUtils;
import com.smartneck.twofive.Fit.util.User.Fit_Preset;
import com.smartneck.twofive.Fit.util.User.Fit_User;

import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.audioStop;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.isExProgress;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.mConnected;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.protocolType;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.setAudio;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.setMessage;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.tabPos;
import static com.smartneck.twofive.Fit.Main.Fit_WeightSettingFragment.currentWeight;
import static com.smartneck.twofive.Fit.Main.Fit_WeightSettingFragment.tmpSetup;
import static com.smartneck.twofive.Fit.Fit_MeasureActivity.CFG_WEIGHT;
import static com.smartneck.twofive.Fit.util.Fit_Constants.CFG_IS_EXERCISE;
import static com.smartneck.twofive.Fit.util.Fit_Constants.TAG;
import static com.smartneck.twofive.Fit.util.Fit_Constants.deviceType;
import static com.smartneck.twofive.Fit.util.User.Fit_Preset.breakTime;
import static com.smartneck.twofive.Fit.util.User.Fit_Preset.isBreakTime;
import static com.smartneck.twofive.Fit.util.User.Fit_Preset.strength;
import static com.smartneck.twofive.Fit.util.User.Fit_Preset.userHeightSetting;

public class Fit_ExerciseFragment extends Fragment {
    TextView tv_weight_max, tv_weight_current, tv_count_current, tv_count_total, tv_set_current, tv_set_total, tv_stop;


    Context mContext;
    Handler handler;
    Button btn_save, btn_stop , btn_setup;

    boolean isExerciseFinish = false;

    public static int CFG_WEIGHT_CURRENT = 0;
    public static int CFG_COUNT[] = {0, 0, 0, 0};
    public static int CFG_SET[] = {0, 0, 0, 0};
    public static boolean isDefault = false;
    static int defaultCount;
    static int defaultSet;
    static int defaultStop;
    static float defaultHeight;
    static int defaultBreakTime = 20;
    static String defaultSound = "female";

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_exercise, container, false);
        mContext = GlobalApplication.getApllication();
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.set_end);

        ((Fit_MainActivity) getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
        Fit_MainActivity.currentLocation = "exercise";
        tv_weight_max = (TextView) view.findViewById(R.id.tv_weight_max);
        tv_weight_current = (TextView) view.findViewById(R.id.tv_weight_current);
        tv_count_current = (TextView) view.findViewById(R.id.tv_count_current);
        tv_count_total = (TextView) view.findViewById(R.id.tv_count_total);
        tv_set_current = (TextView) view.findViewById(R.id.tv_set_current);
        tv_set_total = (TextView) view.findViewById(R.id.tv_set_total);
        tv_stop = (TextView) view.findViewById(R.id.tv_stop);
        btn_setup = view.findViewById(R.id.btn_setup);
        if (isDefault) {
            tv_stop.setText(String.valueOf(defaultStop));
        } else {
            tv_stop.setText(String.valueOf(Fit_Preset.stop));
        }


        btn_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fit_DialogSeatControl weightControlDialog = new Fit_DialogSeatControl(getContext());
                weightControlDialog.show();
            }
        });

        tv_count_current.setText(String.valueOf(CFG_COUNT[1]));
        tv_set_current.setText(String.valueOf(CFG_SET[1]));

        btn_stop = view.findViewById(R.id.btn_stop);
        btn_stop.setVisibility(View.GONE);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnable(false);
            }
        });

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "- btn_save onClick");
                if (CFG_COUNT[1] == 0 && CFG_SET[1] == 0) {
                    // TODO: 2019-07-20 운동 종료 다이얼로그
                    Toast.makeText(mContext, Fit_MainActivity.mContext.getString(R.string.dialog_exercise_quit_message), Toast.LENGTH_SHORT).show();
//                    timerHandler.sendEmptyMessage(TIMER_STOP);
//                    CFG_IS_EXERCISE = false;

                } else {
                    insertExercise();
                    setEnable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    showAlertDialog();
                                }
                            });
                        }
                    }).start();

                }
            }
        });

        setDeviceType();
        isExerciseFinish = false;
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        handler = new Handler();
        setExerciseStart();
        audioStop();

    }

    @Override
    public void onResume() {
        Log.d(TAG, "----- ExerciseFragment onResume");
        // TODO: 2019-10-25 Exercise_Finish
//        if (!isExerciseFinish){

        setEnable(true);
//        }
        Fit_MainActivity.tabPos = 3;

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        CFG_IS_EXERCISE = false;
        tabPos = -1;
//        setWeightThread();
    }


    private void setWeightThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if (protocolType.equals("3a")) {
//                    setMessage(Fit_StringUtils.getCommand("44 3A 07 00 03 " + Fit_StringUtils.getHexStringCode(Fit_Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + Fit_StringUtils.getHexStringCode(setup) + " 00 00"));

//                } else if (protocolType.equals("3b")) {
                    setMessage(new Fit_Commend().sendWeightMove((byte) setup));
//                }
                int count = 0;
                while (mConnected) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "run: " + currentWeight + " / " + Fit_Preset.setup);
                    if (CFG_WEIGHT[1] != tmpSetup) {
//                        if (protocolType.equals("3a")) {
//                            setMessage(Fit_StringUtils.getCommand("44 3A 07 00 03 " + Fit_StringUtils.getHexStringCode(Fit_Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + Fit_StringUtils.getHexStringCode(setup) + " 00 00"));

//                        } else if (protocolType.equals("3b")) {
                            setMessage(new Fit_Commend().sendWeightMove((byte) setup));
//                        }

                    } else if (CFG_WEIGHT[1] == tmpSetup) {
                        Log.d(TAG, "GO_EXERCISE_BREAK: ");
                        Fit_Preset.setup = tmpSetup;
                        break;
                    }

                    if (count > 5) {
                        break;
                    }
                    count++;
                }


            }
        }).start();
    }

    int setup = 0;

    private void setExerciseStart() {


        if (isDefault) {
            setup = getExerciseSetup(Fit_Preset.measureSetup);
        } else {
            setup = getExerciseSetup(Fit_Preset.setup);
        }

//        if (protocolType.equals("3a")) {
//
//            setMessage(Fit_StringUtils.getCommand("44 3A 07 00 03 " + Fit_StringUtils.getHexStringCode(Fit_Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + Fit_StringUtils.getHexStringCode(setup) + " 00 00"));
//
//        } else if (protocolType.equals("3b")) {

            setMessage(new Fit_Commend().sendWeightMove((byte) setup));

//        }


    }

    private int getExerciseSetup(int setup) {

        Log.d(TAG, "getExerciseSetup: " + setup);
        if (!isDefault){
            return setup;
        }

        if (setup == 15) {

             if (strength == 1) {
                setup -= 5;
            }

        } else if (setup == 20) {
            if (strength == 3) {
                setup -= 5;

            } else if (strength == 2) {
                setup -= 5;
            } else if (strength == 1) {
                setup -= 10;
            }
        } else if (setup == 25) {
            if (strength == 3) {
                setup -= 5;
            } else if (strength == 2) {
                setup -= 10;
            } else if (strength == 1) {
                setup -= 15;
            }
        } else if (setup == 30) {
            if (strength == 3) {
                setup -= 10;
            } else if (strength == 2) {
                setup -= 10;
            } else if (strength == 1) {
                setup -= 20;
            }
        } else if (setup == 35) {
            if (strength == 3) {
                setup -= 10;
            } else if (strength == 2) {
                setup -= 15;
            } else if (strength == 1) {
                setup -= 20;
            }
        } else if (setup == 40) {
            if (strength == 3) {
                setup -= 10;
            } else if (strength == 2) {
                setup -= 15;
            } else if (strength == 1) {
                setup -= 20;
            }
        } else if (setup == 45) {
            if (strength == 3) {
                setup -= 15;
            } else if (strength == 2) {
                setup -= 15;
            } else if (strength == 1) {
                setup -= 25;
            }
        } else if (setup == 50) {
            if (strength == 3) {
                setup -= 10;
            } else if (strength == 2) {
                setup -= 20;
            } else if (strength == 1) {
                setup -= 30;
            }
        } else if (setup == 55) {
            if (strength == 3) {
                setup -= 15;
            } else if (strength == 2) {
                setup -= 20;
            } else if (strength == 1) {
                setup -= 35;
            }
        } else if (setup == 60) {
            if (strength == 3) {
                setup -= 15;
            } else if (strength == 2) {
                setup -= 20;
            } else if (strength == 1) {
                setup -= 35;
            }
        } else if (setup == 65) {
            if (strength == 3) {
                setup -= 20;
            } else if (strength == 2) {
                setup -= 25;
            } else if (strength == 1) {
                setup -= 35;
            }
        }

        return setup;

    }

//    private int getExerciseSetup2(int setup){
//
//
//    }

    public void setDeviceType() {


        setEnable(false);

        String angleSign = "°";
//        String lengthSign_ko = "mm";
//        String lengthSign_en = "inch";
//        float height = Fit_Preset.MaxHeight * 3;
        float angle = 1f;
        String heightStr = "";
        String angleStr = "";
//        if (Fit_User.language.equals("ko")) {
//
//            height *= userHeightSetting;
//            heightStr = String.format("%.1f", height) + lengthSign_ko;
//
//        } else if (Fit_User.language.equals("en")) {
//
//            height /= Fit_Constants.INCHES;
//            height *= userHeightSetting;
//            heightStr = String.format("%.1f", height) + lengthSign_en;
//
//        }

        angle *= userHeightSetting;
        angleStr = String.format("%.1f", angle) + angleSign;

        tv_weight_max.setText(angleStr);

//
//        double weight_fit = Preset.MaxWeight * 0.1;
//        tv_weight_max.setText(String.format("%.1f" , weight_fit));
        if (isDefault) {
            tv_count_total.setText(String.valueOf(defaultCount));
            tv_set_total.setText(String.valueOf(defaultSet));
        } else {
            tv_count_total.setText(String.valueOf(Fit_Preset.count));
            tv_set_total.setText(String.valueOf(Fit_Preset.set));
        }


    }

    public void setEnable(boolean status) {
        if (!mConnected) return;
        if (CFG_IS_EXERCISE) return;
        Log.d(TAG, "----- setEnable: " + status);

        Fit_Constants.CFG_IS_EXERCISE = status;

//        btn_start.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);
        btn_stop.setVisibility(View.GONE);


        if (Fit_Constants.CFG_IS_EXERCISE) {

            btn_save.setVisibility(View.VISIBLE);

//            setCountInit();


        } else {
            if (!isExerciseFinish) {

                Fit_MainActivity.setAudio("exercise_start");
            }

            isExProgress = false;

        }
    }


    public static final int TIMER_START = 0;
    public static final int TIMER_STOP = 1;
    public static boolean isActive = false;
    public static boolean isCount = false;

    public TimerHandler timerHandler = new TimerHandler();

    public class TimerHandler extends Handler {
        int CFG_TIMER = 0;
        boolean startCount = true;

        @Override
        public void handleMessage(Message msg) {
            if (!CFG_IS_EXERCISE) return;
            if (isBreakTime) return;
            switch (msg.what) {
                case TIMER_START:
                    CFG_TIMER++;
                    double result_timer = CFG_TIMER * 0.1;
                    int limit = 0;
                    int stop = 0;
                    if (isDefault) {
                        limit = defaultStop * 10;
                        stop = defaultStop;
                    } else {
                        limit = Fit_Preset.stop * 10;
                        stop = Fit_Preset.stop;

                    }
                    String gender = "";
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
                    String soundType = sharedPreferences.getString("sound", "female");
                    if (soundType.equals("male")) {
                        gender = "m";
                    } else if (soundType.equals("female")) {
                        gender = "f";
                    }

                    if (startCount) {
                        if (limit == 0 && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");
                            startCount = false;
                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();

                        } else {
                            setAudio("count_start");
                            startCount = false;
                        }

                    }
                    if (stop == 1) {
                        if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 2) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 3) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 4) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 5) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 4");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 6) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 4");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 5");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 7) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 4");
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 5");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 6");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 8) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 4");
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 5");
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 6");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 7");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 9) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 4");
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 5");
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 6");
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 7");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 8");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 10) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 1");

                            setAudio(gender + "9");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 2");
                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 3");
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 4");
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 5");
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 6");
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 7");
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 8");
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 90 && CFG_TIMER < limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : 9");
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            Log.i(TAG, "handleMessage: count : limit");

                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 11) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "10");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "9");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 90 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 100 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 12) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "11");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "10");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "9");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 90 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 100 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 110 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 13) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "12");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "11");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "10");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "9");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 90 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 100 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 110 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 120 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 14) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "13");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "12");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "11");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "10");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "9");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 90 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 100 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 110 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 120 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 130 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    } else if (stop == 15) {
                        if (CFG_TIMER == 10 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "14");
                        } else if (CFG_TIMER == 20 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "13");
                        } else if (CFG_TIMER == 30 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "12");
                        } else if (CFG_TIMER == 40 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "11");
                        } else if (CFG_TIMER == 50 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "10");
                        } else if (CFG_TIMER == 60 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "9");
                        } else if (CFG_TIMER == 70 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "8");
                        } else if (CFG_TIMER == 80 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "7");
                        } else if (CFG_TIMER == 90 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "6");
                        } else if (CFG_TIMER == 100 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "5");
                        } else if (CFG_TIMER == 110 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "4");
                        } else if (CFG_TIMER == 120 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "3");
                        } else if (CFG_TIMER == 130 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "2");
                        } else if (CFG_TIMER == 140 && CFG_TIMER < limit && !isCount) {
                            setAudio(gender + "1");
                        } else if (CFG_TIMER == limit && !isCount) {
                            CFG_TIMER = 0;
                            this.removeMessages(TIMER_START);
                            setCount();
                        }
                    }


                    this.sendEmptyMessageDelayed(TIMER_START, 100);
                    break;

                case TIMER_STOP:
                    CFG_TIMER = 0;
                    double result_timer_stop = CFG_TIMER;
                    startCount = true;
                    this.removeMessages(TIMER_START);
                    break;
            }
        }
    }

    int height_current;

    public void setExercise(String[] CFG_RECEIVED) {
        // Log.d(Constants.TAG, "----- setExercise: " + CFG_RECEIVED);

        String CFG_RECEIVE_CODE = CFG_RECEIVED[2];

        switch (CFG_RECEIVE_CODE) {
            case "81": // RSP_PP_DATA
                Fit_Preset.seat = Integer.parseInt(CFG_RECEIVED[3], 16);
                break;

            case "82": // EXERCISE_DATA
                if (protocolType.equals("3a")) {
                    height_current = Integer.parseInt(CFG_RECEIVED[4], 16);
                    CFG_WEIGHT_CURRENT = Integer.parseInt(CFG_RECEIVED[8], 16);
                } else if (protocolType.equals("3b")) {
                    if (deviceType.equals("HOM")){
                        height_current = Fit_Preset.getHomeHeightValue(Integer.parseInt(CFG_RECEIVED[8], 16));
                    }else{
                        height_current = Integer.parseInt(CFG_RECEIVED[8], 16);
                    }

                    CFG_WEIGHT_CURRENT = Integer.parseInt(CFG_RECEIVED[5], 16);
                }

                int tempCurrent = height_current;
                // TODO: 2019-05-18 운동 리미트 설정
                int tempMax = 0;
                if (isDefault) {
                    tempMax = (int) (Fit_Preset.MaxHeight * defaultHeight);

                } else {
                    tempMax = (int) (Fit_Preset.MaxHeight * userHeightSetting);

                }


                if (tempCurrent >= tempMax) {
                    Log.d(Fit_Constants.TAG, "- OK: " + tempCurrent + " / " + tempMax);

                    if (isActive == false && isCount == false) {
                        isActive = true;
                        timerHandler.sendEmptyMessage(TIMER_START);
                    }
                } else {
                    Log.d(Fit_Constants.TAG, "- NO: " + tempCurrent + " / " + tempMax);

                    if (tempCurrent == height_current) {
                        isCount = false;
                    }

                    isActive = false;
                    timerHandler.sendEmptyMessage(TIMER_STOP);
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                double result = CFG_WEIGHT_CURRENT * 0.1;

                                if (height_current > 0) {
                                    if (CFG_WEIGHT_CURRENT > 0) {
//                                        if (CFG_WEIGHT_CURRENT >= 70) {
//                                            result += 0.5;
//                                        } else if (CFG_WEIGHT_CURRENT >= 45 && CFG_WEIGHT_CURRENT <= 65) {
//                                            result += 1;
//                                        } else if (CFG_WEIGHT_CURRENT >= 5 && CFG_WEIGHT_CURRENT <= 40) {
//                                            result += 1.5;
//                                        }
                                    }

                                    if (Fit_User.language.equals("en")) {
                                        result *= Fit_Constants.POUND;
                                    }
                                    tv_weight_current.setText(String.format("%.1f", result));
                                } else {
                                    tv_weight_current.setText(String.format("%.1f", result));
                                }
                            }
                        });
                    }
                }).start();
                break;

            case "83": // MOTOR COMPLETE

                break;
        }
    }

//    public void setCountInit() {
//        Log.d(TAG, "----- setCountInit");
//
//        CFG_COUNT[1] = 0;
//        CFG_SET[1] = 0;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }).start();
//    }

    public void setCount() {
        Log.d(TAG, "----- setCount");

        isCount = true;

        int count_max = 0;
        int set_max = 0;
        if (isDefault) {
            count_max = defaultCount;
            set_max = defaultSet;
        } else {
            count_max = Fit_Preset.count;
            set_max = Fit_Preset.set;
        }


        if (CFG_COUNT[1] < count_max) {
            CFG_COUNT[1] = CFG_COUNT[1] + 1;
        }

        if (CFG_COUNT[1] == count_max) {
            CFG_SET[1] = CFG_SET[1] + 1;
            if (CFG_SET[1] != set_max) {
                if (isDefault){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while(true){
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (height_current == 0){
                                    break;
                                }
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Fit_BreakTimeDialog breakTimeDialog = new Fit_BreakTimeDialog(getContext(), getLayoutInflater(), mediaPlayer , defaultBreakTime);
                                    breakTimeDialog.showBreakTimeDialog();
                                    mediaPlayer.start();

                                }
                            });

                        }
                    }).start();



                }else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while(true){
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (height_current == 0){
                                    break;
                                }
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Fit_BreakTimeDialog breakTimeDialog = new Fit_BreakTimeDialog(getContext(), getLayoutInflater(), mediaPlayer , breakTime);
                                    breakTimeDialog.showBreakTimeDialog();
                                    mediaPlayer.start();

                                }
                            });

                        }
                    }).start();

                }


            }
            if (CFG_SET[1] != set_max) CFG_COUNT[1] = 0;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_count_current.setText(Integer.toString(CFG_COUNT[1]));
                        tv_set_current.setText(Integer.toString(CFG_SET[1]));
                    }
                });
            }
        }).start();

        if (CFG_SET[1] == set_max) {
            insertExercise();
//            setCountInit();
            isExerciseFinish = true;
            setEnable(false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    while(true){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (height_current == 0){
                            break;
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAlertDialog();
                        }
                    });

                }
            }).start();

        }
        setAudio("count_complete");


    }

    // TODO: 2019-10-25  exericse_finish
    private void showAlertDialog() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fit_MainActivity.setAudio("exercise_finish");

            }
        }, 1000);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_exercise_complete, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel = view.findViewById(R.id.complete_dismiss);
        TextView count = view.findViewById(R.id.complete_count);
        TextView set = view.findViewById(R.id.complete_set);
        TextView name = view.findViewById(R.id.complete_name);
        TextView stop = view.findViewById(R.id.complete_stop);

        name.setText(Fit_User.name);
        count.setText(String.valueOf(CFG_COUNT[1]));
        set.setText(String.valueOf(CFG_SET[1]));
        if (isDefault) {
            stop.setText(String.valueOf(defaultStop));

        } else {
            stop.setText(String.valueOf(Fit_Preset.stop));

        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                ((Fit_MainActivity) getActivity()).setBottomNavigation("HOME");
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void insertExercise() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int totalCount = 0;
                int totalSet = 0;
                int stop = 0;

                if (isDefault) {
                    totalCount = defaultCount;
                    totalSet = defaultSet;
                    stop = defaultStop;
                } else {
                    totalCount = Fit_Preset.count;
                    totalSet = Fit_Preset.set;
                    stop = Fit_Preset.stop;
                }

                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                param.add("memberNo", Fit_User.MemberNo);
                param.add("count", CFG_COUNT[1]);
                param.add("set", CFG_SET[1]);
                param.add("stop", stop);
                param.add("totalCount", totalCount);
                param.add("totalSet", totalSet);
                param.add("productNo", Fit_Preset.DEVICE_NAME);

                Fit_Address address = new Fit_Address();
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                if (httpConnect.httpConnect(param.getParam(), address.getInsertExercise()) == 200) {

                } else {

                }

                Log.d(TAG, "getReceiveMessage: " + httpConnect.getReceiveMessage());
                Log.d(TAG, "getResponseCode: " + httpConnect.getResponseCode());


            }
        }).start();
    }

}
