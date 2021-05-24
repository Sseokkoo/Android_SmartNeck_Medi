package com.smartneck.twofive.Fit;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.smartneck.twofive.Fit.Main.MainActivity;
import com.smartneck.twofive.Fit.util.Address;
import com.smartneck.twofive.Fit.util.Constants;
import com.smartneck.twofive.Fit.util.HttpConnect;
import com.smartneck.twofive.Fit.util.Param;
import com.smartneck.twofive.Fit.util.User.Preset;
import com.smartneck.twofive.Fit.util.User.User;

import static com.smartneck.twofive.Fit.util.Constants.TAG;


public class IntroActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



        handler = new Handler();
        setVolume();
        getYoutubeUrl();
        autoLogin();
        getTerms();
        String locale = getResources().getConfiguration().locale.getCountry();
        String language = getResources().getConfiguration().locale.getLanguage();
        User.language = language;
        User.country = locale;
        Log.d(Constants.TAG, "local: " + locale + " / lang: " + language);
        Constants constants = new Constants();
        constants.setAgeArray();
        setVolueHeight();

    }

    void setVolueHeight(){
        Log.d(TAG, "setVolueHeight: " + Preset.getHomeHeightValue(50));
    }




    //볼륨조절
    void setVolume() {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        // audioManager.setStreamVolume(볼륨컨트롤, 뷰륨크기, 볼륨상태(audioManager.FLAG...으로 시작하는 인자들...) );
        //Stream music volume max = 15

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);

    }

    /*
     * 자동로그인
     *
     * 자동로그인한적이 있는 사용자는 토큰인증 후 자동로그인 처리
     *
     * 자동 로그인 시 MainActivity로 이동
     *
     */

    void autoLogin() {
        User.getToken(this);

        if (!User.getAutoLoginState(this)) {
            Intent intent = new Intent(getApplicationContext(), com.smartneck.twofive.Fit.LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Param param = new Param();
                param.add("token", User.token);
                Address address = new Address();
                HttpConnect httpConnect = new HttpConnect();
                Log.d(TAG, "param: " + param.getParam());
                if (httpConnect.httpConnect(param.getParam(), address.getAutoLogin()) == 200) {
                    Log.d(TAG, "receive message : " + httpConnect.getReceiveMessage());
                    if (!httpConnect.getReceiveMessage().equals("fail")) {
                        User.getUserInfoJson(getApplicationContext(), httpConnect.getReceiveMessage());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), com.smartneck.twofive.Fit.LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), com.smartneck.twofive.Fit.LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "response code : " + httpConnect.getResponseCode());
                }

            }
        }).start();
    }


    /* Youtube영상 Url 받아오기
    *
    * 실시간 수정을 위해 서버에서 받아옴
    *
    *  */
    private void getYoutubeUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpConnect httpConnect = new HttpConnect();
                Address address = new Address();
                if (httpConnect.httpConnect("", address.getYouTubeUrl()) == 200) {
                    if (!httpConnect.getReceiveMessage().equals(Constants.FAIL)) {

                        String[] urlSplit = httpConnect.getReceiveMessage().split("@");
                        Address.YOUTUBE_HOW_TO_EXERCISE = urlSplit[0];
                        Address.YOUTUBE_SUB_EXERCISE = urlSplit[1];
                        Log.d(TAG, " - - - - - - - - - - YOUTUBE URL: " + Address.YOUTUBE_HOW_TO_EXERCISE);
//                        Address.domain = urlSplit[2];
                    }
                }
            }
        }).start();
    }


    /*
    * 이용약관 받아오기
    *
    * 실시간 수정을 위해 서버에서 받아옴
    *
    * */
    private void getTerms(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpConnect httpConnect = new HttpConnect();
                Address address = new Address();
                if (httpConnect.httpConnect("" , address.getTermsOfUse()) == 200){
                    String split[] = httpConnect.getReceiveMessage().split("#");
                    if (User.language.equals("ko")){
                        Constants.Terms1 = split[0];
                        Constants.Terms2 = split[1];
                        Constants.Terms3 = split[2];
                        Constants.Terms4 = split[3];
                    }else if (User.language.equals("en")){
                        Constants.Terms1 = split[4];
                        Constants.Terms2 = split[5];
                        Constants.Terms3 = split[6];
                        Constants.Terms4 = split[7];
                    }else {
                        Constants.Terms1 = split[4];
                        Constants.Terms2 = split[5];
                        Constants.Terms3 = split[6];
                        Constants.Terms4 = split[7];
                    }

                }
            }
        }).start();
    }

}