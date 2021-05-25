package com.smartneck.twofive

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.smartneck.twofive.Fit.Fit_LoginActivity
import com.smartneck.twofive.Fit.Main.Fit_MainActivity
import com.smartneck.twofive.Fit.util.Fit_Address
import com.smartneck.twofive.Fit.util.Fit_Constants
import com.smartneck.twofive.Fit.util.Fit_HttpConnect
import com.smartneck.twofive.Fit.util.Fit_Param
import com.smartneck.twofive.Fit.util.User.Fit_Preset
import com.smartneck.twofive.Fit.util.User.Fit_User
import com.smartneck.twofive.SQ.DBHelper
import com.smartneck.twofive.util.Address
import com.smartneck.twofive.util.Constants
import com.smartneck.twofive.util.Constants.TAG
import com.smartneck.twofive.util.HttpConnect
import com.smartneck.twofive.util.NetworkStatus

class IntroActivity : AppCompatActivity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        Constants.context = applicationContext
        GlobalApplication.helper = DBHelper(Constants.context)
        NetworkStatus.CURRENT_STATUS = NetworkStatus.getConnectivityStatus(this)
        handler = Handler()
        setVolume()
        //        getYoutubeUrl();
        terms
        //        String locale2 = getResources().getConfiguration().locale.getDisplayCountry();
        val locale = resources.configuration.locale.country
        val language = resources.configuration.locale.language
        Constants.language = language
        //        User.country = locale;
        Log.d(TAG, "local: $locale / lang: $language")
        val constants = Constants()
        constants.setAgeArray()

//        autoLogin() 메디로 가는 오토 로그인 잠금

        val intent = Intent(this, SelectType::class.java)
        startActivity(intent)
        finish()
    }

    //볼륨조절
    fun setVolume() {
        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // audioManager.setStreamVolume(볼륨컨트롤, 뷰륨크기, 볼륨상태(audioManager.FLAG...으로 시작하는 인자들...) );
        //Stream music volume max = 15
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 4, 0)
        }
    }

//    fun autoLogin() {
//
//        val account = userPreference.getString("autoAccount" , "");
//        val password = userPreference.getString("autoPassword" , "")
//        val auto = userPreference.getBoolean("auto" , false)
//        Log.d(TAG , "auto login run\naccount -> $account\npassword -> $password\nauto -> $auto")
//
//        if (!auto){
//
//            val intent = Intent(applicationContext , LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }
//
//
//        if (NetworkStatus.CURRENT_STATUS != NetworkStatus.TYPE_NOT_CONNECTED){
//            Log.d(TAG , "NetWork State > Connected")
//
//            Thread{
//                val httpConnect = HttpConnect()
//                val param = Param()
//                param.add("account" , account)
//                param.add("password" , password)
//                if (httpConnect.httpConnect(param.value , Address().login , true ) == 200){
//                    if (httpConnect.getReceiveMessage() == "success"){
//                        val intent = Intent(applicationContext , BleConnectActivity::class.java)
//                        admin = userPreference.login(account,password)
//                        startActivity(intent)
//                        finish()
//                    }else{
//                        val intent = Intent(applicationContext , LoginActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//            }.start()
//
//
//        }else{
//            Log.d(TAG , "NetWork State > Not Connected")
//
//            admin = userPreference.login(account , password)
//
//            if (admin.account.length == 0){
//                val intent = Intent(applicationContext , LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//                return
//            }
//            val intent = Intent(applicationContext , BleConnectActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//    }

    private val terms: Unit
        private get() {
            Thread(Runnable {
                val httpConnect = HttpConnect()
                val address = Address()
                if (httpConnect.httpConnect("", address.termsOfUse, false) == 200) {
                    val split = httpConnect.getReceiveMessage().split("#").toTypedArray()
                    if (Constants.language == "ko") {
                        Constants.Terms1 = split[0]
                        Constants.Terms2 = split[1]
                        Constants.Terms3 = split[2]
                        Constants.Terms4 = split[3]
                    } else if (Constants.language == "en") {
                        Constants.Terms1 = split[4]
                        Constants.Terms2 = split[5]
                        Constants.Terms3 = split[6]
                        Constants.Terms4 = split[7]
                    } else {
                        Constants.Terms1 = split[4]
                        Constants.Terms2 = split[5]
                        Constants.Terms3 = split[6]
                        Constants.Terms4 = split[7]
                    }
                }
            }).start()
        }
}