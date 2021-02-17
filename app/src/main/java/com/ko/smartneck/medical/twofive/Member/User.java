package com.ko.smartneck.medical.twofive.Member;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class User {


    private int memberNo;
    private String name;
    private String birth;
    private String gender;
    private String phone;
    private int age;
    private String lately;

    public User(int memberNo, String name, String birth, String gender, String phone, int age, String lately) {
        this.memberNo = memberNo;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
        this.lately = lately;
    }

    public User() {
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLately() {
        return lately;
    }

    public void setLately(String lately) {
        this.lately = lately;
    }

    //    public static void setToken(Context context){
//
//        SharedPreferences preferences = context.getSharedPreferences("member" , Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("token" , token);
//        editor.apply();
//    }
//
//    public static void getToken(Context context){
//        SharedPreferences preferences = context.getSharedPreferences("member" , Context.MODE_PRIVATE);
//        token = preferences.getString("token" , "non");
//    }

    public ArrayList<User> getUserInfoJson(String JsonString) {
        ArrayList<User> users = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(JsonString);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                memberNo = jsonObject.getInt("MemberNo");
                name = jsonObject.getString("Name");
                birth = jsonObject.getString("Birth");
                gender = jsonObject.getString("Gender");
                phone = jsonObject.getString("Phone");
                lately = jsonObject.getString("lately");

                Log.d(TAG, "getUserInfoJson: memberNo -> " + memberNo);
                Log.d(TAG, "getUserInfoJson: Name -> " + name);
                Log.d(TAG, "getUserInfoJson: Birth -> " + birth);
                Log.d(TAG, "getUserInfoJson: Gender -> " + gender);
                Log.d(TAG, "getUserInfoJson: Phone -> " + phone);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String getYear = sdf.format(date);
                int currentYear = Integer.parseInt(getYear);
                String[] split = birth.split("-");
                age = currentYear - Integer.parseInt(split[0]) + 1;
                Log.d(TAG, "getUserInfoJson: Age -> " + age);


                String[] latelySplit = lately.split(" ");

                users.add(new User(memberNo , name , birth , gender , phone , age, latelySplit[0]));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }




}
