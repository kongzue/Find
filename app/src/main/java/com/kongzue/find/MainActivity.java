package com.kongzue.find;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.find.bean.BannerData;
import com.kongzue.find.bean.User;
import com.kongzue.find.bean.UserRs;
import com.kongzue.find.bean.Words;
import com.kongzue.find.listviewbean.MainListBean;
import com.kongzue.find.util.BannerObject;
import com.kongzue.find.util.BaseActivity;
import com.kongzue.find.util.BounceScrollView;
import com.kongzue.find.util.EventPassger;
import com.kongzue.find.util.FindBmobPusher;
import com.kongzue.find.util.MainListviewAdapter;
import com.kongzue.find.util.TestData;
import com.kongzue.find.util.Tools;
import com.kongzue.find.work.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

public class MainActivity extends BaseActivity {

    public boolean isActivity = false;

    private boolean inited = false;
    private User localUser;
    private Handler registGetCodeHandler = new Handler();//全局handler
    private int registGetCodeTime = 60;

    private String willDeleteWord = "";
    private int deleteType;

    private Timer timer;

    //存储listview相关数据源
    private List<MainListBean> listHistoryData = new ArrayList<MainListBean>();
    private MainListviewAdapter mainHistoryListviewAdapter;
    private List<MainListBean> listMsgData = new ArrayList<MainListBean>();
    private MainListviewAdapter mainMsgListviewAdapter;
    private List<MainListBean> listFindingData = new ArrayList<MainListBean>();
    private MainListviewAdapter mainFindingListviewAdapter;
    private List<MainListBean> listHotData = new ArrayList<MainListBean>();
    private MainListviewAdapter mainHotListviewAdapter;


    //切换page相关
    private View[] pagesView;
    private int pageCount;
    private int oldTabIndex = 1;
    private LayoutInflater m_Inflater = null;// 视图加载器
    private ArrayList<View> m_contentViewList = new ArrayList<View>(); // 内容视图列表
    private ArrayList<TextView> m_tabList = new ArrayList<TextView>(); // 标签列表

    //main界面相关组件
    private LinearLayout sysStatusBar;
    private ImageView imgMainBkgNosign;
    private ImageView imgMainBkg;
    private HorizontalScrollView tabScroll;
    private LinearLayout tabBar;
    private ViewPager tabContentPager;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;
    private ImageView imgMainNavLove;
    private ImageView imgMainNavHome;
    private ImageView imgMainNavMessage;
    private ImageView imgMainNavTab;
    private RelativeLayout boxMainMenuBkg;
    private RelativeLayout boxMainMenu;
    private TextView txtMainMenuUsernick;
    private ListView listMainMenuListview;
    private SimpleDraweeView imgMainMenuUserface;
    private RelativeLayout boxMainLogin;
    private ImageView imgMainLoginTitleImage;
    private TextView txtMainLoginTitle;
    private TextView txtMainLoginInfo;
    private EditText editLoginLoginPhone;
    private EditText editMainLoginPassword;
    private LinearLayout boxMainRegist;
    private EditText editMainRegistUserNick;
    private EditText editMainRegistCode;
    private Button btnMainRegistGetCode;
    private Button btnMainLoginLoginButton;
    private Button btnMainUserAgreement;
    private Button btnMainLoginForgetPsd;
    private RelativeLayout boxMainDeleteWorditem;
    private TextView txtMainDeleteWorditemTitle;
    private TextView txtMainDeleteWorditemInfo;
    private TextView txtMainDeleteWorditemWord;
    private Button btnMainDeleteWorditemOk;
    private Button btnMainDeleteWorditemNo;

    //main中的home页面相关组件
    private RelativeLayout boxMainHomeBkg;
    private SimpleDraweeView imgMainPagehomeUserface;
    private ImageView imgMainHomeLogo;
    private TextView txtMainHomeSearchbox;
    private TextView txtMainHomeGuess;
    private RelativeLayout boxMainHomeSuggestList;
    private ListView listMainHomeSearchSuggestListview;
    private EditText editMainHomeSearchbox;
    private ImageView imgMainHomeSearchSuggestBkg;
    private ImageView imgMainHomeSearchButton;
    private LinearLayout sysStatusBarHome;

    //main中的love页面相关组件
    private ImageView imgMainLovepageTitle;
    private BounceScrollView mainHotScrollView;
    private RelativeLayout boxBinner;
    private RelativeLayout boxMainHot;
    private ListView listMainHotListview;
    private TextView txtMainHotTitle;
    private ImageView imgMainFindError;


