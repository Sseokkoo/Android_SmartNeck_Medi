package com.smartneck.fit.Fit.Alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.smartneck.fit.R;


public class AlarmActivity extends AppCompatActivity {

    Context context;
    Handler handler;
    //진동 정지용
    static boolean alarm = false;
    static boolean anim = true , anim2 = false;

    //좌측 하단 알람추가 액티비티 이동버튼
    FloatingActionButton faBtn;
    Animation moveUpNotice, fadeIn, moveUpFab;


    //알람리스트
    ListView alarmList;
    AlarmAdapter alarmAdapter;
    static ArrayList<AlarmItem> alarms = new ArrayList<>();
    //알람리스트


    //삭제
    static boolean delete = false;
    boolean allSelect = false;//알람 삭제시 전체선택
    TextView itemDeleteBtn;//삭제버튼
    TextView itemDeleteBtnTv;//삭제버튼
    TextView titleTv;//좌측상단 타이틀 -> 삭제버튼 클릭시 전체선택
    //삭제


    AlarmStorage alarmStorage = new AlarmStorage(context);

    LinearLayout noticeLayout;//알람이 없을시 안내 메시지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        init();
        itemEdit();
        deleteBtn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //알람데이터 로드
            alarmStorage.loadData();

        //리스트 정렬
        sortThread();
        //oneDay알람 갱신
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmList.setVisibility(View.VISIBLE);
        faBtn.startAnimation(moveUpFab);
        if (alarms.size() == 0) {

            noticeLayout.setVisibility(View.VISIBLE);
            noticeLayout.startAnimation(moveUpNotice);
            alarmList.setVisibility(View.GONE);
            itemDeleteBtn.setVisibility(View.GONE);
        } else {
            noticeLayout.setVisibility(View.GONE);
            alarmList.setVisibility(View.VISIBLE);
            itemDeleteBtn.setVisibility(View.GONE);
        }

        //액티비티로 돌아오면 알람이 꺼짐
        stopAlarm();
        //알람데이터 로드
        alarmStorage.loadData();
        //리스트 정렬
        sortThread();
        //oneDay알람 갱신
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {


                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        alarmAdapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        //액티비티 이동 및 앱 종료시 데이터 저장
        alarmStorage.saveData();

    }

