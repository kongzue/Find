package com.kongzue.find.util;

import android.content.Context;

import com.kongzue.find.listviewbean.ChatListBean;
import com.kongzue.find.listviewbean.MainListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chao on 2016/3/4.
 */
public class TestData {

    //本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
    private static TestData mTestData;
    private TestData(){}
    public static TestData getInstance(){
        if (mTestData==null){
            synchronized(TestData.class) {
                if (mTestData==null) {
                    mTestData = new TestData();
                }
            }
        }
        return mTestData;
    }

    //兴趣页面的list测试用数据
    private List<MainListBean> testListHistoryData=null;

    public List<MainListBean> getTestListHistoryData() {
        if (testListHistoryData==null){
            testListHistoryData=new ArrayList<MainListBean>();
            MainListBean bean;
            bean=new MainListBean(1,"N碳社","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"动漫","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"锤子","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"小米","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"魅族","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"旅游","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"科技","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"智慧公社","2810 人感兴趣");
            testListHistoryData.add(bean);
            bean=new MainListBean(1,"kongzue","2810 人感兴趣");
            testListHistoryData.add(bean);
        }
        return testListHistoryData;
    }

    public void setTestListHistoryData(List<MainListBean> testListLoveData) {
        this.testListHistoryData = testListLoveData;
    }

    //消息页面的list测试用数据
    private List<MainListBean> testListMsgData=null;

    public List<MainListBean> getTestListMsgData(Context m_context) {
        if (testListMsgData==null){
            testListMsgData=new ArrayList<MainListBean>();
            MainListBean bean;
//            bean=new MainListBean(2,"18513504233","kongzue","在不在？","N碳社", BitmapFactory.decodeResource(m_context.getResources(), R.drawable.ic_testuser_kongzue),"12:06",false);
//            testListMsgData.add(bean);
//            bean=new MainListBean(2,"18513504233","mofanreal","我想和你聊聊关于锤子T2","锤子手机", BitmapFactory.decodeResource(m_context.getResources(), R.drawable.ic_main_user_noface),"13:15",false);
//            testListMsgData.add(bean);
        }
        return testListMsgData;
    }

    public void setTestListMsgData(List<MainListBean> testListMsgData) {
        this.testListMsgData = testListMsgData;
    }

    //消息界面正在寻找list测试用数据
    private List<MainListBean> testListFindData=null;

    public List<MainListBean> getTestListFindData() {
        if (testListFindData==null){
            testListFindData=new ArrayList<MainListBean>();
            MainListBean bean;
            bean=new MainListBean(1,"旅行","正在寻找...");
            testListFindData.add(bean);
            bean=new MainListBean(1,"产品经理","正在寻找...");
            testListFindData.add(bean);
        }
        return testListFindData;
    }

    public void setTestListFindData(List<MainListBean> testListFindData) {
        this.testListFindData = testListFindData;
    }

    //搜索建议测试用数据
    private List<MainListBean> testListSuggestData=null;

    public List<MainListBean> getTestListSuggestData(String inputText) {
        testListSuggestData=new ArrayList<MainListBean>();
        MainListBean bean;
        if (inputText.substring(0,1).equals("软")) {
            bean = new MainListBean(3, "软件开发", "1200 人在搜");
            testListSuggestData.add(bean);
            bean = new MainListBean(3, "软件编程", "1366 人在搜");
            testListSuggestData.add(bean);
            bean = new MainListBean(3, "软件工程师", "7695 人在搜");
            testListSuggestData.add(bean);
        }
        if (inputText.substring(0,1).equals("锤")) {
            bean = new MainListBean(3, "锤子手机", "2650 人在搜");
            testListSuggestData.add(bean);
            bean = new MainListBean(3, "锤子科技", "200 人在搜");
            testListSuggestData.add(bean);
        }
        if (inputText.substring(0,1).equals("小")) {
            bean = new MainListBean(3, "小米手机", "2465 人在搜");
            testListSuggestData.add(bean);
        }
        if (inputText.substring(0,1).equals("产")) {
            bean = new MainListBean(3, "产品经理", "1200 人在搜");
            testListSuggestData.add(bean);
            bean = new MainListBean(3, "产品设计", "1366 人在搜");
            testListSuggestData.add(bean);
            bean = new MainListBean(3, "产品开发", "7695 人在搜");
            testListSuggestData.add(bean);
        }
        return testListSuggestData;
    }

    public void setTestListSuggestData(List<MainListBean> testListSuggestData) {
        this.testListSuggestData = testListSuggestData;
    }

    //聊天室测试用数据
    private List<ChatListBean> testListChatData=null;

    public List<ChatListBean> getTestListChatData(Context m_context) {
        if (testListChatData==null) {
            testListChatData = new ArrayList<ChatListBean>();
            ChatListBean bean;
            bean=new ChatListBean(3,"您已匹配到同样对 #测试话题 感兴趣的莫小凡",1);
            testListChatData.add(bean);
//            bean=new ChatListBean(1,BitmapFactory.decodeResource(m_context.getResources(), R.drawable.ic_main_user_noface),"莫小凡","12345678","Hi！");
//            testListChatData.add(bean);
//            bean=new ChatListBean(1,BitmapFactory.decodeResource(m_context.getResources(), R.drawable.ic_main_user_noface),"莫小凡","12345678","你觉得这款软件好用不？");
//            testListChatData.add(bean);
        }
        return testListChatData;
    }

    public void setTestListChatData(List<ChatListBean> testListChatData) {
        this.testListChatData = testListChatData;
    }
}
