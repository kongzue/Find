package com.kongzue.find.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by chao on 2016/3/15.
 */
public class UserRs extends BmobObject {
    private User userA;
    private User userB;
    private String word;

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
