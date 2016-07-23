package com.kongzue.find.util;

import android.util.Log;

import com.kongzue.find.ChatActivity;
import com.kongzue.find.MainActivity;
import com.kongzue.find.MyApplication;

/**
 * Created by chao on 2016/3/21.
 */
public class EventPassger {

    private MainActivity mainAactivity;
    private ChatActivity chatActivity;
    private MyApplication myApplication;

    public MyApplication getMyApplication() {
        return myApplication;
    }

    public void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    //本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
    private static EventPassger eventPassger;
    private EventPassger(){}
    public static EventPassger getInstance(){
        if (eventPassger==null){
            synchronized(TestData.class) {
                if (eventPassger==null) {
                    eventPassger = new EventPassger();
                }
            }
        }
        return eventPassger;
    }

    public ChatActivity getChatActivity() {
        return chatActivity;
    }

    public void setChatActivity(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }

    public MainActivity getMainAactivity() {
        return mainAactivity;
    }

    public void setMainAactivity(MainActivity mainAactivity) {
        this.mainAactivity = mainAactivity;
    }

    public boolean isMainActivityActivited(){
        if (mainAactivity!=null){
            return true;
        }
        return false;
    }

    public boolean isChatActivityActivited(){
        if (chatActivity!=null){
            return true;
        }
        return false;
    }

    public void callMainAvtivityRefreshMessageList(){
        if (mainAactivity!=null) {
            mainAactivity.refreshMessage();
        }else{
            Log.i("EventPassger","callMainAvtivityRefreshMessageList Error:mainAactivity is null");
        }
    }

    public void callChatActivityRefreshMessageList(){
        if (chatActivity!=null) {
            chatActivity.getMessages();
        }else{
            Log.i("EventPassger","callMainAvtivityRefreshMessageList Error:chatActivity is null");
        }
    }
}
