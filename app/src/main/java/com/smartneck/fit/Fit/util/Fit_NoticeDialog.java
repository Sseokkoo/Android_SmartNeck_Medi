package com.smartneck.fit.Fit.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smartneck.fit.R;

public class Fit_NoticeDialog {
    Context context;
    LayoutInflater layoutInflater;
    String message;
    String title;
    public Fit_NoticeDialog(Context context , LayoutInflater layoutInflater , String title , String message) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.message = message;
        this.title = title;
    }

    public void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = layoutInflater;
        View view = inflater.inflate(R.layout.dialog_notice, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel = view.findViewById(R.id.notice_cancel);
        TextView content = view.findViewById(R.id.notice_tv);
        TextView tv_title = view.findViewById(R.id.notice_list_title);
        content.setText(message);
        tv_title.setText(title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
