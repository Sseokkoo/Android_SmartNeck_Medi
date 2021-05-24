package com.smartneck.twofive.Fit.Main;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.smartneck.twofive.Fit.AppVersionActivity;
import com.smartneck.twofive.Fit.BleDialog.BleAdapter;
import com.smartneck.twofive.Fit.BleDialog.BleItem;
import com.smartneck.twofive.Fit.Chart.ChartActivity;
import com.smartneck.twofive.Fit.CustomerSupport.Controller.CustomerSupportListActivity;
import com.smartneck.twofive.Fit.ErrorReport.ErrorReportActivity;
import com.smartneck.twofive.Fit.GlobalApplication;
import com.smartneck.twofive.Fit.LoginActivity;
import com.smartneck.twofive.Fit.MeasureActivity;
import com.smartneck.twofive.Fit.Notice.NoticeListActivity;
import com.smartneck.twofive.R;
import com.smartneck.twofive.Review.ReviewActivity;
import com.smartneck.twofive.Fit.UserInfoEdit.UserInfoEditListActivity;
import com.smartneck.twofive.Fit.util.Address;
import com.smartneck.twofive.Fit.util.BluetoothUtils;
import com.smartneck.twofive.Fit.util.BottomNavigationHelper;
import com.smartneck.twofive.Fit.util.Commend;
import com.smartneck.twofive.Fit.util.Constants;
import com.smartneck.twofive.Fit.util.HttpConnect;
import com.smartneck.twofive.Fit.util.Param;
import com.smartneck.twofive.Fit.util.ProgressDialog;
import com.smartneck.twofive.Fit.util.StringUtils;
import com.smartneck.twofive.Fit.util.User.Preset;
import com.smartneck.twofive.Fit.util.User.User;

import static com.smartneck.twofive.Fit.BrowserActivity.isBrowser;
import static com.smartneck.twofive.Fit.Main.WeightSettingFragment.currentWeight;
import static com.smartneck.twofive.Fit.Main.WeightSettingFragment.isMove;
import static com.smartneck.twofive.Fit.Main.WeightSettingFragment.isWeight;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_HEIGHT;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_WEIGHT;
import static com.smartneck.twofive.Fit.MeasureActivity.CFG_WEIGHT_MAX;
import static com.smartneck.twofive.Fit.MeasureActivity.isMeasure;
import static com.smartneck.twofive.Fit.MeasureActivity.moveExercise;
import static com.smartneck.twofive.Fit.MeasureActivity.moveProcedure;
import static com.smartneck.twofive.Fit.util.Constants.CFG_IS_EXERCISE;
import static com.smartneck.twofive.Fit.util.User.Preset.DEVICE_NAME;
import static com.smartneck.twofive.Fit.util.User.Preset.getEntrySelection;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Context mContext;
    public static Activity activity;
    public static boolean isLogout;
    static int fragIndex;
    Handler handler;
    ImageView test;
    NavigationView nav_view;
    BottomNavigationView bottomNavigationView;

    HomeFragment fragment_home;
    ExerciseFragment fragment_exercise;
    ProcedureFragment fragment_procedure;
    ProcedureDefaultFragment fragment_procedure_default;
//    SeatSettingFragment fragment_seat;
//    WeightSettingFragment fragment_weight;
//    HeightFragment fragment_height;
    final int rssiDistance = -90;
    boolean isOpen;
    static boolean isMain;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    static boolean is_82 = false;
    static boolean isSeatMove = false;
    static boolean isWeightMove = false;
    static boolean isClick = false;
    public static int tabPos = 4;
    static String currentLocation = "";

    public static String protocolType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragIndex = 0;
        Log.d(TAG, " ");
        Log.d(TAG, " ");
        Log.d(TAG, " ");
        Log.d(TAG, "■■■■■■■■■■■■■■■■■■■■ START ■■■■■■■■■■■■■■■■■■■■");
        Log.d(TAG, " ");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getEntrySelection(this);
        Preset.soundType = "female";
        mContext = this;
        activity = this;
        handler = new Handler();
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
                        setBottomNavigation("PROCEDURE_DEFAULT");

                        break;
//                    case R.id.navigation_height:
//                        setBottomNavigation("HEIGHT");
//
//
//                        break;
//                    case R.id.navigation_weight:
//                        setBottomNavigation("WEIGHT");
//
//                        fragment_weight = new WeightSettingFragment();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_weight, "weight").commit();
//
//                        break;

                }
                return true;
            }
        });


        // TODO: 2020-06-03 블루투스 위치권한 테스트
        int IS_ACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int IS_ACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (Build.VERSION.SDK_INT >= 29){
            if (IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
                setInit();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }else{
            if (IS_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED && IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
                setInit();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "----- onRequestPermissionsResult: " + requestCode + " / " + permissions.length + " / " + grantResults.length);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    setInit();
                else {
                    finish();
                }
                return;
            }
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
        tabPos = 4;
        isMeasure = false;
        //브라우저 액티비티에서 홈버튼 누르면 홈 프래그먼트로 이동
        getUserPreset();
//        if (CFG_MEMBER_NAME == null) {
//            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
//            CFG_MEMBER_NAME = preferences.getString("name", "");
//        }
        if (isBrowser) {
            setBottomNavigation("HOME");
            isBrowser = false;
        }

        //측정 후 프래그먼트 이동
        if (moveExercise) {
            moveExercise = false;
            setBottomNavigation("EXERCISE");
        } else if (moveProcedure) {
            moveProcedure = false;
            setBottomNavigation("PROCEDURE_DEFAULT");
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
        if (mConnected) {
            fragment_home = null;
            setDisconnect();
        }


    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "----- onBackPressed");
        if (isOpen) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return;
        }


        if (tabPos == 3) {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                setBottomNavigation("HOME");

//            Constants.init();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), getString(R.string.toast_back2), Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (!currentLocation.equals("home")) {
            setBottomNavigation("HOME");
            return;
        }


        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            System.exit(0);

            if (mConnected) setDisconnect();
