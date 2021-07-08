package com.smartneck.fit.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class StringUtils {

    private static final String TAG = "StringUtils";

    private static String byteToHex(byte b) {
        char char1 = Character.forDigit((b & 0xF0) >> 4, 16);
        char char2 = Character.forDigit((b & 0x0F), 16);

        return String.format("0x%1$s%2$s", char1, char2);
    }

    public static String byteArrayInHexFormat2(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if (i > 0) {
                stringBuilder.append(" ");
            }
            String hexString = byteToHex(byteArray[i]);
            stringBuilder.append(hexString);
        }

        return stringBuilder.toString();
    }

    public static String byteArrayInHexFormat(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");
        for (int i = 0; i < byteArray.length; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            String hexString = byteToHex(byteArray[i]);
            stringBuilder.append(hexString);
        }
        stringBuilder.append(" }");

        return stringBuilder.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] bytesFromString(String string) {
        byte[] stringBytes = new byte[0];
        try {
            stringBytes = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Failed to convert message string to byte array");
        }

        return stringBytes;
    }

    @Nullable
    public static String stringFromBytes(byte[] bytes) {
        String byteString = null;
        try {
            byteString = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unable to convert message bytes to string");
        }
        return byteString;
    }

    public static String getCheckSumHex(String str) {
        int xor = 0;
        String[] arr = str.split(" ");

        for (int i = 0; i < arr.length; i++)
            xor ^= Integer.parseInt(arr[i], 16);

        String result = Integer.toHexString(xor);

        return result.toUpperCase();
    }

    public static String getCommand(String str)
    {
        String result = str;

        int xor = 0;
        String[] arr = str.split(" ");

        for (int i = 0; i < arr.length; i++)
            xor ^= Integer.parseInt(arr[i], 16);
        String xor2 = Integer.toHexString(xor);
        if (xor2.length() == 1){
            xor2 = "0" + xor2;
        }

        result += " " + xor2;
//        Log.d(TAG, "getCommand: " + result.toUpperCase());

        return result.toUpperCase();
    }

    public static String getHexStringCode(int val)
    {
        String result = Integer.toString(val, 16);

        if (result.length() == 1) {
            result = "0" + result;
        }

        return result.toUpperCase();
    }
}