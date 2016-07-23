package com.kongzue.find.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by chao on 2016/3/12.
 */
public class Words extends BmobObject {
    private String wordName;            //词名
    private Boolean wordCanUse;         //可用性
    private User createUser;            //创建人
    private Integer wordTimes;          //搜索次数
    private User matchingUser;          //正在查找的人

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public Boolean getWordCanUse() {
        return wordCanUse;
    }

    public void setWordCanUse(Boolean wordCanUse) {
        this.wordCanUse = wordCanUse;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Integer getWordTimes() {
        return wordTimes;
    }

    public void setWordTimes(Integer wordTimes) {
        this.wordTimes = wordTimes;
    }

    public User getMatchingUser() {
        return matchingUser;
    }

    public void setMatchingUser(User matchingUser) {
        this.matchingUser = matchingUser;
    }
}
