package com.kongzue.find;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kongzue.find.util.EventPassger;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

//import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by chao on 2016/3/11.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {

        EventPassger.getInstance().setMyApplication(this);



        super.onCreate();
    }
}
