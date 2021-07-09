package com.smartneck.twofive.Main;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartneck.twofive.BleDialog.BleAdapter;
import com.smartneck.twofive.BleDialog.BleItem;
import com.smartneck.twofive.MeasureActivity;
import com.smartneck.twofive.Member.LoginActivity;
import com.smartneck.twofive.Member.MemberSelectActivity;
import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Address;
import com.smartneck.twofive.util.BluetoothUtils;
import com.smartneck.twofive.util.Constants;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.NetworkStatus;
import com.smartneck.twofive.util.Param;
import com.smartneck.twofive.util.ProgressDialog;
import com.smartneck.twofive.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import static com.smartneck.twofive.GlobalApplication.userPreference;
import static com.smartneck.twofive.Main.ExerciseFragment.isCount;
import static com.smartneck.twofive.Main.ExerciseFragment.isTimer;
import static com.smartneck.twofive.Main.MainActivity.preset;
import static com.smartneck.twofive.Main.WeightSettingFragment.MaxZero;
import static com.smartneck.twofive.Main.WeightSettingFragment.currentWeight;
import static com.smartneck.twofive.Main.WeightSettingFragment.isMove;
import static com.smartneck.twofive.MeasureActivity.CFG_HEIGHT;
import static com.smartneck.twofive.MeasureActivity.CFG_WEIGHT;
import static com.smartneck.twofive.MeasureActivity.CFG_WEIGHT_MAX;
import static com.smartneck.twofive.MeasureActivity.isMeasure;
import static com.smartneck.twofive.util.Constants.REQUEST_ENABLE_BT;
import static com.smartneck.twofive.util.Constants.TAG;

public class BleConnectActivity extends AppCompatActivity {
    Button btn_ble , btn_backup , btn_logout;
    Handler handler;
    TextView tv_ble_message;
    public static Context mContext;
    static boolean is_82 = false;
    public static int tabPos = 4;
    static boolean isSeatMove = false;
    static boolean isWeightMove = false;
    static boolean isClick = false;
    int touchCount = 0;
    int hiddenCount = 0;

    public static Activity bleActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_ble_connect_activity);
        setPerrmmsion();
        handler = new Handler();
        mContext = this;
        bleActivity = this;
        touchCount = 0;
        MainActivity.SettingOn = false;
        // BLE
        Constants.CFG_IS_BLUETOOTH = isBluetooth();

        if (Constants.CFG_IS_BLUETOOTH == true) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        }

//        getBleStatus();
        tv_ble_message = findViewById(R.id.tv_ble_message);

        btn_ble = findViewById(R.id.btn_ble);
        btn_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnable();
                if (!mConnected) {
                    setScanStart(false);

                } else {
                    setDisconnect();

                }

//                }

            }
        });
        btn_backup = findViewById(R.id.btn_backup);
        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getBleUsed();
//        setEnable();
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        //FIXME 테스트 후 제거
        TextView bleTitle = findViewById(R.id.tv_ble_connect_title);
        bleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchCount++;
                if (touchCount == 5){
                    touchCount = 0;
                    if (NetworkStatus.CURRENT_STATUS == NetworkStatus.TYPE_NOT_CONNECTED){
                        Toast.makeText(BleConnectActivity.this, "인터넷 연결 후 사용해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showAlertDialogForBackupData();
                }
            }
        });


        Log.d(TAG, "onCreate: auto " + userPreference.getBoolean("auto" , false));
        Log.d(TAG, "onCreate: auto account"  + userPreference.getString("autoAccount" , ""));
        Log.d(TAG, "onCreate: auto password " + userPreference.getString("autoPassword" , ""));


        View hidden = findViewById(R.id.hidden);
        hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenCount++;
                Log.d(TAG, "onClick: " + hiddenCount + " / " + touchCount);
                if (hiddenCount > 5 && touchCount > 2){
                    Intent intent = new Intent(getApplicationContext() , MemberSelectActivity.class);
                    startActivity(intent);
                }

            }
        });

