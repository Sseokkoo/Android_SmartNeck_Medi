package com.ko.smartneck.medical.twofive.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

import com.ko.smartneck.medical.twofive.SQ.DatabaseHelper;
import com.ko.smartneck.medical.twofive.util.User.Age;

public class Constants {
    public static final String TAG = "LOGICALART";
    public static final String CFG_DOMAIN = "http://twofivecorp.gabia.io";
    public static boolean CFG_IS_INTERNET = false;
    public static boolean CFG_IS_DEVICE = true;
    public static boolean CFG_IS_LOGIN = false;
    public static boolean CFG_IS_LOGIN_MED = false;
    public static boolean CFG_IS_BLUETOOTH = false;
    public static boolean CFG_IS_EXERCISE = false;
    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";

    public static Context context;
    public static String language = "ko";
    public static String DEVICE_NAME = "";
    public static String DEVICE_TYPE = "";
    public static final int REQUEST_ENABLE_BT = 1;
    public static final long SCAN_PERIOD = 10000;

    public static final int REQUEST_MOVE_EXERCISE_SETTING = 527;

    public static final String CFG_CHARACTERISTIC_SERVICE = "00001000-0000-1000-8000-00805F9B34FB";
    public static final UUID CFG_CHARACTERISTIC_SERVICE_UUID = UUID.fromString(CFG_CHARACTERISTIC_SERVICE);
    public static final String CFG_CHARACTERISTIC_WRITE = "00001001-0000-1000-8000-00805F9B34FB";
    public static final UUID CFG_CHARACTERISTIC_WRITE_UUID = UUID.fromString(CFG_CHARACTERISTIC_WRITE);
    public static final String CFG_CHARACTERISTIC_NOTIFICATION = "00001002-0000-1000-8000-00805F9B34FB";
    public static final UUID CFG_CHARACTERISTIC_NOTIFICATION_UUID = UUID.fromString(CFG_CHARACTERISTIC_NOTIFICATION);
    public static final String CFG_CHARACTERISTIC_DESCRIPTOR = "00002902-0000-1000-8000-00805F9B34FB";
    public static final UUID CFG_CHARACTERISTIC_DESCRIPTOR_UUID = UUID.fromString(CFG_CHARACTERISTIC_DESCRIPTOR);

    public static final String KR = "KR";
    public static final String US = "US";
    public static final String CN = "CN";
    public static String Terms1 = "";
    public static String Terms2 = "";
    public static String Terms3 = "";
    public static String Terms4 = "";


    public final static float POUND = 2.205f;
//    public final static float INCHES = 25.4f;

    public static ArrayList<Age> ageArrayListMale;
    public static ArrayList<Age> ageArrayListFemale;

