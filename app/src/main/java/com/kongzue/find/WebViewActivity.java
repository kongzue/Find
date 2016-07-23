package com.kongzue.find;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kongzue.find.util.BaseActivity;

public class WebViewActivity extends BaseActivity {
    private LinearLayout sysStatusBar;
    private WebView webView;
    private ImageView imgAboutBackButton;

    private void assignViews() {
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        webView = (WebView) findViewById(R.id.webView);
        imgAboutBackButton = (ImageView) findViewById(R.id.img_about_back_button);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        assignViews();
        setStatusBarHeight();

        String url= getIntent().getStringExtra("url");

        if (url==null || url.isEmpty()){
            finish();
            return;
        }

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl(url);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());

        imgAboutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
