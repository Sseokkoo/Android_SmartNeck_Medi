package com.smartneck.twofive.Fit.util


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.dialog_alert.*
import com.smartneck.twofive.Fit.Main.MainActivity
import com.smartneck.twofive.R
import com.smartneck.twofive.Fit.util.User.Preset
import com.smartneck.twofive.Fit.util.User.User

class DialogSeatControl(context: Context): Dialog(context) {
    private lateinit var okFunc: () -> Unit
    private lateinit var cancelFunc: () -> Unit
    private lateinit var message: String
    private var isBackPress: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_alert)
        tv_result.text = Preset.seat.toString()

        btn_finish.setOnClickListener {
            dismiss()
        }

        btn_seat_up.setOnClickListener {
            if (Preset.seat == 20){
                return@setOnClickListener
            }
            Preset.seat += 1
            MainActivity.setMessage(Commend().sendSeatMove(Preset.seat.toByte()))
            tv_result.text = Preset.seat.toString()

        }

        btn_seat_down.setOnClickListener {
            if (Preset.seat == 0){
                return@setOnClickListener
            }
            Preset.seat -= 1
            MainActivity.setMessage(Commend().sendSeatMove(Preset.seat.toByte()))
            tv_result.text = Preset.seat.toString()

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
            val param = Param()
            param.add("token", User.token)
            param.add("seat", Preset.seat)
            param.add("memberNo", User.MemberNo)
            param.add("type", "seat")
            val address = Address()
            Log.d(Constants.TAG, "param: " + param.getParam() + "  url: " + address.updatePreset)
            val httpConnect = HttpConnect()
            if (httpConnect.httpConnect(param.getParam(), address.updatePreset) == 200) {
                Log.d(Constants.TAG, "receive message: " + httpConnect.getReceiveMessage())

            }
        }).start()
    }
}