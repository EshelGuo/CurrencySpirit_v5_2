package com.eshel.config;

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
}
