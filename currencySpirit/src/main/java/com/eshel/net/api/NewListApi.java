package com.eshel.net.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * createBy Eshel
 * createTime: 2017/10/5 02:58
 * desc: 新闻类别网络请求
 * https://fengzhihen.com/btcapp/newsInfo?start=1&count=20    // 精华专栏
 * https://fengzhihen.com/btcapp/weiboInfo?start=1&count=20    // 微博专栏
 * https://fengzhihen.com/btcapp/coinInfo?start=1&count=20&sort=volume&desc=false&symbol=USD   // 牛币专栏
 * https://fengzhihen.com/btcapp/coinInfo?start=1&count=20&sort=percent&desc=false&symbol=USD   // 牛币专栏
 * https://fengzhihen.com/btcapp/token/addtag?token=edf2ab61baef4ffc45a1928f0c7302d1db9ad2a6&pid=20&account=a&tagname=aaa&device=android   // 订阅
 * https://fengzhihen.com/btcapp/token/deltag?token=edf2ab61baef4ffc45a1928f0c7302d1db9ad2a6&pid=20&account=a   // 取消订阅
 * https://fengzhihen.com/btcapp/coinInfo/select?symbol=USD&ids=bitcoin&ids=aaa   // 获取自选信息
 * https://fengzhihen.com/btcapp/file/version?device=android
 */

public interface NewListApi {
//	@Headers("Cache-Control: public, max-age=3600")
	@GET("btcapp/newsInfo")
	Call<ResponseBody> essence(@Query("start") int start,@Query("count")int count);
//	@Headers("Cache-Control: public, max-age=3600")
	@GET("btcapp/weiboInfo")
	Call<ResponseBody> information(@Query("start") int start,@Query("count")int count);
//	@Headers("Cache-Control: public, max-age=3600")
	@GET("btcapp/coinInfo")
	Call<ResponseBody> coinInfo(
			@Query("start") int start,
			@Query("count")int count,
			@Query("sort") String sort,// "volume" 市值排序 "percent" //涨幅排序
			@Query("desc") boolean desc,// true 升序 false 降序
			@Query("symbol") String symbol);// USD 美元
	/**
	 * 订阅关注接口
	 * @param token 信鸽推送返回的 token
	 * @param pid 设备唯一标识
	 * @param account 填空
	 * @param tagname coin_id
	 * @param device android
	 * @return aaa
	 */
	@GET("btcapp/token/addtag")
	Call<ResponseBody> addtag(
			@Query("token") String token,
			@Query("pid")String pid,
			@Query("account") String account,
			@Query("tagname") String tagname,
			@Query("device") String device
	);
	//

	/**
	 * 取消订阅关注接口
	 * @param token 信鸽推送返回的 token
	 * @param pid 设备唯一标识
	 * @param account 填空
	 * @param tagname coin_id
	 * @param device android
	 * @return bbb
	 */
	@GET("btcapp/token/deltag")
	Call<ResponseBody> deltag(
			@Query("token") String token,
			@Query("pid")String pid,
			@Query("account") String account,
			@Query("tagname") String tagname,
			@Query("device") String device
	);
	@GET("btcapp/coinInfo/select")
	Call<ResponseBody> select(
			@Query("symbol") String symbol,
			@Query("ids") List<String> ids
	);
	@GET("btcapp/file/version")
	Call<ResponseBody> update(
			@Query("device") String device
	);
}
