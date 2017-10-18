package com.eshel.currencyspirit.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;

import com.eshel.config.AppConfig;
import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.PermissionUtil;
import com.eshel.currencyspirit.util.UIUtil;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;

import baseproject.base.BaseActivity;
import baseproject.permission.Permissions;
import baseproject.permission.RequestPermissionUtil;
import baseproject.util.Log;
import baseproject.util.StringUtils;
import baseproject.util.shape.ShapeUtil;
import xgpush.XGMsage;

/**
 * createBy Eshel
 * createTime: 2017/10/2 20:19
 * desc: 通用的 APP 欢迎界面
 */

public class SplashActivity extends BaseActivity {
	public final int ALL_TIME = 3000;
	public final int REQUEST_PERMISSION_TIME = 1500;
	public int lifeTime = ALL_TIME;
	private Runnable finishSplashTask = new Runnable() {
		@Override
		public void run() {
			enterHomeActivity();
		}
	};
	private Runnable mainTask = new Runnable() {
		@Override
		public void run() {
			saveDrviceId();
			checkFristRun();
			updateNewVersion();
			if(StringUtils.isEmpty(AppConfig.token))
				AppConfig.token = ShapeUtil.get(AppConstant.key_token,"");
		}
	};

	private void checkFristRun() {
		boolean fristRun = ShapeUtil.get(AppConstant.key_fristRun, true);
		if(fristRun) {
			ShapeUtil.put(AppConstant.key_fristRun, false);
		}else {
		}
	}

	private void updateNewVersion() {
		// TODO: 2017/10/4 检查更新
	}
	private void enterHomeActivity() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	private void enterHomeByMsg(){
		if(XGMsage.msg != null){
			enterHomeActivity();
		}else {
			// 防止先进入 activity 后走接收消息回调
			for (int i = 0; i < 15; i++) {
				SystemClock.sleep(10);
				if(XGMsage.msg != null){
					enterHomeActivity();
					break;
				}
			}
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		getWindow().setBackgroundDrawable(new ColorDrawable(UIUtil.getColor(R.color.white)));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if(!CurrencySpiritApp.isExit) {
			enterHomeByMsg();
			return;
		}
		CurrencySpiritApp.isExit = false;
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null) {
			actionBar.hide();
		}
		initMTA();
		requestPermission();
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		RequestPermissionUtil.onRequestPermissionsResult(this,requestCode, permissions, grantResults);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void requestPermission(){
		lifeTime = REQUEST_PERMISSION_TIME;
		PermissionUtil.requestPermission(this, new PermissionUtil.PermissionCallback() {
			@Override
			public void requestAllPermissionSuccess() {
				requestPermissionOver();
			}

			@Override
			public void hasAllPermission() {
				lifeTime = ALL_TIME;
				requestPermissionOver();
			}
		},Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE);
		if(!Permissions.need){
			requestPermissionOver();
		}
	}

	private void initMTA() {
		try {
			StatService.setContext(this.getApplication());
			StatService.startStatService(getApplicationContext(),null,com.tencent.stat.common.StatConstants.VERSION);
			UIUtil.debugToast("MTA 初始化成功");
		} catch (MtaSDkException e) {
			e.printStackTrace();
			UIUtil.debugToast("MTA 初始化失败");
		}
	}

	private void requestPermissionOver(){
		new Thread(mainTask).start();
		CurrencySpiritApp.getApp().getHandler().postDelayed(finishSplashTask,lifeTime);
	}
	private void saveDrviceId(){
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceId = telephonyManager.getDeviceId();
		Log.i("addtag","deviceId: "+deviceId);
		String simSerialNumber = telephonyManager.getSimSerialNumber();
		Log.i("addtag","simSerialNumber: "+simSerialNumber);
		AppConfig.deviceId = "CSA_"+deviceId+"_"+simSerialNumber;
		Log.i("PID: "+AppConfig.deviceId);
		if(ShapeUtil.get(AppConstant.key_deviceId,"").length() == 0){
			ShapeUtil.put(AppConstant.key_deviceId,AppConfig.deviceId);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(PermissionUtil.gotosettinged){
			requestPermission();
		}
	}
}
