package com.ko.smartneck.medical.twofive.Main;

import android.content.Context;
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

import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Commend;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.NoticeDialog;

import static com.ko.smartneck.medical.twofive.GlobalApplication.getApllication;
import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Main.BleConnectActivity.tabPos;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.audioStop;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.Main.WeightSettingFragment.currentWeight;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_HEIGHT;
import static com.ko.smartneck.medical.twofive.MeasureActivity.CFG_WEIGHT_MAX;
import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class ProcedureFragment extends Fragment {

    Context mContext;
    Handler handler;
    ViewGroup view;

    Button btn_save;

    ImageView btn_help_count, btn_help_set, btn_help_stop, btn_help_voice, btn_help_height, btn_help_weight, btn_help_break;
    ImageView btn_count_up, btn_count_down, btn_set_up, btn_set_down, btn_stop_up, btn_stop_down, btn_break_up, btn_break_down, btn_weight_up, btn_weight_down;

    TextView tv_count, tv_set, tv_stop, tv_angle, tv_height, tv_weight, tv_break;


    RadioButton sound_man, sound_woman, sound_child, strength_high, strength_mid, strength_low;


    Spinner spn_height_select;

    NoticeDialog noticeDialog;

    public static float angleLength = 1f;
    String angleSign = "°";
    String lengthSign_ko = "˚"; //수정6
//    String lengthSign_en = "inch"; 수정6
    String lengthSign_en = "˚";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_procedure, container, false);
        mContext = getApllication();
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
//        if (Constants.DEVICE_TYPE.equals("FIT")){ 수정7
//            angleLength = 1f;
//        }else if (Constants.DEVICE_TYPE.equals("MED")){
//            angleLength = 1f;
//        }
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
        MainActivity.setAudio("exercise_setting");
        Log.d(TAG, "----- ProcedureFragment onResume");
        tabPos = 2;

        String soundType = preset.getSoundType();
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


        if (strength_high.isChecked()) {
            preset.setStrength(3);
        } else if (strength_mid.isChecked()) {
            preset.setStrength(2);
        } else if (strength_low.isChecked()) {
            preset.setStrength(1);
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


        strength_high = view.findViewById(R.id.exercise_strength_3);
        strength_mid = view.findViewById(R.id.exercise_strength_2);
        strength_low = view.findViewById(R.id.exercise_strength_1);

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

        btn_count_up = view.findViewById(R.id.procedure_up_count);
        btn_set_up = view.findViewById(R.id.procedure_up_set);
        btn_stop_up = view.findViewById(R.id.procedure_up_stop);

        btn_count_down = view.findViewById(R.id.procedure_down_count);
        btn_set_down = view.findViewById(R.id.procedure_down_set);
        btn_stop_down = view.findViewById(R.id.procedure_down_stop);

        tv_count = view.findViewById(R.id.procedure_tv_count);
        tv_set = view.findViewById(R.id.procedure_tv_set);
        tv_stop = view.findViewById(R.id.procedure_tv_stop);

        tv_count.setText(String.valueOf(preset.getCount()));
        tv_set.setText(String.valueOf(preset.getSet()));
        tv_stop.setText(String.valueOf(preset.getStop()));


        tv_angle = view.findViewById(R.id.tv_exercise_setting_angle);
        tv_height = view.findViewById(R.id.tv_exercise_setting_height);
        spn_height_select = view.findViewById(R.id.spn_exercise_setting_height_select);

        tv_break = view.findViewById(R.id.procedure_tv_break);
        btn_help_break = view.findViewById(R.id.procedure_help_break);
        btn_break_up = view.findViewById(R.id.procedure_up_break);
        btn_break_down = view.findViewById(R.id.procedure_down_break);

        tv_weight = view.findViewById(R.id.procedure_tv_weight);
        btn_help_weight = view.findViewById(R.id.procedure_help_weight);
        btn_weight_up = view.findViewById(R.id.procedure_up_weight);
        btn_weight_down = view.findViewById(R.id.procedure_down_weight);


        tv_weight.setText(String.valueOf(((double) preset.getSetup()) * 0.1));
        tv_break.setText(String.valueOf(preset.getBreakTime()));

//        pastPreset.strength = 3;
//        strength_high.setChecked(true);
//        strength_low.setChecked(false);
//        strength_mid.setChecked(false);
        //fixme 중문 남녀 음성 추가 시 삭제

        if (Constants.language == "zh") {
            TextView cn_gone = view.findViewById(R.id.cn_gone1);
            cn_gone.setVisibility(View.GONE);
            btn_help_voice.setVisibility(View.GONE);
            sound_man.setVisibility(View.GONE);
            sound_woman.setVisibility(View.GONE);
        }

        int entrySelection = preset.getHeightSelected();

        spn_height_select.setSelection(entrySelection);
        onClcik();


    }

    void onClcik() {


        sound_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preset.setSoundType("male");
                sound_man.setChecked(true);
                sound_woman.setChecked(false);
                setChecked();

            }
        });
        sound_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preset.setSoundType("female");
                sound_woman.setChecked(true);
                sound_man.setChecked(false);
                setChecked();
            }
        });
        sound_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preset.setSoundType("child");
                setChecked();

            }
        });
        btn_count_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preset.getCount() < 15) {
                    preset.setCount(preset.getCount() + 1);
                    tv_count.setText(String.valueOf(preset.getCount()));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preset.getCount() > 5) {
                    preset.setCount(preset.getCount() - 1);
                    tv_count.setText(String.valueOf(preset.getCount()));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_set_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preset.getSet() < 4) {
                    preset.setSet(preset.getSet() + 1);
                    tv_set.setText(String.valueOf(preset.getSet()));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_set_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preset.getSet() > 1) {
                    preset.setSet(preset.getSet() - 1);
                    tv_set.setText(String.valueOf(preset.getSet()));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_stop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preset.getStop() < 15) {
                    preset.setStop(preset.getStop() + 1);
                    tv_stop.setText(String.valueOf(preset.getStop()));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_stop_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preset.getStop() > 0) {
                    preset.setStop(preset.getStop() - 1);
                    tv_stop.setText(String.valueOf(preset.getStop()));
                } else {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_break_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preset.getBreakTime() == 60) {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    preset.setBreakTime(preset.getBreakTime() + 5);
                    tv_break.setText(String.valueOf(preset.getBreakTime()));

                }
            }
        });

        btn_break_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preset.getBreakTime() == 15) {
                    Toast.makeText(mContext, getString(R.string.toast_min), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    preset.setBreakTime(preset.getBreakTime() - 5);
                    tv_break.setText(String.valueOf(preset.getBreakTime()));
                }
            }
        });

        btn_weight_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preset.getSetup() >= 65) {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    preset.setSetup(preset.getSetup() + 5);
                    tv_weight.setText(String.valueOf(((double) preset.getSetup()) * 0.1));
                    BleConnectActivity.setMessage(new Commend().sendWeightMove((byte) preset.getSetup()));
                }
            }
        });

        btn_weight_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preset.getSetup() == 0) {
                    Toast.makeText(mContext, getString(R.string.toast_max), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    preset.setSetup(preset.getSetup() - 5);
                    tv_weight.setText(String.valueOf(((double) preset.getSetup()) * 0.1));
                    BleConnectActivity.setMessage(new Commend().sendWeightMove((byte) preset.getSetup()));
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
        final String Message6 = "";
        // TODO: 2020-08-31 Break Time 설명으로 변경
        final String Message7 = "";

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

                userPreference.editPreset(preset);
                ((MainActivity) getActivity()).setBottomNavigation("EXERCISE");


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
        userPreference.editPreset(preset);

    }

    private void setHeightSelectEntry(int selected) {
        preset.setHeightSelected(selected);

        float height = preset.getMaxHeight();
//        float angle = height * 1.1666667f;
        float angle = height * 1f;
        String heightStr = "";
        String angleStr = "";

        switch (selected) {

            //(%)
            case 0://40

                if (Constants.language.equals("en")) {

                    height *= angleLength;
                    height *= 0.4;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6
                }else {

                height *= angleLength;
                height *= 0.4;
//                heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6
                }

                angle *= 0.4;
                angleStr = String.format("%.1f", angle) + angleSign;


                break;

            case 1://45

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; //수정11
                    height *= angleLength;
                    height *= 0.45;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                }else {

                height *= angleLength;
                height *= 0.45;
//                heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

            }

                angle *= 0.45;
                angleStr = String.format("%.1f", angle) + angleSign;




                break;
            case 2://50

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; //수정11
                    height *= angleLength;
                    height *= 0.5;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6
                }else {

                height *= angleLength;
                height *= 0.5;
//                heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6
            }

                angle *= 0.5;
                angleStr = String.format("%.1f", angle) + angleSign;


                break;
            case 3://55

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; //수정11
                    height *= angleLength;
                    height *= 0.55;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6
                } else {

                    height *= angleLength;
                    height *= 0.55;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.55;
                angleStr = String.format("%.1f", angle) + angleSign;


                break;
            case 4://60

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; //수정11
                    height *= angleLength;
                    height *= 0.6;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.6;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.6;
                angleStr = String.format("%.1f", angle) + angleSign;


                break;
            case 5://65

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; //수정11
                    height *= angleLength;
                    height *= 0.65;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.65;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수장6

                }

                angle *= 0.65;
                angleStr = String.format("%.1f", angle) + angleSign;


                break;
            case 6://70

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; //수정11
                    height *= angleLength;
                    height *= 0.7;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.7;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.7;
                angleStr = String.format("%.1f", angle) + angleSign;


                break;
            case 7://75

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; 수정11
                    height *= angleLength;
                    height *= 0.75;