//            Constants.init();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), getString(R.string.toast_back), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "----- onKeyDown: " + keyCode);

        return super.onKeyDown(keyCode, event);
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

        // 운동

//        if (id == R.id.nav_alarm) {
//            Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
//            startActivity(intent);
//        } else
        if (id == R.id.nav_user_info) {
            //개인정보 수정
            Intent intent = new Intent(getApplicationContext(), UserInfoEditListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_device_info) {
            Intent intent = new Intent(getApplicationContext(), AppVersionActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_statistics) {

            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            isLogout = true;
            User.setAutologinState(this, false);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
//            setMemberLogout(getApplicationContext());

        }
//        else if (id == R.id.nav_management) {
//            //유저 알림장 ?? 공지 ??
//        }
        else if (id == R.id.nav_notice) {
            Intent intent = new Intent(getApplicationContext(), NoticeListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            startActivity(intent);
            //            setBrowser(Constants.User.token, "/review");
        } else if (id == R.id.nav_inquery) {
            //고객지원
            Intent intent = new Intent(getApplicationContext(), CustomerSupportListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_error) {
            Intent intent = new Intent(getApplicationContext(), ErrorReportActivity.class);
            startActivity(intent);
        }

//        else if (id == R.id.nav_height) {
//            if (isConnectedBle()) {
//                Intent intent = new Intent(getApplicationContext(), MeasureActivity.class);
//                intent.putExtra("height", true);
//                startActivity(intent);
//            }
//
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    public boolean isInternet() {
        Log.d(TAG, "----- isInternet");

        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected() == true || mobile.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }


    public void setInit() {
        Log.d(TAG, "----- setInit");
        setDesktopIcon();

        // Home
        fragment_home = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_home, "FRAG_HOME").commit();

        // Internet
        Constants.CFG_IS_INTERNET = isInternet();

        if (Constants.CFG_IS_INTERNET == false) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_internet), Toast.LENGTH_SHORT).show();

            finish();

            return;
        }

        // BLE
        Constants.CFG_IS_BLUETOOTH = isBluetooth();

        if (Constants.CFG_IS_BLUETOOTH == true) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        }

//        getBleStatus();


        setEnable();
    }

    // 홈인지 핏인지

    public void setDeviceType(String DeviceType) {
        Log.d(TAG, "----- ChartActivity setDeviceType: " + DeviceType);

        switch (DeviceType) {
            case "HOM":
                break;

            case "FIT":
                break;


        }


    }

    public void setMessageConfirm(String msg, String type, final String move) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        if (type.equals("stopExercise")) {
            ab.setMessage(msg).setCancelable(
                    false).setPositiveButton(R.string.dialog_exercise_quit_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).setNegativeButton(getString(R.string.dialog_exercise_quit_confilm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    setMessage(StringUtils.getCommand("44 3A 01"));
                    //timerHandler.sendEmptyMessage(TIMER_STOP);
                    if (move.equals("home")) {
                        fragment_home = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_home, "FRAG_HOME").commit();
                    } else if (move.equals("procedure")) {
                        fragment_procedure = new ProcedureFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_procedure, "FRAG_PROCEDURE").commit();
                    }

                    CFG_IS_EXERCISE = false;
                }
            });
        } else {
            ab.setMessage(msg).setCancelable(
                    false).setPositiveButton(R.string.dialog_exercise_quit_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

        }

        AlertDialog alert = ab.create();
        alert.show();
    }

    public void setMessageConfirm(String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);


        ab.setMessage(msg).setCancelable(
                false).setPositiveButton(R.string.dialog_exercise_quit_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        AlertDialog alert = ab.create();
        alert.setTitle("안내");
        alert.setIcon(R.drawable.ic_baseline_error_outline_24px);
        alert.show();
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
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Smart Neck");
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
//                for (int i = 0; i < 4; i++){
//                    bottomNavigationView.getMenu().getItem(i).setChecked(false);
//
//                }

                test.setVisibility(View.GONE);
                fragment_home = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_home, "FRAG_HOME").commitAllowingStateLoss();
                ;
                break;
            case "EXERCISE":
                bottomNavigationView.getMenu().getItem(1).setChecked(true);

                fragment_exercise = new ExerciseFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_exercise, "FRAG_EXERCISE").commit();
                test.setVisibility(View.VISIBLE);

                break;

            case "PROCEDURE":
                bottomNavigationView.getMenu().getItem(0).setChecked(true);

                fragment_procedure = new ProcedureFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_procedure, "FRAG_PROCEDURE").commit();
                test.setVisibility(View.VISIBLE);

                break;

//            case "WEIGHT":
//                bottomNavigationView.getMenu().getItem(1).setChecked(true);
//                fragment_weight = new WeightSettingFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_weight, "weight").commit();
//                test.setVisibility(View.VISIBLE);
//
//                break;

//            case "SEAT":
//                bottomNavigationView.getMenu().getItem(0).setChecked(true);
//                bottomNavigationView.setVisibility(View.GONE);
//                fragment_seat = new SeatSettingFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_seat, "seat").commit();
//                test.setVisibility(View.GONE);
//
//                break;

            case "PROCEDURE_DEFAULT":
                fragment_procedure_default = new ProcedureDefaultFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_procedure_default, "FRAG_PROCEDURE_DEFAULT").commit();
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                test.setVisibility(View.VISIBLE);

                break;

