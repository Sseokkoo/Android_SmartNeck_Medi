package com.smartneck.twofive.Fit.Alarm;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import com.smartneck.twofive.R;

import static com.smartneck.twofive.Fit.Alarm.AlarmActivity.anim2;
import static com.smartneck.twofive.Fit.Alarm.AlarmActivity.delete;


public class AlarmAdapter extends BaseAdapter {

    class Holder {
        TextView mon, tue, wed, thu, fri, sat, sun;//요일
        TextView daytime;//오전/오후
        TextView time;//시간 00:00
        Switch useSw;//알람 on/off
        RadioButton deleteCheckBtn;//삭제체크
        LinearLayout linearLayout;//애니메이션 용
        LinearLayout weekLinear;//월~일 텍스트뷰가 들어있는 레이아웃
        TextView dateTv;//날짜
    }

    //뷰 애니메이션
    Animation moveLeft_linear, moveRight_linear, fadeIn_btn, fadeOut_btn;


    Context context;
    Holder holder;
    LayoutInflater inflater;
    ArrayList<AlarmItem> alarms;

    public AlarmAdapter(LayoutInflater inflater, ArrayList<AlarmItem> alarms) {
        this.inflater = inflater;
        this.alarms = alarms;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int i) {
        return alarms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_alarm, null);
            holder = new Holder();
            context = viewGroup.getContext();

            fadeIn_btn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            fadeOut_btn = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            moveLeft_linear = AnimationUtils.loadAnimation(context, R.anim.move_left_linear);
            moveRight_linear = AnimationUtils.loadAnimation(context, R.anim.move_right_linear);
            moveLeft_linear.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    AlarmActivity.anim = true;

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            moveRight_linear.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            AlarmActivity.anim = false;

                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        holder.time = view.findViewById(R.id.alarm_item_time);
        holder.dateTv = view.findViewById(R.id.alarm_item_date);
        holder.useSw = view.findViewById(R.id.alarm_item_switch);
        holder.daytime = view.findViewById(R.id.alarm_item_daytime);
        holder.weekLinear = view.findViewById(R.id.alarm_item_linear_week);
        holder.linearLayout = view.findViewById(R.id.alarm_item_linear);
        holder.deleteCheckBtn = view.findViewById(R.id.alarm_item_delete);
        holder.mon = view.findViewById(R.id.alarm_item_mon);
        holder.tue = view.findViewById(R.id.alarm_item_tue);
        holder.wed = view.findViewById(R.id.alarm_item_wed);
        holder.thu = view.findViewById(R.id.alarm_item_thu);
        holder.fri = view.findViewById(R.id.alarm_item_fri);
        holder.sat = view.findViewById(R.id.alarm_item_sat);
        holder.sun = view.findViewById(R.id.alarm_item_sun);

//        holder.mon.setText(context.getString(R.string.alarm_mon2));
//        holder.tue.setText(context.getString(R.string.alarm_tue2));
//        holder.wed.setText(context.getString(R.string.alarm_wen2));
//        holder.thu.setText(context.getString(R.string.alarm_thu2));
//        holder.fri.setText(context.getString(R.string.alarm_fri2));
//        holder.sat.setText(context.getString(R.string.alarm_sat2));
//        holder.sun.setText(context.getString(R.string.alarm_sun2));


