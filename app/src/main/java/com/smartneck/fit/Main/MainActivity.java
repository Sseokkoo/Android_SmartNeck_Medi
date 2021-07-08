package com.smartneck.fit.Main;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smartneck.fit.AppVersionActivity;
import com.smartneck.fit.Chart.ChartActivity;
import com.smartneck.fit.CustomerSupport.Controller.CustomerSupportListActivity;
import com.smartneck.fit.ErrorReport.ErrorReportActivity;
import com.smartneck.fit.GlobalApplication;
import com.smartneck.fit.Management.MeasureManagementActivity;
import com.smartneck.fit.Notice.NoticeListActivity;
import com.smartneck.fit.R;
import com.smartneck.fit.util.BottomNavigationHelper;
import com.smartneck.fit.util.Commend;
import com.smartneck.fit.util.Constants;
import com.smartneck.fit.util.ProgressDialog;
import com.smartneck.fit.util.User.Member;
import com.smartneck.fit.util.User.Preset;

import static com.smartneck.fit.Main.BleConnectActivity.isSeatMove;
import static com.smartneck.fit.Main.BleConnectActivity.isWeightMove;
import static com.smartneck.fit.Main.BleConnectActivity.mConnected;
import static com.smartneck.fit.Main.BleConnectActivity.mEchoInitialized;
import static com.smartneck.fit.Main.BleConnectActivity.setMessage;
import static com.smartneck.fit.MeasureActivity.isMeasure;
import static com.smartneck.fit.MeasureActivity.moveExercise;
import static com.smartneck.fit.MeasureActivity.moveProcedure;
import static com.smartneck.fit.util.Constants.CFG_IS_EXERCISE;
import static com.smartneck.fit.util.Constants.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Context mContext;
    public static Activity activity;
    public static boolean isLogout;
    static int fragIndex;
    Handler handler;
    ImageView test;
    NavigationView nav_view;
    BottomNavigationView bottomNavigationView;

    public static Preset preset;
    public static Member member;

    HomeFragment fragment_home;
    ExerciseFragment fragment_exercise;
    ProcedureFragment fragment_procedure;
    final int rssiDistance = -90;
    boolean isOpen;
    static boolean isMain;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static boolean moveExerciseSetting = false;




    static String currentLocation = "";
    public static boolean SettingOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        init();
        fragIndex = 0;
        Log.d(TAG, " ");
        Log.d(TAG, " ");
        Log.d(TAG, " ");
        Log.d(TAG, "■■■■■■■■■■■■■■■■■■■■ START ■■■■■■■■■■■■■■■■■■■■");
        Log.d(TAG, " ");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        mContext = this;
        activity = this;
        isLogout = false;

        test = findViewById(R.id.toolbar_home);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomNavigation("HOME");
            }
        });
        test.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 어사이드
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                isOpen = false;
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                isOpen = true;

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        View navigation_view = nav_view.getHeaderView(0);

        // 하단

        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // mConnected = true;

                switch (item.getItemId()) {

                    case R.id.navigation_exercise:
                        setBottomNavigation("EXERCISE");
                        break;

                    case R.id.navigation_procedure:
                        setBottomNavigation("PROCEDURE");

                        break;

                }
                return true;
            }
        });


        int IS_ACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int IS_ACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (IS_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED && IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            setInit();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "----- ChartActivity onResume");
        BleConnectActivity.tabPos = 4;
        isMeasure = false;

