package com.kongzue.find.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by chao on 2016/3/11.
 */
public class User extends BmobUser {

    private String userFace;

    private String userNick;

    private Boolean userCanUse;

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Boolean getUserCanUse() {
        return userCanUse;
    }

    public void setUserCanUse(Boolean userCanUse) {
        this.userCanUse = userCanUse;
    }
}
