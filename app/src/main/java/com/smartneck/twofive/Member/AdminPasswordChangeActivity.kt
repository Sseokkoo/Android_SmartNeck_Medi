package com.smartneck.twofive.Member

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.smartneck.twofive.R
import com.smartneck.twofive.util.Address
import com.smartneck.twofive.util.HttpConnect
import com.smartneck.twofive.util.Param
import kotlinx.android.synthetic.main.activity_user_info_password.*

class AdminPasswordChangeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_password)

        initListener()
    }

    fun initListener(){
        edit_pw_dismiss_btn.setOnClickListener {
            finish()
        }


        btn_save_pw.setOnClickListener{
            if (edit_account.text.toString().length == 0){
                Toast.makeText(this , getString(R.string.join_account_hint) , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (edit_current_pw.text.toString().length == 0 || edit_new_pw.text.toString().length == 0 || edit_confirm.text.toString().length == 0){
                Toast.makeText(this , getString(R.string.toast_insert_all_password) , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edit_new_pw.text.toString() != edit_confirm.text.toString()){
                Toast.makeText(this , getString(R.string.toast_discord_password) , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            val httpConnect = HttpConnect()
            val param = Param()
            param.add("account" , edit_account.text.toString())
            param.add("password" , edit_current_pw.text.toString())
            param.add("new_password" , edit_confirm.text.toString())

            Thread{

                if (httpConnect.httpConnect(param.value , Address().adminPasswordChange , true) == 200){
                    if (!httpConnect.receiveMessage.contains("fail")){
                        runOnUiThread{
                            Toast.makeText(this , getString(R.string.toast_password_changed) , Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }else{

                        runOnUiThread {
                            Toast.makeText(this , getString(R.string.toast_enter_check) , Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }.start()

        }

    }


}