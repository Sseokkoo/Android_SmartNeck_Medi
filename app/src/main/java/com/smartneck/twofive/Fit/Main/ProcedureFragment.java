package com.smartneck.twofive.Fit.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Address;
import com.smartneck.twofive.Fit.util.Constants;
import com.smartneck.twofive.Fit.util.HttpConnect;
import com.smartneck.twofive.Fit.util.NoticeDialog;
import com.smartneck.twofive.Fit.util.Param;
import com.smartneck.twofive.Fit.util.User.Preset;
import com.smartneck.twofive.Fit.util.User.User;

import static com.smartneck.twofive.Fit.GlobalApplication.getApllication;
import static com.smartneck.twofive.Fit.Main.MainActivity.audioStop;
import static com.smartneck.twofive.Fit.util.Constants.TAG;

public class ProcedureFragment extends Fragment {

    Context mContext;
    Handler handler;
    ViewGroup view;

    Button btn_save , btn_setting;


    ImageView btn_help_count, btn_help_set, btn_help_stop, btn_help_voice, btn_help_height , btn_help_weight , btn_help_break;
    ImageView btn_count_up, btn_count_down, btn_set_up, btn_set_down, btn_stop_up, btn_stop_down , btn_break_up , btn_break_down , btn_weight_up , btn_weight_down;

    TextView tv_count, tv_set, tv_stop, tv_angle, tv_height , tv_weight , tv_break;

    RadioButton sound_man, sound_woman, sound_child;
    RadioButton strength_high, strength_mid, strength_low;

    Spinner spn_height_select;

    NoticeDialog noticeDialog;

