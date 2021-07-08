package com.smartneck.fit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.smartneck.fit.R;

@SuppressLint("AppCompatCustomView")
public class EdtIOS extends EditText {

    public EdtIOS(Context context) {
        super(context);

        setBackgroundResource(R.drawable.edit_text_background);

    }

    public EdtIOS(Context context, AttributeSet attrs) {

        super(context, attrs);
        setBackgroundResource(R.drawable.edit_text_background);

    }

    public EdtIOS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundResource(R.drawable.edit_text_background);

    }

    public EdtIOS(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setBackgroundResource(R.drawable.edit_text_background);

    }
}
