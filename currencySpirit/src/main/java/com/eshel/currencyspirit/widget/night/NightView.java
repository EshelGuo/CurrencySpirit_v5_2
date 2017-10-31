package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Created by guoshiwen on 2017/10/31.
 */

public class NightView extends View implements INight{
	public NightView(Context context) {
		this(context,null);
	}

	public NightView(Context context, @Nullable AttributeSet attrs) {
		this(context,attrs,0);
	}

	public NightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
