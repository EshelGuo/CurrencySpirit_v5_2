package com.eshel.currencyspirit.bean;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by guoshiwen on 2017/10/21.
 */

public class Version {
	public String versionName;
	public String versionDesc;
	public String versionDownloadUrl;

	public Version() {
	}

	public Version(String versionName, String versionDesc, String versionDownloadUrl) {
		this.versionName = versionName;
		this.versionDesc = versionDesc;
		this.versionDownloadUrl = versionDownloadUrl;
	}
	public static String getVersionName(Context context){
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "1.0";
	}
}
