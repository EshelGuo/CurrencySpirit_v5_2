package com.eshel.config;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.night.NightViewUtil;

import baseproject.util.DensityUtil;
import baseproject.util.Log;
import baseproject.util.shape.ShapeUtil;

/**
 * Created by guoshiwen on 2017/10/17.
 */

public class AppConfig {
	public static Class<?>[] utilables = {
			Log.class,
			DensityUtil.class,
			ShapeUtil.class,
			NightViewUtil.class
	};
	public static String token;
	public static String deviceId;
	public static String deviceType = "android";
	public static boolean isNight = false;

	public static void nightConfig(){
		NightViewUtil.addResId(
				R.drawable.item_option_selector,
				R.drawable.night_item_option_selector);
		NightViewUtil.addResId(
				R.drawable.item_selector,
				R.drawable.night_item_selector);

		NightViewUtil.addColor(
				UIUtil.getColor(R.color.day_option_bg),
				UIUtil.getColor(R.color.night_option_bg));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.black),
				UIUtil.getColor(R.color.night_black));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.text_gray),
				UIUtil.getColor(R.color.night_text_gray));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.titleColor),
				UIUtil.getColor(R.color.night_titleColor));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.day_option_item_line_bg),
				UIUtil.getColor(R.color.night_option_item_line_bg));
		// TODO: 2017/10/31  添加颜色
	}

}
