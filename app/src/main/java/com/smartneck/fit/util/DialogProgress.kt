package com.smartneck.fit.util


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.smartneck.fit.R
import kotlinx.android.synthetic.main.dialog_progress.*

class DialogProgress(context: Context): Dialog(context) {
    private lateinit var message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        tv_content.text = message


    }



    fun setMessage(message: String){
        this.message = message
    }
}