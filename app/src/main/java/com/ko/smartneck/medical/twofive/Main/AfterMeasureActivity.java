package com.ko.smartneck.medical.twofive.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ko.smartneck.medical.twofive.R;

import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class AfterMeasureActivity extends AppCompatActivity {

    FrameLayout container;
    SeatSettingFragment fragment_seat;
    WeightSettingFragment fragment_weight;
    HeightFragment fragment_height;
    BottomNavigationView bottomNavigationView;
    String selectFrag = "SEAT";
    public static Context mContext;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_measure);
        mContext = this;
        init();


    }

    private void init(){
        container = findViewById(R.id.frame_measure_container);
        bottomNavigationView = findViewById(R.id.navigation_measure);


        selectFrag = getIntent().getStringExtra("selectFrag");

//        BottomNavigationHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // mConnected = true;

                switch (item.getItemId()) {

                    case R.id.navigation_height:
                        setBottomNavigation("HEIGHT");
                        break;


                    case R.id.navigation_seat:
                        setBottomNavigation("SEAT");
                        break;


                    case R.id.navigation_weight:
                        setBottomNavigation("WEIGHT");

//                        fragment_weight = new WeightSettingFragment();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment_weight, "weight").commit();

                        break;

                }
                return true;
            }
        });

        ImageView btn_back = findViewById(R.id.img_after_measure_back_btn);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setBottomNavigation(selectFrag);

    }

    public void setBottomNavigation(String menu) {
        Log.d(TAG, "----- setBottomNavigation: " + menu);

        switch (menu) {


            case "WEIGHT":
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                fragment_weight = new WeightSettingFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_measure_container, fragment_weight, "weight").commit();


                break;

            case "HEIGHT":
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                fragment_height = new HeightFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_measure_container, fragment_height, "height").commit();

                break;


            case "SEAT":
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                fragment_seat = new SeatSettingFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_measure_container, fragment_seat, "seat").commit();

                break;


        }
    }
}
