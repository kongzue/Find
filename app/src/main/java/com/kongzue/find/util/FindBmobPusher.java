package com.kongzue.find.util;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by chao on 2016/3/17.
 */
public class FindBmobPusher extends BmobInstallation {
    /**
     * 用户id-这样可以将设备与用户之间进行绑定
     */
    private String uid;

    public FindBmobPusher(Context context) {
        super(context);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
