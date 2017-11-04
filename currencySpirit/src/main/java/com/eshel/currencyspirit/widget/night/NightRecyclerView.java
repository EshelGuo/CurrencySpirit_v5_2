package com.eshel.currencyspirit.widget.night;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by guoshiwen on 2017/11/4.
 */

public class NightRecyclerView extends RecyclerView implements INight{
	public NightRecyclerView(Context context) {
		super(context);
	}
	public NightRecyclerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public NightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	ItemDecoration mItemDecoration;

	@Override
	public void addItemDecoration(ItemDecoration decor) {
		mItemDecoration = decor;
		super.addItemDecoration(decor);
	}

	@Override
	public void removeItemDecoration(ItemDecoration decor) {
		if(mItemDecoration == decor)
			mItemDecoration = null;
		super.removeItemDecoration(decor);
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
