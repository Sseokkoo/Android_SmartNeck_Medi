package com.smartneck.fit.tmp.Alarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.smartneck.fit.R;

import static com.smartneck.fit.tmp.Alarm.AlarmActivity.alarms;


public class AlarmAddActivity extends AppCompatActivity {

    String TAG = "AlarmAddActivity";
    Calendar calendar;
    AlarmManager alarmManager;
    TimePicker timePicker;
    Context context;

    //저장 취소버튼
    Button saveBtn, cancelBtn;
    //요일별 버튼
    TextView mon, tue, wed, thu, fri, sat, sun;
    //요일별 선택 여부 확인
    boolean selectMon, selectTue, selectWed, selectThu, selectFri, selectSat, selectSun;
    //날짜선택 여부 확인
    boolean datePick = false;
    //시간
    int hour;
    //분
    int minute;
    //해당 알람 인덱스
    int index;
    //년
    int year;
    //월
    int month;
    //일
    int date;
    //텍스트 출력용
    TextView dateTv,
    datePickBtn;
    //알람 타입
    int type;//0 = oneday , 1 = weekday , 2 = date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        init();
        add();
        cancel();
        selectDays();
        datePick();
    }

    private void init() {
        getWindow().setStatusBarColor(Color.parseColor("#cc1b17"));
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;
        calendar = Calendar.getInstance();

        dateTv = findViewById(R.id.alarm_add_date_tv);
        saveBtn = findViewById(R.id.alarm_add_saveBtn);
        cancelBtn = findViewById(R.id.alarm_add_cancelBtn);
        timePicker = findViewById(R.id.alarm_add_timePicker);
        datePickBtn = findViewById(R.id.alarm_add_date_pick);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        mon = findViewById(R.id.alarm_add_mon);
        tue = findViewById(R.id.alarm_add_tue);
        wed = findViewById(R.id.alarm_add_wed);
        thu = findViewById(R.id.alarm_add_thu);
        fri = findViewById(R.id.alarm_add_fri);
        sat = findViewById(R.id.alarm_add_sat);
        sun = findViewById(R.id.alarm_add_sun);

        //내일 날짜 알림 메시지 세팅
        String datestr = null;
        int date = calendar.get(Calendar.DAY_OF_WEEK);
        if (date == 1) {
            datestr = "(일)";
        } else if (date == 2) {
            datestr = "(월)";
        } else if (date == 3) {
            datestr = "(화)";
        } else if (date == 4) {
            datestr = "(수)";
        } else if (date == 5) {
            datestr = "(목)";
        } else if (date == 6) {
            datestr = "(금)";
        } else if (date == 7) {
            datestr = "(토)";
        }
        dateTv.setText("내일 - " + (calendar.get(Calendar.MONTH) + 1) + "월" + (calendar.get(Calendar.DATE) +1)+ "일" + datestr);
        //내일 날짜 알림 메시지 세팅

        Intent intent = getIntent();
        //어레이리스트 인덱스 값
        index = intent.getIntExtra("index", -1);

        //알람 데이터를 받은경우 각 뷰에 기존 값 세팅 (수정 시 사용)
        if (intent.getIntExtra("hour", -1) != -1) {
            hour = intent.getIntExtra("hour", 0);
            minute = intent.getIntExtra("minute", 0);
            selectSun = intent.getBooleanExtra("sun", false);
            selectMon = intent.getBooleanExtra("mon", false);
            selectTue = intent.getBooleanExtra("tue", false);
            selectWed = intent.getBooleanExtra("wed", false);
            selectThu = intent.getBooleanExtra("thu", false);
            selectFri = intent.getBooleanExtra("fri", false);
            selectSat = intent.getBooleanExtra("sat", false);
            if (selectSun) sun.setTextColor(Color.parseColor("#cc1b17"));
            if (selectMon) mon.setTextColor(Color.parseColor("#cc1b17"));
            if (selectTue) tue.setTextColor(Color.parseColor("#cc1b17"));
            if (selectWed) wed.setTextColor(Color.parseColor("#cc1b17"));
            if (selectThu) thu.setTextColor(Color.parseColor("#cc1b17"));
            if (selectFri) fri.setTextColor(Color.parseColor("#cc1b17"));
            if (selectSat) sat.setTextColor(Color.parseColor("#cc1b17"));
            //타임피커에 세팅
            timePicker.setHour(hour);
            timePicker.setMinute(minute);

        }
    }

    /*
    선택한 날짜의 색상 변경 선택 항목 빨간색 , 비 선택 항목 검은색
     */
    private void selectDays() {
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectMon) {
                    mon.setTextColor(Color.parseColor("#cc1b17"));
                    selectMon = true;
                } else {
                    mon.setTextColor(Color.parseColor("#747474"));
                    selectMon = false;
                }
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectTue) {
                    tue.setTextColor(Color.parseColor("#cc1b17"));
                    selectTue = true;
                } else {
                    tue.setTextColor(Color.parseColor("#747474"));
                    selectTue = false;
                }
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectWed) {
                    wed.setTextColor(Color.parseColor("#cc1b17"));
                    selectWed = true;
                } else {
                    wed.setTextColor(Color.parseColor("#747474"));
                    selectWed = false;
                }
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectThu) {
                    thu.setTextColor(Color.parseColor("#cc1b17"));
                    selectThu = true;
                } else {
                    thu.setTextColor(Color.parseColor("#747474"));
                    selectThu = false;
                }
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectFri) {
                    fri.setTextColor(Color.parseColor("#cc1b17"));
                    selectFri = true;
                } else {
                    fri.setTextColor(Color.parseColor("#747474"));
                    selectFri = false;
                }
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectSat) {
                    sat.setTextColor(Color.parseColor("#cc1b17"));
                    selectSat = true;
                } else {
                    sat.setTextColor(Color.parseColor("#747474"));
                    selectSat = false;
                }
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick = false;
                if (!selectSun) {
                    sun.setTextColor(Color.parseColor("#cc1b17"));
                    selectSun = true;
                } else {
                    sun.setTextColor(Color.parseColor("#747474"));
                    selectSun = false;
                }
            }
        });
    }
    /*
    저장버튼 클릭시 데이터를 쉐어드 프리퍼런스에 저장 후 액티비티 종료
     */
    private void add() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hour = timePicker.getHour();
                minute = timePicker.getMinute();

                //날짜, 요일 미선택 type = 0
                if (!selectSun && !selectMon && !selectTue &&
                        !selectWed && !selectThu && !selectFri &&
                        !selectSat && !datePick) {
                    type = 0;
                    if (index > -1) {
                        alarms.set(index, new AlarmItem(type, hour, minute));
                    } else {
                        alarms.add(new AlarmItem(type, hour, minute));
                        index = alarms.size() - 1;
                    }
                }
                //요일 선택 type = 1
                else if (selectSun || selectMon || selectTue ||
                        selectWed || selectThu || selectFri ||
                        selectSat) {

                    type = 1;
                    if (index > -1) {
                        alarms.set(index, new AlarmItem(type,
                                selectMon, selectTue, selectWed, selectThu,
                                selectFri, selectSat, selectSun,
                                hour, minute));

                    } else {
                        alarms.add(new AlarmItem(type,
                                selectMon, selectTue, selectWed, selectThu,
                                selectFri, selectSat, selectSun,
                                hour, minute));
                        index = alarms.size() - 1;
                    }
                }
                //날짜 선택 type = 2
                else if (datePick) {
                    type = 2;
                    if (index > -1) {
                        alarms.set(index, new AlarmItem(type, year, month, date, hour, minute));
                    } else {
                        alarms.add(new AlarmItem(type, year, month, date, hour, minute));
                        index = alarms.size() - 1;
                    }
                }


                calendar = Calendar.getInstance();

                //날짜 선택시 사용
                if (type == 2) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DATE, date);
                }

                //모든타입 공통 시간세팅
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                //시간이 지난 알람은 다음주에 울리도록 설정
                long aTime = System.currentTimeMillis();
                long bTime = calendar.getTimeInMillis();
                long triggerTime = bTime;
                if (aTime > bTime)
                    triggerTime += 1000 * 60 * 60 * 24;


                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("index", index);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

                Toast.makeText(AlarmAddActivity.this, getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
                AlarmStorage alarmStorage = new AlarmStorage(context);

                alarmStorage.saveData();

                finish();


            }
        });
    }

    /*
    취소버튼 클릭 시 액티비티 종료
     */
    private void cancel() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    /*
    알람 날짜 선택

    데이트 피커 다이얼로그에서 날짜 선택 후 확인을 누르면 액티비티의 텍스트뷰에 선택한 날짜가 세팅됨
     */
    private void datePick() {

        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int years, int months, int dates) {
                year = years;
                date = dates;
                month = months;
                datePick = true;
                dateTv.setText(String.format("%d월 %d일", month + 1, date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));


        datePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


    }
}
