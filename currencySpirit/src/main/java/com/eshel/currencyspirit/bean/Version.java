package com.eshel.currencyspirit.bean;

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
}
