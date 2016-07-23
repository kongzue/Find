package com.kongzue.find.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by ZhangChao on 2016/4/29.
 */


//监听键盘出现隐藏的
public class IMMListenerRelativeLayout extends RelativeLayout {
    private InputWindowListener listener;

    //键盘出现隐藏的监听器
    public interface InputWindowListener {
        void show();

        void hidden();
    }

    public IMMListenerRelativeLayout(Context context) {
        super(context);
    }

    public IMMListenerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IMMListenerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h) {
            //L.d("input window show");
            listener.show();
        } else{
            //L.d("input window hidden");
            listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }


}