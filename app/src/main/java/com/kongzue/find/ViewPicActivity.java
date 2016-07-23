package com.kongzue.find;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.find.util.BaseActivity;
import com.kongzue.find.util.ZoomableDraweeView;

public class ViewPicActivity extends BaseActivity{


    private ZoomableDraweeView iv;

    private void assignViews() {
        iv = (ZoomableDraweeView) findViewById(R.id.iv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pic);
        assignViews();
        init();

    }

    private void init() {

        /**
         * 设置状态栏高度分割（针对安卓4.4.4+及安卓4-状态栏是否透明的问题需要再有透明状态栏的情况下手动设置状态栏高度占位符布局，以便将内容布局向下移动）
         * 此方法在BaseActivity中
         */
        setStatusBarHeight();

        String imgUrl=getIntent().getStringExtra("url");
        Log.i("viewPic:",imgUrl);
        if (imgUrl==null || imgUrl.isEmpty()){
            finish();
            return;
        }

        iv.setImageURI(Uri.parse(imgUrl));
    }
}