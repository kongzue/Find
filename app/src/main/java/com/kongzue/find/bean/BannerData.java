package com.kongzue.find.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ZhangChao on 2016/6/6.
 */
public class BannerData extends BmobObject {
    private String Title;
    private String Word;
    private String Url;
    private String AbstractInfo;
    private String PhotoUrl;

    public String getAbstractInfo() {
        return AbstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        AbstractInfo = abstractInfo;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }
}
