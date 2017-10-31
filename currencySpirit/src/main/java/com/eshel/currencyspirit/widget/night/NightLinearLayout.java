package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
	public void setBackgroundDrawable(Drawable background) {
		NightViewUtil.changeNightBackgroundDrawable(background);
		super.setBackgroundDrawable(background);
	}

	@Override
	public void changeNightMode(boolean isNight) {
		setBackgroundDrawable(getBackground());
	}
}
