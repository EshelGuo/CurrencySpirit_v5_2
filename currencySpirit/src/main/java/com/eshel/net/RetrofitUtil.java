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
		call.enqueue(new retrofit2.Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				ResponseBody body = response.body();
				if(response.isSuccessful()){
					try {
						if(body != null) {
							String result = body.string();
							callback.onSuccess(result);
						}else {
							callback.onFailed("msg: "+response.message()+", body: null");
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
					callback.onFailed("msg: "+response.message()+", body: "+bodyS);
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				callback.onFailed(t.getMessage());
			}
		});
	}
}