    //include:main界面热门界面中banner
    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
    private ViewPager adViewPager;
    private List<SimpleDraweeView> imageViews;// 滑动的图片集合
    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;
    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;
    private int currentItem = 0; // 当前图片的索引号
    // 定义的五个指示点
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;
    private ScheduledExecutorService scheduledExecutorService;
    // 轮播banner的数据
    private List<BannerObject> adList;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adViewPager.setCurrentItem(currentItem);
        }

        ;
    };


    //main中的message页面相关组件
    private LinearLayout sysStatusBarMessage;
    private ImageView imgMainMsgpageTitle;
    private BounceScrollView mainMsgScrollView;
    private RelativeLayout boxMainMsgBox;
    private ListView listMainMsgChatlist;
    private TextView txtMainMsgTitleChating;
    private RelativeLayout boxMainFindingBox;
    private ListView listMainMsgFindlist;
    private TextView txtMainMsgTitleFinding;
    private ImageView imgMainMessageError;

    //home界面操作相关
    private boolean openMainHomeEditbox;


    /**
     * 注册所有组件
     */
    private void assignViews() {
        //注册main主界面的组件
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        imgMainBkgNosign = (ImageView) findViewById(R.id.img_main_bkg_nosign);
        imgMainBkg = (ImageView) findViewById(R.id.img_main_bkg);
        tabScroll = (HorizontalScrollView) findViewById(R.id.tab_scroll);
        tabBar = (LinearLayout) findViewById(R.id.tab_bar);
        tabContentPager = (ViewPager) findViewById(R.id.tab_content_pager);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        imgMainNavLove = (ImageView) findViewById(R.id.img_main_nav_love);
        imgMainNavHome = (ImageView) findViewById(R.id.img_main_nav_home);
        imgMainNavMessage = (ImageView) findViewById(R.id.img_main_nav_message);
        imgMainNavTab = (ImageView) findViewById(R.id.img_main_nav_tab);
        boxMainMenuBkg = (RelativeLayout) findViewById(R.id.box_main_menu_bkg);
        boxMainMenu = (RelativeLayout) findViewById(R.id.box_main_menu);
        txtMainMenuUsernick = (TextView) findViewById(R.id.txt_main_menu_usernick);
        listMainMenuListview = (ListView) findViewById(R.id.list_main_menu_listview);
        imgMainMenuUserface = (SimpleDraweeView) findViewById(R.id.img_main_menu_userface);
        boxMainLogin = (RelativeLayout) findViewById(R.id.box_main_login);
        imgMainLoginTitleImage = (ImageView) findViewById(R.id.img_main_login_titleImage);
        txtMainLoginTitle = (TextView) findViewById(R.id.txt_main_login_title);
        txtMainLoginInfo = (TextView) findViewById(R.id.txt_main_login_info);
        editLoginLoginPhone = (EditText) findViewById(R.id.edit_login_login_phone);
        editMainLoginPassword = (EditText) findViewById(R.id.edit_main_login_password);
        boxMainRegist = (LinearLayout) findViewById(R.id.box_main_regist);
        editMainRegistUserNick = (EditText) findViewById(R.id.edit_main_regist_userNick);
        editMainRegistCode = (EditText) findViewById(R.id.edit_main_regist_code);
        btnMainRegistGetCode = (Button) findViewById(R.id.btn_main_regist_getCode);
        btnMainLoginLoginButton = (Button) findViewById(R.id.btn_main_login_loginButton);
        btnMainUserAgreement = (Button) findViewById(R.id.btn_main_userAgreement);
        btnMainLoginForgetPsd = (Button) findViewById(R.id.btn_main_login_forgetPsd);
        boxMainDeleteWorditem = (RelativeLayout) findViewById(R.id.box_main_delete_worditem);
        txtMainDeleteWorditemTitle = (TextView) findViewById(R.id.txt_main_delete_worditem_title);
        txtMainDeleteWorditemInfo = (TextView) findViewById(R.id.txt_main_delete_worditem_info);
        txtMainDeleteWorditemWord = (TextView) findViewById(R.id.txt_main_delete_worditem_word);
        btnMainDeleteWorditemOk = (Button) findViewById(R.id.btn_main_delete_worditem_ok);
        btnMainDeleteWorditemNo = (Button) findViewById(R.id.btn_main_delete_worditem_no);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();      //注册组件
        init();             //预先加载事项
        setEvent();         //设置监听器事件
    }//end onCreate()

    /**
     * 设置监听器事件
     */
    private void setEvent() {
        //底部三个导航按钮
        imgMainNavLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(0);
                changeEditBoxStatus(false);
            }
        });
        imgMainNavHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(1);
                changeEditBoxStatus(false);
            }
        });
        imgMainNavMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(2);
                changeEditBoxStatus(false);
                refreshMessage();
            }
        });
        //点击背景隐藏搜索框
        boxMainHomeBkg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    changeEditBoxStatus(false);
                }
                return false;
            }
        });
        //显示搜索框
        txtMainHomeSearchbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditBoxStatus(true);
            }
        });
        //点击搜索建议菜单
        listMainHomeSearchSuggestListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = ((TextView) view.findViewById(R.id.item_main_listview_title_textTitle)).getText().toString();
                editMainHomeSearchbox.setText(word);
                editMainHomeSearchbox.setSelection(word.length());
            }
        });
        //搜索文本框监听，如果有字显示搜索建议以及搜索按钮，反正隐藏
        editMainHomeSearchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().equals("")) {
                    changeSearchSuggestListview(false);
                    changeSearchButtonVis(false);
                    return;
                }
                changeSearchButtonVis(true);
                //获取搜索测试用建议
                List<MainListBean> listSuggestData = TestData.getInstance().getTestListSuggestData(s.toString());
                if (listSuggestData.size() == 0) {
                    changeSearchSuggestListview(false);
                    return;
                }
                changeSearchSuggestListview(true);

                MainListviewAdapter mainSuggestListviewAdapter = new MainListviewAdapter(MainActivity.this, listSuggestData);
                listMainHomeSearchSuggestListview.setAdapter(mainSuggestListviewAdapter);
                mainSuggestListviewAdapter.notifyDataSetChanged();

                refreshListViewHeight(listMainHomeSearchSuggestListview, mainSuggestListviewAdapter, 16, 50);
            }
        });
        //搜索按钮点击
        imgMainHomeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localUser == null) {
                    showLoginBox(true);
                    toast("您需要先注册一个账号");
                    return;
                }

                imgMainHomeSearchButton.setEnabled(false);
                toast("开始搜寻...");

                final String searchWord = editMainHomeSearchbox.getText().toString().toUpperCase();

                addFindHistory(searchWord);

                BmobQuery<Words> query = new BmobQuery<Words>();
                query.addWhereEqualTo("wordName", searchWord);
                query.setLimit(1);
                query.include("matchingUser");
                query.findObjects(MainActivity.this, new FindListener<Words>() {
                    @Override
                    public void onSuccess(List<Words> list) {
                        if (list.size() == 0) {
                            Log.i("find:", "搜索的词不存在，开始创建并匹配。");
                            Words words = new Words();
                            words.setCreateUser(localUser);
                            words.setMatchingUser(localUser);
                            words.setWordCanUse(true);
                            words.setWordName(searchWord);
                            words.setWordTimes(1);
                            words.save(MainActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    toast("您是第一个对" + searchWord + "感兴趣的人，寻已经开始帮您寻找志同道合的人，请稍候");
                                    changeEditBoxStatus(false);
                                    imgMainHomeSearchButton.setEnabled(true);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    toast("网络好像出了点问题，请稍候重试");
                                    imgMainHomeSearchButton.setEnabled(true);
                                }
                            });
                        } else {
                            Words word = list.get(0);
                            User matchingUser = word.getMatchingUser();
                            imgMainHomeSearchButton.setEnabled(true);
                            if (matchingUser == null) {
                                Log.i("find:", "搜索的词是存在的，但同好是空的，开始匹配。");
                                changeEditBoxStatus(false);
                                word.setMatchingUser(localUser);
                                word.increment("wordTimes");            //搜索次数+1
                                word.update(MainActivity.this);
                                toast("寻已经开始帮您寻找志同道合的人，请稍候");
                            } else {
                                changeEditBoxStatus(false);
                                if (matchingUser.getUsername()==null){
                                    log("matchingUser.getUsername=null");
                                    toast("出了一点问题，匹配这个词的用户丢失在的宇宙中，我们正在努力寻找它...");
                                    word.remove("matchingUser");
                                    word.update(MainActivity.this);
                                    return;
                                }
                                if (localUser.getUsername()==null){
                                    toast("您还没有登录！");
                                    log("localUser.getUsername=null");
                                    return;
                                }
                                if (matchingUser.getUsername().equals(localUser.getUsername())) {
                                    toast("您已经在寻找" + word.getWordName() + "，寻正在全力为您匹配");
                                    TabClick(2);
                                    return;
                                }
                                Log.i("find:", "搜索的词是存在的，同好是" + matchingUser.getUserNick() + "，以匹配到。准备开始清空同好");
                                toast("寻已经找到与您志同道合的人，是" + matchingUser.getUserNick());
                                startChat(matchingUser, word.getWordName());
                                word.remove("matchingUser");
                                word.increment("wordTimes");            //搜索次数+1
                                word.update(MainActivity.this);

                                //开始推送消息给收信用户
                                String strUserNick = matchingUser.getUsername();
                                String strMyUserNick=localUser.getUserNick();
                                BmobPushManager bmobPush = new BmobPushManager(MainActivity.this);
                                BmobQuery<FindBmobPusher> query = FindBmobPusher.getQuery();
                                query.addWhereEqualTo("uid", strUserNick);
                                bmobPush.setQuery(query);
                                JSONObject jo = new JSONObject();
                                try {
                                    jo.put("alert", "已经找到与你同样喜欢 " +word.getWordName() +" 的 "+strUserNick);
                                    jo.put("type", "newmsg");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (jo != null) bmobPush.pushMessage(jo);
                            }
                        }
                        refreshFindingList();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("query:", "error");
                        toast("网络好像出了点问题，请稍候重试");
                        imgMainHomeSearchButton.setEnabled(true);
                    }
                });
            }
        });
        //点击头像显示菜单
        imgMainPagehomeUserface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxMainMenuBkg.setVisibility(View.VISIBLE);
                boxMainMenu.clearAnimation();
                boxMainMenu.setVisibility(View.VISIBLE);
                AlphaAnimation am = new AlphaAnimation(0f, 1f);
                am.setDuration(300);
                am.setFillAfter(true);
                boxMainMenu.startAnimation(am);
            }
        });
        //隐藏菜单
        boxMainMenuBkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });
        //菜单点击
        listMainMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                Intent intent;
                switch (position) {
                    case 0://设置
                        changeEditBoxStatus(false);
                        if (localUser == null) {
                            toast("您需要登录后才可以设置");
                            showLoginBox(true);
                        } else {
                            intent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivity(intent);
//                        if (version > 5) {
//                            overridePendingTransition(R.anim.fade, R.anim.hold);
//                        }
                        }
                        break;
                    case 1://关于
                        changeEditBoxStatus(false);
                        intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        if (version > 5) {
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        }
                        break;
                    case 2://退出
                        finish();
                        break;
                    default:
                        break;
                }
                hideMenu();
            }
        });
        //消息列表点击
        listMainMsgChatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String chatId = ((TextView) view.findViewById(R.id.txt_main_msg_userName)).getText().toString();

                BmobQuery<UserRs> query = new BmobQuery<UserRs>();
                query.include("userA,userB");
                query.getObject(MainActivity.this, chatId, new GetListener<UserRs>() {

                    @Override
                    public void onSuccess(UserRs object) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chatId", object.getObjectId());
                        User matchingUser;
                        if (object.getUserA().getUsername().equals(localUser.getUsername())) {
                            matchingUser = object.getUserB();
                        } else {
                            matchingUser = object.getUserA();
                        }
                        bundle.putSerializable("matchingUser", matchingUser);
                        bundle.putSerializable("wordName", object.getWord());
                        startActivity(ChatActivity.class, bundle, false);
                        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                        if (version > 5) {
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        }
                    }

                    @Override
                    public void onFailure(int code, String arg0) {
                        // TODO Auto-generated method stub
                        toast("查询失败：" + arg0);
                    }

                });

            }
        });
        txtMainMenuUsernick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
                if (localUser == null) {
                    showLoginBox(true);
                } else {

                }
            }
        });
        imgMainMenuUserface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
                if (localUser == null) {
                    showLoginBox(true);
                } else {

                }
            }
        });
        //隐藏登录框
        boxMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginBox(false);
            }
        });

        editLoginLoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if (inputText.length()==11){
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", inputText);
                    query.setLimit(1);
                    query.findObjects(MainActivity.this, new FindListener<User>() {
                        @Override
                        public void onSuccess(List<User> object) {
                            log("查询成功：共"+object.size()+"条数据。");
                            if (object.size()==0){
                                boxMainRegist.setVisibility(View.VISIBLE);
                                txtMainLoginTitle.setText("注册新用户");
                                txtMainLoginInfo.setText("您的手机号未注册过《寻》，请输入以下信息开始注册");
                                editMainLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                btnMainLoginLoginButton.setText("注册");
                            }else{
                                txtMainLoginTitle.setText("登录您的账号");
                                txtMainLoginInfo.setText("使用您的手机号进行登录后可以轻松使用《寻》的功能");
                                boxMainRegist.setVisibility(View.GONE);
                                btnMainLoginLoginButton.setText("登录");
                                editMainLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            }
                        }
                        @Override
                        public void onError(int code, String msg) {
                            log("查询失败："+msg);
                        }
                    });
                }
            }
        });

        //用户协议
        btnMainUserAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("url","http://kongzuewebsite.sinaapp.com/find/agreement.html");
                startActivity(intent);
            }
        });

        //登录
        btnMainLoginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (boxMainRegist.getVisibility()==View.GONE) {

                    if (editLoginLoginPhone.getText().toString()==null || editLoginLoginPhone.getText().toString().isEmpty()){
                        toast("请输入用户名");
                        return;
                    }
                    if (editMainLoginPassword.getText().toString()==null || editMainLoginPassword.getText().toString().isEmpty()){
                        toast("请输入密码");
                        return;
                    }

                    //登陆情况下
                    btnMainLoginLoginButton.setEnabled(false);
                    btnMainLoginLoginButton.setText("登陆中…");

                    try {
                        UserLogin.getInstance().login(editLoginLoginPhone.getText().toString(), editMainLoginPassword.getText().toString(), new LogInListener() {

                            @Override
                            public void done(Object o, BmobException e) {
                                if (e == null) {
                                    localUser = (User) o;
                                    toast("登录成功，欢迎回来，" + localUser.getUserNick());
                                    txtMainMenuUsernick.setText(localUser.getUserNick());

                                    imgMainMenuUserface.setImageURI(Uri.parse(localUser.getUserFace()));
                                    imgMainPagehomeUserface.setImageURI(Uri.parse(localUser.getUserFace()));

                                    showLoginBox(false);
                                } else {
                                    e.printStackTrace();
                                }
                                btnMainLoginLoginButton.setEnabled(true);
                                btnMainLoginLoginButton.setText("登陆");
                            }
                        });
                    }catch (Exception e){
                        toast("登录失败");
                        e.printStackTrace();
                    }
                }else{
                    //注册情况下
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"官方")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"中国")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"国家")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"政府")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"习近平")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"kongzue")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }
                    if (Tools.isHaveWord(editMainRegistUserNick.getText().toString(),"空祖")){
                        toast("您的昵称中存在非法字符");
                        return;
                    }

                    btnMainLoginLoginButton.setEnabled(false);
                    btnMainLoginLoginButton.setText("注册中…");

                    BmobSMS.verifySmsCode(MainActivity.this, editLoginLoginPhone.getText().toString(), editMainRegistCode.getText().toString(), new VerifySMSCodeListener() {

                        @Override
                        public void done(BmobException ex) {

                            if (ex == null) {//短信验证码已验证成功
                                Log.i("regist", "验证通过");
                                //开始注册
                                UserLogin.getInstance().register(editLoginLoginPhone.getText().toString(), editMainLoginPassword.getText().toString(), editMainRegistUserNick.getText().toString(), new LogInListener() {
                                    @Override
                                    public void done(Object o, BmobException e) {
                                        if (e == null) {
                                            toast("注册成功。");

                                            //开始登陆
                                            try {
                                                UserLogin.getInstance().login(editLoginLoginPhone.getText().toString(), editMainLoginPassword.getText().toString(), new LogInListener() {

                                                    @Override
                                                    public void done(Object o, BmobException e) {
                                                        if (e == null) {
                                                            localUser = (User) o;
                                                            toast("登录成功，欢迎回来，" + localUser.getUserNick());
                                                            txtMainMenuUsernick.setText(localUser.getUserNick());

                                                            if (localUser.getUserFace()!=null) {
                                                                imgMainMenuUserface.setImageURI(Uri.parse(localUser.getUserFace()));
                                                                imgMainPagehomeUserface.setImageURI(Uri.parse(localUser.getUserFace()));
                                                            }

                                                            showLoginBox(false);
                                                        } else {
                                                            e.printStackTrace();
                                                        }
                                                        btnMainLoginLoginButton.setEnabled(true);
                                                        btnMainLoginLoginButton.setText("登陆");
                                                    }
                                                });
                                            }catch (Exception r){
                                                toast("登录失败");
                                                r.printStackTrace();
                                            }
                                        } else {
                                            toast("注册失败。");
                                        }
                                        btnMainLoginLoginButton.setEnabled(true);
                                        btnMainLoginLoginButton.setText("注册");
                                    }
                                });
                            } else {
                                Log.i("regist", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                                toast("验证码错误，请重新获取验证码！");
                            }
                        }
                    });
                }
            }
        });

        //验证码
        btnMainRegistGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(MainActivity.this, editLoginLoginPhone.getText().toString(), "注册", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        // TODO Auto-generated method stub
                        if (ex == null) {//验证码发送成功
                            toast("验证码已发送。");
                        } else {
                            toast("验证码发送失败！");
                        }
                    }
                });
                new Thread(new ClassIdCodeTime()).start();//开启倒计时
                btnMainRegistGetCode.setEnabled(false);
            }
        });

        //消息界面错误提示图片点击后
        imgMainMessageError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(1);
                if (localUser == null) {
                    showLoginBox(true);
                }
            }
        });

