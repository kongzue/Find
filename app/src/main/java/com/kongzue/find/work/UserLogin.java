package com.kongzue.find.work;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kongzue.find.MainActivity;
import com.kongzue.find.MyApplication;
import com.kongzue.find.bean.User;
import com.kongzue.find.util.EventPassger;
import com.kongzue.find.util.FindBmobPusher;
import com.kongzue.find.util.listener.QueryUserListener;
import com.kongzue.find.util.listener.UpdateCacheListener;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by chao on 2016/3/11.
 */
public class UserLogin {

    //单例设计模式
    private static UserLogin ourInstance = new UserLogin();
    public static UserLogin getInstance() {
        return ourInstance;
    }
    private UserLogin() {}

    public Context getContext(){
        return EventPassger.getInstance().getMyApplication();
    }

    /** 登录
     * @param username
     * @param password
     * @param listener
     */
    public void login(final String username, String password, final LogInListener listener) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(getCurrentUser(),null);

                BmobQuery<FindBmobPusher> query = new BmobQuery<FindBmobPusher>();
                query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(getContext()));
                query.findObjects(getContext(), new FindListener<FindBmobPusher>() {

                    @Override
                    public void onSuccess(List<FindBmobPusher> object) {
                        if(object.size() > 0){
                            FindBmobPusher mbi = object.get(0);
                            mbi.setUid(username);
                            mbi.update(getContext(),new UpdateListener() {

                                @Override
                                public void onSuccess() {
                                    Log.i("log","设备信息更新成功");
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    Log.i("log","设备信息更新失败:"+msg);
                                }
                            });

                            EventPassger.getInstance().callMainAvtivityRefreshMessageList();
                        }else{
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(null ,new BmobException(i,s));
            }
        });
    }

    /**
     * 退出登录
     */
    public void logout(){
        BmobUser.logOut(getContext());
    }

    public User getCurrentUser(){
        return BmobUser.getCurrentUser(getContext(), User.class);
    }


    /**
     * 注册
     */
    public void register(String username,String password, String userNick, final LogInListener listener) {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserNick(userNick);
        user.setUserCanUse(true);
        user.signUp(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(null,null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(null,new BmobException(i,s));
            }
        });
    }

    /**查询用户
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username,int limit,final FindListener<User> listener){
        BmobQuery<User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser(getContext());
            query.addWhereNotEqualTo("username",user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(1000, "查无此人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }

    /**查询用户信息
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list!=null && list.size()>0){
                    listener.internalDone(list.get(0), null);
                }else{
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }



}
