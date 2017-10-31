package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class NightTextView extends android.support.v7.widget.AppCompatTextView implements INight{

	public NightTextView(Context context) {
		super(context);
	}

	public NightTextView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public NightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setTextColor(@ColorInt int color) {
		color = NightViewUtil.changeNightColor(color);
		super.setTextColor(color);
	}

	@Override
	public void setTextColor(ColorStateList colors) {
		/*int[] color = colors.getColors();
		if(mColorDrawable == null)
			mColorDrawable = new ColorDrawable(colors.getDefaultColor());
		else
			mColorDrawable.setColor(colors.getDefaultColor());
		NightViewUtil.changeNightDrawable(mColorDrawable);*/

		super.setTextColor(colors);
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