    String angleSign = "°";
    String lengthSign_ko = "mm";
    String lengthSign_en = "inch";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_procedure, container, false);
        mContext = getApllication();
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);

        init();


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioStop();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        ExerciseFragment.isDefault = false;
        MainActivity.setAudio("exercise_setting");
        Log.d(TAG, "----- ProcedureFragment onResume");
        MainActivity.tabPos = 2;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
        String soundType = sharedPreferences.getString("sound", "female");
        if (soundType.equals("male")) {
            sound_man.setChecked(true);
            sound_woman.setChecked(false);
            sound_child.setChecked(false);
        } else if (soundType.equals("female")) {
            sound_man.setChecked(false);
            sound_woman.setChecked(true);
            sound_child.setChecked(false);
        } else if (soundType.equals("child")) {
            sound_man.setChecked(false);
            sound_woman.setChecked(false);
            sound_child.setChecked(true);
        }

        if (strength_high.isChecked()){
            Preset.strength = 3;
        }else if (strength_mid.isChecked()){
            Preset.strength = 2;
        }else if (strength_low.isChecked()){
            Preset.strength = 1;
        }
        setChecked();

    }

    private void setChecked() {
        if (sound_man.isChecked()) {
            sound_woman.setChecked(false);
        } else if (sound_woman.isChecked()) {
            sound_man.setChecked(false);
        }
    }


    void init() {

        handler = new Handler();


        if (Preset.count < 5) {
            Preset.count = 5;
        }
        btn_setting = view.findViewById(R.id.btn_exercise_strength_setting);

        sound_man = view.findViewById(R.id.sound_man);
        sound_woman = view.findViewById(R.id.sound_woman);
        sound_child = view.findViewById(R.id.sound_child);

        sound_child.setVisibility(View.GONE);

        btn_save = view.findViewById(R.id.btn_save);
        btn_help_count = view.findViewById(R.id.procedure_help_1);
        btn_help_set = view.findViewById(R.id.procedure_help_2);
        btn_help_stop = view.findViewById(R.id.procedure_help_3);
        btn_help_voice = view.findViewById(R.id.procedure_help_4);
        btn_help_height = view.findViewById(R.id.procedure_help_5);
//        btn_help_weight = view.findViewById(R.id.procedure_help_6);

        btn_count_up = view.findViewById(R.id.procedure_up_count);
        btn_set_up = view.findViewById(R.id.procedure_up_set);
        btn_stop_up = view.findViewById(R.id.procedure_up_stop);

        btn_count_down = view.findViewById(R.id.procedure_down_count);
        btn_set_down = view.findViewById(R.id.procedure_down_set);
        btn_stop_down = view.findViewById(R.id.procedure_down_stop);

        tv_count = view.findViewById(R.id.procedure_tv_count);
        tv_set = view.findViewById(R.id.procedure_tv_set);
        tv_stop = view.findViewById(R.id.procedure_tv_stop);

        tv_count.setText(String.valueOf(Preset.count));
        tv_set.setText(String.valueOf(Preset.set));
        tv_stop.setText(String.valueOf(Preset.stop));


        tv_angle = view.findViewById(R.id.tv_exercise_setting_angle);
        tv_height = view.findViewById(R.id.tv_exercise_setting_height);
        spn_height_select = view.findViewById(R.id.spn_exercise_setting_height_select);

        strength_high = view.findViewById(R.id.exercise_strength_3);
        strength_mid = view.findViewById(R.id.exercise_strength_2);
        strength_low = view.findViewById(R.id.exercise_strength_1);

        tv_break = view.findViewById(R.id.procedure_tv_break);
        btn_help_break = view.findViewById(R.id.procedure_help_break);
        btn_break_up = view.findViewById(R.id.procedure_up_break);
        btn_break_down = view.findViewById(R.id.procedure_down_break);

        tv_weight = view.findViewById(R.id.procedure_tv_weight);
        btn_help_weight = view.findViewById(R.id.procedure_help_weight);
        btn_weight_up = view.findViewById(R.id.procedure_up_weight);
        btn_weight_down = view.findViewById(R.id.procedure_down_weight);


        tv_weight.setText(String.valueOf( (Preset.setup) * 0.1));
        tv_break.setText(String.valueOf(Preset.breakTime));

        //fixme 중문 남녀 음성 추가 시 삭제

        if (User.language == "zh") {
            TextView cn_gone = view.findViewById(R.id.cn_gone1);
            cn_gone.setVisibility(View.GONE);
            btn_help_voice.setVisibility(View.GONE);
            sound_man.setVisibility(View.GONE);
            sound_woman.setVisibility(View.GONE);
        }

        SharedPreferences preferences = getContext().getSharedPreferences("heightEntry", Context.MODE_PRIVATE);
        int entrySelection = preferences.getInt("entry", 8);

        spn_height_select.setSelection(entrySelection);
        onClcik();


    }

    private void onClcik() {
        //    Constants.CFG_PROCEDURE_WEIGHT[1] = progress;
//    Constants.Preset.count = progress;
//    Preset.set = progress;
//    Constants.Preset.stop = progress;

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setBottomNavigation("PROCEDURE_DEFAULT");

            }
        });

        strength_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preset.strength = 3;
                strength_high.setChecked(true);
                strength_low.setChecked(false);
                strength_mid.setChecked(false);
            }
        });

        strength_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preset.strength = 2;
                strength_high.setChecked(false);
                strength_low.setChecked(false);
                strength_mid.setChecked(true);
            }
        });

        strength_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preset.strength = 1;
                strength_high.setChecked(false);
                strength_low.setChecked(true);
                strength_mid.setChecked(false);
            }
        });

        sound_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("sound", "male");
                editor.apply();
                Preset.soundType = "male";
                sound_man.setChecked(true);
                sound_woman.setChecked(false);
                setChecked();

            }
        });
        sound_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("sound", "female");
                editor.apply();
                Preset.soundType = "female";
                sound_woman.setChecked(true);
                sound_man.setChecked(false);
                setChecked();
            }
        });
        sound_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("sound", "child");
                editor.apply();
                Preset.soundType = "child";
                setChecked();

            }
        });
        btn_count_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preset.count < 15) {
                    Preset.count++;
                    tv_count.setText(String.valueOf(Preset.count));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preset.count > 5) {
                    Preset.count--;
                    tv_count.setText(String.valueOf(Preset.count));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_set_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preset.set < 4) {
                    Preset.set++;
                    tv_set.setText(String.valueOf(Preset.set));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_set_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preset.set > 1) {
                    Preset.set--;
                    tv_set.setText(String.valueOf(Preset.set));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_stop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preset.stop < 15) {
                    Preset.stop++;
                    tv_stop.setText(String.valueOf(Preset.stop));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_stop_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Preset.stop > 0) {
                    Preset.stop--;
                    tv_stop.setText(String.valueOf(Preset.stop));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_break_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preset.breakTime == 60 ){
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Preset.breakTime += 5;
                    tv_break.setText(String.valueOf(Preset.breakTime));

                }
            }
        });

        btn_break_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preset.breakTime == 15 ){
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Preset.breakTime -= 5;
                    tv_break.setText(String.valueOf(Preset.breakTime));
                }
            }
        });

        btn_weight_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preset.setup == 65){
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Preset.setup +=5;
                    tv_weight.setText(String.valueOf((Preset.setup) * 0.1));
                }
            }
        });

        btn_weight_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preset.setup == 0){
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Preset.setup -=5;
                    tv_weight.setText(String.valueOf((Preset.setup) * 0.1));
                }
            }
        });

        final String title1 = getString(R.string.exercise_count);
        final String title2 = getString(R.string.exercise_set);
        final String title3 = getString(R.string.exercise_stop_time);
        final String title4 = getString(R.string.exercise_setting_voice_guide);
        final String title5 = getString(R.string.exercise_start_position);
        // TODO: 2020-08-31 weight로 변경 R.String
        final String title6 = getString(R.string.statistics_menu_weight);
        // TODO: 2020-08-31 Break Time로 변경
        final String title7 = getString(R.string.statistics_menu_weight);

        final String Message1 = getString(R.string.dialog_count);
        final String Message2 = getString(R.string.dialog_set);
        final String Message3 = getString(R.string.dialog_stop);
        final String Message4 = getString(R.string.dialog_voice_guide);
        final String Message5 = getString(R.string.dialog_exercise_point);
        // TODO: 2020-08-31 weight 설명으로 변경  R.String
        final String Message6 = getString(R.string.exericse_setting_weight_notice);
        // TODO: 2020-08-31 Break Time 설명으로 변경
        final String Message7 = getString(R.string.exericse_setting_weight_notice);

        btn_help_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title1, Message1);
                noticeDialog.showAlertDialog();
            }
        });
        btn_help_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title2, Message2);
                noticeDialog.showAlertDialog();
            }
        });
        btn_help_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title3, Message3);
                noticeDialog.showAlertDialog();
            }
        });
        btn_help_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title4, Message4);
                noticeDialog.showAlertDialog();
            }
        });

        btn_help_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title5, Message5);
                noticeDialog.showAlertDialog();
            }
        });

        btn_help_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title6, Message6);
                noticeDialog.showAlertDialog();
            }
        });

        btn_help_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog = new NoticeDialog(getActivity(), getLayoutInflater(), title7, Message7);
                noticeDialog.showAlertDialog();
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseFragment.isDefault = false;

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sound", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                if (sound_man.isChecked()) {
                    editor.putString("sound", "male");
                } else if (sound_woman.isChecked()) {
                    editor.putString("sound", "female");
                } else if (sound_child.isChecked()) {
                    editor.putString("sound", "child");
                }

                editor.apply();

                Log.d(TAG, "sound: " + sharedPreferences.getString("sound", "non"));
                updatePreset();
                ((MainActivity) getActivity()).setBottomNavigation("EXERCISE");

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Param param = new Param();
//                        param.add("MemberToken" , User.token);
//                        param.add("ProcedureCount" , String.valueOf(Preset.count));
//                        param.add("ProcedureSet" , String.valueOf(Preset.set));
//                        param.add("ProcedureStop" , String.valueOf(Preset.stop));
//                        Address address = new Address();
//                        Log.d(TAG, "param: " + param.getParam());
//                        HttpConnect httpConnect = new HttpConnect();
//                        if (httpConnect.httpConnect(param.getParam() , address.getUpdatePreset()) == 200){
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ((MainActivity)getActivity()).setBottomNavigation("EXERCISE");
//
//                                }
//                            });
//
//                        }else{
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(mContext, "fail.", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                        Log.d(TAG, "receive: " + httpConnect.getReceiveMessage());
//
//
//                    }
//                }).start();
            }
        });
        spn_height_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setHeightSelectEntry(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        updatePreset();
    }

    private void updatePreset() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Param param = new Param();
                param.add("memberNo", User.MemberNo);
                param.add("token", User.token);
                param.add("count", Preset.count);
                param.add("set", Preset.set);
                param.add("stop", Preset.stop);
                param.add("strength", Preset.strength);
                param.add("type", "preset");
                param.add("breakTime" , Preset.breakTime);
                param.add("setup" , Preset.setup);
                Address address = new Address();
                Log.d(TAG, "param: " + param.getParam());
                HttpConnect httpConnect = new HttpConnect();
                if (httpConnect.httpConnect(param.getParam(), address.getUpdatePreset()) == 200) {


                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "fail.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Log.d(TAG, "receive: " + httpConnect.getReceiveMessage());


            }
        }).start();
    }

    private void setHeightSelectEntry(int selected) {

        SharedPreferences preferences = getContext().getSharedPreferences("heightEntry", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("entry", selected);
        editor.apply();

        float angle = Preset.MaxHeight * 0.9f;
        float height = Preset.MaxHeight;
        String heightStr = "";
        String angleStr;

        switch (selected) {

            //(%)
            case 0://40

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.4;

                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.4;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.4;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.4f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;

            case 1://45

                if (User.language.equals("en!")) {

                    height *= 3;
                    height *= 0.45;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.45;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.45;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.45f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 2://50

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.5;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.5;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.5;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.5f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 3://55

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.55;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.55;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.55;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.55f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 4://60

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.6;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.6;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.6;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.6f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 5://65

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.65;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.65;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.65;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.65f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 6://70

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.7;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.7;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.7;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.7f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 7://75

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.75;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.75;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.75;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.75f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 8://80

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.8;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.8;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.8;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.8f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 9://85

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.85;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.85;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.85;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.85f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 10://90

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.9;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.9;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.9;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.9f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 11://95

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 0.95;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 0.95;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 0.95;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 0.95f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;
            case 12://40

                if (!User.language.equals("en")) {

                    height *= 3;
                    height *= 1;
                    heightStr = String.format("%.1f", height) + lengthSign_ko;

                } else if (User.language.equals("en")) {

                    height *= 3 / Constants.INCHES;
                    height *= 1;
                    heightStr = String.format("%.1f", height) + lengthSign_en;

                }

                angle *= 1;
                angleStr = String.format("%.1f", angle) + angleSign;

                Preset.userHeightSetting = 1f;

                tv_angle.setText(angleStr);
                tv_height.setText(heightStr);

                break;


        }


    }
}