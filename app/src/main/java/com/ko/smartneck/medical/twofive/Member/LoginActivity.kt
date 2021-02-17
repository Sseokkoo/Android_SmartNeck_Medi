package com.ko.smartneck.medical.twofive.Member

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.ko.smartneck.medical.twofive.FindAccountActivity
import com.ko.smartneck.medical.twofive.FindAccountActivity.Companion.ACCOUNT
import com.ko.smartneck.medical.twofive.FindPasswordActivity
import com.ko.smartneck.medical.twofive.GlobalApplication.userPreference
import com.ko.smartneck.medical.twofive.Main.BleConnectActivity
import com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin
import com.ko.smartneck.medical.twofive.R
import com.ko.smartneck.medical.twofive.SQ.AdminSQ
import com.ko.smartneck.medical.twofive.WebViewActivity
import com.ko.smartneck.medical.twofive.util.Address
import com.ko.smartneck.medical.twofive.util.Constants.TAG
import com.ko.smartneck.medical.twofive.util.HttpConnect
import com.ko.smartneck.medical.twofive.util.NetworkStatus
import com.ko.smartneck.medical.twofive.util.Param
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    internal var handler: Handler? = null

    companion object{
        lateinit var localAdmin: AdminSQ
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON)
        init()

    }


    private fun init() {

        handler = Handler()
        setEvent()
        cb_auto_login.isChecked = true
    }

    private fun setEvent() {

        tv_member_account.setOnClickListener {
            val intent = Intent(applicationContext , FindAccountActivity::class.java)
            startActivity(intent)
        }
        tv_member_password.setOnClickListener {
            val intent = Intent(applicationContext , FindPasswordActivity::class.java)
            startActivity(intent)
        }
        et_member_password!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (et_member_id!!.length() >= 8 && et_member_password!!.length() > 0) {

                    btn_login!!.setBackgroundColor(Color.parseColor("#cc1b17"))
                    btn_login!!.setTextColor(Color.parseColor("#ffffff"))
                } else {

                    btn_login!!.setBackgroundColor(Color.parseColor("#bdbdbd"))
                    btn_login!!.setTextColor(Color.parseColor("#ffffff"))
                }
            }
        })

        tv_member_join!!.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            intent.putExtra("type", "M")
            startActivity(intent)

        }


        tv_password_change.setOnClickListener {
            val intent = Intent(applicationContext , AdminPasswordChangeActivity::class.java)
            startActivity(intent)
        }


        btn_login!!.setOnClickListener {
            val account = et_member_id!!.text.toString().trim { it <= ' ' }
            val password = et_member_password!!.text.toString().trim { it <= ' ' }


            if (NetworkStatus.CURRENT_STATUS != NetworkStatus.TYPE_NOT_CONNECTED){

                val currentDateTime = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(currentDateTime)

                val httpConnect = HttpConnect()
                val param = Param()
                param.add("account" , account)
                param.add("password" , password)
                Thread{
                    if (httpConnect.httpConnect(param.value , Address().login , true ) == 200){
                        if (httpConnect.getReceiveMessage() == "success"){
                            val intent = Intent(applicationContext , BleConnectActivity::class.java)
                            if (userPreference.join(account , password , true , dateFormat)){
                                Log.d(TAG , "auto check ${cb_auto_login.isChecked}")

                                userPreference.setBoolean("auto" , cb_auto_login.isChecked)
                                userPreference.setString("autoAccount" , account)
                                userPreference.setString("autoPassword" , password)
                            }
                            admin = userPreference.login(account,password)

                            startActivity(intent)
                            finish()



                        }else{
                            handler!!.post{
                                localLogin(account , password)
                            }

                        }
                    }
                }.start()


            }else{
                localLogin(account , password)

            }

        }
    }

    private fun localLogin(account: String, password: String) {
        admin = userPreference.login(account , password)

        if (admin.account == ""){
            Toast.makeText(this , getString(R.string.toast_enter_check) , LENGTH_SHORT).show()

            return
        }

        val intent = Intent(applicationContext , BleConnectActivity::class.java)

            userPreference.setBoolean("auto" , cb_auto_login.isChecked)
            userPreference.setString("autoAccount" , account)
            userPreference.setString("autoPassword" , password)

        startActivity(intent)
        finish()
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
//        Log.d(TAG, "onActivityResult: ")
//        if (resultCode == Activity.RESULT_OK) {
//            Log.d(TAG, "onActivityResult: " + data!!.getStringExtra("account"))
//            et_member_id!!.setText(data.getStringExtra("account"))
//        }
//    }

    override fun onResume() {
        super.onResume()

        if (ACCOUNT != null) {
            et_member_id!!.setText(ACCOUNT)
            ACCOUNT = null
        }
    }

    override fun onStop() {
        super.onStop()
    }


}