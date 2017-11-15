package com.eshel.viewmodel;

import android.content.Context;

import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.net.api.NewListApi;
import com.eshel.net.factory.RetrofitFactory;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by guoshiwen on 2017/10/21.
 */

public class UpdateVersionUtil {
	public static final String key_code = "code";//0 成功, 其他失败
	public static final String key_download_url = "download_url";
	public static final String key_version_code = "message";
	private static String key_desc = "desc";

	// "1.0.2"	"1.22.0.112"
	public static boolean isNewsVersion(String currentVersion, String newVersion){
		String[] current = currentVersion.split("\\.");
		String[] news = newVersion.split("\\.");
		int len = current.length < news.length ? current.length : news.length;
		for (int i = 0; i < len; i++) {
			Integer newVersion1 = Integer.valueOf(news[i]);
			Integer currentVersion1 = Integer.valueOf(current[i]);
			if(newVersion1 > currentVersion1)
				return true;
			else if(newVersion1 < currentVersion1)
				return false;
		}
		return false;
	}
	public static void updateVersion(final NewVersionCallback callback){
		NewListApi api = RetrofitFactory.getRetrofit().create(NewListApi.class);
		Call<ResponseBody> call = api.update(AppConstant.device);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()){
					try {
						String result = response.body().string();
						JSONObject json = new JSONObject(result);
						if(json.getInt(key_code) == 0){
							Context context = CurrencySpiritApp.getContext();
							String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
							String versioncode = json.getString(key_version_code);
							String desc = json.getString(key_desc);
							callback.hasNewVersion(isNewsVersion(versionName,versioncode),versioncode,desc,json.getString(key_download_url));
						}else {
							callback.updateNewVersionFailed();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					callback.updateNewVersionFailed();
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				callback.updateNewVersionFailed();
				t.printStackTrace();
			}
		});
	}
	public interface NewVersionCallback{
		void hasNewVersion(boolean hasNewVersion,String versionCode, String versionDesc, String versionDownloadUrl);
		void updateNewVersionFailed();
	}
}