        //타입별 텍스트뷰 세팅 하루/요일/날짜
        if (alarms.get(i).getType() == 0) {
            holder.dateTv.setVisibility(View.INVISIBLE);
            holder.weekLinear.setVisibility(View.VISIBLE);
        } else if (alarms.get(i).getType() == 1) {
            holder.dateTv.setVisibility(View.INVISIBLE);
            holder.weekLinear.setVisibility(View.VISIBLE);
        } else if (alarms.get(i).getType() == 2) {
            String weekDay = null;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, alarms.get(i).getYear());
            calendar.add(Calendar.MONTH, alarms.get(i).getMonth());
            calendar.add(Calendar.DATE, alarms.get(i).getDate());
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekday == 1) {
                weekDay = "(일)";
            } else if (weekday == 2) {
                weekDay = "(월)";
            } else if (weekday == 3) {
                weekDay = "(화)";
            } else if (weekday == 4) {
                weekDay = "(수)";
            } else if (weekday == 5) {
                weekDay = "(목)";
            } else if (weekday == 6) {
                weekDay = "(금)";
            } else if (weekday == 7) {
                weekDay = "(토)";
            }
            holder.dateTv.setText((alarms.get(i).getMonth() + 1) + "월" + alarms.get(i).getDate() + "일" + weekDay);
            holder.dateTv.setVisibility(View.VISIBLE);
            holder.weekLinear.setVisibility(View.INVISIBLE);
        }


        //선택요일 색상변경
        if (alarms.get(i).isMon()) {
            holder.mon.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.mon.setTextColor(Color.parseColor("#8C8C8C"));
        }

        if (alarms.get(i).isTue()) {
            holder.tue.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.tue.setTextColor(Color.parseColor("#8C8C8C"));
        }

        if (alarms.get(i).isWed()) {
            holder.wed.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.wed.setTextColor(Color.parseColor("#8C8C8C"));
        }


        if (alarms.get(i).isThu()) {
            holder.thu.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.thu.setTextColor(Color.parseColor("#8C8C8C"));
        }

        if (alarms.get(i).isFri()) {
            holder.fri.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.fri.setTextColor(Color.parseColor("#8C8C8C"));
        }

        if (alarms.get(i).isSat()) {
            holder.sat.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.sat.setTextColor(Color.parseColor("#8C8C8C"));
        }

        if (alarms.get(i).isSun()) {
            holder.sun.setTextColor(Color.parseColor("#cc1b17"));
        } else {
            holder.sun.setTextColor(Color.parseColor("#8C8C8C"));
        }

        //스위치 클릭시 알람 on/off
        if (alarms.get(i).isUse()) {
            holder.useSw.setChecked(true);
        } else {
            holder.useSw.setChecked(false);
        }
        holder.useSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarms.get(i).isUse()) {

                    alarms.get(i).setUse(false);

                } else {

                    alarms.get(i).setUse(true);

                }
            }
        });


        /**알람시간 자릿수 맞춤**/
        String hourStr = null;
        String minute = null;
        int hour = alarms.get(i).getHour();
        if (hour > 12) {
            hour -= 12;
        }
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = String.valueOf(hour);
        }

        if (alarms.get(i).getMinute() < 10) {
            minute = "0" + alarms.get(i).getMinute();
        } else {
            minute = String.valueOf(alarms.get(i).getMinute());
        }
        /**알람시간 자릿수 맞춤**/


        //텍스트뷰에 시간 설정
        if (alarms.get(i).getDayTime().equals("오전")){
            // TODO: 2019-08-01 오전 오후 다국어 세팅
//            holder.daytime.setText(context.getString(R.string.alarm_am));

        }else{
//            holder.daytime.setText(context.getString(R.string.alarm_pm));

        }

        holder.time.setText(hourStr + ":" + minute);

        //애니메이션, visible

            if (!delete) {
                view.startAnimation(moveLeft_linear);
                holder.deleteCheckBtn.setVisibility(View.INVISIBLE);
                holder.useSw.setVisibility(View.VISIBLE);
                holder.useSw.startAnimation(fadeIn_btn);
                if (anim2){
                    holder.deleteCheckBtn.startAnimation(fadeOut_btn);
                }


            } else {

                if (AlarmActivity.anim) {
                    holder.deleteCheckBtn.setVisibility(View.VISIBLE);
                    holder.useSw.setVisibility(View.INVISIBLE);
                    view.startAnimation(moveRight_linear);

                    if (!anim2){
                        holder.deleteCheckBtn.startAnimation(fadeIn_btn);
                        holder.useSw.startAnimation(fadeOut_btn);
                    }




                } else if (AlarmActivity.anim){

                }
            }




        //삭제
        holder.deleteCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarms.get(i).isDelete()) {
                    alarms.get(i).setDelete(false);
                } else {
                    alarms.get(i).setDelete(true);
                }
                notifyDataSetChanged();
            }
        });
        if (alarms.get(i).isDelete()) {
            holder.deleteCheckBtn.setChecked(true);
        } else {
            holder.deleteCheckBtn.setChecked(false);
        }

        return view;
    }


}
