package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class NightLinearLayout extends LinearLayout implements INight{
	public NightLinearLayout(Context context) {
		super(context);
	}

	public NightLinearLayout(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public NightLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
	}
	private NightViewCallback mCallback;
	public NightViewCallback getCallback(){
		if(mCallback == null){
			mCallback = new NightViewCallback();
		}
		return mCallback;
	}
}
