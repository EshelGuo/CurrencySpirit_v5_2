package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class NightRelativeLayout extends RelativeLayout implements INight{
	public NightRelativeLayout(Context context) {
		super(context);
	}

	public NightRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
