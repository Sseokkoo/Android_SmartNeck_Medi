package com.smartneck.fit.Fit;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.smartneck.fit.Fit.Main.Fit_MainActivity;
import com.smartneck.fit.Fit.util.Fit_Address;
import com.smartneck.fit.Fit.util.Fit_Constants;
import com.smartneck.fit.Fit.util.Fit_HttpConnect;
import com.smartneck.fit.Fit.util.Fit_Param;
import com.smartneck.fit.Fit.util.User.Fit_User;
import com.smartneck.fit.R;
import com.smartneck.fit.util.Constants;


public class Fit_IntroActivity extends AppCompatActivity {

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
        Fit_User.language = language;
        Fit_User.country = locale;

        Fit_Constants constants = new Fit_Constants();
        constants.setAgeArray();
    }

    //볼륨조절
    void setVolume() {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        // audioManager.setStreamVolume(볼륨컨트롤, 뷰륨크기, 볼륨상태(audioManager.FLAG...으로 시작하는 인자들...) );
        //Stream music volume max = 15
        if (Build.VERSION_CODES.Q >= Build.VERSION.SDK_INT) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
        }else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
        }

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
        Fit_User.getToken(this);

        if (!Fit_User.getAutoLoginState(this)) {
            Intent intent = new Intent(getApplicationContext(), Fit_LoginActivity.class);
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

                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                Fit_Address address = new Fit_Address();
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam(), address.getAutoLogin()) == 200) {

                    if (!httpConnect.getReceiveMessage().equals("fail")) {
                        Fit_User.getUserInfoJson(getApplicationContext(), httpConnect.getReceiveMessage());
                        Intent intent = new Intent(getApplicationContext(), Fit_MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Fit_LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Fit_LoginActivity.class);
                    startActivity(intent);
                    finish();
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
                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                Fit_Address address = new Fit_Address();
                if (httpConnect.httpConnect("", address.getYouTubeUrl()) == 200) {
                    if (!httpConnect.getReceiveMessage().equals(Constants.FAIL)) {

                        String[] urlSplit = httpConnect.getReceiveMessage().split("@");
                        Fit_Address.YOUTUBE_HOW_TO_EXERCISE = urlSplit[0];
                        Fit_Address.YOUTUBE_SUB_EXERCISE = urlSplit[1];
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
                Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                Fit_Address address = new Fit_Address();
                if (httpConnect.httpConnect("" , address.getTermsOfUse()) == 200){
                    String split[] = httpConnect.getReceiveMessage().split("#");
                    if (Fit_User.language.equals("ko")){
                        Constants.Terms1 = split[0];
                        Constants.Terms2 = split[1];
                        Constants.Terms3 = split[2];
                        Constants.Terms4 = split[3];
                    }else if (Fit_User.language.equals("en")){
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