    @Override
    public void onBackPressed() {
        if (delete) {
            delete = false;
            anim = true;
            animControll();
//            titleTv.setText(getString(R.string.alarm_title));
            titleTv.startAnimation(fadeIn);
            alarmAdapter.notifyDataSetChanged();
            itemDeleteBtn.setVisibility(View.VISIBLE);
            itemDeleteBtn.startAnimation(fadeIn);
            itemDeleteBtnTv.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        getWindow().setStatusBarColor(Color.parseColor("#cc1b17"));
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        context = getApplicationContext();

        alarmStorage = new AlarmStorage(context);
        handler = new Handler();
        moveUpNotice = AnimationUtils.loadAnimation(context, R.anim.move_up_fade_in_notice);
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        moveUpFab = AnimationUtils.loadAnimation(context, R.anim.move_up_fab);

        itemDeleteBtnTv = findViewById(R.id.alarm_delete_tv);
        itemDeleteBtnTv.setVisibility(View.VISIBLE);
        itemDeleteBtn = findViewById(R.id.alarm_delete_cancel);
        itemDeleteBtn.setVisibility(View.GONE);
        titleTv = findViewById(R.id.alarm_title);

        noticeLayout = findViewById(R.id.alarm_notice);

        faBtn = findViewById(R.id.alarm_fabtn);

        alarmList = findViewById(R.id.alarm_listview);
        alarmAdapter = new AlarmAdapter(getLayoutInflater(), alarms);
        alarmList.setAdapter(alarmAdapter);


        faBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmAddActivity.class);
                startActivity(intent);
            }
        });


    }

    private void deleteBtn() {
        itemDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete = false;
                anim = true;
                animControll();
//                titleTv.setText(getString(R.string.alarm_title));
                titleTv.startAnimation(fadeIn);
                alarmAdapter.notifyDataSetChanged();
                itemDeleteBtn.setVisibility(View.GONE);
                itemDeleteBtnTv.setVisibility(View.VISIBLE);
//                itemDeleteBtnTv.setVisibility(View.GONE);

            }
        });
        itemDeleteBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!delete) {
                    delete = true;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                                anim2  = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    alarmAdapter.notifyDataSetChanged();
//                    titleTv.setText(getString(R.string.alarm_select_all));
                    titleTv.startAnimation(fadeIn);
                    itemDeleteBtn.setVisibility(View.VISIBLE);
                    itemDeleteBtnTv.setVisibility(View.VISIBLE);
                    itemDeleteBtnTv.startAnimation(fadeIn);
                    return;
                }
                int checkCount = 0;

                for (int i = 0; i < alarms.size(); i++)
                {
                    if (alarms.get(i).isDelete()){
                        checkCount++;
                    }
                }
                if (checkCount > 0) {

                    showDeleteDialog();
                } else {
//                    Toast.makeText(context, getString(R.string.alarm_delete_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete) {
                    if (!allSelect) {
                        for (int i = 0; i < alarms.size(); i++) {
                            allSelect = true;
                            alarms.get(i).setDelete(true);
                        }
                    } else {
                        for (int i = 0; i < alarms.size(); i++) {
                            allSelect = false;
                            alarms.get(i).setDelete(false);
                        }
                    }
                    alarmAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void stopAlarm() {
        //진동 종료
        if (alarm){
            AlarmPlay alarmPlay = new AlarmPlay(getApplicationContext());
            alarmPlay.stop();
        }
    }

    private void itemEdit() {
        alarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!delete) {
                    Intent intent = new Intent(context, AlarmAddActivity.class);
                    intent.putExtra("hour", alarms.get(position).getHour());
                    intent.putExtra("minute", alarms.get(position).getMinute());
                    intent.putExtra("sun", alarms.get(position).isSun());
                    intent.putExtra("mon", alarms.get(position).isMon());
                    intent.putExtra("tue", alarms.get(position).isTue());
                    intent.putExtra("wed", alarms.get(position).isWed());
                    intent.putExtra("thu", alarms.get(position).isThu());
                    intent.putExtra("fri", alarms.get(position).isFri());
                    intent.putExtra("sat", alarms.get(position).isSat());
                    intent.putExtra("index", position);
                    startActivity(intent);
                } else {
                    if (alarms.get(position).isDelete()) {
                        alarms.get(position).setDelete(false);
                    } else {
                        alarms.get(position).setDelete(true);
                    }
                    alarmAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(getString(R.string.dialog_alarm_message));
//        builder.setPositiveButton(getString(R.string.alarm_delete),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        for (int i = alarms.size() - 1; i >= 0; i--) {
//                            if (alarms.get(i).isDelete()) {
//                                alarms.remove(i);
//                            }
//                        }
//                        titleTv.setText(getString(R.string.alarm_title));
//                        delete = false;
//                        anim = true;
//                        animControll();
//                        sortThread();
//                        alarmAdapter.notifyDataSetChanged();
//                        //삭제한 상태의 리스트 저장
//
//                        alarmStorage.saveData();
//                        if (alarms.size() == 0) {
//
//                            noticeLayout.setVisibility(View.VISIBLE);
//                            noticeLayout.startAnimation(moveUpNotice);
//                            alarmList.setVisibility(View.GONE);
//                            itemDeleteBtn.setVisibility(View.VISIBLE);
//                            itemDeleteBtn.startAnimation(fadeIn);
//                            itemDeleteBtnTv.setVisibility(View.GONE);
//                        }
//                        Toast.makeText(getApplicationContext(), getString(R.string.alarm_delete_complete), Toast.LENGTH_LONG).show();
//                    }
//                });
//        builder.setNegativeButton(getString(R.string.alarm_alarm_cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//        builder.show();
    }

    private void sortThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(alarms, new Comparator<AlarmItem>() {
                            @Override
                            public int compare(AlarmItem o1, AlarmItem o2) {
                                if (o1.getMinute() > o2.getMinute()) {
                                    return 1;
                                } else if (o1.getMinute() < o2.getMinute()) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        Collections.sort(alarms, new Comparator<AlarmItem>() {
                            @Override
                            public int compare(AlarmItem o1, AlarmItem o2) {
                                if (o1.getHour() > o2.getHour()) {
                                    return 1;
                                } else if (o1.getHour() < o2.getHour()) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        alarmAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    void animControll(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                anim2 = false;
            }
        }).start();
    }
}






