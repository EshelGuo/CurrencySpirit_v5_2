package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by guoshiwen on 2017/11/4.
 */

public class NightViewPager extends ViewPager implements INight{
	public NightViewPager(Context context) {
		super(context);
	}

	public NightViewPager(Context context, AttributeSet attrs) {
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
	}
	private NightViewCallback mCallback;
	public NightViewCallback getCallback(){
		if(mCallback == null){
			mCallback = new NightViewCallback();
		}
		return mCallback;
	}
}
