package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class NightImageView extends android.support.v7.widget.AppCompatImageView implements INight{
	public NightImageView(Context context) {
		this(context,null);
	}

	public NightImageView(Context context, @Nullable AttributeSet attrs) {
		this(context,attrs,0);
	}

	public NightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setImageDrawable(@Nullable Drawable drawable) {
		NightViewUtil.changeNightDrawable(drawable);
		super.setImageDrawable(drawable);
	}

	@Override
	public void setBackgroundDrawable(Drawable background) {
		NightViewUtil.changeNightBackgroundDrawable(background);
		super.setBackgroundDrawable(background);
	}

	@Override
	public void changeNightMode(boolean isNight) {
		setBackgroundDrawable(getBackground());
		setImageDrawable(getDrawable());
	}
}
