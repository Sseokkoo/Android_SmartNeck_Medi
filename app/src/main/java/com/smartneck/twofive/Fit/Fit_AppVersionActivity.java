package com.smartneck.twofive.Fit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.twofive.Fit.util.User.Fit_Preset;
import com.smartneck.twofive.R;

public class Fit_AppVersionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_version);
        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        TextView version = findViewById(R.id.tv_app_version);
        TextView tv_create_date = findViewById(R.id.tv_create_date);
        TextView tv_device_name = findViewById(R.id.tv_device_name);
        version.setText("App Version: " + getVersionInfo(this));

        tv_create_date.setText(getResources().getString(R.string.login_id) + pref.getString("id",""));
        tv_device_name.setText(getResources().getString(R.string.prduct)+" : "+ Fit_Preset.DEVICE_NAME);

        ImageView btn_app_version_dismiss = findViewById(R.id.btn_app_version_dismiss);
        btn_app_version_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getVersionInfo(Context context){
        String version = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch(PackageManager.NameNotFoundException e) { }
        return version;
    }


}