//            case "HEIGHT":
//                fragment_height = new HeightFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_height, "FRAG_HEIGHT").commit();
//                bottomNavigationView.getMenu().getItem(0).setChecked(true);
//                test.setVisibility(View.VISIBLE);
//                break;


        }
    }

    private void getUserPreset() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpConnect httpConnect = new HttpConnect();
                Param param = new Param();
                param.add("token", User.token);
                param.add("memberNo", String.valueOf(User.MemberNo));
                Address address = new Address();
                if (httpConnect.httpConnect(param.getParam(), address.getLoadUserPreset()) == 200) {
                    if (!httpConnect.getReceiveMessage().equals("fail")) {
                        Preset.getPresetJson(getApplicationContext(), httpConnect.getReceiveMessage());
                    }
                }

                Log.d(TAG, "response code: " + httpConnect.getResponseCode());
                Log.d(TAG, "receive message : " + httpConnect.getReceiveMessage());
            }
        }).start();


    }

    static MediaPlayer mp;

    public static void setAudio(String Code) {
        // TODO: 2019-05-10 audioStop() 메소드 추가

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
                if (User.language.equals("ko")) {
                    count[1] = sound.load(context, R.raw.female_kr_1, 0);
                } else if (User.language.equals("en")) {
                    count[1] = sound.load(context, R.raw.female_us_1, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[2] = sound.load(context, R.raw.female_kr_2, 0);
                } else if (User.language.equals("en")) {
                    count[2] = sound.load(context, R.raw.female_us_2, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[3] = sound.load(context, R.raw.female_kr_3, 0);
                } else if (User.language.equals("en")) {
                    count[3] = sound.load(context, R.raw.female_us_3, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[4] = sound.load(context, R.raw.female_kr_4, 0);
                } else if (User.language.equals("en")) {
                    count[4] = sound.load(context, R.raw.female_us_4, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[5] = sound.load(context, R.raw.female_kr_5, 0);
                } else if (User.language.equals("en")) {
                    count[5] = sound.load(context, R.raw.female_us_5, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[6] = sound.load(context, R.raw.female_kr_6, 0);
                } else if (User.language.equals("en")) {
                    count[6] = sound.load(context, R.raw.female_us_6, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[7] = sound.load(context, R.raw.female_kr_7, 0);
                } else if (User.language.equals("en")) {
                    count[7] = sound.load(context, R.raw.female_us_7, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[8] = sound.load(context, R.raw.female_kr_8, 0);
                } else if (User.language.equals("en")) {
                    count[8] = sound.load(context, R.raw.female_us_8, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[9] = sound.load(context, R.raw.female_kr_9, 0);
                } else if (User.language.equals("en")) {
                    count[9] = sound.load(context, R.raw.female_us_9, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[10] = sound.load(context, R.raw.female_kr_10, 0);
                } else if (User.language.equals("en")) {
                    count[10] = sound.load(context, R.raw.female_us_10, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[11] = sound.load(context, R.raw.female_kr_11, 0);
                } else if (User.language.equals("en")) {
                    count[11] = sound.load(context, R.raw.female_us_11, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[12] = sound.load(context, R.raw.female_kr_12, 0);
                } else if (User.language.equals("en")) {
                    count[12] = sound.load(context, R.raw.female_us_12, 0);
                } else if (User.language.equals("zh")) {

                    count[12] = sound.load(context, R.raw.tmp_cn_12, 0);
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
                if (User.language.equals("ko")) {
                    count[13] = sound.load(context, R.raw.female_kr_13, 0);
                } else if (User.language.equals("en")) {
                    count[13] = sound.load(context, R.raw.female_us_13, 0);
                } else if (User.language.equals("zh")) {

                    count[13] = sound.load(context, R.raw.tmp_cn_13, 0);
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
                if (User.language.equals("ko")) {
                    count[14] = sound.load(context, R.raw.female_kr_14, 0);
                } else if (User.language.equals("en")) {
                    count[14] = sound.load(context, R.raw.female_us_14, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[15] = sound.load(context, R.raw.female_kr_15, 0);
                } else if (User.language.equals("en")) {
                    count[15] = sound.load(context, R.raw.female_us_15, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[1] = sound.load(context, R.raw.male_kr_1, 0);
                } else if (User.language.equals("en")) {
                    count[1] = sound.load(context, R.raw.male_us_1, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[2] = sound.load(context, R.raw.male_kr_2, 0);
                } else if (User.language.equals("en")) {
                    count[2] = sound.load(context, R.raw.male_us_2, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[3] = sound.load(context, R.raw.male_kr_3, 0);
                } else if (User.language.equals("en")) {
                    count[3] = sound.load(context, R.raw.male_us_3, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[4] = sound.load(context, R.raw.male_kr_4, 0);
                } else if (User.language.equals("en")) {
                    count[4] = sound.load(context, R.raw.male_us_4, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[5] = sound.load(context, R.raw.male_kr_5, 0);
                } else if (User.language.equals("en")) {
                    count[5] = sound.load(context, R.raw.male_us_5, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[6] = sound.load(context, R.raw.male_kr_6, 0);
                } else if (User.language.equals("en")) {
                    count[6] = sound.load(context, R.raw.male_us_6, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[7] = sound.load(context, R.raw.male_kr_7, 0);
                } else if (User.language.equals("en")) {
                    count[7] = sound.load(context, R.raw.male_us_7, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[8] = sound.load(context, R.raw.male_kr_8, 0);
                } else if (User.language.equals("en")) {
                    count[8] = sound.load(context, R.raw.male_us_8, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[9] = sound.load(context, R.raw.male_kr_9, 0);
                } else if (User.language.equals("en")) {
                    count[9] = sound.load(context, R.raw.male_us_9, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[10] = sound.load(context, R.raw.male_kr_10, 0);
                } else if (User.language.equals("en")) {
                    count[10] = sound.load(context, R.raw.male_us_10, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[11] = sound.load(context, R.raw.male_kr_11, 0);
                } else if (User.language.equals("en")) {
                    count[11] = sound.load(context, R.raw.male_us_11, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[12] = sound.load(context, R.raw.male_kr_12, 0);
                } else if (User.language.equals("en")) {
                    count[12] = sound.load(context, R.raw.male_us_12, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[13] = sound.load(context, R.raw.male_kr_13, 0);
                } else if (User.language.equals("en")) {
                    count[13] = sound.load(context, R.raw.male_us_13, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[14] = sound.load(context, R.raw.male_kr_14, 0);
                } else if (User.language.equals("en")) {
                    count[14] = sound.load(context, R.raw.male_us_14, 0);
                } else if (User.language.equals("zh")) {

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
                if (User.language.equals("ko")) {
                    count[15] = sound.load(context, R.raw.male_kr_15, 0);
                } else if (User.language.equals("en")) {
                    count[15] = sound.load(context, R.raw.male_us_15, 0);
                } else if (User.language.equals("zh")) {
                    count[14] = sound.load(context, R.raw.tmp_cn_15, 0);
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
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_please_measure);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_please_measure);

                } else if (User.language.equals("zh")) {

//                    mp = MediaPlayer.create(context, R.raw.tmp_cn_s);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_please_measure);

                }
                break;

            case "chair":
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_chiar);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_chiar);

                } else if (User.language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_chair);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_chiar);

                }
                break;

            case "height":
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_height);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_height);

                } else if (User.language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_height);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_height);

                }
                break;

            case "weight":
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_weight);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_weight);

                } else if (User.language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_weight);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_weight);

                }
                break;


            case "exercise_setting":
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_exericse_setting);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exericse_setting);

                } else if (User.language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_exercise_setting);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exericse_setting);

                }
                break;
            case "exercise_start":
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_exercise_start);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_start);

                } else if (User.language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_exercise_start);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_start);

                }
                break;
            case "exercise_finish":
                if (User.language.equals("ko")) {
                    mp = MediaPlayer.create(context, R.raw.female_kr_script_exercise_finish);

                } else if (User.language.equals("en")) {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_finish);

                } else if (User.language.equals("zh")) {

                    mp = MediaPlayer.create(context, R.raw.tmp_cn_script_exercise_finish);
                } else {
                    mp = MediaPlayer.create(context, R.raw.female_us_script_exercise_finish);

                }
                CFG_IS_EXERCISE = false;
                break;
        }

        if (!CFG_IS_EXERCISE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
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


    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothLeScanner mBluetoothLeScanner;
    public ScanCallback mScanCallback;
    public static BluetoothGatt mBluetoothGatt;
    public Handler mHandler;

    public static boolean mScanning;
    public static boolean mConnected;
    public static boolean mEchoInitialized;

    public Map<String, BluetoothDevice> mScanResults;


//    public String getBleStatus() {
//        Log.d(TAG, "----- getBleStatus");
//
//        // 블루투스 지원 여부
//        if (!Constants.CFG_IS_BLUETOOTH) {
//            Constants.CFG_BLE_STATUS_CODE = "00";
//        } else {
//            int IS_ACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//            int IS_ACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//
//            // 블루투스 권한 획득 여부
//            if (IS_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED || IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
//
//                // 블루투스 활성화 여부
//                if (mBluetoothAdapter.isEnabled()) {
//                    Constants.CFG_BLE_STATUS_CODE = "03";
//
//                    // 기기 연결 여부
//                    if (mConnected) {
//                        Constants.CFG_BLE_STATUS_CODE = "05";
//                    }
//                } else {
//                    Constants.CFG_BLE_STATUS_CODE = "02";
//                }
//            } else {
//                Constants.CFG_BLE_STATUS_CODE = "01";
//            }
//        }
//
//        Log.d(TAG, "- Constants.CFG_BLE_STATUS_CODE: " + Constants.CFG_BLE_STATUS_CODE);
//
//        return Constants.CFG_BLE_STATUS_CODE;
//    }

    public boolean isBluetooth() {
        Log.d(TAG, "----- isBleProgress");

        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();

        if (ba != null) {
            return true;
        } else {
            return false;
        }
    }

    public void setEnable() {
        Log.d(TAG, "----- setEnable");

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "----- onActivityResult: " + resultCode);

        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    if (!mConnected) {
                        fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                        fragment_home.setBleStatus(3);
                    }
                } else {
                    fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                    fragment_home.setBleStatus(2);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    BleAdapter bleAdapter;

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ble_list, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel = view.findViewById(R.id.dialog_ble_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                fragment_home.setBleStatus(3);
                setScanStop();
                mScanning = false;
                dialog.dismiss();

            }
        });
        final ImageView retry = view.findViewById(R.id.dialog_ble_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mScanning) {
                    setScanStart(true);
                }
