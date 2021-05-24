package com.smartneck.twofive.Fit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartneck.twofive.Fit.util.Constants;

public class BrowserActivity extends AppCompatActivity {
    static WebView mWebView;
    static WebView sWebView;
    public static boolean isBrowser;
    TextView tv_location;
    Handler handler;
    ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        isBrowser = true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(Color.parseColor("#cc1b17"));


        handler = new Handler();
        // 브라우저 네비게이션

        btn_back = findViewById(R.id.browser_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_browser_home = (ImageButton) findViewById (R.id.btn_browser_home);
        btn_browser_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();



            }
        });

        ImageButton btn_browser_prev = (ImageButton) findViewById (R.id.btn_browser_prev);


        ImageButton btn_browser_reload = (ImageButton) findViewById (R.id.btn_browser_reload);
        btn_browser_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });

        // 브라우저
        mWebView = (WebView) findViewById(R.id.webView);
        sWebView = mWebView;
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClientClass());
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        // 이동
        Intent intent = getIntent();
        String Token = intent.getExtras().getString("Token");
        String Url = intent.getExtras().getString("Url");

        setUrl(Token, Url);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            tv_location = (TextView) findViewById(R.id.tv_location);
            tv_location.setText(mWebView.getUrl().replace("http://",""));
            return true;
        }
    }

    public void setUrl(String Token, String Url) {
        Log.d (Constants.TAG,"----- setUrl: " + Token + " / " + Url);

        if (Token == null) {
            tv_location = (TextView) findViewById(R.id.tv_location);
            tv_location.setText(Constants.CFG_DOMAIN.replace("http://","") + Url);

            mWebView.loadUrl(Constants.CFG_DOMAIN + Url);
        } else {
            tv_location = (TextView) findViewById(R.id.tv_location);
            tv_location.setText(Constants.CFG_DOMAIN.replace("http://","") + Url);

            mWebView.loadUrl(Constants.CFG_DOMAIN + "/redirect.aspx?MemberToken=" + Token + "&ReturnUrl=" + Url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}