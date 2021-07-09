package com.smartneck.twofive

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.smartneck.twofive.Member.MemberSelectActivity.admin
import com.smartneck.twofive.util.Constants
import kotlinx.android.synthetic.main.activity_app_version.*

class AppVersionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_version)
        tv_app_version.text = "ver : " + getVersionInfo(this)

        tv_create_date.text = "${R.string.sign} : ${admin.createDate}"
        tv_device_name.text = "${R.string.prduct} : ${Constants.DEVICE_NAME}"

        val btn_app_version_dismiss = findViewById<ImageView>(R.id.btn_app_version_dismiss)
        btn_app_version_dismiss.setOnClickListener { finish() }
    }

    fun getVersionInfo(context: Context): String? {
        var version: String? = null
        try {
            val i = context.packageManager.getPackageInfo(context.packageName, 0)
            version = i.versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return version
    }
}