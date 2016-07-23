package com.kongzue.find.util;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.find.MainActivity;
import com.kongzue.find.R;
import com.kongzue.find.bean.ChatData;
import com.kongzue.find.listviewbean.MainListBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by chao on 2016/3/4.
 */
public class MainListviewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<MainListBean> myList;
    private MainActivity m_context;


    public MainListviewAdapter(MainActivity context, List<MainListBean> myList) {
        this.myList = myList;
        m_context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        MainListBean obj;
        ViewHolderMsgContext msgContext;

        obj = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderWordContext wordContext = null;
        msgContext = null;

        if (convertView == null) {
            switch (type) {
                case 1:
                    wordContext = new ViewHolderWordContext();
                    convertView = mInflater.inflate(R.layout.item_main_listview_word, null);
                    wordContext.line = (ImageView) convertView.findViewById(R.id.img_main_list_line);
                    wordContext.itemMainListviewTitleTextTitle = (TextView) convertView.findViewById(R.id.item_main_listview_title_textTitle);
                    wordContext.itemMainListviewTitleTextTip = (TextView) convertView.findViewById(R.id.item_main_listview_title_textTip);

                    convertView.setTag(wordContext);
                    //setEvent here

                    break;
                case 2:
                    msgContext = new ViewHolderMsgContext();
                    convertView = mInflater.inflate(R.layout.item_main_listview_message, null);
                    msgContext.imgMainMsgUserFace = (SimpleDraweeView) convertView.findViewById(R.id.img_main_msg_userFace);
                    msgContext.txtMainMsgUserNick = (TextView) convertView.findViewById(R.id.txt_main_msg_userNick);
                    msgContext.txtMainMsgMessage = (TextView) convertView.findViewById(R.id.txt_main_msg_message);
                    msgContext.txtMainMsgWord = (TextView) convertView.findViewById(R.id.txt_main_msg_word);
                    msgContext.txtMainMsgUserName = (TextView) convertView.findViewById(R.id.txt_main_msg_userName);
                    msgContext.txtMainMsgTime = (TextView) convertView.findViewById(R.id.txt_main_msg_time);
                    msgContext.line = (ImageView) convertView.findViewById(R.id.img_main_list_line);

                    convertView.setTag(msgContext);

                    //setEvent here
                    //获取最近一次聊天记录
                    if (msgContext.txtMainMsgMessage.getTag()==null) {
                        RefreshMessage rf = new RefreshMessage();
                        rf.refreshStart(obj.getMsg_userName(), msgContext.txtMainMsgMessage, position);
                        msgContext.txtMainMsgMessage.setTag(rf);
                    }else{
                        RefreshMessage rf = (RefreshMessage)msgContext.txtMainMsgMessage.getTag();
                        if (!rf.getChatId.equals(obj.getMsg_userName())){
                            rf.stopRefresh();
                            RefreshMessage nrf = new RefreshMessage();
                            nrf.refreshStart(obj.getMsg_userName(), msgContext.txtMainMsgMessage, position);
                            msgContext.txtMainMsgMessage.setTag(nrf);
                        }else{
                            rf.refreshStart(obj.getMsg_userName(), msgContext.txtMainMsgMessage, position);
                        }
                    }

                    break;
                case 3:
                    wordContext = new ViewHolderWordContext();
                    convertView = mInflater.inflate(R.layout.item_main_listview_suggest, null);
                    wordContext.itemMainListviewTitleTextTitle = (TextView) convertView.findViewById(R.id.item_main_listview_title_textTitle);
                    wordContext.itemMainListviewTitleTextTip = (TextView) convertView.findViewById(R.id.item_main_listview_title_textTip);

                    convertView.setTag(wordContext);
                    //setEvent here

                    break;
                default:
                    break;
            }

        } else {
            switch (type) {
                case 1:
                    wordContext = (ViewHolderWordContext) convertView.getTag();
                    break;
                case 2:
                    msgContext = (ViewHolderMsgContext) convertView.getTag();
                    break;
                case 3:
                    wordContext = (ViewHolderWordContext) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (type) {
            case 1:
                wordContext.itemMainListviewTitleTextTitle.setText(obj.getWord_text());
                wordContext.itemMainListviewTitleTextTip.setText(obj.getWord_tip());
                if (position==myList.size()-1){
                    wordContext.line.setVisibility(View.GONE);
                }else{
                    wordContext.line.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (obj.getMsg_userFace()!=null  &&  !obj.getMsg_userFace().equals("")) {
                    String imageUrl = obj.getMsg_userFace();
                    if (!imageUrl.isEmpty()){
                        msgContext.imgMainMsgUserFace.setImageURI(Uri.parse(imageUrl));
                        //设置为圆形外边
                        RoundingParams roundingParams = msgContext.imgMainMsgUserFace.getHierarchy().getRoundingParams();
                        if (roundingParams!=null)roundingParams.setRoundAsCircle(true);
                        msgContext.imgMainMsgUserFace.getHierarchy().setRoundingParams(roundingParams);
                    }

                }
                msgContext.txtMainMsgUserNick.setText(obj.getMsg_userNick());
//                msgContext.txtMainMsgMessage.setText(obj.getMsg_message());
                msgContext.txtMainMsgWord.setText(obj.getMsg_word());
                msgContext.txtMainMsgUserName.setText(obj.getMsg_userName());
                msgContext.txtMainMsgTime.setText(obj.getMsg_time());

                if (position==myList.size()-1){
                    msgContext.line.setVisibility(View.GONE);
                }else{
                    msgContext.line.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                wordContext.itemMainListviewTitleTextTitle.setText(obj.getWord_text());
                wordContext.itemMainListviewTitleTextTip.setText(obj.getWord_tip());
                break;
            default:
                break;
        }

        return convertView;
    }


    //自动刷新消息类
    class RefreshMessage{

        private Timer timer;
        private String getChatId;
        private TextView getTextView;
        private int getPoint;

        private void stopRefresh(){
            timer.cancel();
            timer=null;
        }

        TimerTask task = new TimerTask() {
            public void run() {
                if (m_context.isActivity) {
                    Message message = new Message();
                    message.what = 31;
                    mHandler.sendMessage(message);
                }
            }
        };

        public Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 31) {
                    //refreshStart(getChatId,getTextView,getPoint);
                }
                //super.handleMessage(msg);
            }
        };

        private void refreshStart(String ChatId,final TextView setTextView,final int point){

            Log.i("msgAdp","开始获取最近一次的消息");

            this.getChatId=ChatId;
            this.getTextView=setTextView;
            this.getPoint=point;

//            if (timer==null) {
//                timer = new Timer(true);
//                timer.schedule(task, 100, 10000);
//            }

            BmobQuery<ChatData> query=new BmobQuery<ChatData>();
            query.order("-createdAt");
            query.addWhereEqualTo("chatId",ChatId);
            query.setLimit(1);
            query.findObjects(m_context, new FindListener<ChatData>() {
                @Override
                public void onSuccess(List<ChatData> object) {
                    if (object.size()==0 && point<myList.size()){
                        return;
                    }
                    Log.i("msgAdp","已获取到最近一次聊天记录");
                    ChatData cd=object.get(0);
                    MainListBean obj = myList.get(point);
                    obj.setMsg_message(cd.getMessage());
                    setTextView.setText(obj.getMsg_message());
                }
                @Override
                public void onError(int code, String msg) {
                    Log.i("msgAdp","获取最近一次聊天记录失败");
                }
            });
        }
    }

    /**
     * 根据数据源的position返回需要显示的的layout的type
     * <p>
     * type的值必须从0开始
     */
    @Override
    public int getItemViewType(int position) {

        MainListBean msg = myList.get(position);
        int type = msg.getType();
        return type;
    }

    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 4;
    }


    class ViewHolderMsgContext {
        private SimpleDraweeView imgMainMsgUserFace;
        private TextView txtMainMsgUserNick;
        private TextView txtMainMsgMessage;
        private TextView txtMainMsgWord;
        private TextView txtMainMsgUserName;
        private TextView txtMainMsgTime;
        private ImageView line;
    }

    class ViewHolderWordContext {
        private TextView itemMainListviewTitleTextTitle;
        private TextView itemMainListviewTitleTextTip;
        private ImageView line;
    }

}