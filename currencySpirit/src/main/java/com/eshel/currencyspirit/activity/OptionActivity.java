package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.CurrencyFragment;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.OptionItemView;
import com.eshel.currencyspirit.widget.night.NightViewUtil;

import baseproject.base.BaseActivity;
import baseproject.util.FileUtils;
import baseproject.util.Log;
import baseproject.util.shape.ShapeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * createBy Eshel
 * createTime: 2017/10/2 20:21
 * desc: 通用的 APP 设置 Activity
 */

public class OptionActivity extends BaseActivity {

	@BindView(R.id.feedback)
	OptionItemView mFeedback;
	@BindView(R.id.clean_cache)
	OptionItemView mCleanCache;
	@BindView(R.id.message_onoff)
	OptionItemView mMessageOnoff;
	@BindView(R.id.about)
	OptionItemView mAbout;
	@BindView(R.id.share)
	OptionItemView mShare;
	@BindView(R.id.evaluate)
	OptionItemView mEvaluate;
	@BindView(R.id.version)
	OptionItemView mVersion;
	@BindView(R.id.night_mode)
	OptionItemView mNightMode;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		ButterKnife.bind(this, getContentView());
//		ViewUtil.findAllViewById(this,getContentView(),R.id.class);
		showTitle();
		showBack();
		setTitleText(UIUtil.getString(R.string.item_option));
		mMessageOnoff.setChecked(ShapeUtil.get(AppConstant.key_push, true));
		mNightMode.setChecked(NightViewUtil.getNightMode());
		setSwipeBackEnable(true);

//		mCleanCache.setItemText(FileUtils.fileSizeFormat(getCacheDir().length()));
	}

	@OnClick({R.id.feedback, R.id.clean_cache, R.id.message_onoff, R.id.about, R.id.share, R.id.evaluate, R.id.version,R.id.night_mode})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.feedback:
				Log.i("COLOR",new ColorDrawable(Color.GRAY).getColor());
				break;
			case R.id.clean_cache:
				FileUtils.clearCache(getApplicationContext(), new FileUtils.ClearCacheCallback() {
					@Override
					public void clearCacheSuccess() {
						UIUtil.toast("清除缓存成功");
						mCleanCache.setItemText("0.0B");
					}
				});
				break;
			case R.id.message_onoff:
				Log.i("pushChecked: " + mMessageOnoff.getChecked());
				ShapeUtil.put(AppConstant.key_push, mMessageOnoff.getChecked());
				if (!isOnResume) {
					return;
				}
				if (mMessageOnoff.getChecked()) {
					CurrencySpiritApp.registerXGPush();
					UIUtil.toast(UIUtil.getString(R.string.push_on));
				} else {
					CurrencySpiritApp.unRegisterXGPush();
					UIUtil.toast(UIUtil.getString(R.string.push_off));
				}
				break;
			case R.id.about:
				startActivity(new Intent(this, AboutActivity.class));
				break;
			case R.id.share:
				break;
			case R.id.evaluate:
				break;
			case R.id.version:
				break;
			case R.id.night_mode:
				mNightMode.setChecked(!NightViewUtil.getNightMode());
				NightViewUtil.changeNightMode(!NightViewUtil.getNightMode(),this);
				try {
					HomeActivity homeActivity = (HomeActivity) BaseActivity.getActivity(HomeActivity.class);
					if (homeActivity != null)
						NightViewUtil.changeNightMode(NightViewUtil.getNightMode(), homeActivity);
				}catch(Throwable e){
					e.printStackTrace();
				}
				break;
		}
	}

	private boolean isOnResume;

	@Override
	protected void onResume() {
		super.onResume();
		isOnResume = true;
		mCleanCache.setItemText(FileUtils.fileSizeFormat(getCacheDir().length()));
	}

	@Override
	protected void onPause() {
		super.onPause();
		isOnResume = false;
	}
}
