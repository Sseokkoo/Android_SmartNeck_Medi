package com.smartneck.fit.YouTube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smartneck.fit.Main.BleConnectActivity;
import com.smartneck.fit.R;
import com.smartneck.fit.util.Commend;

public class ProtocolTestActivity extends AppCompatActivity {
    EditText edt_saet, edt_Weight;
    Button btn_go_exercise, btn_seat_up, btn_seat_down, btn_weight_up, btn_weight_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_test);

        init();
    }

    private void init(){
        edt_saet = findViewById(R.id.edt_go_exercise_seat);
        edt_Weight = findViewById(R.id.edt_go_exercise_weight);

        btn_go_exercise = findViewById(R.id.btn_go_exercise);
        btn_seat_up = findViewById(R.id.btn_seat_up_test);
        btn_seat_down = findViewById(R.id.btn_seat_down_test);
        btn_weight_up = findViewById(R.id.btn_weight_up_test);
        btn_weight_down = findViewById(R.id.btn_weight_down_test);
        setlistener();
    }

    private void setlistener(){
        btn_seat_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleConnectActivity.setMessage(new Commend().sendGoExercise((byte)2 , (byte)20));

            }
        });

        btn_seat_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleConnectActivity.setMessage(new Commend().sendGoExercise((byte)4 , (byte)40));

            }
        });

        btn_weight_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleConnectActivity.setMessage(new Commend().sendWeightMove((byte)20));

            }
        });
        btn_weight_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleConnectActivity.setMessage(new Commend().sendWeightMove((byte)40));

            }
        });
    }
}