//        userPreference.addExericse(member , preset , 5 , 3);


        //측정 후 프래그먼트 이동
        if (moveExercise) {
            moveExercise = false;
            setBottomNavigation("EXERCISE");
        } else if (moveProcedure) {
            moveProcedure = false;
            setBottomNavigation("PROCEDURE");
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "----- ChartActivity onDestroy");
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }



    }

    @Override
    public void onBackPressed() {
        if (currentLocation.equals("home")){
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_device_info) {
            Intent intent = new Intent(getApplicationContext(), AppVersionActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_statistics) {

            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_notice) {
            Intent intent = new Intent(getApplicationContext(), NoticeListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_inquery) {
            //고객지원
            Intent intent = new Intent(getApplicationContext(), CustomerSupportListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_error) {
            Intent intent = new Intent(getApplicationContext(), ErrorReportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics_management){
            Intent intent = new Intent(getApplicationContext() , MeasureManagementActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_exercise_management){
//
//        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    public void setInit() {
        Log.d(TAG, "----- setInit");
        setDesktopIcon();

        // Home
        fragment_home = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_home, "FRAG_HOME").commit();
        setDeviceInit();


    }

    private void init(){
        Intent intent = getIntent();
        preset = (Preset) intent.getSerializableExtra("preset");
        member = (Member) intent.getSerializableExtra("member");

        Log.e(TAG, "MainActivity init: \nmember -> " + member.toString() + "\npreset -> " + preset.toString() );
    }

    public void setDeviceInit() {
        if (mConnected || mEchoInitialized) {

            final ProgressDialog progressDialog = new ProgressDialog(mContext, getLayoutInflater());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show(getString(R.string.dialog_ble_init));
                        }
                    });

                    if (member.toString().equals("")) {
                        ((BleConnectActivity) BleConnectActivity.mContext).setScanStop();
                    }
                }
            }).start();

            //todo GO_EXERCISE 테스트 필요
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setMessage(new Commend().sendGoExercise((byte) preset.getSeat(), (byte) preset.getSetup()));
                    try {
                        while (true) {
                            Thread.sleep(300);
                            Log.e("확인", isWeightMove + "" + isSeatMove);
                            if (isWeightMove == false && isSeatMove == false) {
                                SettingOn = false;
                                Log.e("확인1", isWeightMove + "" + isSeatMove);
                                progressDialog.dismiss();
                                break;
                            } else {
                                Log.e("확인2", isWeightMove + "" + isSeatMove);
                                SettingOn = true;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }


    public void setDesktopIcon() {
        Log.d(TAG, "----- setDesktopIcon");

        SharedPreferences pref = getSharedPreferences("NECKTHPOWER", MODE_PRIVATE);

        if (pref.getString("SHORTCUT", "").isEmpty()) {
            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);

            shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            shortcutIntent.setClassName(this, getClass().getName());
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Smart Neck\nMedical");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
            intent.putExtra("duplicate", false);
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            sendBroadcast(intent);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("SHORTCUT", "1");
            editor.commit();
        }
    }

    public void setBottomNavigation(String menu) {
        Log.d(TAG, "----- setBottomNavigation: " + menu);

        switch (menu) {
            case "HOME":

                test.setVisibility(View.GONE);
                fragment_home = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_home, "home").commitAllowingStateLoss();

                break;
            case "EXERCISE":
                bottomNavigationView.getMenu().getItem(1).setChecked(true);

                fragment_exercise = new ExerciseFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_exercise, "exercise").commit();
                test.setVisibility(View.VISIBLE);

                break;

            case "PROCEDURE":

                fragment_procedure = new ProcedureFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_procedure, "procedure").commit();
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                test.setVisibility(View.VISIBLE);

                break;

        }
    }

    static MediaPlayer mp;

    public static void setAudio(String Code) {
        // TODO: 2019-05-10 audioStop() 메소드 추가
        String language = Constants.language;
        Log.d(TAG, "----- setAudio: " + Code);
        Context context = GlobalApplication.getAppContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences("sound", MODE_PRIVATE);
        String gender = sharedPreferences.getString("sound", "female");

        SoundPool sound;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            sound = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        } else {
            sound = new SoundPool(1,         // 최대 음악파일의 개수
                    AudioManager.STREAM_MUSIC, // 스트림 타입
                    0);        // 음질 - 기본값:0

        }


        if (mp != null && mp.isPlaying()) {
            mp.stop();
//            mp.release();
        }
        final int count[] = new int[16];

        switch (Code) {
            case "count_start":
                // 카운트
                Log.d(TAG, "setAudio: countstart");
                final int countStart = sound.load(context, R.raw.count, 0);
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(countStart, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;

            case "count_complete":
                // 카운트
                Log.d(TAG, "setAudio: count");
                final int soundCount = sound.load(context, R.raw.count_complete, 0);
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(soundCount, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;

            case "count_progress":
                // 카운트
                Log.d(TAG, "setAudio: count");
                final int countProgress = sound.load(context, R.raw.count_progress, 0);
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(countProgress, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;

            case "f1":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[1] = sound.load(context, R.raw.female_kr_1, 0);
                } else if (language.equals("en")) {
                    count[1] = sound.load(context, R.raw.female_us_1, 0);
                } else if (language.equals("zh")) {

                    count[1] = sound.load(context, R.raw.tmp_cn_1, 0);
                } else {
                    count[1] = sound.load(context, R.raw.female_us_1, 0);

                }
                final int count1f = count[1];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count1f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f2":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[2] = sound.load(context, R.raw.female_kr_2, 0);
                } else if (language.equals("en")) {
                    count[2] = sound.load(context, R.raw.female_us_2, 0);
                } else if (language.equals("zh")) {

                    count[2] = sound.load(context, R.raw.tmp_cn_2, 0);
                } else {
                    count[2] = sound.load(context, R.raw.female_us_2, 0);

                }
                final int count2f = count[2];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count2f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f3":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[3] = sound.load(context, R.raw.female_kr_3, 0);
                } else if (language.equals("en")) {
                    count[3] = sound.load(context, R.raw.female_us_3, 0);
                } else if (language.equals("zh")) {

                    count[3] = sound.load(context, R.raw.tmp_cn_3, 0);
                } else {
                    count[3] = sound.load(context, R.raw.female_us_3, 0);

                }
                final int count3f = count[3];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count3f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f4":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[4] = sound.load(context, R.raw.female_kr_4, 0);
                } else if (language.equals("en")) {
                    count[4] = sound.load(context, R.raw.female_us_4, 0);
                } else if (language.equals("zh")) {

                    count[4] = sound.load(context, R.raw.tmp_cn_4, 0);
                } else {
                    count[4] = sound.load(context, R.raw.female_us_4, 0);

                }
                final int count4f = count[4];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count4f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f5":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[5] = sound.load(context, R.raw.female_kr_5, 0);
                } else if (language.equals("en")) {
                    count[5] = sound.load(context, R.raw.female_us_5, 0);
                } else if (language.equals("zh")) {

                    count[5] = sound.load(context, R.raw.tmp_cn_5, 0);
                } else {
                    count[5] = sound.load(context, R.raw.female_us_5, 0);

                }
                final int count5f = count[5];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count5f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f6":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[6] = sound.load(context, R.raw.female_kr_6, 0);
                } else if (language.equals("en")) {
                    count[6] = sound.load(context, R.raw.female_us_6, 0);
                } else if (language.equals("zh")) {

                    count[6] = sound.load(context, R.raw.tmp_cn_6, 0);
                } else {
                    count[6] = sound.load(context, R.raw.female_us_6, 0);

                }
                final int count6f = count[6];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count6f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f7":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[7] = sound.load(context, R.raw.female_kr_7, 0);
                } else if (language.equals("en")) {
                    count[7] = sound.load(context, R.raw.female_us_7, 0);
                } else if (language.equals("zh")) {

                    count[7] = sound.load(context, R.raw.tmp_cn_7, 0);
                } else {
                    count[7] = sound.load(context, R.raw.female_us_7, 0);

                }
                final int count7f = count[7];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count7f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f8":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[8] = sound.load(context, R.raw.female_kr_8, 0);
                } else if (language.equals("en")) {
                    count[8] = sound.load(context, R.raw.female_us_8, 0);
                } else if (language.equals("zh")) {

                    count[8] = sound.load(context, R.raw.tmp_cn_8, 0);
                } else {
                    count[8] = sound.load(context, R.raw.female_us_8, 0);

                }
                final int count8f = count[8];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count8f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f9":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[9] = sound.load(context, R.raw.female_kr_9, 0);
                } else if (language.equals("en")) {
                    count[9] = sound.load(context, R.raw.female_us_9, 0);
                } else if (language.equals("zh")) {

                    count[9] = sound.load(context, R.raw.tmp_cn_9, 0);
                } else {
                    count[9] = sound.load(context, R.raw.female_us_9, 0);

                }
                final int count9f = count[9];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count9f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f10":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[10] = sound.load(context, R.raw.female_kr_10, 0);
                } else if (language.equals("en")) {
                    count[10] = sound.load(context, R.raw.female_us_10, 0);
                } else if (language.equals("zh")) {

                    count[10] = sound.load(context, R.raw.tmp_cn_10, 0);
                } else {
                    count[10] = sound.load(context, R.raw.female_us_10, 0);
                }
                final int count10f = count[10];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count10f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f11":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[11] = sound.load(context, R.raw.female_kr_11, 0);
                } else if (language.equals("en")) {
                    count[11] = sound.load(context, R.raw.female_us_11, 0);
                } else if (language.equals("zh")) {

                    count[11] = sound.load(context, R.raw.tmp_cn_11, 0);
                } else {
                    count[11] = sound.load(context, R.raw.female_us_11, 0);

                }
                final int count11f = count[11];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count11f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f12":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[12] = sound.load(context, R.raw.female_kr_12, 0);
                } else if (language.equals("en")) {
                    count[12] = sound.load(context, R.raw.female_us_12, 0);
                } else if (language.equals("zh")) {

                    count[1] = sound.load(context, R.raw.tmp_cn_12, 0);
                } else {
                    count[12] = sound.load(context, R.raw.female_us_12, 0);

                }
                final int count12f = count[12];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count12f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f13":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[13] = sound.load(context, R.raw.female_kr_13, 0);
                } else if (language.equals("en")) {
                    count[13] = sound.load(context, R.raw.female_us_13, 0);
                } else if (language.equals("zh")) {

                    count[1] = sound.load(context, R.raw.tmp_cn_13, 0);
                } else {
                    count[13] = sound.load(context, R.raw.female_us_13, 0);

                }
                final int count13f = count[13];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count13f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f14":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[14] = sound.load(context, R.raw.female_kr_14, 0);
                } else if (language.equals("en")) {
                    count[14] = sound.load(context, R.raw.female_us_14, 0);
                } else if (language.equals("zh")) {

                    count[14] = sound.load(context, R.raw.tmp_cn_14, 0);
                } else {
                    count[14] = sound.load(context, R.raw.female_us_14, 0);

                }
                final int count14f = count[14];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count14f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "f15":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[15] = sound.load(context, R.raw.female_kr_15, 0);
                } else if (language.equals("en")) {
                    count[15] = sound.load(context, R.raw.female_us_15, 0);
                } else if (language.equals("zh")) {

                    count[15] = sound.load(context, R.raw.tmp_cn_15, 0);
                } else {
                    count[15] = sound.load(context, R.raw.female_us_15, 0);

                }
                final int count15f = count[15];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count15f, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m1":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[1] = sound.load(context, R.raw.male_kr_1, 0);
                } else if (language.equals("en")) {
                    count[1] = sound.load(context, R.raw.male_us_1, 0);
                } else if (language.equals("zh")) {

                    count[1] = sound.load(context, R.raw.tmp_cn_1, 0);
                } else {
                    count[1] = sound.load(context, R.raw.male_us_1, 0);

                }
                final int count1m = count[1];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count1m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m2":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[2] = sound.load(context, R.raw.male_kr_2, 0);
                } else if (language.equals("en")) {
                    count[2] = sound.load(context, R.raw.male_us_2, 0);
                } else if (language.equals("zh")) {

                    count[2] = sound.load(context, R.raw.tmp_cn_2, 0);
                } else {
                    count[2] = sound.load(context, R.raw.male_us_2, 0);

                }
                final int count2m = count[2];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count2m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m3":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[3] = sound.load(context, R.raw.male_kr_3, 0);
                } else if (language.equals("en")) {
                    count[3] = sound.load(context, R.raw.male_us_3, 0);
                } else if (language.equals("zh")) {

                    count[3] = sound.load(context, R.raw.tmp_cn_3, 0);
                } else {
                    count[3] = sound.load(context, R.raw.male_us_3, 0);

                }
                final int count3m = count[3];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count3m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m4":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[4] = sound.load(context, R.raw.male_kr_4, 0);
                } else if (language.equals("en")) {
                    count[4] = sound.load(context, R.raw.male_us_4, 0);
                } else if (language.equals("zh")) {

                    count[4] = sound.load(context, R.raw.tmp_cn_4, 0);
                } else {
                    count[4] = sound.load(context, R.raw.male_us_4, 0);

                }
                final int count4m = count[4];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count4m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m5":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[5] = sound.load(context, R.raw.male_kr_5, 0);
                } else if (language.equals("en")) {
                    count[5] = sound.load(context, R.raw.male_us_5, 0);
                } else if (language.equals("zh")) {

                    count[5] = sound.load(context, R.raw.tmp_cn_5, 0);
                } else {
                    count[5] = sound.load(context, R.raw.male_us_5, 0);

                }
                final int count5m = count[5];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count5m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m6":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[6] = sound.load(context, R.raw.male_kr_6, 0);
                } else if (language.equals("en")) {
                    count[6] = sound.load(context, R.raw.male_us_6, 0);
                } else if (language.equals("zh")) {

                    count[6] = sound.load(context, R.raw.tmp_cn_6, 0);
                } else {
                    count[6] = sound.load(context, R.raw.male_us_6, 0);

                }
                final int count6m = count[6];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count6m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m7":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[7] = sound.load(context, R.raw.male_kr_7, 0);
                } else if (language.equals("en")) {
                    count[7] = sound.load(context, R.raw.male_us_7, 0);
                } else if (language.equals("zh")) {

                    count[7] = sound.load(context, R.raw.tmp_cn_7, 0);
                } else {
                    count[7] = sound.load(context, R.raw.male_us_7, 0);

                }
                final int count7m = count[7];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count7m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m8":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[8] = sound.load(context, R.raw.male_kr_8, 0);
                } else if (language.equals("en")) {
                    count[8] = sound.load(context, R.raw.male_us_8, 0);
                } else if (language.equals("zh")) {

                    count[8] = sound.load(context, R.raw.tmp_cn_8, 0);
                } else {
                    count[8] = sound.load(context, R.raw.male_us_8, 0);

                }
                final int count8m = count[8];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count8m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m9":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[9] = sound.load(context, R.raw.male_kr_9, 0);
                } else if (language.equals("en")) {
                    count[9] = sound.load(context, R.raw.male_us_9, 0);
                } else if (language.equals("zh")) {

                    count[9] = sound.load(context, R.raw.tmp_cn_9, 0);
                } else {
                    count[9] = sound.load(context, R.raw.male_us_9, 0);

                }
                final int count9m = count[9];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count9m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m10":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[10] = sound.load(context, R.raw.male_kr_10, 0);
                } else if (language.equals("en")) {
                    count[10] = sound.load(context, R.raw.male_us_10, 0);
                } else if (language.equals("zh")) {

                    count[10] = sound.load(context, R.raw.tmp_cn_10, 0);
                } else {
                    count[10] = sound.load(context, R.raw.male_us_10, 0);

                }
                final int count10m = count[10];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count10m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m11":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[11] = sound.load(context, R.raw.male_kr_11, 0);
                } else if (language.equals("en")) {
                    count[11] = sound.load(context, R.raw.male_us_11, 0);
                } else if (language.equals("zh")) {

                    count[11] = sound.load(context, R.raw.tmp_cn_11, 0);
                } else {
                    count[11] = sound.load(context, R.raw.male_us_11, 0);

                }
                final int count11m = count[11];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count11m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m12":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[12] = sound.load(context, R.raw.male_kr_12, 0);
                } else if (language.equals("en")) {
                    count[12] = sound.load(context, R.raw.male_us_12, 0);
                } else if (language.equals("zh")) {

                    count[12] = sound.load(context, R.raw.tmp_cn_12, 0);
                } else {
                    count[12] = sound.load(context, R.raw.male_us_12, 0);

                }
                final int count12m = count[12];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count12m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m13":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[13] = sound.load(context, R.raw.male_kr_13, 0);
                } else if (language.equals("en")) {
                    count[13] = sound.load(context, R.raw.male_us_13, 0);
                } else if (language.equals("zh")) {

                    count[13] = sound.load(context, R.raw.tmp_cn_13, 0);
                } else {
                    count[13] = sound.load(context, R.raw.male_us_13, 0);

                }
                final int count13m = count[13];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count13m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m14":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[14] = sound.load(context, R.raw.male_kr_14, 0);
                } else if (language.equals("en")) {
                    count[14] = sound.load(context, R.raw.male_us_14, 0);
                } else if (language.equals("zh")) {

                    count[14] = sound.load(context, R.raw.tmp_cn_14, 0);
                } else {
                    count[14] = sound.load(context, R.raw.male_us_14, 0);

                }
                final int count14m = count[14];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count14m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
            case "m15":
                // 카운트
                Log.d(TAG, "setAudio: count");
                if (language.equals("ko")) {
                    count[15] = sound.load(context, R.raw.male_kr_15, 0);
                } else if (language.equals("en")) {
                    count[15] = sound.load(context, R.raw.male_us_15, 0);
                } else {
                    count[15] = sound.load(context, R.raw.male_us_15, 0);

                }
                final int count15m = count[15];
                sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(final SoundPool soundPool, int audio, int status) {

                        soundPool.play(count15m, 100, 100, 0, 0, 1.0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                soundPool.release();
                            }
                        }).start();
                    }
                });
                break;