//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.75;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.75;
                angleStr = String.format("%.1f", angle) + angleSign;

                break;
            case 8://80

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; 수정11
                    height *= angleLength;
                    height *= 0.8;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.8;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.8;
                angleStr = String.format("%.1f", angle) + angleSign;

                break;
            case 9://85

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; 수정11
                    height *= angleLength;
                    height *= 0.85;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.85;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.85;
                angleStr = String.format("%.1f", angle) + angleSign;

                break;
            case 10://90

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; 수정11
                    height *= angleLength;
                    height *= 0.9;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= 3;
                    height *= 0.9;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.9;
                angleStr = String.format("%.1f", angle) + angleSign;

                break;
            case 11://95

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; 수정11
                    height *= angleLength;
                    height *= 0.95;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 0.95;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 0.95;
                angleStr = String.format("%.1f", angle) + angleSign;

                break;
            case 12://40

                if (Constants.language.equals("en")) {

//                    height *= angleLength / Constants.INCHES; 수정11
                    height *= angleLength;
                    height *= 1;
//                    heightStr = String.format("%.1f", height) + lengthSign_en; 수정6

                } else {

                    height *= angleLength;
                    height *= 1;
//                    heightStr = String.format("%.1f", height) + lengthSign_ko; 수정6

                }

                angle *= 1;
                angleStr = String.format("%.1f", angle) + angleSign;

                break;

        }
        tv_angle.setText(angleStr);
        tv_height.setText(heightStr);


    }
}