//        备注，热词界面改版取消了标签切换功能
        //热词页面正在寻找标签点击
//        imgMainLovepageTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lovePageTabClick(0);
//            }
//        });
        //热词页面热词标签点击
//        imgMainLovepageHot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lovePageTabClick(1);
//            }
//        });

//        备注，历史功能暂时删除，所以此方法暂停使用
//        //热词历史列表点击
//        listMainLoveHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String word=((TextView)view.findViewById(R.id.item_main_listview_title_textTitle)).getText().toString();
//                for (MainListBean m:listFindingData){
//                    String w=m.getWord_text();
//                    if (w.equals(word)){
//                        toast("您已经在寻找"+w+"兴趣相同的人，请稍候~");
//                        return;
//                    }else{
//                        txtMainHomeSearchbox.setText(word);
//                        editMainHomeSearchbox.setText(word);
//                        TabClick(1);
//                        changeEditBoxStatus(true);
//                    }
//                }
//            }
//        });
//        //长按删除热词历史
//        listMainLoveHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                deleteType=2;
//                String word=((TextView)view.findViewById(R.id.item_main_listview_title_textTitle)).getText().toString();
//                willDeleteWord=word;
//                txtMainDeleteWorditemWord.setText(willDeleteWord);
//                showWordDeleteDialog(true);
//                return true;
//            }
//        });
        //正在搜索热词点击
        listMainMsgFindlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toast("这里显示的是您正在寻找的兴趣词，长按可以删除");
            }
        });
        //长按删除兴趣热词
        listMainMsgFindlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteType = 1;
                String word = ((TextView) view.findViewById(R.id.item_main_listview_title_textTitle)).getText().toString();
                willDeleteWord = word;
                txtMainDeleteWorditemWord.setText(willDeleteWord);
                showWordDeleteDialog(true);
                return true;
            }
        });
        //关闭删除确认对话框
        boxMainDeleteWorditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWordDeleteDialog(false);
            }
        });
        btnMainDeleteWorditemNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWordDeleteDialog(false);
            }
        });
        //开始删除
        btnMainDeleteWorditemOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtMainDeleteWorditemWord.getText() != null && !txtMainDeleteWorditemWord.getText().toString().equals("")) {
                    Log.i("deleteWord", "开始删除" + txtMainDeleteWorditemWord.getText());
                    if (deleteType == 1) {
                        deleteMatching(txtMainDeleteWorditemWord.getText().toString());
                    } else if (deleteType == 2) {
                        deleteHistory(txtMainDeleteWorditemWord.getText().toString());
                    }
                } else {
                    Log.i("deleteWord", "无法删除");
                    toast("删除寻词失败");
                }
                showWordDeleteDialog(false);
            }
        });
    }//end setEvent()

    private void deleteMatching(String searchWord) {
        BmobQuery<Words> query = new BmobQuery<Words>();
        query.addWhereEqualTo("wordName", searchWord);
        query.setLimit(1);
        query.findObjects(MainActivity.this, new FindListener<Words>() {
            @Override
            public void onSuccess(List<Words> list) {
                Log.i("deleteMatching:", "删除正在匹配成功");
                Words word = list.get(0);
                word.remove("matchingUser");
                word.update(MainActivity.this);
                refreshFindingList();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("query:", "error");
                toast("好像连不上网了呢QAQ");
            }
        });
    }

    private void showWordDeleteDialog(boolean vis) {
        boxMainDeleteWorditem.clearAnimation();
        if (vis) {
            if (boxMainDeleteWorditem.getVisibility() == View.VISIBLE) {
                return;
            }
            if (deleteType == 1) {
                txtMainDeleteWorditemTitle.setText("删除正在寻找");
                txtMainDeleteWorditemInfo.setText("您确定删除正在匹配的兴趣词" + willDeleteWord + "吗？");
            } else if (deleteType == 2) {
                txtMainDeleteWorditemTitle.setText("删除历史");
                txtMainDeleteWorditemInfo.setText("您确定删除历史兴趣词" + willDeleteWord + "吗？");
            }
            boxMainDeleteWorditem.setVisibility(View.VISIBLE);
            AlphaAnimation am = new AlphaAnimation(0f, 1f);
            am.setDuration(300);
            am.setFillAfter(true);
            boxMainDeleteWorditem.startAnimation(am);
        } else {
            if (boxMainDeleteWorditem.getVisibility() == View.GONE) {
                return;
            }
            deleteType = 0;
            willDeleteWord = "";
            AlphaAnimation am = new AlphaAnimation(1f, 0f);
            am.setDuration(300);
            am.setFillAfter(true);
            boxMainDeleteWorditem.startAnimation(am);
            am.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    boxMainDeleteWorditem.clearAnimation();
                    boxMainDeleteWorditem.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void addFindHistory(String searchWord) {
        String historyDatas = "";
        String historyTime = "";
        for (int i = 0; i < listHistoryData.size(); i++) {
            MainListBean b = listHistoryData.get(i);
            if (b.getWord_text().equals(searchWord)) {
                //历史记录中存在该词汇
                listHistoryData.remove(i);
            }
            historyDatas = historyDatas + "/-/" + b.getWord_text();
            historyTime = historyTime + "/-/" + b.getWord_tip();
        }
        historyDatas = historyDatas + "/-/" + searchWord;
        historyTime = historyTime + "/-/" + (new SimpleDateFormat("HH:mm")).format(new Date());


        SharedPreferences sp = getSharedPreferences("find_history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("history", historyDatas);
        editor.putString("time", historyTime);
        editor.commit();
        MainListBean bean = new MainListBean(1, searchWord, (new SimpleDateFormat("HH:mm")).format(new Date()));
        listHistoryData.add(bean);

        refreshFindingList();
    }

    private void startChat(final User matchingUser, final String wordName) {
        Log.i("chat", "进入会话");
        BmobQuery<UserRs> eq1 = new BmobQuery<UserRs>();
        eq1.addWhereEqualTo("userA", matchingUser);
        eq1.addWhereEqualTo("userB", localUser);
        BmobQuery<UserRs> eq2 = new BmobQuery<UserRs>();
        eq2.addWhereEqualTo("userA", localUser);
        eq2.addWhereEqualTo("userB", matchingUser);
        List<BmobQuery<UserRs>> queries = new ArrayList<BmobQuery<UserRs>>();
        queries.add(eq1);
        queries.add(eq2);
        BmobQuery<UserRs> mainQuery = new BmobQuery<UserRs>();
        mainQuery.addWhereEqualTo("word", wordName);
        mainQuery.or(queries);
        mainQuery.findObjects(this, new FindListener<UserRs>() {
            @Override
            public void onSuccess(List<UserRs> object) {
                if (object.size() == 0) {
                    final UserRs us = new UserRs();
                    us.setUserA(localUser);
                    us.setUserB(matchingUser);
                    us.setWord(wordName);
                    us.save(MainActivity.this, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            Log.i("addRs", "添加关系成功，开始创建会话" + us.getObjectId());

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("chatId", us.getObjectId());
                            bundle.putSerializable("matchingUser", matchingUser);
                            bundle.putSerializable("wordName", wordName);
                            startActivity(ChatActivity.class, bundle, false);
                            int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                            if (version > 5) {
                                overridePendingTransition(R.anim.fade, R.anim.hold);
                            }
                            refreshMessage();
                        }

                        @Override
                        public void onFailure(int code, String arg0) {
                            Log.i("addRs", "添加关系失败");
                            toast("无法为您寻找，请检查网络");
                        }
                    });
                } else {
                    //存在相同的会话
                    UserRs ur = object.get(0);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chatId", ur.getObjectId());
                    bundle.putSerializable("matchingUser", matchingUser);
                    bundle.putSerializable("wordName", wordName);
                    startActivity(ChatActivity.class, bundle, false);
                    int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                    if (version > 5) {
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                    }
                    refreshMessage();
                }
            }

            @Override
            public void onError(int code, String msg) {


            }
        });
    }


    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    /**
     * 显示登录提示框
     */
    public void showLoginBox(boolean vis) {
        boxMainLogin.clearAnimation();
        if (vis) {
            if (boxMainLogin.getVisibility() == View.VISIBLE) {
                return;
            }
            boxMainLogin.setVisibility(View.VISIBLE);
            AlphaAnimation am = new AlphaAnimation(0f, 1f);
            am.setDuration(300);
            am.setFillAfter(true);
            boxMainLogin.startAnimation(am);

            am.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    editLoginLoginPhone.setFocusable(true);
                    editLoginLoginPhone.requestFocus();
                    editLoginLoginPhone.setFocusableInTouchMode(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editLoginLoginPhone, InputMethodManager.SHOW_FORCED);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            if (boxMainLogin.getVisibility() == View.GONE) {
                return;
            }
            AlphaAnimation am = new AlphaAnimation(1f, 0f);
            am.setDuration(300);
            am.setFillAfter(true);
            boxMainLogin.startAnimation(am);
            am.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    boxMainLogin.clearAnimation();
                    boxMainLogin.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editLoginLoginPhone.getWindowToken(), 0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }//end showRegistBox()


    /**
     * 隐藏菜单
     */
    private void hideMenu() {
        boxMainMenuBkg.setVisibility(View.GONE);
        boxMainMenu.clearAnimation();
        AlphaAnimation am = new AlphaAnimation(1f, 0f);
        am.setDuration(300);
        am.setFillAfter(true);
        boxMainMenu.startAnimation(am);
        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boxMainMenu.clearAnimation();
                boxMainMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }//end hideMenu()

    /**
     * 设置是否显示搜索按钮
     */
    private void changeSearchButtonVis(boolean vis) {
        imgMainHomeSearchButton.clearAnimation();
        if (vis && imgMainHomeSearchButton.getVisibility() == View.GONE) {
            imgMainHomeSearchButton.setVisibility(View.VISIBLE);
            AlphaAnimation am = new AlphaAnimation(0f, 1f);
            am.setDuration(500);
            am.setFillAfter(true);
            imgMainHomeSearchButton.startAnimation(am);
        } else if (!vis && imgMainHomeSearchButton.getVisibility() == View.VISIBLE) {
            AlphaAnimation am = new AlphaAnimation(1f, 0f);
            am.setDuration(300);
            am.setFillAfter(true);
            imgMainHomeSearchButton.startAnimation(am);
            am.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imgMainHomeSearchButton.clearAnimation();
                    imgMainHomeSearchButton.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * 设置是否显示搜索建议
     */
    private void changeSearchSuggestListview(boolean vis) {
        boxMainHomeSuggestList.clearAnimation();
        imgMainHomeSearchSuggestBkg.clearAnimation();
        if (vis) {
            if (boxMainHomeSuggestList.getVisibility() == View.GONE) {
                AlphaAnimation am = new AlphaAnimation(0f, 1f);
                am.setDuration(500);
                am.setFillAfter(true);
                boxMainHomeSuggestList.startAnimation(am);
                imgMainHomeSearchSuggestBkg.startAnimation(am);
            }
            boxMainHomeSuggestList.setVisibility(View.VISIBLE);
            imgMainHomeSearchSuggestBkg.setVisibility(View.VISIBLE);
        } else {
            if (boxMainHomeSuggestList.getVisibility() == View.VISIBLE) {
                AlphaAnimation am = new AlphaAnimation(1f, 0f);
                am.setDuration(500);
                am.setFillAfter(true);
                boxMainHomeSuggestList.startAnimation(am);
                imgMainHomeSearchSuggestBkg.startAnimation(am);
                am.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        boxMainHomeSuggestList.clearAnimation();
                        imgMainHomeSearchSuggestBkg.clearAnimation();
                        boxMainHomeSuggestList.setVisibility(View.GONE);
                        imgMainHomeSearchSuggestBkg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }//end changeSearchSuggestListview()

    /**
     * 设置文本框是否开启
     */
    private void changeEditBoxStatus(boolean isOpened) {
        //Log.i(">>>",openMainHomeEditbox+","+isOpened);
        if (openMainHomeEditbox == isOpened) {
            return;
        }
        final int translateValue = Tools.dip2px(MainActivity.this, 243);
        if (isOpened) {
            //文本框位移动画
            TranslateAnimation ta = new TranslateAnimation(0f, 0f, 0f, -translateValue);
            ta.setDuration(300);
            ta.setFillAfter(true);
            txtMainHomeSearchbox.startAnimation(ta);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    editMainHomeSearchbox.setVisibility(View.VISIBLE);
                    txtMainHomeSearchbox.clearAnimation();
                    txtMainHomeSearchbox.setVisibility(View.GONE);
                    //开启输入法
                    editMainHomeSearchbox.setFocusable(true);
                    editMainHomeSearchbox.requestFocus();
                    editMainHomeSearchbox.setFocusableInTouchMode(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editMainHomeSearchbox, InputMethodManager.SHOW_FORCED);
                    //如果文本框有文字，则显示搜索按钮
                    if (!editMainHomeSearchbox.getText().toString().equals(""))
                        changeSearchButtonVis(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //其他渐变
            AlphaAnimation am = new AlphaAnimation(1f, 0f);
            am.setDuration(500);
            am.setFillAfter(true);
            imgMainHomeLogo.startAnimation(am);
            imgMainPagehomeUserface.startAnimation(am);
            txtMainHomeGuess.startAnimation(am);

        } else {
            changeSearchButtonVis(false);
            //关闭输入法
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editMainHomeSearchbox.getWindowToken(), 0);
            editMainHomeSearchbox.setVisibility(View.GONE);
            txtMainHomeSearchbox.setVisibility(View.VISIBLE);
            txtMainHomeSearchbox.setText(editMainHomeSearchbox.getText().toString());
            //文本框位移动画
            TranslateAnimation ta = new TranslateAnimation(0f, 0f, -translateValue, 0f);
            ta.setDuration(300);
            ta.setFillAfter(true);
            txtMainHomeSearchbox.startAnimation(ta);
            //其他渐变
            AlphaAnimation am = new AlphaAnimation(0f, 1.0f);
            am.setDuration(500);
            am.setFillAfter(true);
            imgMainHomeLogo.startAnimation(am);
            imgMainPagehomeUserface.startAnimation(am);
            txtMainHomeGuess.startAnimation(am);

            //关闭搜索建议
            boxMainHomeSuggestList.clearAnimation();
            imgMainHomeSearchSuggestBkg.clearAnimation();
            boxMainHomeSuggestList.setVisibility(View.GONE);
            imgMainHomeSearchSuggestBkg.setVisibility(View.GONE);
        }
        openMainHomeEditbox = isOpened;
    }//end changeEditBoxStatus()

    /**
     * 初始化组件
     */
    private void init() {

        //自动登录
        localUser = UserLogin.getInstance().getCurrentUser();
        //更新设备信息
        if (localUser!=null) {
            BmobQuery<FindBmobPusher> query = new BmobQuery<FindBmobPusher>();
            query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(EventPassger.getInstance().getMyApplication()));
            query.findObjects(EventPassger.getInstance().getMyApplication(), new FindListener<FindBmobPusher>() {

                @Override
                public void onSuccess(List<FindBmobPusher> object) {
                    if (object.size() > 0) {
                        FindBmobPusher mbi = object.get(0);
                        mbi.setUid(localUser.getUsername());
                        mbi.update(EventPassger.getInstance().getMyApplication(), new UpdateListener() {

                            @Override
                            public void onSuccess() {
                                Log.i("log", "设备信息更新成功");
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                Log.i("log", "设备信息更新失败:" + msg);
                            }
                        });

                        EventPassger.getInstance().callMainAvtivityRefreshMessageList();
                    } else {
                    }
                }

                @Override
                public void onError(int code, String msg) {
                }
            });
        }else{
            log("未更新设备信息");
        }

        //初始化切换tab
        pageCount = 3;
        addPages();
        tabContentPager.setAdapter(new TabContentViewPagerAdapter());
        tabContentPager.setOffscreenPageLimit(3);       //缓存3个页面
        tabContentPager.setOnPageChangeListener(new TabContentPager_OnPageChangeListener());
        TabClick(1);
        /**
         * 设置状态栏高度分割（针对安卓4.4.4+及安卓4-状态栏是否透明的问题需要再有透明状态栏的情况下手动设置状态栏高度占位符布局，以便将内容布局向下移动）
         * 此方法在BaseActivity中
         */
        setStatusBarHeight();
        //初始化菜单
        String[] mPath = {"个人设置", "关于", "退出"};
        listMainMenuListview.setAdapter(new ArrayAdapter<String>(this, R.layout.item_main_menu, R.id.txt_item_main_menu, mPath));
        //设置已初始化标记
        inited = true;

    }//end init()

    @Override
    protected void onResume() {
        super.onResume();

        isActivity = true;

        //加载用户信息
        localUser = UserLogin.getInstance().getCurrentUser();
        if (localUser == null) {
            txtMainMenuUsernick.setText("未登录");
            if (imgMainMenuUserface != null) imgMainMenuUserface.setImageURI(Uri.parse(""));
            if (imgMainPagehomeUserface != null) imgMainPagehomeUserface.setImageURI(Uri.parse(""));
        } else {
            txtMainMenuUsernick.setText(localUser.getUserNick());
//            if (listMainLoveHistory!=null)loadFindHistory();   暂时取消了搜索历史列表所以此刷新历史记录的方法暂废
            if (imgMainMenuUserface != null && localUser.getUserFace() != null)
                imgMainMenuUserface.setImageURI(Uri.parse(localUser.getUserFace()));
            if (imgMainPagehomeUserface != null && localUser.getUserFace() != null)
                imgMainPagehomeUserface.setImageURI(Uri.parse(localUser.getUserFace()));
        }

        refreshMessage();
        EventPassger.getInstance().setMainAactivity(this);

        if (Tools.StartFindWords!=null && !Tools.StartFindWords.isEmpty()){
            editMainHomeSearchbox.setText(Tools.StartFindWords);
            TabClick(1);
            changeEditBoxStatus(true);
            Tools.StartFindWords="";
        }

        String openInfo=getIntent().getStringExtra("open");
        if (openInfo!=null && !openInfo.isEmpty()){
            TabClick(2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {

        EventPassger.getInstance().setMainAactivity(null);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editMainHomeSearchbox.getWindowToken(), 0);
        isActivity = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
//        BmobIM.getInstance().clear();
        //完全退出应用时需调用clearObserver来清除观察者
//        BmobNotificationManager.getInstance(this).clearObserver();
    }

    //该方法用于切换热词界面，由于该界面改版取消了热词切换选项卡所以此方法已废
//    private void lovePageTabClick(int index){
//        if (index==0){          //寻找页面
//            imgMainLovepageHot.setAlpha(0.3f);
//            imgMainLovepageTitle.setAlpha(1f);
//            if (localUser==null){                                                       //未登录
//                imgMainFindError.setImageResource(R.drawable.img_main_find_nologin);
//                imgMainFindError.setVisibility(View.VISIBLE);
//                mainLoveScrollView.setVisibility(View.GONE);
//                mainHotScrollView.setVisibility(View.GONE);
//            }else {                                                                     //已登录但没消息
//                if (listMainLoveListview.getVisibility()==View.GONE && listMainLoveHistory.getVisibility()==View.GONE){
//                    imgMainFindError.setImageResource(R.drawable.img_main_find_nofinding);
//                    imgMainFindError.setVisibility(View.VISIBLE);
//                    mainLoveScrollView.setVisibility(View.GONE);
//                    mainHotScrollView.setVisibility(View.GONE);
//                }else {
//                    imgMainFindError.setVisibility(View.GONE);
//                    mainLoveScrollView.setVisibility(View.VISIBLE);
//                    mainHotScrollView.setVisibility(View.GONE);
//                }
//            }
//        }else{                  //热门页面
//            imgMainLovepageHot.setAlpha(1f);
//            imgMainLovepageTitle.setAlpha(0.3f);
//            imgMainFindError.setVisibility(View.GONE);
//            mainLoveScrollView.setVisibility(View.GONE);
//            mainHotScrollView.setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * 刷新消息
     */
    public void refreshMessage() {
        Log.i("tfmsg", "刷新消息开始");
        if (mainMsgListviewAdapter != null) {
            mainMsgListviewAdapter = new MainListviewAdapter(MainActivity.this, listMsgData);
            listMainMsgChatlist.setAdapter(mainMsgListviewAdapter);
            mainMsgListviewAdapter.notifyDataSetChanged();
        }
        if (localUser == null) {
            //不显示消息框，显示未登陆提示
            imgMainMessageError.setImageResource(R.drawable.img_main_message_nologin);
            mainMsgScrollView.setVisibility(View.GONE);
            imgMainMessageError.setVisibility(View.VISIBLE);
            return;
        } else {
            mainMsgScrollView.setVisibility(View.VISIBLE);
            imgMainMessageError.setVisibility(View.GONE);
        }

        //加载正在寻找
        refreshFindingList();

        //消息列表
        BmobQuery<UserRs> eq1 = new BmobQuery<UserRs>();
        eq1.addWhereEqualTo("userB", localUser);
        BmobQuery<UserRs> eq2 = new BmobQuery<UserRs>();
        eq2.addWhereEqualTo("userA", localUser);
        List<BmobQuery<UserRs>> queries = new ArrayList<BmobQuery<UserRs>>();
        queries.add(eq1);
        queries.add(eq2);

        BmobQuery<UserRs> msgQuery = new BmobQuery<UserRs>();
        msgQuery.include("userA,userB");
        msgQuery.or(queries);
        msgQuery.findObjects(MainActivity.this, new FindListener<UserRs>() {
            @Override
            public void onSuccess(List<UserRs> list) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                Log.i("msglist", "已刷新");
                if (list == null) {
                    Log.i("msglist", "list为空，退出刷新");
                    return;
                }
                if (list.size() == 0) {
                    boxMainMsgBox.setVisibility(View.GONE);
                } else {
                    boxMainMsgBox.setVisibility(View.VISIBLE);
                    if (listMsgData.size() != list.size()) {
                        listMsgData.clear();
                        for (UserRs us : list) {
                            String userNick, userName, newMessage, chatId, userFace;
                            User ua = us.getUserA();
                            User ub = us.getUserB();
                            newMessage = "";
                            chatId = us.getObjectId();
                            if (ua.getUsername().equals(localUser.getUsername())) {
                                userName = ub.getUsername();
                                userNick = ub.getUserNick();
                                userFace = ub.getUserFace();
                            } else {
                                userName = ua.getUsername();
                                userNick = ua.getUserNick();
                                userFace = ua.getUserFace();
                            }
                            MainListBean bean = new MainListBean(2, chatId, userNick, newMessage, us.getWord(), userFace, "", true);
                            listMsgData.add(bean);
                        }
                        mainMsgListviewAdapter = new MainListviewAdapter(MainActivity.this, listMsgData);
                        listMainMsgChatlist.setAdapter(mainMsgListviewAdapter);
                        mainMsgListviewAdapter.notifyDataSetChanged();
                        refreshListViewHeight(listMainMsgChatlist, mainMsgListviewAdapter, 0, 66);
                    }
                }
                refreshMsgLayout();
            }

            @Override
            public void onError(int i, String s) {
                if (timer == null) {
                    Log.i("msg", "消息刷新失败");
                    toast("网络好像有问题呃...");
                    boxMainMsgBox.setVisibility(View.GONE);
//
//                    timer = new Timer(true);
//                    timer.schedule(task, 100, 5000);

                    refreshMsgLayout();
                }
            }
        });
    }//end refreshMessage()

    private void refreshFindingList() {
        //寻找列表
        BmobQuery<Words> wordsQuery = new BmobQuery<Words>();
        wordsQuery.addWhereEqualTo("matchingUser", localUser);
        wordsQuery.order("-createdAt");
        wordsQuery.findObjects(MainActivity.this, new FindListener<Words>() {
            @Override
            public void onSuccess(List<Words> list) {
                Log.i("msg", "正在寻找列表刷新完成：" + list.size());
                if (list.size() == 0) {
                    boxMainFindingBox.setVisibility(View.GONE);
                } else {
                    boxMainFindingBox.setVisibility(View.VISIBLE);
                    if (list.size() != listFindingData.size()) {
                        listFindingData.clear();
                        for (int i = 0; i < list.size(); i++) {
                            Words words = list.get(i);
                            MainListBean bean = new MainListBean(1, words.getWordName(), words.getWordTimes() + "人也在寻找");
                            listFindingData.add(bean);
                        }
                        mainFindingListviewAdapter = new MainListviewAdapter(MainActivity.this, listFindingData);
                        listMainMsgFindlist.setAdapter(mainFindingListviewAdapter);
                        mainFindingListviewAdapter.notifyDataSetChanged();
                        refreshListViewHeight(listMainMsgFindlist, mainFindingListviewAdapter, 0, 56);
                    }
                }
                refreshMsgLayout();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("msg", "正在寻找列表刷新失败");
                boxMainFindingBox.setVisibility(View.GONE);
                refreshMsgLayout();
            }
        });
    }

    /**
     * 用于刷新消息界面状态，当没有消息且没有寻找时显示提示信息
     */
    private void refreshMsgLayout() {
        if (boxMainMsgBox.getVisibility() == View.GONE && boxMainFindingBox.getVisibility() == View.GONE) {
            imgMainMessageError.setImageResource(R.drawable.img_main_message_nomessage);
            imgMainMessageError.setVisibility(View.VISIBLE);
        } else {
            imgMainMessageError.setVisibility(View.GONE);
        }
    }

    private void initAdObject(View pV) {

        imageViews = new ArrayList<SimpleDraweeView>();

        // 点
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        dot0 = pV.findViewById(R.id.v_dot0);
        dot1 = pV.findViewById(R.id.v_dot1);
        dot2 = pV.findViewById(R.id.v_dot2);
        dot3 = pV.findViewById(R.id.v_dot3);
        dot4 = pV.findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);

        tv_date = (TextView) pV.findViewById(R.id.tv_date);
        tv_title = (TextView) pV.findViewById(R.id.tv_title);
        tv_topic_from = (TextView) pV.findViewById(R.id.tv_topic_from);
        tv_topic = (TextView) pV.findViewById(R.id.tv_topic);

        addDynamicView(pV);
        adViewPager = (ViewPager) pV.findViewById(R.id.vp);
        adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void addDynamicView(View pV) {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        for (int i = 0; i < adList.size(); i++) {
            SimpleDraweeView imageView = new SimpleDraweeView(this);
            // 异步加载图片
            imageView.setImageURI(Uri.parse(adList.get(i).getImgUrl()));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
            dots.get(i).setVisibility(View.VISIBLE);
            dotList.add(dots.get(i));
        }
    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            BannerObject BannerObject = adList.get(position);
            tv_title.setText("#" + BannerObject.getTitle()); // 设置标题
            tv_date.setText(BannerObject.getDate());
            tv_topic_from.setText(BannerObject.getTopicFrom());
            tv_topic.setText(BannerObject.getTopic());
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return adList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimpleDraweeView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            final BannerObject bannerObject = adList.get(position);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (bannerObject.getTopicFrom() == null || bannerObject.getTopicFrom().isEmpty()) {
//                        editMainHomeSearchbox.setText(bannerObject.getTopic());
//                        TabClick(1);
//                        changeEditBoxStatus(true);
                        Intent intent = new Intent(MainActivity.this,ActActivity.class);
                        intent.putExtra("photoUrl",bannerObject.getImgUrl());
                        intent.putExtra("title",bannerObject.getTitle());
                        intent.putExtra("words",bannerObject.getTopic());
                        intent.putExtra("abstract",bannerObject.getAbstractInfo());

                        startActivity(intent);
                        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                        if (version > 5) {
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        }
                    } else {
                        toast("跳转到：" + bannerObject.getTopicFrom());
                    }
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    /**
     * 添加滑动page页面
     */
    private void addPages() {
        if (m_Inflater == null) m_Inflater = getLayoutInflater();
        if (pagesView == null) pagesView = new View[pageCount];
        //绑定所有布局及注册相应布局组件
        for (int i = 0; i < pageCount; i++) {
            switch (i) {
                case 0:
                    pagesView[0] = m_Inflater.inflate(R.layout.activity_main_love, null);
                    //注册love界面的组件
                    imgMainLovepageTitle = (ImageView) pagesView[0].findViewById(R.id.img_main_lovepage_title);
                    mainHotScrollView = (BounceScrollView) pagesView[0].findViewById(R.id.main_hot_scrollView);
                    txtMainHotTitle = (TextView) pagesView[0].findViewById(R.id.txt_main_hot_title);
                    listMainHotListview = (ListView) pagesView[0].findViewById(R.id.list_main_hot_listview);
                    imgMainFindError = (ImageView) pagesView[0].findViewById(R.id.img_main_find_error);
                    boxMainHot = (RelativeLayout) pagesView[0].findViewById(R.id.box_main_hot);

                    //注册banner
                    initAdData(pagesView[0]);

                    //加载兴趣界面数据
                    loadLovePageData();
                    break;
                case 1:
                    pagesView[1] = m_Inflater.inflate(R.layout.activity_main_home, null);
                    boxMainHomeBkg = (RelativeLayout) pagesView[1].findViewById(R.id.box_main_home_bkg);
                    imgMainPagehomeUserface = (SimpleDraweeView) pagesView[1].findViewById(R.id.img_main_pagehome_userface);
                    imgMainHomeLogo = (ImageView) pagesView[1].findViewById(R.id.img_main_home_logo);
                    txtMainHomeSearchbox = (TextView) pagesView[1].findViewById(R.id.txt_main_home_searchbox);
                    txtMainHomeGuess = (TextView) pagesView[1].findViewById(R.id.txt_main_home_guess);
                    boxMainHomeSuggestList = (RelativeLayout) pagesView[1].findViewById(R.id.box_main_home_suggestList);
                    listMainHomeSearchSuggestListview = (ListView) pagesView[1].findViewById(R.id.list_main_home_searchSuggest_listview);
                    editMainHomeSearchbox = (EditText) pagesView[1].findViewById(R.id.edit_main_home_searchbox);
                    imgMainHomeSearchSuggestBkg = (ImageView) pagesView[1].findViewById(R.id.img_main_home_searchSuggest_bkg);
                    imgMainHomeSearchButton = (ImageView) pagesView[1].findViewById(R.id.img_main_home_searchButton);
                    sysStatusBarHome = (LinearLayout) pagesView[1].findViewById(R.id.sys_statusBar_home);
                    setStatusBarHeightByLayout(sysStatusBarHome);

                    break;
                case 2:
                    pagesView[2] = m_Inflater.inflate(R.layout.activity_main_message, null);
                    imgMainMsgpageTitle = (ImageView) pagesView[2].findViewById(R.id.img_main_msgpage_title);
                    mainMsgScrollView = (BounceScrollView) pagesView[2].findViewById(R.id.main_msg_scrollView);
                    boxMainMsgBox = (RelativeLayout) pagesView[2].findViewById(R.id.box_main_msg_box);
                    listMainMsgChatlist = (ListView) pagesView[2].findViewById(R.id.list_main_msg_chatlist);
                    txtMainMsgTitleChating = (TextView) pagesView[2].findViewById(R.id.txt_main_msg_title_chating);
                    boxMainFindingBox = (RelativeLayout) pagesView[2].findViewById(R.id.box_main_finding_box);
                    listMainMsgFindlist = (ListView) pagesView[2].findViewById(R.id.list_main_msg_findlist);
                    txtMainMsgTitleFinding = (TextView) pagesView[2].findViewById(R.id.txt_main_msg_title_finding);
                    imgMainMessageError = (ImageView) pagesView[2].findViewById(R.id.img_main_message_error);
                    sysStatusBarMessage = (LinearLayout) pagesView[2].findViewById(R.id.sys_statusBar_message);
                    setStatusBarHeightByLayout(sysStatusBarMessage);

                    //加载消息界面数据
                    refreshMessage();
                    break;
                default:
                    break;
            }
            m_contentViewList.add(pagesView[i]);
            TextView tab = new TextView(this);
            m_tabList.add(tab);
            tab.setId(m_tabList.size());
            tab.setTag(m_tabList.size() - 1);
            tabBar.addView(tab);
        }
    }//end addPages()

    private void initAdData(final View view) {
        BmobQuery<BannerData> query = new BmobQuery<BannerData>();
        query.setLimit(10);
        query.findObjects(this, new FindListener<BannerData>() {
            @Override
            public void onSuccess(List<BannerData> object) {
                // TODO Auto-generated method stub
                log("已获取轮播图数据。");
                adList = new ArrayList<BannerObject>();
                int index=0;
                for (BannerData bannerData : object) {
                    BannerObject BannerObject = new BannerObject();
                    BannerObject.setId(index+"");
                    BannerObject.setDate(bannerData.getCreatedAt());
                    BannerObject.setTitle(bannerData.getTitle());
                    BannerObject.setTopicFrom(bannerData.getUrl());
                    BannerObject.setTopic(bannerData.getWord());
                    BannerObject.setImgUrl(bannerData.getPhotoUrl());
                    BannerObject.setAd(false);
                    BannerObject.setAbstractInfo(bannerData.getAbstractInfo());
                    adList.add(BannerObject);
                    index++;
                }
                if (adList.size() != 0) {
                    initAdObject(view);
                    startAd();
                }
            }

            @Override
            public void onError(int code, String msg) {
                log("取轮播图数据失败");
            }
        });

    }

    /**
     * 添加消息（message）页面数据
     */
    private void loadMsgPageData() {

        //获取测试用数据
        listMsgData = TestData.getInstance().getTestListMsgData(this);
        mainMsgListviewAdapter = new MainListviewAdapter(this, listMsgData);
        listMainMsgChatlist.setAdapter(mainMsgListviewAdapter);
        mainMsgListviewAdapter.notifyDataSetChanged();

        refreshListViewHeight(listMainMsgChatlist, mainMsgListviewAdapter, 0, 0);

    }//end loadMsgPageData()

    /**
     * 寻找中（love）页面数据
     */
    private void loadLovePageData() {

//        //获取测试用数据
//        listFindingData = TestData.getInstance().getTestListFindData();
//        mainFindingListviewAdapter = new MainListviewAdapter(this, listFindingData);
//        listMainLoveListview.setAdapter(mainFindingListviewAdapter);
//        mainFindingListviewAdapter.notifyDataSetChanged();
//
//        refreshListViewHeight(listMainLoveListview, mainFindingListviewAdapter,0,0);

//        加载搜索历史记录
//        listHistoryData = TestData.getInstance().getTestListHistoryData();

        //加载热门词汇
        loadHotWords();
    }//end loadLovePageData()

    //原是用于删除历史记录的，但暂时去掉了历史记录列表所以该方法暂废
    private void deleteHistory(String word) {
        String historyDatas = "";
        String historyTime = "";

        SharedPreferences sp = getSharedPreferences("find_history", Context.MODE_PRIVATE);
        String history = sp.getString("history", "");
        String time = sp.getString("time", "");
        String[] words = new String[]{}, times = new String[]{};
        listHistoryData.clear();

        SharedPreferences.Editor editor = sp.edit();

        if (history != null && !history.equals("")) {
            words = history.split("/-/");
            times = time.split("/-/");
            for (int i = 0; i < words.length; i++) {
                String w = words[i];
                if (w != null && !w.equals("")) {
                    if (word.equals(w)) {
                        Log.i("refreshHistoryList", w + "已经被刷新");
                    } else {
                        MainListBean bean = new MainListBean(1, w, times[i]);
                        listHistoryData.add(bean);
                        historyDatas = historyDatas + "/-/" + w;
                        historyTime = historyTime + "/-/" + times[i];
                    }
                }
            }

            Log.i("HistoryListDatas:commit", historyDatas);
            Log.i("HistoryListTime:commit", historyTime);
            editor.putString("history", historyDatas);
            editor.putString("time", historyTime);
            editor.commit();
        }
//        if (listHistoryData==null || listHistoryData.size()==0){
//            listMainLoveHistory.setVisibility(View.GONE);
//            txtMainLoveHistory.setVisibility(View.GONE);
//        }else{
//            listMainLoveHistory.setVisibility(View.VISIBLE);
//            txtMainLoveHistory.setVisibility(View.VISIBLE);
//            mainHistoryListviewAdapter = new MainListviewAdapter(this, listHistoryData);
//            listMainLoveHistory.setAdapter(mainHistoryListviewAdapter);
//            mainHistoryListviewAdapter.notifyDataSetChanged();
//            refreshListViewHeight(listMainLoveHistory, mainHistoryListviewAdapter, 0,0);
//        }
        refreshFindingList();
    }

//    public void loadFindHistory() {
//        SharedPreferences sp = getSharedPreferences("find_history", Context.MODE_PRIVATE);
//        String history = sp.getString("history", "");
//        String time = sp.getString("time", "");
//        String[] words=new String[]{},times=new String[]{};
//        listHistoryData.clear();
//        if (history!=null && !history.equals("")){
//            words=history.split("/-/");
//            times=time.split("/-/");
//            for (int i=0;i<words.length;i++){
//                String w=words[i];
//                if (w!=null && !w.equals("")) {
//                    MainListBean bean = new MainListBean(1, w, times[i]);
//                    listHistoryData.add(bean);
//                }
//            }
//        }else{
//            mainHistoryListviewAdapter = new MainListviewAdapter(this, listHistoryData);
//            listMainLoveHistory.setAdapter(mainHistoryListviewAdapter);
//            mainHistoryListviewAdapter.notifyDataSetChanged();
//        }
//        if (listHistoryData==null || listHistoryData.size()==0){
//            listMainLoveHistory.setVisibility(View.GONE);
//            txtMainLoveHistory.setVisibility(View.GONE);
//        }else{
//            listMainLoveHistory.setVisibility(View.VISIBLE);
//            txtMainLoveHistory.setVisibility(View.VISIBLE);
//            mainHistoryListviewAdapter = new MainListviewAdapter(this, listHistoryData);
//            listMainLoveHistory.setAdapter(mainHistoryListviewAdapter);
//            mainHistoryListviewAdapter.notifyDataSetChanged();
//            refreshListViewHeight(listMainLoveHistory, mainHistoryListviewAdapter, 0,0);
//        }
//        refreshFindHistory();
//
//    }
//
//    private void refreshFindHistory(){
//        if (listMainLoveHistory.getVisibility()==View.GONE && listMainLoveListview.getVisibility()==View.GONE) {
//            imgMainFindError.setImageResource(R.drawable.img_main_find_nofinding);
//            imgMainFindError.setVisibility(View.VISIBLE);
//        }else{
//            imgMainFindError.setVisibility(View.GONE);
//        }
//    }

    /**
     * 寻找中（love）热门词汇页面数据
     */
    private void loadHotWords() {
        //寻找列表
        BmobQuery<Words> wordsQuery = new BmobQuery<Words>();
        wordsQuery.order("-wordTimes");
        wordsQuery.setLimit(20);
        wordsQuery.findObjects(MainActivity.this, new FindListener<Words>() {
            @Override
            public void onSuccess(List<Words> list) {
                if (list.size() != 0) {
                    txtMainHotTitle.setVisibility(View.VISIBLE);
                    boxMainHot.setVisibility(View.VISIBLE);
                    if (list.size() != listHotData.size()) {
                        for (int i = 0; i < list.size(); i++) {
                            Words words = list.get(i);
                            MainListBean bean = new MainListBean(1, words.getWordName(), words.getWordTimes() + "人也在寻找");
                            listHotData.add(bean);
                        }
                        mainHotListviewAdapter = new MainListviewAdapter(MainActivity.this, listHotData);
                        listMainHotListview.setAdapter(mainHotListviewAdapter);
                        mainHotListviewAdapter.notifyDataSetChanged();
                        refreshListViewHeight(listMainHotListview, mainHotListviewAdapter, 0, 56);

                        listMainHotListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                editMainHomeSearchbox.setText(listHotData.get(position).getWord_text());
                                TabClick(1);
                                changeEditBoxStatus(true);
                            }
                        });
                    }
                } else {
                    txtMainHotTitle.setVisibility(View.GONE);
                    boxMainHot.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i("msg", "正在寻找列表刷新失败");
                txtMainHotTitle.setVisibility(View.GONE);
                boxMainHot.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 动态刷新listview高度
     */
    private void refreshListViewHeight(ListView listView, Adapter adapter, int margin, int itemHeightInDp) {
        margin = 0;
        if (itemHeightInDp == 0) itemHeightInDp = 66;
        int listViewHeight = 0;
        int adaptCount = adapter.getCount();
        listViewHeight = Tools.dip2px(this, itemHeightInDp) * adaptCount;
        listViewHeight += Tools.dip2px(this, margin);
        LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = LayoutParams.FILL_PARENT;
        layoutParams.height = listViewHeight;
        listView.setLayoutParams(layoutParams);
    }//end refreshListViewHeight()

    /**
     * ViewPager适配器
     */
    class TabContentViewPagerAdapter extends PagerAdapter {
        public TabContentViewPagerAdapter() {
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(m_contentViewList.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return m_contentViewList.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(m_contentViewList.get(arg1), 0);
            return m_contentViewList.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }//end TabContentViewPagerAdapter()

    /**
     * 页面切换监听器
     */
    class TabContentPager_OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override

        public void onPageSelected(int arg0) {
            // arg0参数传入的是滑动到第几个页面，从0开始计数
            TabClick(arg0);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }//end TabContentPager_OnPageChangeListener()

    /**
     * 切换标签方法
     */
    private void TabClick(int tabIndex) {
        tabContentPager.setCurrentItem(tabIndex, true);
        int old_Left = 0;
        int new_Left = 0;
        switch (oldTabIndex) {
            case 0:
                old_Left = 0;
                break;
            case 1:
                old_Left = 58;
                break;
            case 2:
                old_Left = 58 * 2;
                break;
            default:
                break;
        }
        old_Left = Tools.dip2px(MainActivity.this, old_Left);
        AlphaAnimation am = null;      //背景渐变动画
        switch (tabIndex) {
            case 0:
                //背景渐变动画
                am = new AlphaAnimation(1.0f, 0f);
                new_Left = 0;
                break;
            case 1:
                am = new AlphaAnimation(0f, 1.0f);
                new_Left = 58;
                break;
            case 2:
                am = new AlphaAnimation(1.0f, 0f);
                new_Left = 58 * 2;
                break;
            default:
                break;
        }
        am.setFillAfter(true);
        am.setDuration(300);
        if (inited) imgMainBkg.startAnimation(am);
        //tab滑动动画
        new_Left = Tools.dip2px(MainActivity.this, new_Left);
        TranslateAnimation ta = new TranslateAnimation(old_Left, new_Left, 0, 0);
        ta.setDuration(300);
        ta.setFillAfter(true);
        ta.setFillAfter(true);
        imgMainNavTab.startAnimation(ta);
        oldTabIndex = tabIndex;
    }//end TabClick()

    /**
     * 按下按键的判断
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //如果文本框是显示的，按下返回键关闭文本框的显示状态
            if (editMainHomeSearchbox.getVisibility() == View.VISIBLE) {
                changeEditBoxStatus(false);
                return true;
            }
            if (boxMainMenuBkg.getVisibility() == View.VISIBLE) {
                hideMenu();
                return true;
            }
            if (boxMainLogin.getVisibility() == View.VISIBLE) {
                showLoginBox(false);
                return true;
            }
            if (boxMainDeleteWorditem.getVisibility() == View.VISIBLE) {
                showWordDeleteDialog(false);
                return true;
            }
        }
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
        return true;
    }//end onKeyDown()

    /**
     * 获取验证码倒计时
     */
    class ClassIdCodeTime implements Runnable {//倒计时逻辑子线程

        @Override
        public void run() {
            while (registGetCodeTime > 0) {//整个倒计时执行的循环
                registGetCodeTime--;
                registGetCodeHandler.post(new Runnable() {//通过它在UI主线程中修改显示的剩余时间
                    @Override
                    public void run() {
                        btnMainRegistGetCode.setText("重新获取：" + registGetCodeTime + "秒");
                    }
                });
                try {
                    Thread.sleep(1000);//线程休眠一秒钟     这个就是倒计时的间隔时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //下面是倒计时结束逻辑
            registGetCodeHandler.post(new Runnable() {
                @Override
                public void run() {
                    btnMainRegistGetCode.setText("获取验证码");
                    btnMainRegistGetCode.setEnabled(true);
                }
            });
            registGetCodeTime = 60;//修改倒计时剩余时间变量为60秒
        }
    }

    TimerTask task = new TimerTask() {
        public void run() {
            if (isActivity) {
                Message message = new Message();
                message.what = 31;
                mHandler.sendMessage(message);
            }
        }
    };

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 31) {
                refreshMessage();
            }
            //super.handleMessage(msg);
        }
    };
}
