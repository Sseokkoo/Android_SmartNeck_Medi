package com.smartneck.twofive

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.smartneck.twofive.util.Address
import com.smartneck.twofive.util.HttpConnect
import com.smartneck.twofive.util.Param
import kotlinx.android.synthetic.main.activity_find_password.*


class FindPasswordActivity : AppCompatActivity() {

    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        init()
        setEvent()
    }

    private fun init() {
        handler = Handler()
    }

    private fun setEvent() {
        btnBack!!.setOnClickListener { finish() }

        btnSendTempPassword!!.setOnClickListener {

            getPassword()
        }
    }

    private fun getPassword() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("정보 확인중...")
        progressDialog.setCancelable(true)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        progressDialog.show()
        Thread(Runnable {

            var param = Param()
//            val phone = "${spn_country.selectedItem.toString()}-${find_account_phone1.selectedItem.toString()}-${find_account_phone2.text.toString()}-${find_account_phone3.text.toString()}"
            val phone = "${find_account_phone1.selectedItem.toString()}${find_account_phone2.text.toString()}${find_account_phone3.text.toString()}"
            val account = edtAccount.text.toString()
            Log.e("확인",phone);
            param.add("account", account)
            param.add("phone", phone)
            val address = Address()
            var httpConnect = HttpConnect()
            if (httpConnect.httpConnect(param.value, address.findPassword, true) == 200) {

                if (httpConnect.receiveMessage.contains("success")) {
                    httpConnect = HttpConnect()
                    param = Param()
                    param.add("account", account)
                    if (httpConnect.httpConnect(param.value, address.sendMailForPassword, true) == 200) {
//                        if (httpConnect.receiveMessage.contains("success")) {
                            handler!!.post {
                                Toast.makeText(this, getString(R.string.send_to_mail), Toast.LENGTH_SHORT).show()
                                finish()
                            }
//                        } else {
//                            runOnUiThread {
//                                Toast.makeText(this, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show()
//                            }
//
//
//                        }

                    }
                } else {
                    handler!!.post {
                        Toast.makeText(this, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show()
                    }
                }

            }
            runOnUiThread { progressDialog.dismiss() }

        }).start()
    }

//    private fun showAlertDialog(result: String) {
//        val builder = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        val view = inflater.inflate(R.layout.dialog_account, null)
//        builder.setView(view)
//        val dialog = builder.create()
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val dismiss = view.findViewById<TextView>(R.id.find_account_ok)
//        val contents = view.findViewById<TextView>(R.id.find_account_contents)
//        var sps: SpannableStringBuilder? = null
//        if (Constants.language == "ko") {
//            sps = SpannableStringBuilder("""회원님의 비밀번호는 ${result}입니다.
//
// 비밀번호가 기억나지 않는 경우에는 임시 비밀번호 발송 기능을 이해주세요.""")
//            sps.setSpan(StyleSpan(Typeface.BOLD), 11, 11 + result.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        } else if (Constants.language == "en") {
//            sps = SpannableStringBuilder("Your password is $result\n\n If you have forgotten your password, please use the temporary password sending function.")
//            sps.setSpan(StyleSpan(Typeface.BOLD), 16, 16 + result.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
//        contents.text = sps
//        dismiss.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }
}