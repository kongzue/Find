package com.kongzue.find.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.kongzue.find.MainActivity;
import com.kongzue.find.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.push.PushConstants;

/**
 * Created by chao on 2016/3/17.
 */
public class FindPushMessageReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String message=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            showMessageInNotification(message);
        }
    }

    public void showMessageInNotification(String message){

        Log.i("msg","接收到来自推送的消息："+message);

        String msgType="";
        String msgText="";
        String msgUrl="";
        String msgVer="";

        String strTitle="";
        Intent intent=null;

        try {
            JSONObject jo= new JSONObject(message);
            msgType=jo.getString("type");
            msgText=jo.getString("alert");
            msgUrl=jo.getString("url");
            msgVer=jo.getString("ver");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("msgVer",msgVer);
        if (msgType.equals("update") && msgVer.equals("beta1")){
            return;
        }

        if (msgType != null && msgType.equals("update")) {
            strTitle = "新版本！";
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(msgUrl);
            intent.setData(content_url);
        } else if (msgType != null && msgType.equals("newmsg")) {
            strTitle = "新消息！";

            intent = new Intent(context, MainActivity.class);
            intent.putExtra("open", "message_page");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        } else {
            strTitle = "寻";
            intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }

        if (EventPassger.getInstance().isMainActivityActivited()){
            //用户在主界面操作
            EventPassger.getInstance().callMainAvtivityRefreshMessageList();
        }else if(EventPassger.getInstance().isChatActivityActivited()){
            //用户在聊天界面操作
            EventPassger.getInstance().callChatActivityRefreshMessageList();
        }else{
            //显示通知
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,intent, 0);
            // 通过Notification.Builder来创建通知，注意API Level
            // API11之后才支持
            Notification notify2 = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_statusbar_icon) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap icon)
                    //.setTicker(msgText)// 设置在status bar上显示的提示文字
                    .setContentTitle(strTitle)// 设置在下拉status bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                    .setContentText(msgText)// TextView中显示的详细内容
                    .setContentIntent(pendingIntent2) // 关联PendingIntent
                    //.setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                    .getNotification(); // 需要注意build()是在API level
            notify2.defaults=Notification.DEFAULT_SOUND;        //系统默认音效
            // 16及之后增加的，在API11中可以使用getNotificatin()来代替
            notify2.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(1, notify2);
        }



//
//
//        String strTitle="";
//        Notification baseNF = new Notification.Builder()
//                .setContentTitle("")
//                .setContentText("")
//                .setSmallIcon(R.mipmap.ic_statusbar_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .build();
//
//        //设置通知在状态栏显示的图标
//        baseNF.icon = R.mipmap.ic_statusbar_icon;
//
//        //通知时在状态栏显示的内容
//        baseNF.tickerText = msgText;
//
//        //通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.
//        //如果要全部采用默认值, 用 DEFAULT_ALL.
//        //此处采用默认声音
//        baseNF.defaults = Notification.DEFAULT_SOUND;
//
//        //第二个参数 ：下拉状态栏时显示的消息标题 expanded message title
//        //第三个参数：下拉状态栏时显示的消息内容 expanded message text
//        //第四个参数：点击该通知时执行页面跳转
//        PendingIntent pd;
//        Intent intent=null;
//        if (msgType!=null && msgType.equals("update")) {
//            strTitle = "新版本！";
//            intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(msgUrl);
//            intent.setData(content_url);
//            pd = PendingIntent.getActivity(context, 0, intent, 0);
//        }else if (msgType!=null && msgType.equals("newmsg")){
//            strTitle = "新消息！";
//
//            intent = new Intent(context, MainActivity.class);
//            intent.putExtra("open","message_page");
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            //Cache.getInstance().setIntentToMessagePage(true);
//            pd = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        }else{
//            strTitle="乐享消息";
//            intent = new Intent(context,MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            pd = PendingIntent.getActivity(context, 0, intent, 0);
//        }
//        baseNF.flags |= Notification.FLAG_AUTO_CANCEL;
//        baseNF.setLatestEventInfo(context,strTitle, msgText, pd);
//
//        //发出状态栏通知
//        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(110, baseNF);
    }


    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

}
