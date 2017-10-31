package com.eshel.currencyspirit;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;

import com.eshel.config.AppConfig;
import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.util.ProcessUtil;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.night.NightViewUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mta.track.DebugMode;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import org.json.JSONObject;

import baseproject.base.BaseApplication;
import baseproject.util.Log;
import baseproject.util.shape.ShapeUtil;

/**
 * createBy Eshel
 * createTime: 2017/10/4 20:24
 * desc: TODO
 */

public class CurrencySpiritApp extends BaseApplication{
	private static final String ACTIVITY_LIFECYCLE_TAG = "activityLifecycle";
	private static CurrencySpiritApp app;
	private static String mainThreadName;
	public static boolean isExit = true;
	@Override
	public void onCreate() {
		super.onCreate();
		NightViewUtil.setNightMode(true);
		StatConfig.setAutoExceptionCaught(true);//开启异常捕获
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Log.e(e.getMessage());
				e.printStackTrace();
				Process.killProcess(Process.myPid());
				System.exit(0);
			}
		});
//		StatConfig.initNativeCrashReport (this, null);//native 异常捕获
		Log.i("appprocess: "+ProcessUtil.getCurrentProcessName(getApplicationContext()));
		app = this;
		mainThreadName = Thread.currentThread().getName();
		String currentProcessName = ProcessUtil.getCurrentProcessName(getApplicationContext());
		if(currentProcessName.equals(getPackageName())) {
			mainOnCreate();
			UIUtil.debugToast("信鸽推送 开始注册");
		}else {
			otherOnCreate(currentProcessName);
		}
	}
	static boolean registerSuccess;
	public void mainOnCreate(){
		FileDownloader.setup(this);
//		FileDownloadLog.NEED_LOG = true;
		StatConfig.setDebugEnable(false);
		StatConfig.setStatSendStrategy(StatReportStrategy.INSTANT);
		StatConfig.setAppKey("AG69XWKJB64D");
		StatisticsDataAPI.instance(this,DebugMode.DEBUG_OFF);
		//开启信鸽日志输出
		XGPushConfig.enableDebug(getApplicationContext(), UIUtil.isDebug());
		if(!ShapeUtil.get(AppConstant.key_push,true))
			return;
		registerXGPush();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
				@Override
				public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onCreate");
				}

				@Override
				public void onActivityStarted(Activity activity) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onStart");
				}

				@Override
				public void onActivityResumed(Activity activity) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onResume");
				}

				@Override
				public void onActivityPaused(Activity activity) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onPause");
				}

				@Override
				public void onActivityStopped(Activity activity) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onStop");
				}

				@Override
				public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onActivitysaveInstanceState");
				}

				@Override
				public void onActivityDestroyed(Activity activity) {
					Log.i(ACTIVITY_LIFECYCLE_TAG,activity.getClass().getSimpleName() + " onDestorye");
				}
			});
		}
	}

	public static void registerXGPush(){
		if(!registerSuccess)
			//信鸽注册代码
			XGPushManager.registerPush(getContext(), new XGIOperateCallback() {
				@Override
				public void onSuccess(Object data, int flag) {
					registerSuccess = true;
					Log.i("TPush", "注册成功，设备token为：" + data);
					if(ShapeUtil.get(AppConstant.key_token,"").length() == 0) {
						ShapeUtil.put(AppConstant.key_token, data.toString());
						AppConfig.token = data.toString();
					}
					if (UIUtil.isDebug()) {
						ClipboardManager clipboardManager = (ClipboardManager) getApp().getSystemService(Context.CLIPBOARD_SERVICE);
						clipboardManager.setText(data.toString());
						Log.i("addtag","token: "+data.toString());
						UIUtil.debugToast("token 已经成功复制到剪切板 , 请使用 token 调试");
					}
				}

				@Override
				public void onFail(Object data, int errCode, String msg) {
					registerSuccess = false;
					Log.i("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
	/*				getHandler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if(!registerSuccess){
								UIUtil.debugToast("信鸽推送 尝试重新注册");
								mainOnCreate();
							}
						}
					},2000);*/
				}
			});
		getApp().getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!registerSuccess){
					UIUtil.debugToast("信鸽推送 尝试重新注册");
					getApp().mainOnCreate();
				}
			}
		},10000);

		/*XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
			@Override
			public void handleNotify(XGNotifaction xgNotifaction) {

			}
		});*/
	}
	public static void unRegisterXGPush(){
		if(registerSuccess)
			XGPushManager.unregisterPush(getContext());
	}
	public void otherOnCreate(String currentProcessName){}

	@Override
	public void onTerminate() {
		app = null;
		super.onTerminate();
	}

	public static String getMainThreadName(){
		return mainThreadName;
	}
	public static CurrencySpiritApp getApp() {
		return app;
	}
}
