package com.kongzue.find;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.find.util.BaseActivity;

public class AboutActivity extends BaseActivity {

    private LinearLayout sysStatusBar;
    private ImageView imgAboutBkg;
    private TextView txtAboutTitle;
    private ImageView imgAboutBackButton;
    private ImageView imgAboutTitleImg;
    private TextView txtAboutTip;

    private void assignViews() {
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        imgAboutBkg = (ImageView) findViewById(R.id.img_about_bkg);
        txtAboutTitle = (TextView) findViewById(R.id.txt_about_title);
        imgAboutBackButton = (ImageView) findViewById(R.id.img_about_back_button);
        imgAboutTitleImg = (ImageView) findViewById(R.id.img_about_titleImg);
        txtAboutTip = (TextView) findViewById(R.id.txt_about_tip);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        assignViews();
        init();
        setEvent();
    }

    private void setEvent() {
        imgAboutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtAboutTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.kongzue.com"));
                startActivity(intent);
            }
        });
    }

    private void init() {

        /**
         * 设置状态栏高度分割（针对安卓4.4.4+及安卓4-状态栏是否透明的问题需要再有透明状态栏的情况下手动设置状态栏高度占位符布局，以便将内容布局向下移动）
         * 此方法在BaseActivity中
         */
        setStatusBarHeight();
    }
}
