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
 */

public interface NewListApi {
	/**
	 * https://fengzhihen.com/btcapp/newsInfo?start=1&count=20
	 * 精华专栏
	 */
	@GET("btcapp/newsInfo")Call<ResponseBody> essence(@Query("start") int start,@Query("count")int count);
	/**
	 * https://fengzhihen.com/btcapp/weiboInfo?start=1&count=20
	 * 微博专栏
	 */
	@GET("btcapp/weiboInfo")Call<ResponseBody> information(@Query("start") int start,@Query("count")int count);
	/**
	 * https://fengzhihen.com/btcapp/coinInfo?start=1&count=20&sort=volume&desc=false&symbol=USD
	 * 牛币专栏 todo 接口更新
	 * @param sort "volume" 市值排序 "percent" //涨幅排序
	 * @param desc true 升序 false 降序
	 * @param symbol USD 美元
	 */
	@GET("btcapp/coinInfo") Call<ResponseBody> coinInfo(@Query("start") int start, @Query("count")int count, @Query("sort") String sort, @Query("desc") boolean desc, @Query("symbol") String symbol);
	/**
	 * 订阅关注接口
	 * https://fengzhihen.com/btcapp/token/addtag?token=edf2ab61baef4ffc45a1928f0c7302d1db9ad2a6&pid=20&account=a&tagname=aaa&device=android
	 * @param token 信鸽推送返回的 token
	 * @param pid 设备唯一标识
	 * @param account 填空
	 * @param tagname coin_id
	 * @param device android
	 */
	@GET("btcapp/token/addtag") Call<ResponseBody> addtag(@Query("token") String token, @Query("pid")String pid, @Query("account") String account,@Query("tagname") String tagname, @Query("device") String device);
	/**
	 * 取消订阅关注接口
	 * https://fengzhihen.com/btcapp/token/deltag?token=edf2ab61baef4ffc45a1928f0c7302d1db9ad2a6&pid=20&account=a
	 * @param token 信鸽推送返回的 token
	 * @param pid 设备唯一标识
	 * @param account 填 空
	 * @param tagname coin_id
	 * @param device android
	 */
	@GET("btcapp/token/deltag") Call<ResponseBody> deltag(@Query("token") String token, @Query("pid")String pid, @Query("account") String account, @Query("tagname") String tagname, @Query("device") String device);
	/**
	 * https://fengzhihen.com/btcapp/coinInfo/select?symbol=USD&ids=bitcoin&ids=aaa
	 * 获取自选信息
	 */
	@GET("btcapp/coinInfo/select")Call<ResponseBody> select(@Query("symbol") String symbol, @Query("ids") List<String> ids);
	/**
	 * https://fengzhihen.com/btcapp/file/version?device=android
	 * 检查更新接口
	 */
	@GET("btcapp/file/version") Call<ResponseBody> update(@Query("device") String device);
	/**
	 * https://fengzhihen.com/btcapp/search?pattern=%E6%AF%94%E7%89%B9&symbol=USD
	 * 搜索接口
	 */
	@GET("btcapp/search") Call<ResponseBody> search(@Query("pattern") String keyword, @Query("symbol") String symbol);
	/**
	 * https://fengzhihen.com/btcapp/siteinfo?id=bitcoin
	 * 获取当前币种所在的交易平台信息
	 * todo 接口不完善 symbol
	 */
	@GET("btcapp/siteinfo")
	Call<ResponseBody> siteinfo(@Query("id") String coin_id,@Query("symbol") String symbol);
}
