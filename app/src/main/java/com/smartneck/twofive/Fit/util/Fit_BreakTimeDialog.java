package com.smartneck.twofive.Fit.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.User.Fit_User;

import static com.smartneck.twofive.Fit.util.User.Fit_Preset.isBreakTime;

public class Fit_BreakTimeDialog {
    Context context;
    LayoutInflater layoutInflater;
    Handler handler;
    MediaPlayer mediaPlayer;
    int breakTime;
    public Fit_BreakTimeDialog(Context context , LayoutInflater layoutInflater , MediaPlayer mediaPlayer , int breakTime) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        handler = new Handler();
        this.mediaPlayer = mediaPlayer;
        this.breakTime = breakTime;
    }

    public void showBreakTimeDialog() {
        isBreakTime = true;
        String title = "Break Time";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = layoutInflater;
        View view = inflater.inflate(R.layout.dialog_break_time, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView tv_timeCount = view.findViewById(R.id.tv_break_time_count);
        TextView tv_title = view.findViewById(R.id.tv_break_time_title);
        tv_timeCount.setText(String.valueOf(breakTime));
        tv_title.setText(title);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isBreakTime = false;
                mediaPlayer.stop();

            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;

            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();




        new Thread(new Runnable() {
            @Override
            public void run() {
                String message_ko = "초 후 운동을 시작합니다.";
                String message_zh = " 秒钟后开始锻炼。";
                String message_en_1 = "Start exercising after ";
                String message_en_2 = " second.";

                        for (int i = breakTime; 0 <= i; i--){


                            String message = "";
                            final SpannableStringBuilder sps;

                            if (Fit_User.language.equals("ko")){
                                message = i + message_ko;
                                sps = new SpannableStringBuilder(message);
                                sps.setSpan(new AbsoluteSizeSpan(200), 0, String.valueOf(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new AbsoluteSizeSpan(80), String.valueOf(i).length(), String.valueOf(i).length() + message_ko.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 0, String.valueOf(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new StyleSpan(Typeface.BOLD), 0, String.valueOf(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                            }else if (Fit_User.language.equals("en")){
                                message = message_en_1 + i + message_en_2;
                                sps = new SpannableStringBuilder(message);
                                sps.setSpan(new AbsoluteSizeSpan(200), message_en_1.length(), (i + message_en_1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new AbsoluteSizeSpan(80), 0, message_en_1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new AbsoluteSizeSpan(80), (i + message_en_1).length(), message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), message_en_1.length(), (i + message_en_1).length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new StyleSpan(Typeface.BOLD), message_en_1.length(), (i + message_en_1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                            }else if (Fit_User.language.equals("zh")){

                                message = i + message_zh;
                                sps = new SpannableStringBuilder(message);
                                sps.setSpan(new AbsoluteSizeSpan(200), 0, String.valueOf(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new AbsoluteSizeSpan(80), String.valueOf(i).length(), String.valueOf(i).length() + message_zh.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 0, String.valueOf(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sps.setSpan(new StyleSpan(Typeface.BOLD), 0, String.valueOf(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            }else{
                                message = message_en_1 + i + message_en_2;
                                sps = new SpannableStringBuilder(message);

                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    tv_timeCount.setText(sps);
                                }
                            });

                            if (i == 0){
                                dialog.dismiss();
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

            }
        }).start();

    }
}
