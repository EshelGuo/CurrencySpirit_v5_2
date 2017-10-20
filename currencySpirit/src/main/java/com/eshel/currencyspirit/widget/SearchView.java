package com.eshel.currencyspirit.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * createBy Eshel
 * createTime: 2017/10/20 00:34
 * desc: TODO
 */

public class SearchView extends EditText {

	// 构造方法
	public SearchView(Context context) {
		this(context,null);
	}

	public SearchView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private void init() {
		addTextChangedListener(mTextWatcher);
	}

	// 触摸事件
	// 判断DrawableLeft/DrawableRight是否被点击
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 触摸状态
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 监听DrawableLeft
			if (onDrawableLeftListener != null) {
				// 判断DrawableLeft是否被点击
				Drawable drawableLeft = getCompoundDrawables()[0];
				// 当按下的位置 < 在EditText的到左边间距+图标的宽度+Padding
				if (drawableLeft != null && event.getRawX() <= (getLeft() + getTotalPaddingLeft() + drawableLeft.getBounds().width())) {
					// 执行DrawableLeft点击事件
					onDrawableLeftListener.onDrawableLeftClick();
				}
			}

			// 监听DrawableRight
			if (onDrawableRightListener != null) {
				Drawable drawableRight = getCompoundDrawables()[2];
				// 当按下的位置 > 在EditText的到右边间距-图标的宽度-Padding
				if (drawableRight != null && event.getRawX() >= (getRight() - getTotalPaddingRight() - drawableRight.getBounds().width())) {
					// 执行DrawableRight点击事件
					onDrawableRightListener.onDrawableRightClick();
				}
			}
		}
		return super.onTouchEvent(event);
	}

	// 定义一个DrawableLeft点击事件接口
	public interface OnDrawableLeftListener {
		void onDrawableLeftClick();
	}

	private OnDrawableLeftListener onDrawableLeftListener;

	public void setOnDrawableLeftListener(OnDrawableLeftListener onDrawableLeftListener) {
		this.onDrawableLeftListener = onDrawableLeftListener;
	}

	// 定义一个DrawableRight点击事件接口
	public interface OnDrawableRightListener {
		void onDrawableRightClick();
	}

	private OnDrawableRightListener onDrawableRightListener;

	public void setOnDrawableRightListener(OnDrawableRightListener onDrawableRightListener) {
		this.onDrawableRightListener = onDrawableRightListener;
	}
}