//            case "01":
//                // 넥스파워 가족이 되신 것을 환영합니다. 넥스파워 버튼 아래에 있는 제품 번호를 입력해 주세요.
//                mp = MediaPlayer.create(context, R.raw.audio_01);
//                break;

            case "please_measure":
                // 측정 버튼을 눌러서 측정을 해주시기 바랍니다. (1회)
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_please_measure);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_please_measure);

                } else if (language.equals("zh")) {

//                    mp = MediaPlayer.create(context, R.raw.tmp_cn_s);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_please_measure);

                }
                break;

            case "chair":
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_chiar);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_chiar);

                } else if (language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_chair);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_chiar);

                }
                break;

            case "height":
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_height);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_height);

                } else if (language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_height);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_height);

                }
                break;

            case "weight":
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_weight);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_weight);

                } else if (language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_weight);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_weight);

                }
                break;


            case "exercise_setting":
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_exericse_setting);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exericse_setting);

                } else if (language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_exercise_setting);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exericse_setting);

                }
                break;
            case "exercise_start":
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_exercise_start);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_start);

                } else if (language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_exercise_start);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_start);

                }
                break;
            case "exercise_finish":
                if (language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_exercise_finish);

                } else if (language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_finish);

                } else if (language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_exercise_finish);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_finish);

                }
                CFG_IS_EXERCISE = false;
                break;

            case "set_end":
                mp = MediaPlayer.create(context, R.raw.set_end);


                break;
        }

        if (!CFG_IS_EXERCISE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mp.start();

                }
            }).start();
        }


    }

    public static void audioStop() {
        if (mp != null) {
            mp.stop();
//            mp.release();
//            mp = null;
        }
    }



}
