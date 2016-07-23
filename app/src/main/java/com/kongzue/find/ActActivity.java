package com.kongzue.find;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.find.listviewbean.MainListBean;
import com.kongzue.find.util.ActListviewAdapter;
import com.kongzue.find.util.BaseActivity;
import com.kongzue.find.util.EventPassger;
import com.kongzue.find.util.MainListviewAdapter;
import com.kongzue.find.util.Tools;

import java.util.ArrayList;
import java.util.List;

//活动页
public class ActActivity extends BaseActivity {


    private List<MainListBean> listData;
    ActListviewAdapter aa;

    private String actTitle;
    private String actPhotoUrl;
    private String actAbstract;
    private String actWords;

    private LinearLayout sysStatusBar;
    private SimpleDraweeView imgActPhoto;
    private TextView txtActTitle;
    private TextView txtActAbstract;
    private TextView txtActWords;
    private ListView listAbstractWords;
    private TextView txtListTitle;

    private void assignViews() {
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        imgActPhoto = (SimpleDraweeView) findViewById(R.id.img_actPhoto);
        txtActTitle = (TextView) findViewById(R.id.txt_actTitle);
        txtActAbstract = (TextView) findViewById(R.id.txt_actAbstract);
        txtActWords = (TextView) findViewById(R.id.txt_actWords);
        listAbstractWords = (ListView) findViewById(R.id.list_abstractWords);
        txtListTitle = (TextView) findViewById(R.id.txt_listTitle);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);

        assignViews();
        init();
        setEvent();
    }

    private void setEvent() {
        listAbstractWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word=listData.get(position).getWord_text();
                Tools.StartFindWords=word;
                finish();
            }
        });
    }

    private void init() {

        /**
         * 设置状态栏高度分割（针对安卓4.4.4+及安卓4-状态栏是否透明的问题需要再有透明状态栏的情况下手动设置状态栏高度占位符布局，以便将内容布局向下移动）
         * 此方法在BaseActivity中
         */
        setStatusBarHeight();

        actTitle=getIntent().getStringExtra("title");
        actPhotoUrl=getIntent().getStringExtra("photoUrl");
        actWords=getIntent().getStringExtra("words");
        actAbstract=getIntent().getStringExtra("abstract");

        if (actTitle == null || actTitle.isEmpty()){
            finish();
            toast("数据错误，请重新启动寻");
            return;
        }

        txtActTitle.setText("#" + actTitle);
        imgActPhoto.setImageURI(Uri.parse(actPhotoUrl));
        txtActWords.setText(actWords);
        txtActAbstract.setText(actAbstract);

        String[] words = new String[]{};

        words = actWords.split(",");

        listData = new ArrayList<MainListBean>();

        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            if (w != null && !w.equals("")) {
                Log.i(">>",w);
                MainListBean bean = new MainListBean(1, w, "");
                listData.add(bean);
            }
        }
        aa = new ActListviewAdapter(this, listData);
        listAbstractWords.setAdapter(aa);
        aa.notifyDataSetChanged();
    }
}