//                else {
//                    Toast.makeText(ChartActivity.this, "장치를 검색중입니다.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        ListView bleList = view.findViewById(R.id.dialog_ble_listView);
        bleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (bleItems.get(position).getName().contains("SMARTNECK1")) {
//                    Constants.CFG_DEVICE_TYPE = "FIT";
//                } else if (bleItems.get(position).getName().contains("SMARTNECK2")) {
//                    Constants.CFG_DEVICE_TYPE = "HOM";
//                }
                setConnect(bleItems.get(position).getBluetoothDevice());
                setScanStop();
                dialog.dismiss();
            }
        });
        bleAdapter = new BleAdapter(bleItems, getLayoutInflater());
        bleList.setAdapter(bleAdapter);


        dialog.setCancelable(false);
        dialog.show();
    }

    public void setScanStart(boolean isDialog) {
        Log.d(TAG, "----- setScanStart");
        bleItems.clear();
        fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        fragment_home.setBleStatus(4);

        if (mScanning == true) {
            return;
        }

        mScanResults = new HashMap<>();
        mScanCallback = new BleScanCallback(mScanResults);

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(mScanCallback);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "- mHandler.postDelayed run");

                if (!mConnected) {
                    setScanStop();

                    fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                    fragment_home.setBleStatus(6);
                }
            }
        }, Constants.SCAN_PERIOD);
        if (!isDialog) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAlertDialog();

                        }
                    });
                }
            }).start();
        }

        mScanning = true;
    }

    public void setScanStop() {
        Log.d(TAG, "----- setScanStop");

        if (mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);

            // Scan Complete
            if (mScanResults.isEmpty()) {
                return;
            }
        }
        fragment_home.setBleStatus(4);

        mScanCallback = null;
        mScanning = false;
        mHandler = null;
    }

    public void setConnect(BluetoothDevice device) {
        Log.d(TAG, "----- setConnect: " + device.getName().replace(" ", "") + " / " + device.getAddress());
        GattClientCallback gattClientCallback = new GattClientCallback();
        mBluetoothGatt = device.connectGatt(this, true, gattClientCallback);
//        mBluetoothGatt.requestMtu(60);
        if (device.getName().contains("SMARTNECK1")){
            Constants.deviceType = "FIT";
        }else if (device.getName().contains("SMARTNECK2")){
            Constants.deviceType = "HOM";
            Log.d(TAG, " - - - - - - - - - - Current Device Type -> HOM");
        }
        DEVICE_NAME = device.getName().substring(9);
        Log.d(TAG, " - - - - - DEVICE_NAME: " + DEVICE_NAME.substring(9) + " / DEVICE_TYPE: " + Constants.deviceType);
        final ProgressDialog progressDialog = new ProgressDialog(mContext, getLayoutInflater());
        progressDialog.show(getString(R.string.dialog_ble_connecting));
        new Thread(new Runnable() {
            @Override
            public void run() {
                float count = 0;
                while (!isBleProgress) {
                    count += 0.5f;
                    if (count == 20) {
                        progressDialog.dismiss();
                        ((MainActivity) MainActivity.mContext).setScanStop();
                        isBleProgress = true;
                    }
                    Log.d(TAG, "isBleProgress: " + isBleProgress + "sec: " + count);
                    try {
                        Thread.sleep(499);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                progressDialog.dismiss();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isBleProgress = false;
            }
        }).start();
    }

    public void setDisconnect() {
        Log.d(TAG, "----- setDisconnect");

        mConnected = false;
        mEchoInitialized = false;
        isBleProgress = false;
        mScanning = false;
        if (mBluetoothGatt != null) {
            //((ChartActivity)ChartActivity.mContext).setMessage(StringUtils.getCommand("44 3A 01"));

            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();

        }

//        if (Constants.CFG_FRAG_MODE == 0) {
        if (fragment_home != null){
            fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
            fragment_home.setBleStatus(3);
        }

//        }
    }

    public static void setMessage(String message) {
        Log.d(TAG, "----- setMessage: " + message);

        if (!mConnected || !mEchoInitialized) {
            Log.d(TAG, "- mConnected: " + mConnected);
            Log.d(TAG, "- mEchoInitialized: " + mEchoInitialized);

            return;
        }

        message = message.replaceAll(" ", "");

        byte[] messageBytes = StringUtils.hexStringToByteArray(message);

        if (messageBytes.length == 0) {
            Log.d(TAG, "- setMessage: Unable to convert message to bytes.");

            return;
        }

        BluetoothGattService service = mBluetoothGatt.getService(Constants.CFG_CHARACTERISTIC_SERVICE_UUID);
        BluetoothGattCharacteristic characteristic_write = service.getCharacteristic(Constants.CFG_CHARACTERISTIC_WRITE_UUID);

        characteristic_write.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        characteristic_write.setValue(messageBytes);

        boolean success = mBluetoothGatt.writeCharacteristic(characteristic_write);

        if (success) {
            Log.d(TAG, "▶ Sent Message: " + StringUtils.byteArrayInHexFormat(messageBytes));
        } else {
            Log.d(TAG, "▶ Sent Message: Failed to write dat.");
        }
    }

    public static void setMessage(byte[] commend) {
        Log.d(TAG, "----- sendCommend: " + commend);

        if (!mConnected || !mEchoInitialized) {
            Log.d(TAG, "- mConnected: " + mConnected);
            Log.d(TAG, "- mEchoInitialized: " + mEchoInitialized);

            return;
        }


        if (commend.length == 0) {
            Log.d(TAG, "- setMessage: Unable to convert message to bytes.");

            return;
        }

        BluetoothGattService service = mBluetoothGatt.getService(Constants.CFG_CHARACTERISTIC_SERVICE_UUID);
        BluetoothGattCharacteristic characteristic_write = service.getCharacteristic(Constants.CFG_CHARACTERISTIC_WRITE_UUID);

        characteristic_write.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        characteristic_write.setValue(commend);

        boolean success = mBluetoothGatt.writeCharacteristic(characteristic_write);

        if (success) {
            Log.d(TAG, "▶ Sent Message: " + StringUtils.byteArrayInHexFormat(commend));
        } else {
            Log.d(TAG, "▶ Sent Message: Failed to write dat.");
        }
    }

    int sentCount = 0;

    public void setDeviceInit() {
//        Log.d(TAG, "----- setDeviceInit: " + Constants.CFG_DEVICE_TYPE + " / " + CFG_HEIGHT_MAX[1] + " / " + CFG_MEMBER_NO);

        if (mConnected || mEchoInitialized) {
            if (Preset.MaxWeight == 0 || Preset.MaxHeight == 0) {
                Log.d(TAG, "setDeviceInit: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                Preset.seat = 0;
                Preset.setup = 0;
            }
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
                    isInit = false;
                    float count = 0;
                    while (!isInit) {
                        count += 0.5f;
                        if (count == 100) {
                            progressDialog.dismiss();
                            ((MainActivity) MainActivity.mContext).setScanStop();
                            isInit = true;
                        }
                        Log.d(TAG, "isBleProgress: " + isBleProgress + "sec: " + count);
                        try {
                            Thread.sleep(499);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    progressDialog.dismiss();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    isInit = false;
                }
            }).start();

            //todo GO_EXERCISE 테스트 필요
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setMessage(new Commend().sendGoExercise((byte) Preset.seat, (byte) Preset.setup));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(Preset.setup) + " 00 00"));
                    while (true) {


                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!is_82) {
                            if (i == 3) {


                                setMessage(new Commend().sendGoExercise((byte) Preset.seat, (byte) Preset.setup));

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(Preset.setup) + " 00 00"));



                                i = 0;

                            }
                        } else {
                            break;
                        }

                        i++;

                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!mConnected) break;
                        if (sentCount > 3) break;
                        if (protocolType.equals("3b")) break;
                        setMessage(StringUtils.getCommand("44 3A 09 01 76"));

                    }
                }
            }).start();
        }
    }

    public void enableCharacteristicNotification(BluetoothGatt
                                                         gatt, BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "----- enableCharacteristicNotification: " + characteristic.getUuid().toString());

        boolean success = gatt.setCharacteristicNotification(characteristic, true);

        if (success) {
            Log.d(TAG, "- Characteristic notification set successfully for " + characteristic.getUuid().toString());

            if (BluetoothUtils.isEchoCharacteristic(characteristic)) {
                mEchoInitialized = true;
            }
        } else {
            Log.d(TAG, "- Characteristic notification set failure for " + characteristic.getUuid().toString());
        }

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(Constants.CFG_CHARACTERISTIC_DESCRIPTOR_UUID);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    public static boolean isBleProgress;
    public static boolean isExProgress;
    public static boolean isHeiProgress;
    public static boolean isWeiProgress;
    static boolean isInit;

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        byte[] bytes = characteristic.getValue();

        String CFG_RECEIVED_RAW = StringUtils.byteArrayInHexFormat2(bytes).replace("0x", "");
        Log.d(TAG, "◀ CFG_RECEIVED_RAW: " + CFG_RECEIVED_RAW);

        String[] CFG_RECEIVED = CFG_RECEIVED_RAW.split(" ");


        if (CFG_RECEIVED[1].equals("3a")) {
            protocolType = "3a";
            Log.d(TAG, "readCharacteristic: Protocol Type = 3a");

            if (isMeasure) {
                ((MeasureActivity) MeasureActivity.mContext).setMeasure(CFG_RECEIVED);
            }


            if (CFG_RECEIVED[2].equals("84")) {

                if (CFG_RECEIVED[5].equals("00")) {
                    isSeatMove = false;
                } else if (CFG_RECEIVED[5].equals("01")) {
                    isSeatMove = true;
                }

                if (CFG_RECEIVED[6].equals("00")) {
                    isWeightMove = false;
                } else if (CFG_RECEIVED[6].equals("01")) {
                    isWeightMove = true;
                }

                if (tabPos == 0) {
                    if (!isClick) {
                        Preset.seat = Integer.parseInt(CFG_RECEIVED[3], 16);

                    } else {
                        if (CFG_RECEIVED[5].equals("00")) {
                            isClick = false;
                        }
                    }
                }
                if (tabPos == 1) {
//                if (!MeasureActivity.isHeight) {
//
//                }
//                Log.d(TAG, "tabPos: " + tabPos);

//                    Log.d(TAG, "isMove: " + isMove + "/ weight: " + Integer.parseInt(CFG_RECEIVED[8], 16) + " / current: " + currentWeight);
//
//                    if (isMove && currentWeight * 10 == Integer.parseInt(CFG_RECEIVED[8], 16)) {
//                        isMove = false;
//                        Log.d(TAG, "isMove: " + isMove);
//                    }
//
//                    CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);
//                    CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
//
//                    if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1] && CFG_HEIGHT[1] > 0)
//                        CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];


                    if (!isClick && !isWeightMove && !isSeatMove) {

                        Preset.setup = Integer.parseInt(CFG_RECEIVED[4], 16);


                    } else {
                        if (CFG_RECEIVED[6].equals("00")) {
                            isClick = false;
                        }
                    }
                }


//            Log.d(TAG, "isSeatMove: " + isSeatMove);
//            Log.d(TAG, "isWeightMove: " + isWeightMove);
                Log.d(TAG, "readCharacteristic: seat->" + Preset.seat);
            }

            if (!is_82 && CFG_RECEIVED[2].equals("82")) {
                is_82 = true;
            }

            if (!isExProgress && CFG_RECEIVED[2].equals("82") && !isMeasure) {
                isExProgress = true;
            }
            if (!isWeiProgress && CFG_RECEIVED[2].equals("82") && isMeasure) {
                isWeiProgress = true;
            }

            if (isWeight && CFG_RECEIVED[2].equals("82")) {
//                Log.d(TAG, "isMove: " + isMove + "/ weight: " + Integer.parseInt(CFG_RECEIVED[8], 16) + " / current: " + currentWeight);

                if (isMove && currentWeight * 10 == Integer.parseInt(CFG_RECEIVED[8], 16)) {
                    isMove = false;
//                    Log.d(TAG, "isMove: " + isMove);
                }

                CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);
                CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);

                if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1] && CFG_HEIGHT[1] > 0)
                    CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
            }


            if (isWeightMove && CFG_RECEIVED[2].equals("83")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isWeightMove = false;
                    }
                }).start();
                return;
            }

            if (isMain && !isInit && CFG_RECEIVED[2].equals("84") && !isMeasure) {
                if (CFG_RECEIVED[5].equals("00") && CFG_RECEIVED[6].equals("00")) {

                    isInit = true;
                }

            }


            if (tabPos == 0) {
                HeightFragment frag = (HeightFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                frag.setHeight(CFG_RECEIVED);
            }
            if (tabPos == 1){
//                if (!MeasureActivity.isHeight) {
//
//                }
//                Log.d(TAG, "tabPos: " + tabPos);
//                    Log.d(TAG, "isMove: " + isMove + "/ weight: " + Integer.parseInt(CFG_RECEIVED[8], 16) + " / current: " + currentWeight);

                    if (isMove && currentWeight * 10 == Integer.parseInt(CFG_RECEIVED[8], 16)) {
                        isMove = false;
//                        Log.d(TAG, "isMove: " + isMove);
                    }

                    CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[4], 16);
                    CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);

                    if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1] && CFG_HEIGHT[1] > 0)
                        CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
            }

            if (tabPos == 3) {


                ExerciseFragment frag = (ExerciseFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                frag.setExercise(CFG_RECEIVED);

            }
        } else if (CFG_RECEIVED[1].equals("3b")) {
            protocolType = "3b";
            Log.d(TAG, "readCharacteristic: Protocol Type = 3b");
            if (isMeasure) {
                ((MeasureActivity) MeasureActivity.mContext).setMeasure(CFG_RECEIVED);
            }

            //0 - 44
            //1 - 3B
            //2 - 82
            //3 - Current Seat
            //4 - is Seat Move
            //5 - Current Weight
            //6 - is Weight Move
            //7 - Weight Setup
            //8 - Height
            //9 - Check Sum

            if (CFG_RECEIVED[2].equals("82")) {
                if (!is_82) is_82 = true;
                CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[5], 16);



                if (CFG_RECEIVED[4].equals("00")) {
                    isSeatMove = false;
                } else if (CFG_RECEIVED[4].equals("01")) {
                    isSeatMove = true;
                }

                if (CFG_RECEIVED[6].equals("00")) {
                    isWeightMove = false;
                } else if (CFG_RECEIVED[6].equals("01")) {
                    isWeightMove = true;
                }

                if (tabPos == 0) {
                    if (!isClick) {
                        Preset.seat = Integer.parseInt(CFG_RECEIVED[3], 16);

                    } else {
                        if (CFG_RECEIVED[4].equals("00")) {
                            isClick = false;
                        }
                    }
                } else if (tabPos == 1) {

                    if (!isClick && !isWeightMove && !isSeatMove) {

                        Preset.setup = Integer.parseInt(CFG_RECEIVED[7], 16);


                    } else {
                        if (CFG_RECEIVED[6].equals("00")) {
                            isClick = false;
                        }
                    }
                }


            }


            if (!isExProgress && !isMeasure) {
                isExProgress = true;
            }
            if (!isWeiProgress && isMeasure) {
                isWeiProgress = true;
            }

            if (isWeight) {
//                Log.d(TAG, "isMove: " + isMove + "/ weight: " + Integer.parseInt(CFG_RECEIVED[8], 16) + " / current: " + currentWeight);

                if (isMove && currentWeight * 10 == Integer.parseInt(CFG_RECEIVED[5], 16)) {
                    isMove = false;
//                    Log.d(TAG, "isMove: " + isMove);
                }

                CFG_HEIGHT[1] = Integer.parseInt(CFG_RECEIVED[8], 16);
                CFG_WEIGHT[1] = Integer.parseInt(CFG_RECEIVED[5], 16);

                if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1] && CFG_HEIGHT[1] > 0)
                    CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
            }


            if (isWeightMove && CFG_RECEIVED[2].equals("83")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isWeightMove = false;
                    }
                }).start();
                return;
            }

            if (isMain && !isInit && !isMeasure) {
                if (CFG_RECEIVED[4].equals("00") && CFG_RECEIVED[6].equals("00")) {

                    isInit = true;
                }

            }


            if (tabPos == 0) {
                //                ((ChartActivity)ChartActivity.mContext).setConnect(bleItems.get(position).getBluetoothDevice());

                ((AfterMeasureActivity)AfterMeasureActivity.mContext).setHeight(CFG_RECEIVED);
//                HeightFragment frag = (HeightFragment) getSupportFragmentManager().findFragmentById(R.id.frame_measure_container);
//                frag.setHeight(CFG_RECEIVED);

            }

            if (tabPos == 3) {


                ExerciseFragment frag = (ExerciseFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                assert frag != null;
                frag.setExercise(CFG_RECEIVED);

            }
        }

    }

    public static ArrayList<BleItem> bleItems = new ArrayList();

    public class BleScanCallback extends ScanCallback {
        private Map<String, BluetoothDevice> mScanResults;


        BleScanCallback(Map<String, BluetoothDevice> scanResults) {
            mScanResults = scanResults;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // Log.d(Constants.TAG, "- BleScanCallback onScanResult");

            addScanResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            // Log.d(Constants.TAG, "- BleScanCallback onBatchScanResults");

            for (ScanResult result : results) {
                addScanResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "- BleScanCallback onScanFailed: " + errorCode);
        }


        private void addScanResult(ScanResult result) {

            Log.d(TAG, "- BleScanCallback addScanResult: " + result.getDevice().getAddress() + " / " + result.getDevice().getName() + " size : " + mScanResults.size());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    bleAdapter.notifyDataSetChanged();
                }
            });
            BluetoothDevice device = result.getDevice();

            mScanResults.put(device.getAddress(), device);

            if (bleItems.size() == 0 && device.getName() != null) {
                // TODO: 2019-10-18 Smartneck 1 , 2
                if (device.getName().contains("SMARTNECK1") || device.getName().contains("SMARTNECK2")) {

                    bleItems.add(new BleItem(device, device.getAddress(), device.getName()));
                }
            }

            for (int i = 0; i < bleItems.size(); i++) {

                if (device.getName() != null) {
                    if (device.getName().contains("SMARTNECK1") || device.getName().contains("SMARTNECK2")) {
                        for (int j = 0; j < bleItems.size(); j++) {
                            if (bleItems.get(j).getName().equals(device.getName())) {
                                return;
                            }
                        }
                        bleItems.add(new BleItem(device, device.getAddress(), device.getName()));

                    }
                }

            }

            for (int i = 0; i < bleItems.size(); i++) {
                Log.d(TAG, "addScanResult: ArrayList" + bleItems.get(i).getAddress());
            }


        }
    }

    public class GattClientCallback extends BluetoothGattCallback {
        Timer mRssiTimer;

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            //일정거리 이상 떨어지면 블루투스 연결 해제
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.d(TAG, String.format("BluetoothGatt ReadRssi[%d]", rssi));
//                if (rssi < rssiDistance) {
//                    Log.d(TAG, "disconnect Device for rssi : " + rssi);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //Toast.makeText(getApplicationContext(), "기구에 앉아서 조작해주세요.", Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(getApplicationContext(), "기구와 거리가 멀어져\n 연결을 해제합니다.", Toast.LENGTH_SHORT).show();
//                                    // TODO: 2019-05-21 rssi 신호 기준 지정 후 주석해제
//                                    //setDisconnect();
//                                }
//                            });
//                        }
//                    }).start();
//
//                }

            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            // Log.d(Constants.TAG, "----- GattClientCallback onConnectionStateChange: " + status + " / " + newState);

            super.onConnectionStateChange(gatt, status, newState);

            // TODO: 2019-05-22 여기서 bleprogress 제거  후 initprogress 실행
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "----- GattClientCallback onConnectionStateChange BluetoothProfile.STATE_CONNECTED: " + newState);
                mConnected = true;
                isBleProgress = true;


                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        mBluetoothGatt.readRemoteRssi();
                    }
                };
                mRssiTimer = new Timer();
                mRssiTimer.schedule(task, 1000, 1000);
                fragment_home = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
                fragment_home.setBleStatus(5);


                setDeviceInit();
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "----- GattClientCallback onConnectionStateChange BluetoothProfile.STATE_DISCONNECTED: " + newState);

                setDisconnect();
                return;
            }

            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.d(TAG, "----- GattClientCallback onConnectionStateChange BluetoothGatt.GATT_FAILURE: " + status);

                setDisconnect();
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "----- GattClientCallback onConnectionStateChange BluetoothGatt.!GATT_SUCCESS: " + status);

                setDisconnect();
                return;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "----- GattClientCallback onServicesDiscovered: " + status);

            super.onServicesDiscovered(gatt, status);

            if (status != BluetoothGatt.GATT_SUCCESS) {
                return;
            }

            BluetoothGattService service = mBluetoothGatt.getService(Constants.CFG_CHARACTERISTIC_SERVICE_UUID);
            BluetoothGattCharacteristic characteristic_notification = service.getCharacteristic(Constants.CFG_CHARACTERISTIC_NOTIFICATION_UUID);

            enableCharacteristicNotification(gatt, characteristic_notification);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "----- GattClientCallback onDescriptorWrite: " + status);

            BluetoothGattCharacteristic characteristic = gatt.getService(Constants.CFG_CHARACTERISTIC_SERVICE_UUID).getCharacteristic(Constants.CFG_CHARACTERISTIC_WRITE_UUID);
            characteristic.setValue(new byte[]{1, 1});
            gatt.writeCharacteristic(characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            // Log.d(Constants.TAG, "----- GattClientCallback onCharacteristicWrite");
            super.onCharacteristicWrite(gatt, characteristic, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {

                Log.d(TAG, "onCharacteristicWrite: " + mConnected + " / " + mEchoInitialized);

            } else {
                setDisconnect();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "----- GattClientCallback onCharacteristicRead: " + status);

            super.onCharacteristicRead(gatt, characteristic, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "----- GattClientCallback onCharacteristicRead BluetoothGatt.GATT_SUCCESS");

                readCharacteristic(characteristic);
            } else {
                setDisconnect();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // Log.d(Constants.TAG, "----- GattClientCallback onCharacteristicChanged: " + characteristic.getUuid().toString());

            super.onCharacteristicChanged(gatt, characteristic);

            readCharacteristic(characteristic);
        }
    }


}