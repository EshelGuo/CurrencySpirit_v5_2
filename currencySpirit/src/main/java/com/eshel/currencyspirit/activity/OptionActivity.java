package com.eshel.currencyspirit.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.bean.Version;
import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.CurrencyFragment;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.OptionItemView;
import com.eshel.currencyspirit.widget.night.NightViewUtil;
import com.eshel.viewmodel.UpdateVersionUtil;
import com.j256.ormlite.misc.VersionUtils;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.mta.track.DebugMode;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.StatConfig;

import baseproject.base.BaseActivity;
import baseproject.util.FileUtils;
import baseproject.util.Log;
import baseproject.util.StringUtils;
import baseproject.util.WebImageUtil;
import baseproject.util.shape.ShapeUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
		showTitle();
		showBack();
		setTitleText(UIUtil.getString(R.string.item_option));
		mMessageOnoff.setChecked(ShapeUtil.get(AppConstant.key_push, true));
		mNightMode.setChecked(NightViewUtil.getNightMode());
		setSwipeBackEnable(true);
		String versionInfo = "version: " + Version.getVersionName(this);
		CharSequence versionText = versionInfo;
		if(SplashActivity.mVersion != null){
			boolean newsVersion = UpdateVersionUtil.isNewsVersion(Version.getVersionName(this), SplashActivity.mVersion.versionName);
			if(newsVersion) {
				String newVersionInfo = " New";
				versionInfo += newVersionInfo;
				versionText = StringUtils.getHighLightText(versionInfo,Color.RED,versionInfo.indexOf(newVersionInfo),versionInfo.length());
			}
		}
		mVersion.setItemText(versionText);
	}

	@OnClick({R.id.feedback, R.id.clean_cache, R.id.message_onoff, R.id.about, R.id.share, R.id.evaluate, R.id.version,R.id.night_mode})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.feedback:
				feedback();
//				Intent intent = new Intent(this,FeedbackActivity.class);
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
				Log.logTags();
				showShare();
				break;
			case R.id.evaluate:
				gotoGiveOneMark();
				break;
			case R.id.version:
				updateVersion();
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
	private void showShare() {

		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
//		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("币动精灵 实时币信息、涨跌一手信息在握.");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("https://www.fengzhihen.com/");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("- 多种数字货币市值、涨跌幅排名查询,实时微博twiter 消息推送早知道, 官方网站: https://www.fengzhihen.com/ ,快来下载使用吧");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(WebImageUtil.saveImg(this,R.mipmap.bite_sprite));//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("https://www.fengzhihen.com/");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("非常好用的比特币资讯软件, 快来下载使用吧");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("https://www.fengzhihen.com/");
		// 启动分享GUI
		oks.show(this);
	}
	private void gotoGiveOneMark(){
		 try{
			 Uri uri = Uri.parse("market://details?id="+getPackageName());
			 Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 startActivity(intent);
		 }catch(ActivityNotFoundException e){
			 UIUtil.toastShort("未能找到应用市场, 请确保设备安装了应用市场");
		 }
	}
	private void feedback() {
/*		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "shiwen.gsw@aliyun.com" });
		i.putExtra(Intent.EXTRA_SUBJECT, "请在此输入反馈主题");
		i.putExtra(Intent.EXTRA_TEXT, "请在此输入您要反馈的内容, 我们很希望能得到您的建议！！！");
		startActivity(Intent.createChooser(i,
				"Select email application."));*/
		Intent data=new Intent(Intent.ACTION_SENDTO);
		data.setData(Uri.parse("mailto:shiwen.gsw@aliyun.com"));
		data.putExtra(Intent.EXTRA_SUBJECT, "请在此输入反馈主题");
		data.putExtra(Intent.EXTRA_TEXT, "请在此输入您要反馈的内容, 我们很希望能得到您的建议！！！");
		startActivity(data);
	}
	private void updateVersion() {
		if(SplashActivity.mVersion != null){
			boolean newsVersion = UpdateVersionUtil.isNewsVersion(Version.getVersionName(this), SplashActivity.mVersion.versionName);
			if(newsVersion) {
				SplashActivity.downloadNewVersion(this,SplashActivity.mVersion);
			}else {
				UIUtil.toastShort("已是最新版本");
			}
		}else {
			UIUtil.toastShort("未发现新版本");
		}
	}
}
