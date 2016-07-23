package com.kongzue.find.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.find.ChatActivity;
import com.kongzue.find.R;
import com.kongzue.find.listviewbean.ChatListBean;

import java.util.List;

/**
 * Created by chao on 2016/3/6.
 */
public class ChatListviewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<ChatListBean> myList;
    private ChatActivity m_context;

    public ChatListviewAdapter(ChatActivity context, List<ChatListBean> myList) {
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

        ChatListBean obj = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderLeftMsgContext leftContext = null;
        ViewHolderRightMsgContext rightContext = null;
        ViewHolderSystemInfoContext sysContext = null;

        if (convertView == null) {
            switch (type) {
                case 1:
                    leftContext = new ViewHolderLeftMsgContext();
                    convertView = mInflater.inflate(R.layout.item_chat_message_left, null);
                    leftContext.imgChatMessageLeftUserface = (SimpleDraweeView) convertView.findViewById(R.id.img_chat_message_left_userface);
                    leftContext.txtChatMessageLeftMsg = (TextView) convertView.findViewById(R.id.txt_chat_message_msg);
                    leftContext.txtChatMessageLeftUsernick = (TextView) convertView.findViewById(R.id.txt_chat_message_left_usernick);
                    leftContext.txtChatMessageLeftUsername = (TextView) convertView.findViewById(R.id.txt_chat_message_left_username);
                    leftContext.imgChatMessagePhoto = (SimpleDraweeView) convertView.findViewById(R.id.img_chat_message_photo);


                    convertView.setTag(leftContext);
                    //setEvent here

                    break;
                case 2:
                    rightContext = new ViewHolderRightMsgContext();
                    convertView = mInflater.inflate(R.layout.item_chat_message_right, null);
                    rightContext.txtChatMessageRightMsg = (TextView) convertView.findViewById(R.id.txt_chat_message_msg);
                    rightContext.imgChatMessagePhoto = (SimpleDraweeView) convertView.findViewById(R.id.img_chat_message_photo);

                    convertView.setTag(rightContext);
                    //setEvent here

                    break;
                case 3:
                    sysContext = new ViewHolderSystemInfoContext();
                    convertView = mInflater.inflate(R.layout.item_chat_message_systeminfo, null);
                    sysContext.txtChatMessageSystemInfo = (TextView) convertView.findViewById(R.id.txt_chat_message_msg);

                    convertView.setTag(sysContext);
                    //setEvent here

                    break;
                case 4:
                    leftContext = new ViewHolderLeftMsgContext();
                    convertView = mInflater.inflate(R.layout.item_chat_message_people, null);
                    leftContext.imgChatMessageLeftUserface = (SimpleDraweeView) convertView.findViewById(R.id.img_chat_message_left_userface);
                    leftContext.txtChatMessageLeftUsernick = (TextView) convertView.findViewById(R.id.txt_chat_message_left_usernick);
                    leftContext.txtChatMessageLeftWord = (TextView) convertView.findViewById(R.id.txt_chat_message_left_word);

                    convertView.setTag(leftContext);
                    //setEvent here

                    break;
                default:
                    break;
            }

        } else {
            switch (type) {
                case 1:
                    leftContext = (ViewHolderLeftMsgContext) convertView.getTag();
                    break;
                case 2:
                    rightContext = (ViewHolderRightMsgContext) convertView.getTag();
                    break;
                case 3:
                    sysContext = (ViewHolderSystemInfoContext) convertView.getTag();
                    break;
                case 4:
                    leftContext = (ViewHolderLeftMsgContext) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (type) {
            case 1:
                if (obj.getLeftMsgUserFace()!=null) {
//                    leftContext.imgChatMessageLeftUserface.setImageURI(Uri.parse(obj.getLeftMsgUserFace()));
                }//leftContext.imgChatMessageLeftUserface.setImageBitmap(obj.getLeftMsgUserFace());
                leftContext.txtChatMessageLeftUsername.setText(obj.getLeftMsgUserName());
                leftContext.txtChatMessageLeftUsernick.setText(obj.getLeftMsgUserNick());
                if (Tools.isHaveWord(obj.getLeftMsgMesage(),"我发送了一张照片给你，更新最新版本的《寻》可以直接查看：")) {
                    String picUrl=obj.getLeftMsgMesage().replaceAll("我发送了一张照片给你，更新最新版本的《寻》可以直接查看：","");
                    Log.i("loading chat pic...",picUrl);
                    leftContext.imgChatMessagePhoto.setImageURI(Uri.parse(picUrl));

                    leftContext.txtChatMessageLeftMsg.setVisibility(View.GONE);
                    leftContext.imgChatMessagePhoto.setVisibility(View.VISIBLE);
                    leftContext.txtChatMessageLeftMsg.setText("img://"+picUrl);
                }else{
                    leftContext.txtChatMessageLeftMsg.setVisibility(View.VISIBLE);
                    leftContext.imgChatMessagePhoto.setVisibility(View.GONE);
                    leftContext.txtChatMessageLeftMsg.setText(obj.getLeftMsgMesage());
                }
                break;
            case 2:
                if (Tools.isHaveWord(obj.getRightMsgMessage(),"我发送了一张照片给你，更新最新版本的《寻》可以直接查看：")) {
                    String picUrl=obj.getRightMsgMessage().replaceAll("我发送了一张照片给你，更新最新版本的《寻》可以直接查看：","");
                    Log.i("loading chat pic...",picUrl);
                    rightContext.imgChatMessagePhoto.setImageURI(Uri.parse(picUrl));

                    rightContext.txtChatMessageRightMsg.setVisibility(View.GONE);
                    rightContext.imgChatMessagePhoto.setVisibility(View.VISIBLE);
                    rightContext.txtChatMessageRightMsg.setText("img://"+picUrl);
                }else{
                    rightContext.txtChatMessageRightMsg.setVisibility(View.VISIBLE);
                    rightContext.imgChatMessagePhoto.setVisibility(View.GONE);
                    rightContext.txtChatMessageRightMsg.setText(obj.getRightMsgMessage());
                }
                break;
            case 3:
                sysContext.txtChatMessageSystemInfo.setText(obj.getSystemInfoMsg());
                break;
            case 4:
                if (obj.getLeftMsgUserFace()!=null) {
                    leftContext.imgChatMessageLeftUserface.setImageURI(Uri.parse(obj.getLeftMsgUserFace()));
                }
                leftContext.txtChatMessageLeftUsernick.setText("Hi，我是"+obj.getLeftMsgUserNick());
                leftContext.txtChatMessageLeftWord.setText("我也对 #"+obj.getFavWord()+" 感兴趣");
                break;
            default:
                break;
        }

        return convertView;
    }

    /**
     * 根据数据源的position返回需要显示的的layout的type
     * <p>
     * type的值必须从0开始
     */
    @Override
    public int getItemViewType(int position) {

        ChatListBean msg = myList.get(position);
        int type = msg.getType();
        return type;
    }

    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 5;
    }


    class ViewHolderLeftMsgContext {
        private SimpleDraweeView imgChatMessageLeftUserface;
        private TextView txtChatMessageLeftMsg;
        private TextView txtChatMessageLeftUsernick;
        private TextView txtChatMessageLeftUsername;
        private TextView txtChatMessageLeftWord;
        private SimpleDraweeView imgChatMessagePhoto;
    }

    class ViewHolderRightMsgContext {
        private TextView txtChatMessageRightMsg;
        private SimpleDraweeView imgChatMessagePhoto;
    }

    class ViewHolderSystemInfoContext {
        private TextView txtChatMessageSystemInfo;
    }

}
