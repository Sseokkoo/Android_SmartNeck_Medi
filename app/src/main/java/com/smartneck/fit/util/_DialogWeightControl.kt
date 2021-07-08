//package com.smartneck.twofive.util
//
//import android.app.Dialog
//import android.content.Context
//import android.os.Bundle
//import com.smartneck.twofive.R
//import kotlinx.android.synthetic.main.dialog_alert.*
//
//
//class ActivityDialogAlert(context: Context) : Dialog(context) {
//
//    private lateinit var okFunc: () -> Unit
//    private lateinit var cancelFunc: () -> Unit
//    private lateinit var message: String
//    private var isBackPress: Boolean = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dialog_alert)
//
//        btn_finish.setOnClickListener {
//            dismiss()
//        }
//    }
//
//    fun setOkFunction(func: () -> Unit){
//        okFunc = func
//    }
//
//    fun setCancelFunction(func: () -> Unit){
//        cancelFunc = func
//    }
//}