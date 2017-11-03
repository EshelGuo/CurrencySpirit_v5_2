package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class NightTextView extends android.support.v7.widget.AppCompatTextView implements INight{

	public NightTextView(Context context) {
		this(context,null);
	}

	public NightTextView(Context context, @Nullable AttributeSet attrs) {
		this(context,attrs,0);
	}

	public NightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setTextColor(@ColorInt int color) {
		super.setTextColor(getCallback().setTextColor(color));
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
