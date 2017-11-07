package com.eshel.currencyspirit.util;

import android.os.Handler;
import android.os.Looper;

import com.eshel.currencyspirit.CurrencySpiritApp;


/**
 * Created by guoshiwen on 2017/10/13.
 */

public class ThreadUtil {
	public static boolean isMainThread(){
		if(Looper.myLooper() == Looper.getMainLooper())
			return true;
		if(Thread.currentThread().getName().equals(CurrencySpiritApp.getMainThreadName()))
			return true;
		else
			return false;
	}

	public static Handler getHandler() {
		return CurrencySpiritApp.getApp().getHandler();
	}
}
