package com.smartneck.twofive.Fit.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smartneck.twofive.R;

public class ProgressDialog {
    Context activityContext;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Handler handler;

    public ProgressDialog(Context activityContext, LayoutInflater inflater) {
        this.activityContext = activityContext;
        this.inflater = inflater;
    }

    public void show(String message) {
        handler = new Handler();

        builder = new AlertDialog.Builder(activityContext);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        TextView tv_message = view.findViewById(R.id.progress_dialog_text);
        tv_message.setText(message);
        final TextView tv_close = view.findViewById(R.id.progress_dialog_close);
        tv_close.setVisibility(View.GONE);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;

            }
        });


        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_close.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();
    }

    public void dismiss() {
        dialog.dismiss();
    }


}