    public void setAgeArray() {
        ageArrayListMale = new ArrayList<>();
        ageArrayListMale.add(new Age(8 , 0));
        ageArrayListMale.add(new Age(9 , 0));
        ageArrayListMale.add(new Age(10, 0));
        ageArrayListMale.add(new Age(11, 0));
        ageArrayListMale.add(new Age(12, 0));
        ageArrayListMale.add(new Age(13, 0));
        ageArrayListMale.add(new Age(14, 0));
        ageArrayListMale.add(new Age(15, 0));
        ageArrayListMale.add(new Age(16, 0));
        ageArrayListMale.add(new Age(17, 100));
        ageArrayListMale.add(new Age(18, 100));
        ageArrayListMale.add(new Age(19, 100));
        ageArrayListMale.add(new Age(20, 100));
        ageArrayListMale.add(new Age(21, 100));
        ageArrayListMale.add(new Age(22, 100));
        ageArrayListMale.add(new Age(23, 100));
        ageArrayListMale.add(new Age(24, 100));
        ageArrayListMale.add(new Age(25, 100));
        ageArrayListMale.add(new Age(26, 100));
        ageArrayListMale.add(new Age(27, 100));
        ageArrayListMale.add(new Age(28, 100));
        ageArrayListMale.add(new Age(29, 100));
        ageArrayListMale.add(new Age(30, 100));
        ageArrayListMale.add(new Age(31, 100));
        ageArrayListMale.add(new Age(32, 100));
        ageArrayListMale.add(new Age(33, 100));
        ageArrayListMale.add(new Age(34, 100));
        ageArrayListMale.add(new Age(35, 100));
        ageArrayListMale.add(new Age(36, 95));
        ageArrayListMale.add(new Age(37, 95));
        ageArrayListMale.add(new Age(38, 95));
        ageArrayListMale.add(new Age(39, 95));
        ageArrayListMale.add(new Age(40, 95));
        ageArrayListMale.add(new Age(41, 90));
        ageArrayListMale.add(new Age(42, 90));
        ageArrayListMale.add(new Age(43, 90));
        ageArrayListMale.add(new Age(44, 90));
        ageArrayListMale.add(new Age(45, 90));
        ageArrayListMale.add(new Age(46, 90));
        ageArrayListMale.add(new Age(47, 85));
        ageArrayListMale.add(new Age(48, 85));
        ageArrayListMale.add(new Age(49, 85));
        ageArrayListMale.add(new Age(50, 85));
        ageArrayListMale.add(new Age(51, 85));
        ageArrayListMale.add(new Age(52, 80));
        ageArrayListMale.add(new Age(53, 80));
        ageArrayListMale.add(new Age(54, 80));
        ageArrayListMale.add(new Age(55, 80));
        ageArrayListMale.add(new Age(56, 80));
        ageArrayListMale.add(new Age(57, 75));
        ageArrayListMale.add(new Age(58, 75));
        ageArrayListMale.add(new Age(59, 75));
        ageArrayListMale.add(new Age(60, 75));
        ageArrayListMale.add(new Age(61, 70));
        ageArrayListMale.add(new Age(62, 70));
        ageArrayListMale.add(new Age(63, 70));
        ageArrayListMale.add(new Age(64, 70));
        ageArrayListMale.add(new Age(65, 65));
        ageArrayListMale.add(new Age(66, 65));
        ageArrayListMale.add(new Age(67, 60));
        ageArrayListMale.add(new Age(68, 60));
        ageArrayListMale.add(new Age(69, 55));
        ageArrayListMale.add(new Age(70, 55));
        ageArrayListMale.add(new Age(71, 50));
        ageArrayListMale.add(new Age(72, 50));
        ageArrayListMale.add(new Age(73, 45));
        ageArrayListMale.add(new Age(74, 45));
        ageArrayListMale.add(new Age(75, 40));
        ageArrayListMale.add(new Age(76, 40));
        ageArrayListMale.add(new Age(77, 35));
        ageArrayListMale.add(new Age(78, 35));
        ageArrayListMale.add(new Age(79, 35));
        ageArrayListMale.add(new Age(80, 30));
        ageArrayListMale.add(new Age(81, 30));
        ageArrayListMale.add(new Age(82, 25));
        ageArrayListMale.add(new Age(83, 25));
        ageArrayListMale.add(new Age(84, 20));
        ageArrayListMale.add(new Age(85, 20));

        ageArrayListFemale = new ArrayList<>();
        ageArrayListFemale.add(new Age(8 , 0));
        ageArrayListFemale.add(new Age(9 , 0));
        ageArrayListFemale.add(new Age(10, 0));
        ageArrayListFemale.add(new Age(11, 0));
        ageArrayListFemale.add(new Age(12, 0));
        ageArrayListFemale.add(new Age(13, 0));
        ageArrayListFemale.add(new Age(14, 0));
        ageArrayListFemale.add(new Age(15, 0));
        ageArrayListFemale.add(new Age(16, 0));
        ageArrayListFemale.add(new Age(17, -15 + 100));
        ageArrayListFemale.add(new Age(18, -15 + 100));
        ageArrayListFemale.add(new Age(19, -15 + 100));
        ageArrayListFemale.add(new Age(20, -15 + 100));
        ageArrayListFemale.add(new Age(21, -15 + 100));
        ageArrayListFemale.add(new Age(22, -15 + 100));
        ageArrayListFemale.add(new Age(23, -15 + 100));
        ageArrayListFemale.add(new Age(24, -15 + 100));
        ageArrayListFemale.add(new Age(25, -15 + 100));
        ageArrayListFemale.add(new Age(26, -15 + 100));
        ageArrayListFemale.add(new Age(27, -15 + 100));
        ageArrayListFemale.add(new Age(28, -15 + 100));
        ageArrayListFemale.add(new Age(29, -15 + 100));
        ageArrayListFemale.add(new Age(30, -15 + 100));
        ageArrayListFemale.add(new Age(31, -15 + 100));
        ageArrayListFemale.add(new Age(32, -15 + 100));
        ageArrayListFemale.add(new Age(33, -15 + 100));
        ageArrayListFemale.add(new Age(34, -15 + 100));
        ageArrayListFemale.add(new Age(35, -15 + 100));
        ageArrayListFemale.add(new Age(36, -15 + 95));
        ageArrayListFemale.add(new Age(37, -15 + 95));
        ageArrayListFemale.add(new Age(38, -15 + 95));
        ageArrayListFemale.add(new Age(39, -15 + 95));
        ageArrayListFemale.add(new Age(40, -15 + 95));
        ageArrayListFemale.add(new Age(41, -15 + 90));
        ageArrayListFemale.add(new Age(42, -15 + 90));
        ageArrayListFemale.add(new Age(43, -15 + 90));
        ageArrayListFemale.add(new Age(44, -15 + 90));
        ageArrayListFemale.add(new Age(45, -15 + 90));
        ageArrayListFemale.add(new Age(46, -15 + 90));
        ageArrayListFemale.add(new Age(47, -15 + 85));
        ageArrayListFemale.add(new Age(48, -15 + 85));
        ageArrayListFemale.add(new Age(49, -15 + 85));
        ageArrayListFemale.add(new Age(50, -15 + 85));
        ageArrayListFemale.add(new Age(51, -15 + 85));
        ageArrayListFemale.add(new Age(52, -15 + 80));
        ageArrayListFemale.add(new Age(53, -15 + 80));
        ageArrayListFemale.add(new Age(54, -15 + 80));
        ageArrayListFemale.add(new Age(55, -15 + 80));
        ageArrayListFemale.add(new Age(56, -15 + 80));
        ageArrayListFemale.add(new Age(57, -15 + 75));
        ageArrayListFemale.add(new Age(58, -15 + 75));
        ageArrayListFemale.add(new Age(59, -15 + 75));
        ageArrayListFemale.add(new Age(60, -15 + 75));
        ageArrayListFemale.add(new Age(61, -15 + 70));
        ageArrayListFemale.add(new Age(62, -15 + 70));
        ageArrayListFemale.add(new Age(63, -15 + 70));
        ageArrayListFemale.add(new Age(64, -15 + 70));
        ageArrayListFemale.add(new Age(65, -15 + 65));
        ageArrayListFemale.add(new Age(66, -15 + 65));
        ageArrayListFemale.add(new Age(67, -15 + 60));
        ageArrayListFemale.add(new Age(68, -15 + 60));
        ageArrayListFemale.add(new Age(69, -15 + 55));
        ageArrayListFemale.add(new Age(70, -15 + 55));
        ageArrayListFemale.add(new Age(71, -15 + 50));
        ageArrayListFemale.add(new Age(72, -15 + 50));
        ageArrayListFemale.add(new Age(73, -15 + 45));
        ageArrayListFemale.add(new Age(74, -15 + 45));
        ageArrayListFemale.add(new Age(75, -15 + 40));
        ageArrayListFemale.add(new Age(76, -15 + 40));
        ageArrayListFemale.add(new Age(77, -15 + 35));
        ageArrayListFemale.add(new Age(78, -15 + 35));
        ageArrayListFemale.add(new Age(79, -15 + 35));
        ageArrayListFemale.add(new Age(80, -15 + 30));
        ageArrayListFemale.add(new Age(81, -15 + 30));
        ageArrayListFemale.add(new Age(82, -15 + 25));
        ageArrayListFemale.add(new Age(83, -15 + 25));
        ageArrayListFemale.add(new Age(84, -15 + 20));
        ageArrayListFemale.add(new Age(85, -15 + 20));
    }

//    public static final float male_8 =  40;
//    public static final float male_9 =  45;
//    public static final float male_10 = 50;
//    public static final float male_11 = 55;
//    public static final float male_12 = 60;
//    public static final float male_13 = 65;
//    public static final float male_14 = 70;
//    public static final float male_15 = 75;
//    public static final float male_16 = 76;
//    public static final float male_17 = 77;
//    public static final float male_18 = 78;
//    public static final float male_19 = 79;
//    public static final float male_20 = 85;
//    public static final float male_21 = 86;
//    public static final float male_22 = 87;
//    public static final float male_23 = 88;
//    public static final float male_24 = 89;
//    public static final float male_25 = 90;
//    public static final float male_26 = 89;
//    public static final float male_27 = 88;
//    public static final float male_28 = 87;
//    public static final float male_29 = 86;
//    public static final float male_30 = 85;
//    public static final float male_31 = 84;
//    public static final float male_32 = 83;
//    public static final float male_33 = 82;
//    public static final float male_34 = 81;
//    public static final float male_35 = 80;
//    public static final float male_36 = 79.5f;
//    public static final float male_37 = 79;
//    public static final float male_38 = 78.5f;
//    public static final float male_39 = 78;
//    public static final float male_40 = 77.5f;
//    public static final float male_41 = 77;
//    public static final float male_42 = 76.5f;
//    public static final float male_43 = 76;
//    public static final float male_44 = 75.5f;
//    public static final float male_45 = 75;
//    public static final float male_46 = 74.5f;
//    public static final float male_47 = 74;
//    public static final float male_48 = 73.5f;
//    public static final float male_49 = 73;
//    public static final float male_50 = 72.5f;
//    public static final float male_51 = 72;
//    public static final float male_52 = 71.5f;
//    public static final float male_53 = 71;
//    public static final float male_54 = 70.5f;
//    public static final float male_55 = 70;
//    public static final float male_56 = 69.5f;
//    public static final float male_57 = 69;
//    public static final float male_58 = 68.5f;
//    public static final float male_59 = 68;
//    public static final float male_60 = 67.5f;
//    public static final float male_61 = 67;
//    public static final float male_62 = 66.5f;
//    public static final float male_63 = 66;
//    public static final float male_64 = 65.5f;
//    public static final float male_65 = 65;
//    public static final float male_66 = 64.5f;
//    public static final float male_67 = 64;
//    public static final float male_68 = 63.5f;
//    public static final float male_69 = 63;
//    public static final float male_70 = 62;
//    public static final float male_71 = 61;
//    public static final float male_72 = 60;
//    public static final float male_73 = 59;
//    public static final float male_74 = 58;
//    public static final float male_75 = 57;
//    public static final float male_76 = 56;
//    public static final float male_77 = 55;
//    public static final float male_78 = 54;
//    public static final float male_79 = 53;
//    public static final float male_80 = 45;
//    public static final float male_81 = 44;
//    public static final float male_82 = 43;
//    public static final float male_83 = 42;
//    public static final float male_84 = 41;
//    public static final float male_85 = 40;
//
//    public static final float  female_8 = -15 + 40;
//    public static final float  female_9 = -15 + 45;
//    public static final float female_10 = -15 + 50;
//    public static final float female_11 = -15 + 55;
//    public static final float female_12 = -15 + 60;
//    public static final float female_13 = -15 + 65;
//    public static final float female_14 = -15 + 70;
//    public static final float female_15 = -15 + 75;
//    public static final float female_16 = -15 + 76;
//    public static final float female_17 = -15 + 77;
//    public static final float female_18 = -15 + 78;
//    public static final float female_19 = -15 + 79;
//    public static final float female_20 = -15 + 85;
//    public static final float female_21 = -15 + 86;
//    public static final float female_22 = -15 + 87;
//    public static final float female_23 = -15 + 88;
//    public static final float female_24 = -15 + 89;
//    public static final float female_25 = -15 + 90;
//    public static final float female_26 = -15 + 89;
//    public static final float female_27 = -15 + 88;
//    public static final float female_28 = -15 + 87;
//    public static final float female_29 = -15 + 86;
//    public static final float female_30 = -15 + 85;
//    public static final float female_31 = -15 + 84;
//    public static final float female_32 = -15 + 83;
//    public static final float female_33 = -15 + 82;
//    public static final float female_34 = -15 + 81;
//    public static final float female_35 = -15 + 80;
//    public static final float female_36 = -15 + 79.5f;
//    public static final float female_37 = -15 + 79;
//    public static final float female_38 = -15 + 78.5f;
//    public static final float female_39 = -15 + 78;
//    public static final float female_40 = -15 + 77.5f;
//    public static final float female_41 = -15 + 77;
//    public static final float female_42 = -15 + 76.5f;
//    public static final float female_43 = -15 + 76;
//    public static final float female_44 = -15 + 75.5f;
//    public static final float female_45 = -15 + 75;
//    public static final float female_46 = -15 + 74.5f;
//    public static final float female_47 = -15 + 74;
//    public static final float female_48 = -15 + 73.5f;
//    public static final float female_49 = -15 + 73;
//    public static final float female_50 = -15 + 72.5f;
//    public static final float female_51 = -15 + 72;
//    public static final float female_52 = -15 + 71.5f;
//    public static final float female_53 = -15 + 71;
//    public static final float female_54 = -15 + 70.5f;
//    public static final float female_55 = -15 + 70;
//    public static final float female_56 = -15 + 69.5f;
//    public static final float female_57 = -15 + 69;
//    public static final float female_58 = -15 + 68.5f;
//    public static final float female_59 = -15 + 68;
//    public static final float female_60 = -15 + 67.5f;
//    public static final float female_61 = -15 + 67;
//    public static final float female_62 = -15 + 66.5f;
//    public static final float female_63 = -15 + 66;
//    public static final float female_64 = -15 + 65.5f;
//    public static final float female_65 = -15 + 65;
//    public static final float female_66 = -15 + 64.5f;
//    public static final float female_67 = -15 + 64;
//    public static final float female_68 = -15 + 63.5f;
//    public static final float female_69 = -15 + 63;
//    public static final float female_70 = -15 + 62;
//    public static final float female_71 = -15 + 61;
//    public static final float female_72 = -15 + 60;
//    public static final float female_73 = -15 + 59;
//    public static final float female_74 = -15 + 58;
//    public static final float female_75 = -15 + 57;
//    public static final float female_76 = -15 + 56;
//    public static final float female_77 = -15 + 55;
//    public static final float female_78 = -15 + 54;
//    public static final float female_79 = -15 + 53;
//    public static final float female_80 = -15 + 45;
//    public static final float female_81 = -15 + 44;
//    public static final float female_82 = -15 + 43;
//    public static final float female_83 = -15 + 42;
//    public static final float female_84 = -15 + 41;
//    public static final float female_85 = -15 + 40;
}