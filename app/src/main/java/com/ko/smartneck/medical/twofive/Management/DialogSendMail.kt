package com.ko.smartneck.medical.twofive.Management

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin
import com.ko.smartneck.medical.twofive.R
import com.ko.smartneck.medical.twofive.util.Address
import com.ko.smartneck.medical.twofive.util.Constants
import com.ko.smartneck.medical.twofive.util.HttpConnect
import com.ko.smartneck.medical.twofive.util.Param
import kotlinx.android.synthetic.main.dialog_send_mail.*

class DialogSendMail(context: Context , var json: String ): Dialog(context) {
    private lateinit var okFunc: () -> Unit
    private lateinit var cancelFunc: () -> Unit
    private lateinit var handler: Handler
    private lateinit var message: String
    private var isBackPress: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_send_mail)
        handler = Handler()
        btn_cancel.setOnClickListener {
            dismiss()
        }

       btn_send.setOnClickListener {

           Thread{
               val httpConnect = HttpConnect()
               val param = Param()
               param.add("mail" , edt_mail.text.toString())
               param.add("json" , json)
               param.add("account" , admin.account)
               Log.e(Constants.TAG, "######onCreate: ${param.value}" )

               handler.post {
                   edt_mail.setText("이메일 전송중입니다.")
                   edt_mail.setTextColor(Color.GRAY)
                   edt_mail.isEnabled = false
                   edt_mail.isClickable = false
                   btn_send.isEnabled = false
                   btn_send.isClickable = false
                   btn_cancel.isEnabled = false
                   btn_cancel.isClickable = false
               }

               if (httpConnect.httpConnect(param.value , Address().sendMailExcel , true) == 200){
                    handler.post{
                        Toast.makeText(context, "이메일 전송 완료.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
               }else{
                   handler.post{
                       Toast.makeText(context, "이메일 전송 실패.", Toast.LENGTH_SHORT).show()
                       dismiss()
                   }
               }
           }.start()

       }
    }

    fun setOkFunction(func: () -> Unit){
        okFunc = func
    }

    fun setCancelFunction(func: () -> Unit){
        cancelFunc = func
    }
}