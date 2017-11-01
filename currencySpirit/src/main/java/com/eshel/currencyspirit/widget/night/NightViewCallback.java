package com.eshel.currencyspirit.widget.night;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.FrameLayout;

import com.eshel.currencyspirit.R;

import baseproject.util.Log;

/**
 * Created by guoshiwen on 2017/11/1.
 */

public class NightViewCallback {
	private int resId;
	private int color;
	private Drawable mDrawable;
	public int setBackgroundColor(int color){
		isSetColor = true;
		resId = 0;
		this.color = color;
		return color;
	}

	public int setBackgroundResource(@DrawableRes int resid) {
		isSetResourse = true;
		resId = resid;
		return NightViewUtil.changeNightResId(resid);
	}

	public Drawable setBackgroundDrawable(Drawable background) {
		if(isSetResourse){
			isSetResourse = false;
		}else {
			if(isSetColor){
				isSetColor = false;
			}else {
				color = 0;
			}
			resId = 0;
			NightViewUtil.changeNightBackgroundDrawable(background);
		}
		mDrawable = background;
		return background;
	}

	public void changeNightMode(View view, boolean isNight) {
		if(view.getId() == R.id.feedback){
			Log.i("");
		}
		if(resId!=0)
			view.setBackgroundResource(resId);
		else if(color != 0){
			view.setBackgroundColor(color);
		} else {
			if(mDrawable != null)
				view.setBackground(mDrawable);
		}
	}
	public boolean isSetResourse = false;
	public boolean isSetColor = false;
}
