package com.eshel.net;

import com.eshel.net.api.NewListApi;
import com.eshel.net.api.StringCallback;
import com.eshel.net.factory.RetrofitFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by guoshiwen on 2017/11/15.
 */

public class RetrofitUtil {
	public static NewListApi createApi(){
		NewListApi newListApi = RetrofitFactory.getRetrofit().create(NewListApi.class);
		return newListApi;
	}
	public static void enqueueCall(Call<ResponseBody> call, final StringCallback callback){
		final long ago = System.currentTimeMillis();
		call.enqueue(new retrofit2.Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				long now = System.currentTimeMillis();
				long time = now - ago;
				ResponseBody body = response.body();
				if(response.isSuccessful()){
					try {
						if(body != null) {
							String result = body.string();
							callback.onSuccess(result,time);
						}else {
							callback.onFailed("msg: "+response.message()+", body: null",time);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					String bodyS = "null";
					if(body != null)
						try {
							bodyS = body.string();
						} catch (IOException e) {
							e.printStackTrace();
						}
					callback.onFailed("msg: "+response.message()+", body: "+bodyS,time);
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				long now = System.currentTimeMillis();
				long time = now - ago;
				callback.onFailed(t.getMessage(),time);
				t.printStackTrace();
			}
		});
	}
}
