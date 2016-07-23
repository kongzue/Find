package com.kongzue.find.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kongzue.find.MainActivity;
import com.kongzue.find.util.listener.UpdateCacheListener;
import com.kongzue.find.work.UserLogin;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by chao on 2016/3/11.
 */
public class MessageHandler  {
//    private Context context;
//
//    public MessageHandler(Context context) {
//        this.context = context;
//    }
//
//    //当接收到服务器发来的消息时，此方法被调用
//    @Override
//    public void onMessageReceive(final MessageEvent event) {
//        UserLogin.getInstance().updateUserInfo(event, new UpdateCacheListener() {
//            @Override
//            public void done(BmobException e) {
//                BmobIMMessage msg = event.getMessage();
//                Log.i("msg", "获取到了消息：" + msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra());
//                if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
//                    //自行处理自定义消息类型
//                    Toast.makeText(context, msg.getMsgType() + "," + msg.getContent(), Toast.LENGTH_SHORT).show();
//                } else {//SDK内部内部支持的消息类型
//                    if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
//                        Intent pendingIntent = new Intent(context, MainActivity.class);
//                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        //多条消息合并成一条通知：有XX个联系人发来了XX条消息
//                        BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
//                        //始终只有一条通知，新消息覆盖旧消息
//                        //BmobIMMessage msg =event.getMessage();
//                        //BmobIMUserInfo info =event.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        //Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        //BmobNotificationManager.getInstance().showNotification(largetIcon,info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
//                    }
//                    Log.i("event","直接发送消息事件");
//                    EventBus.getDefault().post(event);
//                }
//            }
//        });
//    }
//
//    //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
//    @Override
//    public void onOfflineReceive(final OfflineMessageEvent event) {
//        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
//        Map<String, List<MessageEvent>> map = event.getEventMap();
//        Log.i("ofl", "离线消息属于" + map.size() + "个用户");
//        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
//            List<MessageEvent> list = entry.getValue();
//            //挨个检测离线用户信息是否需要更新
//            UserLogin.getInstance().updateUserInfo(list.get(0), new UpdateCacheListener() {
//                @Override
//                public void done(BmobException e) {
//                    EventBus.getDefault().post(event);
//                }
//            });
//        }
//    }
}