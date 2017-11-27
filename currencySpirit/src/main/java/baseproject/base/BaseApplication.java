package baseproject.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.util.ProcessUtil;
import com.eshel.currencyspirit.util.UIUtil;
import com.mob.MobApplication;

import baseproject.manager.UtilManager;
import baseproject.util.Log;

/**
 * 项目名称: BaseProject
 * 创建人: Eshel
 * 创建时间:2017/10/2 14时42分
 * 描述: TODO
 */

public class BaseApplication extends MobApplication {
private static Context mContext;
private static Handler mHandler;
	@Override
	public void onCreate() {
		super.onCreate();
		long ago = CurrencySpiritApp.time;
		CurrencySpiritApp.time = System.currentTimeMillis();
		android.util.Log.i("LogTAG_Default","Application鸡肋初始化时间: " + (CurrencySpiritApp.time - ago));
		mContext = getApplicationContext();
		mHandler = new Handler();
		UtilManager.initUtils();
//		if()
	}


	@Override
	public void onTerminate() {
		UtilManager.deinitUtils();
		mContext = null;
		mHandler = null;
		super.onTerminate();
	}
	public static Context getContext(){
		return mContext;
	}

	public Handler getHandler() {
		return mHandler;
	}
}
