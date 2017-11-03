package com.eshel.currencyspirit.widget.night;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.OptionItemView;

import baseproject.util.Log;
import baseproject.util.ViewUtil;

/**
 * Created by guoshiwen on 2017/11/1.
 */

public class NightViewCallback {
	private int resId;
	private int color;
	private Drawable mDrawable;
	private int textColor;
	public int setBackgroundColor(int color){
		isSetColor = true;
		resId = 0;
		color = NightViewUtil.changeNightColor(color);
		this.color = color;
		return color;
	}
	public int setTextColor(int color){
		return textColor = NightViewUtil.changeNightColor(color);
	}

	public int setBackgroundResource(@DrawableRes int resid) {
		isSetResourse = true;
		resId = resid;
		return NightViewUtil.changeNightResId(resid);
	}

	public Drawable setBackgroundDrawable(Drawable background) {
		if(debugView != null && debugView instanceof OptionItemView){
			Log.i("");
		}
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

	public void setStateBarColor(int colorRes){
		int color = NightViewUtil.changeNightColor(UIUtil.getColor(colorRes));
		ViewUtil.changeCurrentActivityStateBarColor(color);
	}

	View debugView;
	public void changeNightMode(View view, boolean isNight) {
		debugView = view;
		setStateBarColor(R.color.titleColor);
		if(resId!=0)
			view.setBackgroundResource(resId);
		else if(color != 0){
			view.setBackgroundColor(color);
		} else {
			if(mDrawable != null)
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					view.setBackground(mDrawable);
				}else {
					view.setBackgroundDrawable(mDrawable);
				}
		}
		if(view instanceof TextView){
			TextView textView = (TextView) view;
			textView.setTextColor(textView.getCurrentTextColor());
		}
		if(view instanceof ImageView){
			ImageView imageView = (ImageView) view;
			imageView.setImageDrawable(imageView.getDrawable());
		}
	}
	public boolean isSetResourse = false;
	public boolean isSetColor = false;
}