//        hidden.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext() , MeasureManagementActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnected) {
            setDisconnect();
        }
    }

    void setPerrmmsion() {
        int IS_ACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int IS_ACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (android.os.Build.VERSION.SDK_INT >= 29) {
            if (IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        } else {
            if (IS_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED && IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }
    }

    private void showAlertDialogForBackupData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_load_backup_data, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView btn_load = view.findViewById(R.id.btn_load_data);
        EditText edt_account = view.findViewById(R.id.edt_load_account);
        EditText edt_password = view.findViewById(R.id.edt_load_password);
        EditText edt_phone = view.findViewById(R.id.edt_load_phone);


        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_account.length() == 0){
                    Toast.makeText(BleConnectActivity.this, "계정을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_account.length() == 0){
                    Toast.makeText(BleConnectActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_account.length() == 0){
                    Toast.makeText(BleConnectActivity.this, "휴대폰번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpConnect httpConnect = new HttpConnect();
                        Param param = new Param();
                        param.add("admin" , edt_account.getText().toString());
                        param.add("password" , edt_password.getText().toString());
                        param.add("phone" , edt_phone.getText().toString());
                        if (httpConnect.httpConnect(param.getValue() , new Address().getGetData() , true) == 200){
                            if (!httpConnect.getReceiveMessage().equals("fail")){
                                // 받은 데이터 파싱해서 세팅 및 쉐어드 프리퍼런스에 저장
                                userPreference.getBackData(httpConnect.getReceiveMessage());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BleConnectActivity.this, "데이터 불러오기 완료.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(BleConnectActivity.this, "입력한 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                }).start();

            }
        });

        dialog.show();
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

    String BLE_STATE_MSG;

    public void setBleStatus(final int Code) {
        Log.d(TAG, "----- setBleStatus: " + Code);

        BluetoothUtils.BLE_STATE_CODE = Code;

        switch (Code) {
            case 0:
                break;

            case 1:


                break;

            case 2:
//                BLE_STATE_MSG = getString(R.string.ble_02);
                break;

            case 3:
//                BLE_STATE_MSG = getString(R.string.ble_03);
                break;

            case 4:
//                BLE_STATE_MSG = getString(R.string.ble_04);
                break;

            case 5:


                break;

            case 6:
//                BLE_STATE_MSG = getString(R.string.ble_06);
                break;
        }

        Log.d(TAG, "- Constants.CFG_BLE_STATUS_MSG: " + BLE_STATE_MSG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        tv_ble_status.setText(BLE_STATE_MSG);

                        switch (Code) {
                            case 0:
//                                btn_ble.setVisibility(View.GONE);
                                BLE_STATE_MSG = getString(R.string.toast_please_connect);
                                btn_ble.setText(R.string.btn_ble_connect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                break;

                            case 1:
//                                btn_ble.setVisibility(View.GONE);
                                BLE_STATE_MSG = getString(R.string.toast_please_connect);
                                btn_ble.setText(R.string.btn_ble_connect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                break;

                            case 2:
//                                btn_ble.setText(R.string.btn_ble_enable);
                                BLE_STATE_MSG = getString(R.string.toast_please_connect);
                                btn_ble.setText(R.string.btn_ble_connect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));
                                break;

                            case 3:
                                BLE_STATE_MSG = getString(R.string.toast_please_connect);
                                btn_ble.setText(R.string.btn_ble_connect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));
                                break;

                            case 4:
                                BLE_STATE_MSG = getString(R.string.toast_please_connect);
                                btn_ble.setText(R.string.btn_ble_connect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));
                                break;

                            case 5:
                                BLE_STATE_MSG = getString(R.string.ble_05);
                                btn_ble.setText(R.string.btn_ble_disconnect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));

                                break;

                            case 6:
                                BLE_STATE_MSG = getString(R.string.toast_please_connect);
                                btn_ble.setText(R.string.btn_ble_connect);
                                tv_ble_message.setText(BLE_STATE_MSG);
                                btn_ble.setBackgroundResource(R.drawable.button_background_enabled);
                                btn_ble.setTextColor(Color.parseColor("#ffffff"));

                                break;
                        }
                    }
                });
            }
        }).start();
    }

    public void getBleUsed() {
        Log.d(TAG, "----- getBleStatus");

        // 블루투스 지원 여부
        if (!Constants.CFG_IS_BLUETOOTH) {

        } else {
            int IS_ACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int IS_ACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            // 블루투스 권한 획득 여부
            if (IS_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED || IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
                setEnable();
            }
        }


    }

    public boolean isBluetooth() {

        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();

        if (ba != null) {
            Log.d(TAG, "----- isBleProgress - true");

            return true;
        } else {
            Log.d(TAG, "----- isBleProgress - false");

            return false;
        }
    }

    public void setEnable() {
        Log.d(TAG, "----- setEnable");
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "----- onActivityResult: " + resultCode);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    if (!mConnected) {
                        setBleStatus(3);
                    }
                } else {
                    setBleStatus(2);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    BleAdapter bleAdapter;

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ble_list, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel = view.findViewById(R.id.dialog_ble_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBleStatus(3);
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
        setBleStatus(4);

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

                    setBleStatus(6);
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
        setBleStatus(4);

        mScanCallback = null;
        mScanning = false;
        mHandler = null;
    }

    public void setConnect(BluetoothDevice device) {
        Log.d(TAG, "----- setConnect: " + device.getName().replace(" ", "") + " / " + device.getAddress());
        GattClientCallback gattClientCallback = new GattClientCallback();
        mBluetoothGatt = device.connectGatt(this, true, gattClientCallback);
//        mBluetoothGatt.requestMtu(60);
        Constants.DEVICE_NAME = device.getName().substring(9);
        if (Constants.DEVICE_NAME.contains("1K")){
            Constants.DEVICE_TYPE = "FIT";
        }else if (Constants.DEVICE_NAME.contains("3K")){
            Constants.DEVICE_TYPE = "MED";
        }
        Log.e(TAG, "###setConnect: divice name - " + Constants.DEVICE_NAME + "divice type - " + Constants.DEVICE_TYPE);

        final ProgressDialog progressDialog = new ProgressDialog(mContext, getLayoutInflater());
        progressDialog.show(getString(R.string.dialog_ble_connecting));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isBleProgress) {
                    if (mScanning && mBluetoothAdapter == null && !mBluetoothAdapter.isEnabled() && mBluetoothLeScanner == null) {
                        progressDialog.dismiss();
                        setScanStop();
                        isBleProgress = true;
                    }
                    Log.d(TAG, "isBleProgress: " + isBleProgress);
                    try {
                        Thread.sleep(499);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MemberSelectActivity.class);
                        startActivity(intent);

                    }
                });
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

        setBleStatus(3);
    }

    public void logout(){
        userPreference.setBoolean("auto" , false);
        userPreference.setString("autoAccount" , "");
        userPreference.setString("autoPassword" , "");
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        startActivity(intent);
        finish();
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
//            Log.d(TAG, "▶ Sent Message: " + StringUtils.byteArrayInHexFormat(messageBytes));
        } else {
//            Log.d(TAG, "▶ Sent Message: Failed to write dat.");
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
    public static boolean isInit;
    public static boolean isExerciseHeightInit;

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        byte[] bytes = characteristic.getValue();

        String CFG_RECEIVED = StringUtils.byteArrayInHexFormat2(bytes).replace("0x", "");
        // TODO: 2020-03-17 시리얼 로그
//        Log.d(TAG, "◀ CFG_RECEIVED_RAW: " + CFG_RECEIVED);

        String[] values = CFG_RECEIVED.split(" ");


        if (values[0].equals("44") && values[1].equals("3b")) {


            if (values[2].equals("82")) {

                is_82 = true;

                if (!isInit) {
                    if (preset.getSeat() == Integer.parseInt(values[3], 16) && preset.getSetup() == Integer.parseInt(values[5], 16)) {
                        isInit = true;
                    }
                }

                if (values[4].equals("00")) {
                    isSeatMove = false;
                } else if (values[4].equals("01")) {
                    isSeatMove = true;
                }

                if (values[6].equals("00")) {
                    isWeightMove = false;
                } else if (values[6].equals("01")) {
                    isWeightMove = true;
                }

                if (tabPos == 0) {
                    if (!isClick) {
                        preset.setSeat(Integer.parseInt(values[3], 16));

                    } else {
                        if (values[4].equals("00")) {
                            isClick = false;
                        }
                    }
                } else if (tabPos == 1) {


                    CFG_HEIGHT[1] = Integer.parseInt(values[8], 16);
                    CFG_WEIGHT[1] = Integer.parseInt(values[5], 16);

                    if (MaxZero) {
                        CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (CFG_WEIGHT[1] == currentWeight * 10) {
                                    CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                                    MaxZero = false;
                                }
                            }
                        },500);

                    } else {
                        if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1])
                            CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                    }

                } else if (tabPos == 2) {


                    if (isMove && currentWeight * 10 == Integer.parseInt(values[8], 16)) {
                        isMove = false;
                        Log.d(TAG, "isMove: " + isMove);
                    }

                    CFG_HEIGHT[1] = Integer.parseInt(values[8], 16);
                    CFG_WEIGHT[1] = Integer.parseInt(values[5], 16);

                    if (MaxZero) {
                        CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (CFG_WEIGHT[1] == currentWeight * 10) {
                                    CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                                    MaxZero = false;
                                }
                            }
                        },500);

                    } else {
                        if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1])
                            CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                    }

                    if (!isClick) {
//                        Preset.setup = Integer.parseInt(values[7], 16);

                    } else {
                        if (values[6].equals("00")) {
                            isClick = false;
                        }
                    }
                } else if (tabPos == 4) {
//                    Log.d(TAG, "readCharacteristic: " + isExerciseHeightInit);
                    //운동하다가 멈춘경우 다시 원점으로 돌아와야 운동 됨
                    CFG_HEIGHT[1] = Integer.parseInt(values[8], 16);
                    CFG_WEIGHT[1] = Integer.parseInt(values[5], 16);
                    if (CFG_HEIGHT[1] == 0){
                        isExerciseHeightInit = false;
                    }

                    if (isExerciseHeightInit){
                        ExerciseFragment.countTimer = 0;
                        isTimer = false;
                        isCount = false;
                        return;
                    }
                    if (preset.getMaxHeight() * preset.getHeightSetting() <= CFG_HEIGHT[1]) {
                        isTimer = true;
                    } else if (preset.getMaxHeight() * preset.getHeightSetting() >= CFG_HEIGHT[1]) {
                        ExerciseFragment.countTimer = 0;
                        if (isTimer){
                            isExerciseHeightInit = true;
                        }
                        isTimer = false;
                        isCount = false;



                    }




                    if (isMove && currentWeight * 10 == Integer.parseInt(values[8], 16)) {
                        isMove = false;
                        Log.d(TAG, "isMove: " + isMove);
                    }

                    CFG_HEIGHT[1] = Integer.parseInt(values[8], 16);
                    CFG_WEIGHT[1] = Integer.parseInt(values[5], 16);

                    if (MaxZero) {
                        CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (CFG_WEIGHT[1] == currentWeight * 10) {
                                    CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                                    MaxZero = false;
                                }
                            }
                        },500);

                    } else {
                        if (CFG_WEIGHT[1] >= CFG_WEIGHT_MAX[1])
                            CFG_WEIGHT_MAX[1] = CFG_WEIGHT[1];
                    }


