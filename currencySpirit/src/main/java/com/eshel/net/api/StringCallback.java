package com.eshel.net.api;

/**
 * Created by guoshiwen on 2017/11/15.
 */

public interface StringCallback {
	void onSuccess(String result);
	void onFailed(String errMsg);
}
