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
import com.kongzue.find.ActActivity;
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
 * Created by ZhangChao on 2016/6/17.
 */
public class ActListviewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<MainListBean> myList;
    private ActActivity m_context;


    public ActListviewAdapter(ActActivity context, List<MainListBean> myList) {
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

        MainListBean obj = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderWordContext wordContext = null;

        if (convertView == null) {
            wordContext = new ViewHolderWordContext();
            convertView = mInflater.inflate(R.layout.item_main_listview_word, null);
            wordContext.line = (ImageView) convertView.findViewById(R.id.img_main_list_line);
            wordContext.itemMainListviewTitleTextTitle = (TextView) convertView.findViewById(R.id.item_main_listview_title_textTitle);
            wordContext.itemMainListviewTitleTextTip = (TextView) convertView.findViewById(R.id.item_main_listview_title_textTip);

            convertView.setTag(wordContext);
        } else {
            wordContext = (ViewHolderWordContext) convertView.getTag();
        }

        wordContext.itemMainListviewTitleTextTitle.setText(obj.getWord_text());
        wordContext.itemMainListviewTitleTextTip.setText(obj.getWord_tip());
        if (position==myList.size()-1){
            wordContext.line.setVisibility(View.GONE);
        }else{
            wordContext.line.setVisibility(View.VISIBLE);
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

        MainListBean msg = myList.get(position);
        int type = msg.getType();
        return type;
    }

    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    class ViewHolderWordContext {
        private TextView itemMainListviewTitleTextTitle;
        private TextView itemMainListviewTitleTextTip;
        private ImageView line;
    }

}
