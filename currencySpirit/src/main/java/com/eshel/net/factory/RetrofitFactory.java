package com.eshel.net.factory;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.net.Url;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import baseproject.util.NetUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;


/**
 * createBy Eshel
 * createTime: 2017/10/5 03:06
 * desc: TODO
 */

public class RetrofitFactory {
	private static Retrofit mRetrofit;
	private static final Interceptor LoggingInterceptor = new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			long t1 = System.nanoTime();
			Response response = chain.proceed(request);
			long t2 = System.nanoTime();
			return response;
		}
	};
	/**
	 * 云端响应头拦截器，用来配置缓存策略
	 * Dangerous interceptor that rewrites the server's cache-control header.
	 */
	private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			if(!NetUtils.hasNetwork(CurrencySpiritApp.getContext())){
				request = request.newBuilder()
						.cacheControl(CacheControl.FORCE_CACHE)
						.build();
			}
			Response originalResponse = chain.proceed(request);
			if(NetUtils.hasNetwork(CurrencySpiritApp.getContext())){
				//有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
				String cacheControl = request.cacheControl().toString();
				return originalResponse.newBuilder()
						.header("Cache-Control", cacheControl)
						.removeHeader("Pragma")
						.build();
			}else{
				return originalResponse.newBuilder()
						.header("Cache-Control", "public, only-if-cached, max-stale=2419200")
						.removeHeader("Pragma")
						.build();
			}
		}
	};

	public static Retrofit getRetrofit(){
		if(mRetrofit == null){
			synchronized (RetrofitFactory.class){
				if(mRetrofit == null){
					OkHttpClient client = new OkHttpClient.Builder()
							.connectTimeout(10000L, TimeUnit.MILLISECONDS)       //设置连接超时
							.readTimeout(10000L,TimeUnit.MILLISECONDS)          //设置读取超时
							.writeTimeout(10000L,TimeUnit.MILLISECONDS)         //设置写入超时
							.cache(new Cache(CurrencySpiritApp.getContext().getCacheDir(),10 * 1024 * 1024))   //设置缓存目录和10M缓存
							.build();
					/*client.interceptors().add(LoggingInterceptor);
					client.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
					client.interceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);*/

					mRetrofit = new Retrofit.Builder()
							.client(client)
							.baseUrl(Url.baseUrl)
							.build();
				}
			}
		}
		return mRetrofit;
	}
}
