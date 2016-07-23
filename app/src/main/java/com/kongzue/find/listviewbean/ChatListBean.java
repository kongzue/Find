package com.kongzue.find.listviewbean;

import java.util.Date;

/**
 * Created by chao on 2016/3/6.
 * Chat聊天界面中所有Listview信息的数据源
 */
public class ChatListBean {
    //类型标记
    private int type;               //1：左消息（别人发的）；2：右消息；3：系统消息；4：顶部消息
    //左消息
    private String leftMsgUserFace;
    private String leftMsgUserNick;
    private String leftMsgUserName;
    private String leftMsgMesage;
    //右消息
    private String rightMsgMessage;
    //系统消息
    private String systemInfoMsg;
    //顶部类型
    private String favWord;

    public String getFavWord() {
        return favWord;
    }

    public void setFavWord(String favWord) {
        this.favWord = favWord;
    }

    private Date msgTime;

    public ChatListBean(){

    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    //快速创建左消息
    public ChatListBean(int type,String leftMsgUserFace,String leftMsgUserNick,String leftMsgUserName,String leftMsgMesage){
        this.type=type;
        this.leftMsgUserFace=leftMsgUserFace;
        this.leftMsgUserNick=leftMsgUserNick;
        this.leftMsgUserName=leftMsgUserName;
        this.leftMsgMesage=leftMsgMesage;

        msgTime=new Date();
    }

    //快速创建右消息
    public ChatListBean(int type,String rightMsgMessage){
        this.type=type;
        this.rightMsgMessage=rightMsgMessage;

        msgTime=new Date();
    }

    //快速创建顶部消息
    public ChatListBean(int type,String nickName,String photoUrl,String favWord){
        this.type=4;
        this.leftMsgUserNick=nickName;
        this.leftMsgUserFace=photoUrl;
        this.favWord=favWord;
    }

    //快速创建系统提示消息
    public ChatListBean(int type,String systemInfoMsg,int systemInfoId){
        this.type=type;
        this.systemInfoMsg=systemInfoMsg;

        msgTime=new Date();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLeftMsgUserFace() {
        return leftMsgUserFace;
    }

    public void setLeftMsgUserFace(String leftMsgUserFace) {
        this.leftMsgUserFace = leftMsgUserFace;
    }

    public String getLeftMsgUserNick() {
        return leftMsgUserNick;
    }

    public void setLeftMsgUserNick(String leftMsgUserNick) {
        this.leftMsgUserNick = leftMsgUserNick;
    }

    public String getLeftMsgUserName() {
        return leftMsgUserName;
    }

    public void setLeftMsgUserName(String leftMsgUserName) {
        this.leftMsgUserName = leftMsgUserName;
    }

    public String getLeftMsgMesage() {
        return leftMsgMesage;
    }

    public void setLeftMsgMesage(String leftMsgMesage) {
        this.leftMsgMesage = leftMsgMesage;
    }

    public String getRightMsgMessage() {
        return rightMsgMessage;
    }

    public void setRightMsgMessage(String rightMsgMessage) {
        this.rightMsgMessage = rightMsgMessage;
    }

    public String getSystemInfoMsg() {
        return systemInfoMsg;
    }

    public void setSystemInfoMsg(String systemInfoMsg) {
        this.systemInfoMsg = systemInfoMsg;
    }
}
