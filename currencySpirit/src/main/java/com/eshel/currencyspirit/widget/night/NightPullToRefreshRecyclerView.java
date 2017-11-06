package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

/**
 * Created by guoshiwen on 2017/11/4.
 */

public class NightPullToRefreshRecyclerView extends PullToRefreshRecyclerView implements INight{

	private int mSchemecolor;

	public NightPullToRefreshRecyclerView(Context context) {
		super(context);
	}

	public NightPullToRefreshRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void setBackgroundColor(@ColorInt int color) {
		super.setBackgroundColor(getCallback().setBackgroundColor(color));
	}
	@Override
	public void setBackgroundResource(@DrawableRes int resid) {
		super.setBackgroundResource(getCallback().setBackgroundResource(resid));
	}
	@Override
	public void setBackgroundDrawable(Drawable background) {
		super.setBackgroundDrawable(getCallback().setBackgroundDrawable(background));
	}
	@Override
	public void changeNightMode(boolean isNight) {
		getCallback().changeNightMode(this,isNight);
		if(mSchemecolor != 0){
			setProgressBackgroundColorSchemeColor(mSchemecolor);
		}
	}
	private NightViewCallback mCallback;
	public NightViewCallback getCallback(){
		if(mCallback == null){
			mCallback = new NightViewCallback();
		}
		return mCallback;
	}

	@Override
	public void setProgressBackgroundColorSchemeColor(@ColorInt int color) {
		color = NightViewUtil.changeNightColor(color);
		mSchemecolor = color;
		super.setProgressBackgroundColorSchemeColor(color);
	}
}
