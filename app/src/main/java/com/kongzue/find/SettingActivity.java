package com.kongzue.find;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kongzue.find.bean.User;
import com.kongzue.find.util.BaseActivity;
import com.kongzue.find.util.EventPassger;
import com.kongzue.find.work.UserLogin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class SettingActivity extends BaseActivity implements
        ImageChooserListener {

    //Bmob文件管理器
    BmobFile bmobFile=null;
    private ProgressDialog loadingDialog = null;            //等待对话框

    //选择照片
    private final static String TAG = "ICA";
    private ImageChooserManager imageChooserManager;
    private ProgressBar pbar;
    private String filePath;
    private int chooserType;
    private boolean isActivityResultOver = false;
    private String originalFilePath;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;

    //用户相关
    private User localUser;
    List<Map<String,Object>> settingListData;

    private LinearLayout sysStatusBar;
    private ImageView imgSettingTitleBarBkg;
    private TextView txtSettingTitle;
    private ImageView imgSettingBackButton;
    private SimpleDraweeView imgSettingUserFace;
    private TextView txtSettingUserNick;
    private ListView listSetting;
    private RelativeLayout boxSettingChangeUserNick;
    private TextView textView;
    private TextView txtSettingChangeUserNickTip;
    private TextView txtSettingChangeUserNickHideTip;
    private EditText editSettingChangeUserNickEditBox;
    private Button btnSettingChangeUserNickOk;
    private Button btnSettingChangeUserNickCancel;
    private ProgressBar progressBar;

    private void assignViews() {
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        imgSettingTitleBarBkg = (ImageView) findViewById(R.id.img_setting_titleBar_bkg);
        txtSettingTitle = (TextView) findViewById(R.id.txt_setting_title);
        imgSettingBackButton = (ImageView) findViewById(R.id.img_setting_back_button);
        imgSettingUserFace = (SimpleDraweeView) findViewById(R.id.img_setting_userFace);
        txtSettingUserNick = (TextView) findViewById(R.id.txt_setting_userNick);
        listSetting = (ListView) findViewById(R.id.list_setting);
        boxSettingChangeUserNick = (RelativeLayout) findViewById(R.id.box_setting_changeUserNick);
        textView = (TextView) findViewById(R.id.textView);
        txtSettingChangeUserNickTip = (TextView) findViewById(R.id.txt_setting_changeUserNick_tip);
        txtSettingChangeUserNickHideTip = (TextView) findViewById(R.id.txt_setting_changeUserNick_hideTip);
        editSettingChangeUserNickEditBox = (EditText) findViewById(R.id.edit_setting_changeUserNick_editBox);
        btnSettingChangeUserNickOk = (Button) findViewById(R.id.btn_setting_changeUserNick_ok);
        btnSettingChangeUserNickCancel = (Button) findViewById(R.id.btn_setting_changeUserNick_cancel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        pbar = (ProgressBar) findViewById(R.id.progressBar);
        pbar.setVisibility(View.GONE);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
        setEvent();
    }

    private void setEvent() {
        //头像点击
        imgSettingUserFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                final String[] cities = {"拍照", "从相册里选择"};
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
                            imageChooserManager = new ImageChooserManager(SettingActivity.this,
                                    ChooserType.REQUEST_CAPTURE_PICTURE, true);
                            imageChooserManager.setImageChooserListener(SettingActivity.this);
                            try {
                                pbar.setVisibility(View.VISIBLE);
                                filePath = imageChooserManager.choose();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            chooserType = ChooserType.REQUEST_PICK_PICTURE;
                            imageChooserManager = new ImageChooserManager(SettingActivity.this,
                                    ChooserType.REQUEST_PICK_PICTURE, true);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            imageChooserManager.setExtras(bundle);
                            imageChooserManager.setImageChooserListener(SettingActivity.this);
                            imageChooserManager.clearOldFiles();
                            try {
                                pbar.setVisibility(View.VISIBLE);
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
        //返回按钮
        imgSettingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //列表点击
        listSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp = getSharedPreferences("find_history", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                switch (position){
                    case 0:
                        showNickEditBox(true);
                        break;
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        final String[] cities = {"拍照", "从相册里选择"};
                        builder.setItems(cities, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
                                    imageChooserManager = new ImageChooserManager(SettingActivity.this,
                                            ChooserType.REQUEST_CAPTURE_PICTURE, true);
                                    imageChooserManager.setImageChooserListener(SettingActivity.this);
                                    try {
                                        pbar.setVisibility(View.VISIBLE);
                                        filePath = imageChooserManager.choose();
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    chooserType = ChooserType.REQUEST_PICK_PICTURE;
                                    imageChooserManager = new ImageChooserManager(SettingActivity.this,
                                            ChooserType.REQUEST_PICK_PICTURE, true);
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    imageChooserManager.setExtras(bundle);
                                    imageChooserManager.setImageChooserListener(SettingActivity.this);
                                    imageChooserManager.clearOldFiles();
                                    try {
                                        pbar.setVisibility(View.VISIBLE);
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
                        break;
                    case 2:
                        //清空数据
                        editor.putString("history", "");
                        editor.putString("time", "");
                        editor.commit();
                        toast("本地记录已经清空");
                        MainActivity ma=EventPassger.getInstance().getMainAactivity();
//                        if (ma!=null)ma.loadFindHistory();
                        break;
                    case 3:
                        editor.putString("history", "");
                        editor.putString("time", "");
                        editor.commit();
                        Fresco.getImagePipeline().clearCaches();
                        //退出登录
                        UserLogin.getInstance().logout();
                        //重启程序
                        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        });

        boxSettingChangeUserNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNickEditBox(false);
            }
        });

        btnSettingChangeUserNickCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNickEditBox(false);
            }
        });

        btnSettingChangeUserNickOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newNickname=editSettingChangeUserNickEditBox.getText().toString();
                User newUser = new User();
                newUser.setUserNick(newNickname);
                newUser.update(SettingActivity.this,localUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        toast("昵称已经更新为"+newNickname);
                        //重启程序
                        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        toast("修改昵称失败，请检查网络");
                    }
                });
            }
        });
    }

    private void init() {
        //绑定控件
        assignViews();
        /**
         * 设置状态栏高度分割（针对安卓4.4.4+及安卓4-状态栏是否透明的问题需要再有透明状态栏的情况下手动设置状态栏高度占位符布局，以便将内容布局向下移动）
         * 此方法在BaseActivity中
         */
        setStatusBarHeight();
        //获取用户信息
        localUser = UserLogin.getInstance().getCurrentUser();
        if (localUser==null){
            finish();
            return;
        }
        txtSettingUserNick.setText(localUser.getUserNick());
        //初始化设置菜单
        if (settingListData==null){
            settingListData=new ArrayList<Map<String, Object>>();
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("settingTitle","修改昵称");
            map.put("settingTip","修改您的昵称");
            map.put("settingValue",localUser.getUserNick());
            settingListData.add(map);
            map=new HashMap<String, Object>();
            map.put("settingTitle","修改头像");
            map.put("settingTip","修改您的头像");
            map.put("settingValue","");
            settingListData.add(map);
            map=new HashMap<String, Object>();
            map.put("settingTitle","清空历史记录");
            map.put("settingTip","清除本地寻找过的历史记录");
            map.put("settingValue","");
            settingListData.add(map);
            map=new HashMap<String, Object>();
            map.put("settingTitle","退出登录");
            map.put("settingTip","退出您的账号并使用其他帐号登录");
            map.put("settingValue","");
            settingListData.add(map);
            SimpleAdapter adapter = new SimpleAdapter(this, settingListData,
                    R.layout.item_setting, new String[] { "settingTitle", "settingTip", "settingValue" },
                    new int[] { R.id.item_main_listview_title_textTitle, R.id.item_main_listview_title_textTip, R.id.item_main_listview_value });
            listSetting.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        //加载自己的头像
        if (localUser.getUserFace()!=null && !localUser.getUserFace().equals("")) {
            imgSettingUserFace.setImageURI(Uri.parse(localUser.getUserFace()));
        }
    }

    private void showNickEditBox(boolean vis){
        boxSettingChangeUserNick.clearAnimation();
        if (vis){
            boxSettingChangeUserNick.setEnabled(true);
            boxSettingChangeUserNick.setVisibility(View.VISIBLE);
            AlphaAnimation am = new AlphaAnimation(0f, 1.0f);
            am.setDuration(500);
            am.setFillAfter(true);
            boxSettingChangeUserNick.startAnimation(am);
            editSettingChangeUserNickEditBox.setText(localUser.getUserNick());
        }else{
            boxSettingChangeUserNick.setEnabled(false);
            boxSettingChangeUserNick.setVisibility(View.VISIBLE);
            AlphaAnimation am = new AlphaAnimation(1.0f, 0f);
            am.setDuration(500);
            am.setFillAfter(true);
            boxSettingChangeUserNick.startAnimation(am);
            am.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    boxSettingChangeUserNick.clearAnimation();
                    boxSettingChangeUserNick.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //如果文本框是显示的，按下返回键关闭文本框的显示状态
            if (boxSettingChangeUserNick.getVisibility()==View.VISIBLE) {
                showNickEditBox(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }//end onKeyDown()

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i("选择图像", "Chosen Image: Is not null");
                    //加载头像
                    //loadImage(imgSettingUserFace, image.getFileThumbnail());

                    //显示等待对话框
                    loadingDialog = ProgressDialog.show(SettingActivity.this, "", "正在设置...", true, false);
                    loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK  && event.getAction() == KeyEvent.ACTION_DOWN) {
                                loadingDialog.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });

                    //开始上传
                    String picPath = image.getFileThumbnail();
                    bmobFile = new BmobFile(new File(picPath));
                    bmobFile.uploadblock(SettingActivity.this, new UploadFileListener() {

                        @Override
                        public void onSuccess() {
                            loadingDialog.dismiss();
                            //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                            final String url=bmobFile.getFileUrl(SettingActivity.this);
                            Log.i("selectPhoto","上传文件成功:" + bmobFile.getUrl());

                            //删除旧头像文件
                            String s=localUser.getUserFace();
                            if (s!=null && !s.equals("")) {
                                final String oldFaceUrl = s.replace("http://file.bmob.cn/", "");

                                BmobFile file = new BmobFile();
                                file.setUrl(oldFaceUrl);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
                                file.delete(SettingActivity.this, new DeleteListener() {

                                    @Override
                                    public void onSuccess() {
                                        Log.i("delete", "用户旧头像删除成功");
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        Log.i("delete", "用户旧头像删除失败" + oldFaceUrl + ";" + msg);
                                    }
                                });
                            }

                            //更新用户头像数据
                            User newUser = new User();
                            newUser.setUserFace(url);
                            newUser.update(SettingActivity.this,localUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    toast("设置头像成功");
                                    imgSettingUserFace.setImageURI(Uri.parse(url));
                                }
                                @Override
                                public void onFailure(int code, String msg) {
                                    toast("设置头像失败，请检查网络");
                                }
                            });

                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            loadingDialog.dismiss();
                            Log.i("selectPhoto","上传文件失败：" + msg);
                            toast("设置头像失败，请检查网络");
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

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
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
            pbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("activity_result_over")) {
                isActivityResultOver = savedInstanceState.getBoolean("activity_result_over");
                originalFilePath = savedInstanceState.getString("orig");
                thumbnailFilePath = savedInstanceState.getString("thumb");
                thumbnailSmallFilePath = savedInstanceState.getString("thumbs");
            }
        }
        Log.i(TAG, "Restoring Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        Log.i(TAG, "Activity Result Over: " + isActivityResultOver);
        if (isActivityResultOver) {
            populateData();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void populateData() {
        Log.i(TAG, "Populating Data");
        loadImage(imgSettingUserFace, thumbnailFilePath);
    }

    private void loadImage(SimpleDraweeView iv, final String path) {
//        Picasso.with(SettingActivity.this)
//                .load(Uri.fromFile(new File(path)))
//                .fit()
//                .centerInside()
//                .into(iv, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.i(TAG, "Picasso Success Loading Thumbnail - " + path);
//                    }
//
//                    @Override
//                    public void onError() {
//                        Log.i(TAG, "Picasso Error Loading Thumbnail Small - " + path);
//                    }
//                });
        iv.setImageURI(Uri.fromFile(new File(path)));
        //iv.setImageBitmap(ImageUtil.ToRoundBitmap(ImageUtil.FileToBitmap(this,path)));
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
//                Log.i(TAG, "On Images Chosen: " + images.size());
//                onImageChosen(images.getImage(0));
                //多选照片
                toast("请不要选择多张照片");
            }
        });
    }
}
