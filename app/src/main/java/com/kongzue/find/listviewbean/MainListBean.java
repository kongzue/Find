package com.kongzue.find.listviewbean;

/**
 * Created by chao on 2016/3/4.
 * MainActivity界面中所有listview中item的JavaBean数据源
 */
public class MainListBean {
    //类型标记
    private int type;                   //列表item样式，0：标题文字（已废弃）；1：兴趣词；2：用户消息；3：小型兴趣词布局
    //标题文字
    private String title_text;
    private int title_color;
    //兴趣词条
    private String word_text;
    private String word_tip;
    //用户消息
    private String msg_userNick;
    private String msg_userName;
    private String msg_message;
    private String msg_word;
    private String msg_userFace;
    private String msg_time;
    private boolean msg_isNew;

    public MainListBean(){

    }

    //快速创建数据源
    //type=0(已作废)
    public MainListBean(int type, String title_text, int title_color){
        this.type=type;
        this.title_text=title_text;
        this.title_color=title_color;
    }

    //type=1
    public MainListBean(int type,String word_text,String word_tip){
        this.type=type;
        this.word_text=word_text;
        this.word_tip=word_tip;
    }

    //type=2
    public MainListBean(int type,String msg_userName,String msg_userNick,String msg_message,String msg_word,String msg_userFace,String msg_time,boolean msg_isNew){
        this.type=type;
        this.msg_userName=msg_userName;
        this.msg_userNick=msg_userNick;
        this.msg_message=msg_message;
        this.msg_word=msg_word;
        this.msg_userFace=msg_userFace;
        this.msg_time=msg_time;
        this.msg_isNew=msg_isNew;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle_text() {
        return title_text;
    }

    public void setTitle_text(String title_text) {
        this.title_text = title_text;
    }

    public int getTitle_color() {
        return title_color;
    }

    public void setTitle_color(int title_color) {
        this.title_color = title_color;
    }

    public String getWord_text() {
        return word_text;
    }

    public void setWord_text(String word_text) {
        this.word_text = word_text;
    }

    public String getWord_tip() {
        return word_tip;
    }

    public void setWord_tip(String word_tip) {
        this.word_tip = word_tip;
    }

    public String getMsg_userName() {
        return msg_userName;
    }

    public void setMsg_userName(String msg_userName) {
        this.msg_userName = msg_userName;
    }

    public String getMsg_userNick() {
        return msg_userNick;
    }

    public void setMsg_userNick(String msg_userNick) {
        this.msg_userNick = msg_userNick;
    }

    public String getMsg_message() {
        return msg_message;
    }

    public void setMsg_message(String msg_message) {
        this.msg_message = msg_message;
    }

    public String getMsg_word() {
        return msg_word;
    }

    public void setMsg_word(String msg_word) {
        this.msg_word = msg_word;
    }

    public String getMsg_userFace() {
        return msg_userFace;
    }

    public void setMsg_userFace(String msg_userFace) {
        this.msg_userFace = msg_userFace;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public boolean isMsg_isNew() {
        return msg_isNew;
    }

    public void setMsg_isNew(boolean msg_isNew) {
        this.msg_isNew = msg_isNew;
    }
}
