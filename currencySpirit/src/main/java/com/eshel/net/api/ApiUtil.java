package com.eshel.net.api;

import com.eshel.net.RetrofitUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by guoshiwen on 2017/11/15.
 */

public class ApiUtil {
	public static void search(String keyword, StringCallback callback){
		search(keyword,"USD",callback);
	}
	public static void search(String keyword,String symbol, StringCallback callback){
		Call<ResponseBody> call = RetrofitUtil.createApi().search(keyword, symbol);
		RetrofitUtil.enqueueCall(call,callback);
	}

}
