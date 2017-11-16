package com.eshel.currencyspirit.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;

import com.eshel.config.AppConfig;
import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.bean.Version;
import com.eshel.currencyspirit.util.PermissionUtil;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.viewmodel.UpdateVersionUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;

import java.io.File;

import baseproject.base.BaseActivity;
import baseproject.permission.Permissions;
import baseproject.permission.RequestPermissionUtil;
import baseproject.util.Log;
import baseproject.util.StringUtils;
import baseproject.util.ViewUtil;
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
	boolean permissionRequestOver = false;
	static boolean updating = false;
	private static Version mVersion;
	private static boolean apkInstall;
	boolean appRunOnBackground = false;
	private Runnable finishSplashTask = new Runnable() {
		@Override
		public void run() {
			if(!apkInstall)
				enterHomeActivity();
		}
	};
	private Runnable mainTask = new Runnable() {
		@Override
		public void run() {
			if(mVersion != null && !CurrencySpiritApp.isExit){
				while (BaseActivity.getTopActivity() == null)
					Thread.yield();
				downloadNewVersion(BaseActivity.getTopActivity(),mVersion);
			}
			saveDrviceId();
			checkFristRun();
			if(StringUtils.isEmpty(AppConfig.token))
				AppConfig.token = ShapeUtil.get(AppConstant.key_token,"");
			delApk();
		}
	};

	private void delApk() {
		try {
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			File apk = new File(getExternalFilesDir(null).getAbsolutePath() + "/bidongjingling_"+versionName+".apk");
			if(apk.exists() && apk.isFile()){
				apk.delete();
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static boolean noEntryHome;

	private void checkFristRun() {
		boolean fristRun = ShapeUtil.get(AppConstant.key_fristRun, true);
		if(fristRun) {
			ShapeUtil.put(AppConstant.key_fristRun, false);
		}else {
		}
	}
	public synchronized static void downloadNewVersion(final BaseActivity activity, final Version version){
		if(updating)
			return;
		updating = true;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(activity)
						.setTitle(activity.getString(R.string.new_version)+version.versionName)
						.setMessage(version.versionDesc)
						.setNegativeButton(R.string.talk_later, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								updating = false;
								if(noEntryHome){
									noEntryHome = false;
									if(activity instanceof SplashActivity){
										((SplashActivity) activity).enterHomeActivity();
									}
								}
							}
						})
						.setPositiveButton(R.string.now_update, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								final ProgressDialog progressDialog = new ProgressDialog(activity);
								progressDialog.setTitle("下载中");
								progressDialog.setProgress(0);
								progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								progressDialog.setCancelable(false);
								progressDialog.show();
								final String filePath = activity.getExternalFilesDir(null).getAbsolutePath() + "/bidongjingling_" + version.versionName + ".apk";
								BaseDownloadTask task = FileDownloader.getImpl().create(version.versionDownloadUrl)
										.setPath(filePath)
										.setListener(new FileDownloadSampleListener() {
											@Override
											protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
												progressDialog.setProgress(soFarBytes * 100 / totalBytes);
												if(soFarBytes == totalBytes) {
													progressDialog.setProgress(100);
												}
											}
											@Override
											protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
											}
											@Override
											protected void error(BaseDownloadTask task, Throwable e) {
												progressDialog.dismiss();
												UIUtil.toast("下载失败");
												updating = false;
												if(noEntryHome){
													if(activity instanceof SplashActivity){
														((SplashActivity) activity).enterHomeActivity();
													}
												}
											}
											@Override
											protected void completed(BaseDownloadTask task) {
												progressDialog.dismiss();
												updating = false;
												Intent intent = new Intent(Intent.ACTION_VIEW);
												Uri uri;
												if(Build.VERSION.SDK_INT >= 24){
													uri = FileProvider.getUriForFile(activity, "com.eshel.currencyspirit.fileprovider", new File(filePath));
													intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
												}else {
													uri = Uri.fromFile(new File(filePath));
												}
												intent.setDataAndType(uri,"application/vnd.android.package-archive");
												activity.startActivity(intent);
												apkInstall = true;
												mVersion = null;
											}
										});
								int id = task.start();
							}
						}).setCancelable(false)
						.show();
			}
		});
	}

	private void updateNewVersion() {
		UpdateVersionUtil.updateVersion(new UpdateVersionUtil.NewVersionCallback() {
			@Override
			public void hasNewVersion(boolean hasNewVersion, String versionCode, String versionDesc, String versionDownloadUrl) {
				if(hasNewVersion){
					mVersion = new Version(versionCode,versionDesc,versionDownloadUrl);
					if(permissionRequestOver && !CurrencySpiritApp.isExit){
						new Thread(new Runnable() {
							@Override
							public void run() {
								while(BaseActivity.getTopActivity() == null)
									Thread.yield();
								downloadNewVersion(BaseActivity.getTopActivity(),mVersion);
							}
						}).start();
					}
				}
			}
			@Override
			public void updateNewVersionFailed() {
			}
		});
	}
	private void enterHomeActivity() {
		if(updating){
			noEntryHome = true;
			return;
		}
		if(appRunOnBackground)
			return;
		if(!(BaseActivity.getTopActivity() instanceof SplashActivity)){
			// 还未进入主界面就被用户压入后台 此时记下状态, 待 SplashActivity 再次 onResume 的时候 根据该值判断是直接进入主界面还是走欢迎界面的逻辑
			appRunOnBackground = true;
			return;
		}
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
		super.onCreate(savedInstanceState);
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			ViewUtil.hideStateBar(this);
		}*/
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
		updateNewVersion();
		requestPermission();
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
			StatService.startStatService(getApplicationContext(),"3102570311",com.tencent.stat.common.StatConstants.VERSION);
			UIUtil.debugToast("MTA 初始化成功");
		} catch (MtaSDkException e) {
			e.printStackTrace();
			UIUtil.debugToast("MTA 初始化失败");
		}
	}

	private void requestPermissionOver(){
		permissionRequestOver = true;
		new Thread(mainTask).start();
		CurrencySpiritApp.getApp().getHandler().postDelayed(finishSplashTask,lifeTime);
	}
	private void saveDrviceId(){
		try {
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
		}catch (Exception e){
		    e.printStackTrace();
		}catch (Error e){
		    e.printStackTrace();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(PermissionUtil.gotosettinged){
			requestPermission();
		}
		if(apkInstall){
			enterHomeActivity();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		updating = false;
		apkInstall = false;
	}
	@Override
	public void onBackPressed() {
		CurrencySpiritApp.isExit = true;
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(appRunOnBackground){
			appRunOnBackground = false;
			enterHomeActivity();
		}
	}
}
