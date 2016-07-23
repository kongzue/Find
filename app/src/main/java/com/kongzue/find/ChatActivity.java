package com.kongzue.find;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kongzue.find.bean.ChatData;
import com.kongzue.find.bean.User;
import com.kongzue.find.listviewbean.ChatListBean;
import com.kongzue.find.util.ApcpActivity;
import com.kongzue.find.util.ChatListviewAdapter;
import com.kongzue.find.util.EventPassger;
import com.kongzue.find.util.FindBmobPusher;
import com.kongzue.find.util.HttpDownloader;
import com.kongzue.find.util.IMMListenerRelativeLayout;
import com.kongzue.find.util.Tools;
import com.kongzue.find.work.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ChatActivity extends ApcpActivity implements
        ImageChooserListener {

    //Bmob文件管理器
    BmobFile bmobFile=null;
    private ProgressDialog loadingDialog = null;            //等待对话框

    //选择照片
    private final static String TAG = "ICA";
    private ImageChooserManager imageChooserManager;
    private String filePath;
    private int chooserType;
    private boolean isActivityResultOver = false;
    private String originalFilePath;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;

    //other
    private String chatId;
    private User matchingUser;
    private String wordName;
    private User localUser;
    private float chatSendBoxAlpha;

    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private boolean scrollDown=false; //到达底部标记
    private float touchDown=0;

    //聊天室列表数据存储
    private List<ChatListBean> listChat = new ArrayList<ChatListBean>();
    private ChatListviewAdapter chatListviewAdapter;

    //聊天室组件
    private LinearLayout sysStatusBar;
    private ImageView imgChatBkg;
    private ListView listChatMessageListView;
    private ImageView imgChatTitleBarBkg;
    private TextView txtChatTitle;
    private ImageView imgChatBackButton;
    private TextView txtChatWord;
    private RelativeLayout boxChatSendBox;
    private EditText editChatSendBox;
    private TextView txtChatSendButton;
    private ImageView imgChatSendMoreButton;
    private ImageView imgSend;
    private RelativeLayout mainLayout;
    private ImageView imgChatAddPic;


    //绑定聊天室组件
    private void assignViews() {
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        imgChatBkg = (ImageView) findViewById(R.id.img_chat_bkg);
        listChatMessageListView = (ListView) findViewById(R.id.list_chat_messageListView);
        imgChatTitleBarBkg = (ImageView) findViewById(R.id.img_chat_titleBar_bkg);
        txtChatTitle = (TextView) findViewById(R.id.txt_chat_title);
        imgChatBackButton = (ImageView) findViewById(R.id.img_chat_back_button);
        txtChatWord = (TextView) findViewById(R.id.txt_chat_word);
        boxChatSendBox = (RelativeLayout) findViewById(R.id.box_chat_sendBox);
        editChatSendBox = (EditText) findViewById(R.id.edit_chat_sendBox);
        txtChatSendButton = (TextView) findViewById(R.id.txt_chat_send_button);
        imgChatSendMoreButton = (ImageView) findViewById(R.id.img_chat_sendMore_button);
        imgSend = (ImageView) findViewById(R.id.img_send);
        mainLayout = (RelativeLayout)findViewById(R.id.main_Layout);
        imgChatAddPic = (ImageView) findViewById(R.id.img_chat_addPic);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        assignViews();
        initData();
        setEvent();
    }

    private void setEvent() {

        imgChatAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                final String[] cities = {"拍照", "从相册里选择"};
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            imageChooserManager = new ImageChooserManager(ChatActivity.this,
                                    ChooserType.REQUEST_CAPTURE_PICTURE, true);
                            imageChooserManager.setImageChooserListener(ChatActivity.this);
                            try {
                                filePath = imageChooserManager.choose();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            imageChooserManager = new ImageChooserManager(ChatActivity.this,
                                    ChooserType.REQUEST_PICK_PICTURE, true);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            imageChooserManager.setExtras(bundle);
                            imageChooserManager.setImageChooserListener(ChatActivity.this);
                            imageChooserManager.clearOldFiles();
                            try {
                                filePath = imageChooserManager.choose();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.show();
            }
        });
//        mainLayout.setListener(new IMMListenerRelativeLayout.InputWindowListener() {
//            @Override
//            public void show() {
//                listChatMessageListView.setSelection(listChatMessageListView.getBottom());
//            }
//
//            @Override
//            public void hidden() {
//
//            }
//        });

        listChatMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    return ;
                }
                final String value= ((TextView)view.findViewById(R.id.txt_chat_message_msg)).getText().toString();
                if (value!=null && Tools.isHaveWord(value,"img://")){
                    Intent intent = new Intent(ChatActivity.this,ViewPicActivity.class);
                    intent.putExtra("url",value.replaceAll("img://",""));
                    startActivity(intent);
                }
            }
        });

        listChatMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    return false;
                }
                final String value= ((TextView)view.findViewById(R.id.txt_chat_message_msg)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                if (value!=null && Tools.isHaveWord(value,"img://")){
                    final String[] cities = {"详细查看", "保存这张图片"};
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent intent = new Intent(ChatActivity.this,ViewPicActivity.class);
                                intent.putExtra("url",value.replaceAll("img://",""));
                                startActivity(intent);
                            } else {
                                HttpDownloader hd=new HttpDownloader();
                                hd.downFile(value.replaceAll("img://",""), Environment.getExternalStorageDirectory().toString(),"1.png");
                            }
                        }
                    });
                }else {
                    final String[] cities = {"复制", "分享这句话"};
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                if (value != null && !value.equals("")) {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    cmb.setText(value.trim());
                                    Toast.makeText(ChatActivity.this, "已经复制到剪贴板", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain"); // 纯文本
                                intent.putExtra(Intent.EXTRA_SUBJECT, "来自寻的分享");
                                intent.putExtra(Intent.EXTRA_TEXT, value);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(Intent.createChooser(intent, "来自寻的分享"));
                            }
                        }
                    });
                }
                builder.show();
                return true;
            }
        });
        //返回按钮
        imgChatBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                finish();
                if (version > 5) {
                    overridePendingTransition(R.anim.hold, R.anim.back);
                }
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = editChatSendBox.getText().toString();
                if (msg == null || msg.equals("")) {
                    return;
                }

                imgSend.setImageResource(R.drawable.img_sending);
                imgSend.setEnabled(false);

                ChatListBean bean = new ChatListBean(2, msg);
                listChat.add(bean);
                chatListviewAdapter.notifyDataSetChanged();

                //滚动到底部
                listChatMessageListView.setSelection(listChatMessageListView.getBottom());

                //发送消息
                ChatData cd = new ChatData();
                cd.setChatId(chatId);
                cd.setMessage(msg);
                cd.setMessageType(0);
                cd.setSender(localUser);
                cd.save(ChatActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        getMessages();
                        imgSend.setImageResource(R.drawable.img_send);
                        imgSend.setEnabled(true);

                        //开始推送消息给收信用户
                        String strUserNick = matchingUser.getUsername();
                        String strMyUserNick=localUser.getUserNick();
                        BmobPushManager bmobPush = new BmobPushManager(ChatActivity.this);
                        BmobQuery<FindBmobPusher> query = FindBmobPusher.getQuery();
                        query.addWhereEqualTo("uid", strUserNick);
                        bmobPush.setQuery(query);
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("alert", strMyUserNick + "：" + msg);
                            jo.put("type", "newmsg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jo != null) bmobPush.pushMessage(jo);
                        Log.i("chat", "消息发送结束");

                        editChatSendBox.setText("");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("chat", "消息发送失败");
                        imgSend.setImageResource(R.drawable.img_send);
                        imgSend.setEnabled(true);
                    }
                });
            }
        });

        listChatMessageListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listChatMessageListView.smoothScrollToPositionFromTop(listChat.size()-1, 0);
            }
        });

        listChatMessageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (listChatMessageListView.getLastVisiblePosition() == (listChatMessageListView.getCount() - 1)) {
                            scrollDown=true;
                        }
                        // 判断滚动到顶部
                        if (listChatMessageListView.getFirstVisiblePosition() == 0) {
                            scrollDown=false;
                        }

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        scrollDown=false;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        scrollDown=false;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                    if (scrollFlag) {
                        if (firstVisibleItem > lastVisibleItemPosition) {// 上滑

                        } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editChatSendBox.getWindowToken(), 0);
                        } else {
                            return;
                        }
                        lastVisibleItemPosition = firstVisibleItem;
                    }
            }
        });

        listChatMessageListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                        if (touchDown > event.getY() && scrollDown) {
                            editChatSendBox.setFocusable(true);
                            editChatSendBox.requestFocus();
                            editChatSendBox.setFocusableInTouchMode(true);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(editChatSendBox, InputMethodManager.SHOW_FORCED);
                            listChatMessageListView.setSelection(listChatMessageListView.getBottom());
                        }
                }
                touchDown = event.getY();
                return false;
            }
        });

        editChatSendBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editChatSendBox.getText().toString().equals("")){
                    imgSend.setVisibility(View.GONE);
                    if (chatSendBoxAlpha==0.3f){
                        return;
                    }
                    AlphaAnimation am = new AlphaAnimation(chatSendBoxAlpha, 0.3f);
                    am.setDuration(500);
                    am.setFillAfter(true);
                    boxChatSendBox.startAnimation(am);
                    chatSendBoxAlpha=0.3f;
                }else{
                    imgSend.setVisibility(View.VISIBLE);
                    if (chatSendBoxAlpha==1f){
                        return;
                    }
                    AlphaAnimation am = new AlphaAnimation(chatSendBoxAlpha, 1f);
                    am.setDuration(500);
                    am.setFillAfter(true);
                    boxChatSendBox.startAnimation(am);
                    chatSendBoxAlpha=1f;
                }
            }
        });

    }

    private void initData() {
        //获取测试用数据
//        listChat = TestData.getInstance().getTestListChatData(this);
//        chatListviewAdapter = new ChatListviewAdapter(this, listChat);
//        listChatMessageListView.setAdapter(chatListviewAdapter);
//        chatListviewAdapter.notifyDataSetChanged();

        localUser = UserLogin.getInstance().getCurrentUser();

        //获取聊天数据
        chatId = (String) getBundle().getSerializable("chatId");
        matchingUser = (User) getBundle().getSerializable("matchingUser");
        wordName = (String) getBundle().getSerializable("wordName");

        txtChatTitle.setText(matchingUser.getUserNick());
        txtChatWord.setText(wordName);


        chatListviewAdapter = new ChatListviewAdapter(ChatActivity.this, listChat);
        listChatMessageListView.setAdapter(chatListviewAdapter);
        chatListviewAdapter.notifyDataSetChanged();

        AlphaAnimation am = new AlphaAnimation(1f, 0.3f);
        am.setDuration(500);
        am.setFillAfter(true);
        boxChatSendBox.startAnimation(am);
        chatSendBoxAlpha=0.3f;

        editChatSendBox.setHint("说点什么吧");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getMessages();
    }

    public void getMessages() {
        BmobQuery<ChatData> query = new BmobQuery<ChatData>();
        query.addWhereEqualTo("chatId", chatId);
        query.setLimit(50);
        query.include("sender");
        query.findObjects(this, new FindListener<ChatData>() {
            @Override
            public void onSuccess(List<ChatData> object) {
                if (object.size() != 0) {
                    listChat.clear();
                    ChatListBean bean;

                    bean = new ChatListBean(4,matchingUser.getUserNick(),matchingUser.getUserFace(),wordName);
                    listChat.add(bean);

                    for (int i=0;i<object.size();i++) {
                        ChatData cd = object.get(i);
                        User sender = cd.getSender();
                        String msgText = cd.getMessage();
                        String userNick = sender.getUserNick();
                        String userName = sender.getUsername();
                        String userFace=sender.getUserFace();
                        if (userName.equals(localUser.getUsername())) {
                            bean = new ChatListBean(2, msgText);
                        } else {
                            bean = new ChatListBean(1, userFace, userNick, userName, msgText);
                        }
                        listChat.add(bean);
                    }
                }
                chatListviewAdapter = new ChatListviewAdapter(ChatActivity.this, listChat);
                listChatMessageListView.setAdapter(chatListviewAdapter);
                chatListviewAdapter.notifyDataSetChanged();
                listChatMessageListView.setSelection(listChatMessageListView.getBottom());
            }

            @Override
            public void onError(int code, String msg) {
                Log.i("rfChat", "更新聊天记录失败");
            }
        });
    }


