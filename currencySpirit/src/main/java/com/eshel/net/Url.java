package com.eshel.net;

/**
 * createBy Eshel
 * createTime: 2017/10/5 03:08
 * desc: TODO
 */

public class Url {
	public static String baseUrl = "https://fengzhihen.com/btcapp/";
	public static String weibboBaseUrl = "https://m.weibo.cn/status/";
	public static String getWeiboUrl(String weiboId){
		return weibboBaseUrl.concat(weiboId);
	}
}