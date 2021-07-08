package com.smartneck.fit.Fit.util


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.dialog_alert.*
import com.smartneck.fit.Fit.Main.Fit_MainActivity
import com.smartneck.fit.R
import com.smartneck.fit.Fit.util.User.Fit_Preset
import com.smartneck.fit.Fit.util.User.Fit_User

class Fit_DialogSeatControl(context: Context): Dialog(context) {
    private lateinit var okFunc: () -> Unit
    private lateinit var cancelFunc: () -> Unit
    private lateinit var message: String
    private var isBackPress: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_alert)
        tv_result.text = Fit_Preset.seat.toString()

        btn_finish.setOnClickListener {
            dismiss()
        }

        btn_seat_up.setOnClickListener {
            if (Fit_Preset.seat == 20){
                return@setOnClickListener
            }
            Fit_Preset.seat += 1
            Fit_MainActivity.setMessage(Fit_Commend().sendSeatMove(Fit_Preset.seat.toByte()))
            tv_result.text = Fit_Preset.seat.toString()

        }

        btn_seat_down.setOnClickListener {
            if (Fit_Preset.seat == 0){
                return@setOnClickListener
            }
            Fit_Preset.seat -= 1
            Fit_MainActivity.setMessage(Fit_Commend().sendSeatMove(Fit_Preset.seat.toByte()))
            tv_result.text = Fit_Preset.seat.toString()

        }

        setOnDismissListener {
            updateSeat();
//            GlobalApplication.userPreference.editPreset(preset)
        }
    }

    fun setOkFunction(func: () -> Unit){
        okFunc = func
    }

    fun setCancelFunction(func: () -> Unit){
        cancelFunc = func
    }

    fun updateSeat() {
        Thread(Runnable {
            val param = Fit_Param()
            param.add("token", Fit_User.token)
            param.add("seat", Fit_Preset.seat)
            param.add("memberNo", Fit_User.MemberNo)
            param.add("type", "seat")
            val address = Fit_Address()
            Log.d(Fit_Constants.TAG, "param: " + param.getParam() + "  url: " + address.updatePreset)
            val httpConnect = Fit_HttpConnect()
            if (httpConnect.httpConnect(param.getParam(), address.updatePreset) == 200) {
                Log.d(Fit_Constants.TAG, "receive message: " + httpConnect.getReceiveMessage())

            }
        }).start()
    }
}