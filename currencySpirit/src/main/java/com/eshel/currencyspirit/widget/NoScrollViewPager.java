package com.eshel.currencyspirit.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.eshel.currencyspirit.widget.night.NightViewPager;

public class NoScrollViewPager extends NightViewPager {
    private boolean noScroll = true;
  
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollViewPager(Context context) {
        super(context);
    }
    public void setNoScroll(boolean noScroll) {  
        this.noScroll = noScroll;  
    }
    @Override  
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */  
        if (noScroll)  
            return false;  
        else  
            return super.onTouchEvent(arg0);  
    }
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent arg0) {  
        if (noScroll)  
            return false;  
        else  
            return super.onInterceptTouchEvent(arg0);  
    }
}