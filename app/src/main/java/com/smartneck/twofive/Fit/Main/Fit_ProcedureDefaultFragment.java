package com.smartneck.twofive.Fit.Main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.smartneck.twofive.GlobalApplication;
import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Fit_NoticeDialog;
import com.smartneck.twofive.Fit.util.User.Fit_Preset;

import static com.smartneck.twofive.Fit.Main.Fit_ExerciseFragment.defaultCount;
import static com.smartneck.twofive.Fit.Main.Fit_ExerciseFragment.defaultHeight;
import static com.smartneck.twofive.Fit.Main.Fit_ExerciseFragment.defaultSet;
import static com.smartneck.twofive.Fit.Main.Fit_ExerciseFragment.defaultStop;
import static com.smartneck.twofive.Fit.Main.Fit_MainActivity.audioStop;
import static com.smartneck.twofive.Fit.util.Fit_Constants.TAG;

public class Fit_ProcedureDefaultFragment extends Fragment {

    Context mContext;
    Handler handler;
    ViewGroup view;

    Button btn_save;


    ImageView btn_help;


    RadioButton strength_high, strength_mid, strength_low;


    Fit_NoticeDialog noticeDialog;

    TextView tv_procedure_btn;

    static int strength = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fit_fragment_procedure_default, container, false);
        mContext = GlobalApplication.getApllication();
        ((Fit_MainActivity) getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);

        init();


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioStop();
        defaultHeight = 1f;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Fit_ExerciseFragment.isDefault = true;
        Fit_MainActivity.setAudio("exercise_setting");
        Log.d(TAG, "----- ProcedureFragment onResume");
        if (strength == 3) {
            strength_high.setChecked(true);
            strength_mid.setChecked(false);
            strength_low.setChecked(false);
        } else if (strength == 2) {
            strength_high.setChecked(false);
            strength_mid.setChecked(true);
            strength_low.setChecked(false);
        } else if (strength == 1) {
            strength_high.setChecked(false);
            strength_mid.setChecked(false);
            strength_low.setChecked(true);
        } else {
            strength = 3;
            strength_high.setChecked(true);
            strength_mid.setChecked(false);
            strength_low.setChecked(false);
        }

        if (strength_high.isChecked()) {
            Fit_Preset.strength = 3;
            defaultCount = 15;
            defaultSet = 2;
            defaultStop = 6;
            defaultHeight = 0.8f;

        } else if (strength_mid.isChecked()) {
            Fit_Preset.strength = 2;
            defaultCount = 12;
            defaultSet = 2;
            defaultStop = 4;
            defaultHeight = 0.7f;

        } else if (strength_low.isChecked()) {
            Fit_Preset.strength = 1;
            defaultCount = 10;
            defaultSet = 2;
            defaultStop = 2;
            defaultHeight = 0.6f;

        }


    }


    private void init() {

        handler = new Handler();


        if (Fit_Preset.count < 5) {
            Fit_Preset.count = 5;
        }

        btn_save = view.findViewById(R.id.btn_save);

        btn_help = view.findViewById(R.id.procedure_default_help);

        strength_high = view.findViewById(R.id.rb_exercise_default_setting_1);
        strength_mid = view.findViewById(R.id.rb_exercise_default_setting_2);
        strength_low = view.findViewById(R.id.rb_exercise_default_setting_3);

        tv_procedure_btn = view.findViewById(R.id.tv_procedure_default_setting_btn);
        //fixme 중문 남녀 음성 추가 시 삭제

//        SharedPreferences preferences = getContext().getSharedPreferences("heightEntry", Context.MODE_PRIVATE);
//        int entrySelection = preferences.getInt("entry", 8);

        onClcik();


    }

    private void onClcik() {

        strength_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strength = 3;
                strength_high.setChecked(true);
                strength_mid.setChecked(false);
                strength_low.setChecked(false);
                Fit_Preset.strength = 3;
                defaultCount = 15;
                defaultSet = 2;
                defaultStop = 6;
                defaultHeight = 0.8f;
            }
        });

        strength_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strength = 2;
                strength_high.setChecked(false);
                strength_mid.setChecked(true);
                strength_low.setChecked(false);
                Fit_Preset.strength = 2;
                defaultCount = 12;
                defaultSet = 2;
                defaultStop = 4;
                defaultHeight = 0.7f;
            }
        });

        strength_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strength = 1;
                strength_high.setChecked(false);
                strength_mid.setChecked(false);
                strength_low.setChecked(true);
                Fit_Preset.strength = 1;
                defaultCount = 10;
                defaultSet = 2;
                defaultStop = 2;
                defaultHeight = 0.6f;
            }
        });

        tv_procedure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Fit_MainActivity) getActivity()).setBottomNavigation("EXERCISE");

            }
        });

        final String title1 = getString(R.string.exericse_setting_weight);
//        final String message1 = getString(R.string.exercise_set);
        final String message1 = getString(R.string.exercise_default_notice);

//
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog = new Fit_NoticeDialog(getActivity(), getLayoutInflater(), title1, message1);
                noticeDialog.showAlertDialog();
            }
        });





        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fit_ExerciseFragment.isDefault = true;
                ((Fit_MainActivity) getActivity()).setBottomNavigation("PROCEDURE");

//                Preset.count


            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();



    }


}