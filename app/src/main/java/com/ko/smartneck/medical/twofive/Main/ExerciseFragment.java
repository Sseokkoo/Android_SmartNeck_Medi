package com.ko.smartneck.medical.twofive.Main;

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

import com.ko.smartneck.medical.twofive.GlobalApplication;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.BreakTimeDialog;
import com.ko.smartneck.medical.twofive.util.Commend;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.DialogSeatControl;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;


import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.isExProgress;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.mConnected;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.setMessage;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.tabPos;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.audioStop;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.member;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.setAudio;
import static com.ko.smartneck.medical.twofive.Main.ProcedureFragment.angleLength;
import static com.ko.smartneck.medical.twofive.Main.WeightSettingFragment.isWeight;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_HEIGHT;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_WEIGHT;
import static com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin;
import static com.ko.smartneck.medical.twofive.util.Constants.CFG_IS_EXERCISE;
import static com.ko.smartneck.medical.twofive.util.Constants.POUND;
import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class ExerciseFragment extends Fragment {
    TextView tv_weight_max, tv_weight_current, tv_count_current, tv_count_total, tv_set_current, tv_set_total, tv_stop;


    Context mContext;
    Handler handler;
    Button btn_save, btn_stop , btn_setup;
    double maxWeight;

    boolean isExerciseFinish = false;

    public static int CFG_WEIGHT_CURRENT = 0;
    public static int CFG_COUNT[] = {0, 0, 0, 0};
    public static int CFG_SET[] = {0, 0, 0, 0};
    MediaPlayer mediaPlayer;
    int stop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_exercise, container, false);
        mContext = GlobalApplication.getApllication();
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.set_end);
        stop = preset.getStop();
        ((MainActivity) Objects.requireNonNull(getActivity())).bottomNavigationView.setVisibility(View.VISIBLE);
        MainActivity.currentLocation = "exercise";
        tv_weight_max = (TextView) view.findViewById(R.id.tv_weight_max);
        tv_weight_current = (TextView) view.findViewById(R.id.tv_weight_current);
        tv_count_current = (TextView) view.findViewById(R.id.tv_count_current);
        tv_count_total = (TextView) view.findViewById(R.id.tv_count_total);
        tv_set_current = (TextView) view.findViewById(R.id.tv_set_current);
        tv_set_total = (TextView) view.findViewById(R.id.tv_set_total);
        tv_stop = (TextView) view.findViewById(R.id.tv_stop);
        tv_stop.setText(String.valueOf(preset.getStop()));

        tv_count_current.setText(String.valueOf(CFG_COUNT[1]));
        tv_set_current.setText(String.valueOf(CFG_SET[1]));

        btn_stop = view.findViewById(R.id.btn_stop);
        btn_setup = view.findViewById(R.id.btn_setup);
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
                    Toast.makeText(mContext, MainActivity.mContext.getString(R.string.dialog_exercise_quit_message), Toast.LENGTH_SHORT).show();
//                    timerHandler.sendEmptyMessage(TIMER_STOP);
//                    CFG_IS_EXERCISE = false;

                } else {
                    userPreference.addExericse(member , preset , CFG_COUNT[1] , CFG_SET[1]);
                    setEnable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
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
            }
        });

        btn_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSeatControl weightControlDialog = new DialogSeatControl(getContext());
                weightControlDialog.show();
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
        audioStop();

    }

    @Override
    public void onStart() {
        super.onStart();
        setExerciseStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "----- ExerciseFragment onResume");
        // TODO: 2019-10-25 Exercise_Finish
//        if (!isExerciseFinish){
//        mediaPlayer.stop();

        setEnable(true);
//        }
        tabPos = 4;

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        CFG_IS_EXERCISE = false;
        tabPos = -1;
        isWeight = false;