//                    ExerciseFragment frag = (ExerciseFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
////                    ((ExerciseFragment) getSupportFragmentManager().findFragmentByTag("FRAG_EXERCISE")).setExercise(values);
//
//                    frag.testMethod();
//                    if (frag != null){
//                        frag.setExercise(values);
//                    }else{
//                        Log.d(TAG, "readCharacteristic: method null,,,");
//                    }


                }


                if (!isExProgress && !isMeasure) {
                    isExProgress = true;
                }
                if (!isWeiProgress && isMeasure) {
                    isWeiProgress = true;
                }
                if (isMeasure) {
                    ((MeasureActivity) MeasureActivity.mContext).setMeasure(values);
                }

            }


//            if (isWeightMove && values[2].equals("83")) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        isWeightMove = false;
//                    }
//                }).start();
//                return;
//            }

        }


//        if (isMain && !isInit && values[2].equals("84") && !isMeasure) {
////            if (Preset.setup == Integer.parseInt(CFG_RECEIVED[8], 16)) {
//            if (values[5].equals("00") && values[6].equals("00")) {
////                setMessage(StringUtils.getCommand("44 3A 01"));
//                isInit = true;
//            }
//
//        }


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
                if (device.getName().contains("SMARTNECK1") || device.getName().contains("SMARTNECK2")|| device.getName().contains("SMARTNECK3")) {

                    bleItems.add(new BleItem(device, device.getAddress(), device.getName()));
                }
            }

            for (int i = 0; i < bleItems.size(); i++) {

                if (device.getName() != null) {
                    if (device.getName().contains("SMARTNECK1") || device.getName().contains("SMARTNECK2")|| device.getName().contains("SMARTNECK3")) {
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
                setBleStatus(5);


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

    //    public static void setBrowser(String token, String url) {
//        Log.d(TAG, "----- setBrowser: " + token + " / " + url);
//
//        if (url != null) {
//            Context context = GlobalApplication.getAppContext();
//
//            Intent intent = new Intent(context, BrowserActivity.class);
//            intent.putExtra("Token", token);
//            intent.putExtra("Url", url);
//
//            context.startActivity(intent);
//        }
//    }
}
