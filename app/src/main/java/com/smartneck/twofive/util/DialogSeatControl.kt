package com.smartneck.twofive.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.smartneck.twofive.GlobalApplication
import com.smartneck.twofive.Main.BleConnectActivity
import com.smartneck.twofive.Main.MainActivity.preset
import com.smartneck.twofive.R
import kotlinx.android.synthetic.main.dialog_alert.*

class DialogSeatControl(context: Context): Dialog(context) {
    private lateinit var okFunc: () -> Unit
    private lateinit var cancelFunc: () -> Unit
    private lateinit var message: String
    private var isBackPress: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_alert)
        tv_result.text = preset.seat.toString()

        btn_finish.setOnClickListener {
            dismiss()
        }

        btn_seat_up.setOnClickListener {
            if (preset.seat == 20){
                return@setOnClickListener
            }
            preset.seat += 1
            BleConnectActivity.setMessage(Commend().sendSeatMove(preset.seat.toByte()))
            tv_result.text = preset.seat.toString()

        }

        btn_seat_down.setOnClickListener {
            if (preset.seat == 0){
                return@setOnClickListener
            }
            preset.seat -= 1
            BleConnectActivity.setMessage(Commend().sendSeatMove(preset.seat.toByte()))
            tv_result.text = preset.seat.toString()

        }

        setOnDismissListener {
            GlobalApplication.userPreference.editPreset(preset)
        }
    }

    fun setOkFunction(func: () -> Unit){
        okFunc = func
    }

    fun setCancelFunction(func: () -> Unit){
        cancelFunc = func
    }
}