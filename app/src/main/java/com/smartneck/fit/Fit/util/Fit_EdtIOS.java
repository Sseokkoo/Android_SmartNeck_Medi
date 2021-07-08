package com.smartneck.fit.Fit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.smartneck.fit.R;

@SuppressLint("AppCompatCustomView")
public class Fit_EdtIOS extends EditText {

    public Fit_EdtIOS(Context context) {
        super(context);

        setBackgroundResource(R.drawable.edit_text_background);

    }

    public Fit_EdtIOS(Context context, AttributeSet attrs) {

        super(context, attrs);
        setBackgroundResource(R.drawable.edit_text_background);

    }

    public Fit_EdtIOS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundResource(R.drawable.edit_text_background);

    }

    public Fit_EdtIOS(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setBackgroundResource(R.drawable.edit_text_background);

    }
}
