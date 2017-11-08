package com.eshel.config;

import android.graphics.Color;

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
		NightViewUtil.addResId(
				R.drawable.title_bg,
				R.drawable.night_title_bg);
		NightViewUtil.addResId(
				R.drawable.bottom_bar_bg,
				R.drawable.night_bottom_bar_bg);
		NightViewUtil.addResId(
				R.drawable.search_drawable,
				R.drawable.night_search_drawable);
		NightViewUtil.addResId(
				R.drawable.btn_selector,
				R.drawable.night_btn_selector);

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
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.item_down),
				UIUtil.getColor(R.color.night_item_down));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.item_up),
				UIUtil.getColor(R.color.night_item_up));
		NightViewUtil.addColor(
				UIUtil.getColor(android.R.color.transparent),
				UIUtil.getColor(android.R.color.transparent));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.tabColor),
				UIUtil.getColor(R.color.night_tabColor));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.dividerColor),
				UIUtil.getColor(R.color.night_dividerColor));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.bottomTabColor),
				UIUtil.getColor(R.color.night_bottomTabColor));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.bottomBarColor),
				UIUtil.getColor(R.color.night_bottomBarColor));
		NightViewUtil.addColor(
				UIUtil.getColor(R.color.text_white),
				UIUtil.getColor(R.color.night_text_white));
		NightViewUtil.addColor(
				UIUtil.getColor(android.R.color.darker_gray),
				Color.parseColor("#dddddd"));
		// TODO: 2017/10/31  添加颜色
	}

}
