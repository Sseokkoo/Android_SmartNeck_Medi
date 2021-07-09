package com.smartneck.twofive

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.smartneck.twofive.util.Address
import com.smartneck.twofive.util.Constants
import com.smartneck.twofive.util.HttpConnect
import com.smartneck.twofive.util.Param
import kotlinx.android.synthetic.main.activity_find_account.*

class FindAccountActivity : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_account)
        init()
        setEvent()
    }

    private fun init() {
        handler = Handler()
//        if (Constants.language != "en") {
//            name3.setVisibility(View.GONE)
//            name2.setImeOptions(EditorInfo.IME_ACTION_DONE)
//        } else {
//            name3.setVisibility(View.VISIBLE)
//            name2.setImeOptions(EditorInfo.IME_ACTION_NEXT)
//        }
    }

    private fun setEvent() {
        btnBack!!.setOnClickListener { finish() }

        btnFind!!.setOnClickListener {
//            val fullName = if (Constants.language == "en") {
//
//                val firstName = name1!!.text.toString().trim { it <= ' ' }
//
//                val middleName = name2!!.text.toString().trim { it <= ' ' }
//
//                val lastName = name3!!.text.toString().trim { it <= ' ' }
//
//                "$firstName $middleName $lastName"
//
//            } else {
//
//                val firstName = name1!!.text.toString().trim { it <= ' ' }
//
//                val middleName = name2!!.text.toString().trim { it <= ' ' }
//
//                "$firstName $middleName"
//
//            }

            val name = name1.text.toString()

            val phone = phone1!!.selectedItem.toString() + phone2!!.text.toString() + phone3!!.text.toString()

            getAccount(name, phone)

        }
    }

    private fun getAccount(fullName: String, phone: String) {

        Thread(Runnable {

            val param = Param()

            param.add("name", fullName)
            param.add("phone", phone)
            param.add("type", "account")

            val address = Address()

            val httpConnect = HttpConnect()

            if (httpConnect.httpConnect(param.value, address.findAccount, true) == 200) {
                if (!httpConnect.receiveMessage.contains("fail")){

                    Log.d(Constants.TAG , "${httpConnect.receiveMessage}")
                    runOnUiThread { showSettingPopup(httpConnect.receiveMessage) }
                }else{
                    //입력값 불일치로 계정찾기 실패
                    runOnUiThread { Toast.makeText(this@FindAccountActivity, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show() }
                }

            }
        }).start()
    }

    companion object {
        var ACCOUNT: String? = null
    }

    private fun showSettingPopup(msg: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_account, null)
        val title: TextView = view.findViewById(R.id.find_account_title)
        val content: TextView = view.findViewById(R.id.find_account_contents)
        val btnOk: TextView = view.findViewById(R.id.find_account_ok)



        val alertDialog = AlertDialog.Builder(this).create()


        title.text = getString(R.string.find_account)

        content.text = if (msg == ""){
            "정보를 찾을수 없습니다."
        }else{
            msg
        }
        btnOk.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }
}