//    public int findPosition(BmobIMMessage message) {
//        int index = listChat.size();
//        int position = -1;
//        for (int i = 0; i < listChat.size(); i++) {
//            if (message.equals(listChat.get(i))) {
//                position = index;
//                break;
//            }
//        }
//        return position;
//    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            int version = Integer.valueOf(android.os.Build.VERSION.SDK);
            finish();
            if (version > 5) {
                overridePendingTransition(R.anim.hold, R.anim.back);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        EventPassger.getInstance().setChatActivity(this);
        super.onResume();
    }


    @Override
    protected void onPause() {
        EventPassger.getInstance().setChatActivity(null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast("无法选择图片");
                Log.i("Can't Choose Image",reason);
            }
        });

    }

    @Override
    public void onImagesChosen(ChosenImages images) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast("暂时不支持选择多张照片");
            }
        });
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                if (image != null) {
                    Log.i("选择图像", "Chosen Image: Is not null");
                    toast("发送照片中...");

                    //开始上传
                    String picPath = thumbnailFilePath;
                    bmobFile = new BmobFile(new File(picPath));
                    bmobFile.uploadblock(ChatActivity.this, new UploadFileListener() {

                        @Override
                        public void onSuccess() {
                            //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                            final String url=bmobFile.getFileUrl(ChatActivity.this);
                            Log.i("selectPhoto","上传文件成功:" + bmobFile.getUrl());

                            final String msg = "我发送了一张照片给你，更新最新版本的《寻》可以直接查看："+bmobFile.getUrl();

                            ChatListBean bean = new ChatListBean(2, msg);
                            listChat.add(bean);
                            chatListviewAdapter.notifyDataSetChanged();

                            //滚动到底部
                            listChatMessageListView.setSelection(listChatMessageListView.getBottom());

                            //发送消息
                            ChatData cd = new ChatData();
                            cd.setChatId(chatId);
                            cd.setMessage(msg);
                            cd.setMessageType(0);
                            cd.setSender(localUser);
                            cd.save(ChatActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    getMessages();
                                    imgSend.setImageResource(R.drawable.img_send);
                                    imgSend.setEnabled(true);

                                    //开始推送消息给收信用户
                                    String strUserNick = matchingUser.getUsername();
                                    String strMyUserNick=localUser.getUserNick();
                                    BmobPushManager bmobPush = new BmobPushManager(ChatActivity.this);
                                    BmobQuery<FindBmobPusher> query = FindBmobPusher.getQuery();
                                    query.addWhereEqualTo("uid", strUserNick);
                                    bmobPush.setQuery(query);
                                    JSONObject jo = new JSONObject();
                                    try {
                                        jo.put("alert", strMyUserNick + "：" + msg);
                                        jo.put("type", "newmsg");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (jo != null) bmobPush.pushMessage(jo);
                                    Log.i("chat", "消息发送结束");

                                    editChatSendBox.setText("");
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.i("chat", "消息发送失败");
                                    imgSend.setImageResource(R.drawable.img_send);
                                    imgSend.setEnabled(true);
                                }
                            });

                            getMessages();
                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Log.i("selectPhoto","上传文件失败：" + msg);
                            toast("发送图片失败，请检查网络");
                        }
                    });

                    //图像路径为image.getFilePathOriginal()
                    //加载图像image.getFileThumbnail();
                    //加载小图像image.getFileThumbnailSmall();
                } else {
                    Log.i("未选择图像", "Chosen Image: Is null");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {

        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

}
