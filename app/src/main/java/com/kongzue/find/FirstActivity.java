package com.kongzue.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kongzue.find.util.BaseActivity;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class FirstActivity extends BaseActivity {

    private ImageView imgFirstBkg;
    private ImageView imgFirstLogo;

    private void assignViews() {
        imgFirstBkg = (ImageView) findViewById(R.id.imgFirstBkg);
        imgFirstLogo = (ImageView) findViewById(R.id.imgFirstLogo);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        assignViews();
        init();
    }

    private void init() {
        //背景渐变
        AlphaAnimation am = new AlphaAnimation(0f, 1.0f);
        am.setDuration(1600);
        am.setFillAfter(true);
        imgFirstLogo.startAnimation(am);
        //延时跳转到下一个界面
        AlphaAnimation am2 = new AlphaAnimation(0.9f, 1.0f);
        am2.setDuration(100);
        imgFirstBkg.startAnimation(am2);

        am2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initSDK();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initSDK() {
        //CrashReport.initCrashReport(this, "900015045", false);
        Bmob.initialize(this, "b7730ed724833736a020a688d97f18d8");

        //初始化推送
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

        //初始化Fresco
        Fresco.initialize(this);
    }
}