//        setMessage(new Commend().sendWeightMove((byte) preset.getSetup()));

    }

    String weightString = "";
    public static boolean isTimer = true;
    public static boolean isCount = false;
    public static int countTimer = 0;


    private void setExerciseStart() {

        isWeight = true;
//        CFG_IS_EXERCISE = true;
        int setup = preset.getSetup();
//        setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(setup) + " 00 00"));
        setMessage(new Commend().sendWeightMove((byte) setup));


        new Thread(new Runnable() {
            @Override
            public void run() {
                isTimer = false;
                isCount = false;
                CFG_IS_EXERCISE = true;
                countTimer = 0;

                String gender = "";
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
                String soundType = sharedPreferences.getString("sound", "female");
                if (soundType.equals("male")) {
                    gender = "m";
                } else if (soundType.equals("female")) {
                    gender = "f";
                }


                while (CFG_IS_EXERCISE) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (preset.isBreakTime()) continue;
                    double currentWeight = CFG_WEIGHT[1] * 0.1;

                    if (CFG_HEIGHT[1] == 0) {
                        weightString = String.format("%.1f", currentWeight);
                    } else if (CFG_HEIGHT[1] > 0) {
//태그2
                        if (currentWeight >= 7) {
                            currentWeight += 0.5;
                        } else if (currentWeight >= 4.5 && currentWeight <= 6.5) {
                            currentWeight += 1;
                        } else if (currentWeight >= 0.5 && currentWeight <= 4) {
                            currentWeight += 1.5;
                        }

                        if (Constants.language.equals("en")) {
                            currentWeight *= POUND;
                        }
                        weightString = String.format("%.1f", currentWeight);


                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_weight_current.setText(weightString);
                        }
                    });

                    if (isTimer && !isCount) {

                        countTimer += 1;
                        if (countTimer == 1 && preset.getStop() != 0) {
                            //stop 사운드 출력
                            setAudio("count_start");
                        }



                        if (countTimer >= preset.getStop() * 10) {

                            CFG_COUNT[1]++;
                            countTimer = 0;

                            isCount = true;
                            //count 사운드 출력
                            setAudio("count_complete");

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_count_current.setText(String.valueOf(CFG_COUNT[1]));

                                }
                            });
                            if (CFG_COUNT[1] == preset.getCount()) {
                                CFG_SET[1]++;
                                if (CFG_SET[1] == preset.getSet()) {
                                    /*
                                    * 운동종료
                                    * */
                                    userPreference.addExericse(member , preset , CFG_COUNT[1] , CFG_SET[1]);
                                    isExerciseFinish = true;
                                    setEnable(false);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while(CFG_HEIGHT[1] != 0){
                                                try {
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showAlertDialog();
//                                                    setAudio("count_complete");

                                                }
                                            });

                                        }
                                    }).start();


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




                                    //운동 종료
                                    //db에 정보입력
                                    //다이얼로그 출력
                                    break;
                                } else {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while(CFG_HEIGHT[1] != 0){
                                                try {
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mediaPlayer.start();
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            final BreakTimeDialog breakTimeDialog = new BreakTimeDialog(getContext(), getLayoutInflater() , mediaPlayer);
                                                            breakTimeDialog.showBreakTimeDialog();
                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    }).start();



                                }
                                CFG_COUNT[1] = 0;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_count_current.setText(String.valueOf(CFG_COUNT[1]));
                                        tv_set_current.setText(String.valueOf(CFG_SET[1]));
                                    }
                                });

                            }


                        } else if (countTimer < stop * 10) {
                            if (stop == 15) {
                                if (countTimer == 10) {
                                    setAudio(gender + 14);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 13);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 12);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 11);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 10);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 9);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 90) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 100) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 110) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 120) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 130) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 140) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 14) {
                                if (countTimer == 10) {
                                    setAudio(gender + 13);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 12);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 11);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 10);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 9);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 90) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 100) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 110) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 120) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 130) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 13) {
                                if (countTimer == 10) {
                                    setAudio(gender + 12);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 11);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 10);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 9);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 90) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 100) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 110) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 120) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 12) {
                                if (countTimer == 10) {
                                    setAudio(gender + 11);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 10);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 9);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 90) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 100) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 110) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 11) {
                                if (countTimer == 10) {
                                    setAudio(gender + 10);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 9);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 90) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 100) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 10) {
                                if (countTimer == 10) {
                                    setAudio(gender + 9);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 90) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 9) {
                                if (countTimer == 10) {
                                    setAudio(gender + 8);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 80) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 8) {
                                if (countTimer == 10) {
                                    setAudio(gender + 7);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 70) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 7) {
                                if (countTimer == 10) {
                                    setAudio(gender + 6);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 60) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 6) {
                                if (countTimer == 10) {
                                    setAudio(gender + 5);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 50) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 5) {
                                if (countTimer == 10) {
                                    setAudio(gender + 4);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 40) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 4) {
                                if (countTimer == 10) {
                                    setAudio(gender + 3);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 30) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 3) {
                                if (countTimer == 10) {
                                    setAudio(gender + 2);
                                } else if (countTimer == 20) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 2) {
                                if (countTimer == 10) {
                                    setAudio(gender + 1);
                                }
                            } else if (stop == 1) {

                            }

                        } else {
                            countTimer = 0;
                        }


                    }
                }//end while
            }


        }).start();
    }

    private int getExerciseSetup(int setup) {
        if (setup < 30) {
            return setup;
        } else if (setup >= 30) {
            return getExerciseSetup2(setup);
        }
        return 0;

    }

    private int getExerciseSetup2(int setup) {
        if (preset.getStrength()== 3) {
            return setup - 5;

        } else if (preset.getStrength()== 2) {
            return setup - 10;
        }else  if (preset.getStrength()== 1) {
            return setup - 20;
        }
        return setup;

    }


    public void setDeviceType() {


        setEnable(false);

        String angleSign = "°";
        String lengthSign_ko = "˚";
        String lengthSign_en = "˚";
        float height = preset.getMaxHeight();
        float angle = height * 1f;
        String heightStr = "";
        String angleStr = "";
        if (Constants.language.equals("ko")) {

            height *= angleLength;
            height *= preset.getHeightSetting();
//            heightStr = String.format("%.1f", height) + lengthSign_ko;

        } else if (Constants.language.equals("en")) {

            height *= angleLength;
            height *= preset.getHeightSetting();
//            heightStr = String.format("%.1f", height) + lengthSign_en;

        }

        angle *= preset.getHeightSetting();
        angleStr = String.format("%.1f", angle) + angleSign;

//        Preset.userHeightSetting = 0.95f;
//        double weight_fit = Preset.MaxWeight * 0.1;
//
//        tv_weight_max.setText(String.format("%.1f", weight_fit));
        tv_weight_max.setText(angleStr);

        tv_count_total.setText(String.valueOf(preset.getCount()));
        tv_set_total.setText(String.valueOf(preset.getSet()));

    }

    public void setEnable(boolean status) {
        if (!mConnected) return;
        if (CFG_IS_EXERCISE) return;
        Log.d(TAG, "----- setEnable: " + status);

        Constants.CFG_IS_EXERCISE = status;

//        btn_start.setVisibility(View.GONE);
        btn_stop.setVisibility(View.GONE);


        if (Constants.CFG_IS_EXERCISE) {

            btn_save.setVisibility(View.VISIBLE);

//            setCountInit();


        } else {
            if (!isExerciseFinish) {

                MainActivity.setAudio("exercise_start");
            }

            isExProgress = false;

        }
    }


    public static final int TIMER_START = 0;
    public static final int TIMER_STOP = 1;


    public TimerHandler timerHandler = new TimerHandler();

    public class TimerHandler extends Handler {
        int CFG_TIMER = 0;
        boolean startCount = true;

        @Override
        public void handleMessage(Message msg) {
            if (!CFG_IS_EXERCISE) return;
            if (preset.isBreakTime()) return;

            switch (msg.what) {
                case TIMER_START:
                    CFG_TIMER++;
                    double result_timer = CFG_TIMER * 0.1;

                    int limit = stop * 10;
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


    public void setCount() {
        Log.d(TAG, "----- setCount");

        isCount = true;

        int count_max = preset.getCount();
        int set_max = preset.getSet();


        if (CFG_COUNT[1] < count_max) {
            CFG_COUNT[1] = CFG_COUNT[1] + 1;
        }

        if (CFG_COUNT[1] == count_max) {
            CFG_SET[1] = CFG_SET[1] + 1;
            if (CFG_SET[1] != set_max) {
//                BreakTimeDialog breakTimeDialog = new BreakTimeDialog(getContext() , getLayoutInflater());
//                breakTimeDialog.showBreakTimeDialog();

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
            userPreference.addExericse(member , preset , CFG_COUNT[1] , CFG_SET[1] );
//            setCountInit();
            isExerciseFinish = true;
            setEnable(false);
            showAlertDialog();

        }
        setAudio("count_complete");


    }

    // TODO: 2019-10-25  exericse_finish
    private void showAlertDialog() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.setAudio("exercise_finish");

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

        name.setText(member.getName());
        count.setText(String.valueOf(CFG_COUNT[1]));
        set.setText(String.valueOf(CFG_SET[1]));
        stop.setText(String.valueOf(preset.getStop()));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
//                ((MainActivity) getActivity()).setBottomNavigation("HOME");
                getActivity().finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

//    private void insertExercise() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Param param = new Param();
////                param.add("token", User.token);
//                param.add("count", CFG_COUNT[1]);
//                param.add("set", CFG_SET[1]);
//                param.add("stop", stop);
//                param.add("totalCount", pastPreset.count);
//                param.add("totalSet", pastPreset.set);
//                param.add("productNo", pastPreset.DEVICE_NAME);
//
//                Address address = new Address();
//                HttpConnect httpConnect = new HttpConnect();
//                if (httpConnect.httpConnect(param.getValue(), address.getInsertExercise(), true) == 200) {
//
//                } else {
//
//                }
//
//
//            }
//        }).start();
//    }

}
