package com.eshel.currencyspirit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.widget.night.NightEditText;

/**
 * createBy Eshel
 * createTime: 2017/10/20 00:34
 * desc: TODO
 */

public class SearchView extends NightEditText {

	private float mDownX = -1;

	// 构造方法
	public SearchView(Context context) {
		this(context,null);
	}

	public SearchView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
		int searchIcon = ta.getResourceId(R.styleable.SearchView_search_icon, -1);
		int cleanIcon = ta.getResourceId(R.styleable.SearchView_clean_icon, -1);
		if (searchIcon != -1)
			setSearchIcon(searchIcon);
		if (cleanIcon != -1)
			setCleanIcon(cleanIcon);
		setCleanIcon(-1);
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
			if(getText().length() != 0){
				if(cleanIcon != null)
					setCompoundDrawables(searchIcon,null,cleanIcon,null);
			}else {
				setCompoundDrawables(searchIcon,null,null,null);
			}
		}
	};

	private void init() {
		addTextChangedListener(mTextWatcher);
		setOnDrawableRightListener(new OnDrawableRightListener() {
			@Override
			public void onDrawableRightClick() {
				setText("");
			}
		});
		getFocus();
	}

	// 触摸事件
	// 判断DrawableLeft/DrawableRight是否被点击
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			mDownX = event.getRawX();
		}
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
			if (onDrawableRightListener != null && mDownX != -1) {
				Drawable drawableRight = getCompoundDrawables()[2];
				// 当按下的位置 > 在EditText的到右边间距-图标的宽度-Padding
				if (drawableRight != null && event.getRawX() >= (getRight() - getTotalPaddingRight() - drawableRight.getBounds().width())
						&& event.getRawX() < getRight()-3
						&& mDownX >= (getRight() - getTotalPaddingRight() - drawableRight.getBounds().width())) {
					// 执行DrawableRight点击事件
					onDrawableRightListener.onDrawableRightClick();
				}
			}
			mDownX = -1;
		}
		getFocus();
		return super.onTouchEvent(event);
	}
	public void getFocus(){
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
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
	Drawable searchIcon;
	Drawable cleanIcon;
	public void setSearchIcon(int resId){
		Drawable drawable = null;
		if(resId != -1) {
			drawable = getResources().getDrawable(resId);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			searchIcon = drawable;
		}
		setCompoundDrawables(drawable,null,cleanIcon,null);
	}
	public void setCleanIcon(int resId){
		Drawable drawable = null;
		if(resId != -1) {
			drawable = getResources().getDrawable(resId);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			cleanIcon = drawable;
		}
		setCompoundDrawables(searchIcon,null,drawable,null);
	}
}
