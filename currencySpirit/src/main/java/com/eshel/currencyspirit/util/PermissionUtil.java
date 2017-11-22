package com.eshel.currencyspirit.util;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.SplashActivity;

import java.util.HashSet;

import baseproject.permission.NeverAgainUtil;
import baseproject.permission.Permission;
import baseproject.permission.Permissions;
import baseproject.permission.RequestCallback;
import baseproject.util.Log;
import baseproject.util.StringUtils;

/**
 * Created by GuoShiwen on 2017/9/6.
 */
public class PermissionUtil {
	public static Activity activity;
	public static boolean gotosettinged;
	static RequestCallback callback = new RequestCallback() {
		DialogInterface.OnClickListener cancleClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO: 2017/9/6
				System.exit(0);
			}
		};
		public DialogInterface.OnClickListener confirmClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO: 2017/9/6
				Permissions.goBack(activity,callback);
			}
		};
		DialogInterface.OnClickListener gotoSettingClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//// TODO: 2017/9/6
				gotosettinged = true;
				NeverAgainUtil.newInstance().gotoPermissionSettingUI(activity, 0);
			}
		};

		@Override
		public void requestSuccess(Permission permission) {
			Log.i("permission", "request permission(" + permission.permission + ") success");
			successPermission.add(permission.permission);
			if(successPermission.size() == requestPermissionCount){
				if(permissionCallback != null) {
					permissionCallback.requestAllPermissionSuccess();
					activity = null;
				}
			}
		}

		@Override
		public boolean requestFailed(Permission permission) {
			String msg = UIUtil.getString(R.string.permission_failed_msg);
			String desc = getPermissionDesc(permission);
			if(!StringUtils.isEmpty(desc)){
				msg = desc+UIUtil.getString(R.string.permission_again);
			}
			Permissions.savePermission();
			new AlertDialog.Builder(activity)
					.setNegativeButton(R.string.cancle, cancleClick)
					.setCancelable(false)
					.setMessage(msg)
					.setTitle(R.string.warn)
					.setPositiveButton(R.string.again_accredit, confirmClick)
					.show();
			return true;
		}

		@Override
		public void havePermissioned(Permission permission) {
			havePermissioned.add(permission.permission);
			if(havePermissioned.size() == requestPermissionCount){
				if(permissionCallback != null) {
					permissionCallback.hasAllPermission();
					activity = null;
				}
			}
			successPermission.add(permission.permission);
			if(successPermission.size() == requestPermissionCount){
				if(permissionCallback != null) {
					permissionCallback.requestAllPermissionSuccess();
					activity = null;
				}
			}
		}

		@Override
		public boolean userSelectNeverAgain(Permission permission, NeverAgainUtil neverAgainUtil) {
			String msg = UIUtil.getString(R.string.permission_failed_msg);
			String desc = getPermissionDesc(permission);
			if(!StringUtils.isEmpty(desc)){
				msg = desc+UIUtil.getString(R.string.permission_gotosetting);
			}
			Permissions.savePermission();
			new AlertDialog.Builder(activity)
					.setNegativeButton(R.string.cancle, cancleClick)
					.setCancelable(false)
					.setMessage(msg)
					.setTitle(R.string.warn)
					.setPositiveButton(R.string.goto_setting, gotoSettingClick)
					.show();
			return true;
		}
	};
	private static String getPermissionDesc(Permission permission){
		String desc = "";
		switch (permission.permission){
			case Manifest.permission.READ_PHONE_STATE:
				desc = UIUtil.getString(R.string.read_phone_desc);
				break;
		}
		return desc;
	}
	private static HashSet<String> havePermissioned = new HashSet<>();
	private static HashSet<String> successPermission = new HashSet<>();
	private static int requestPermissionCount;
	public static void requestPermission(Activity activity,PermissionCallback callback, String ... permission){
		gotosettinged = false;
		permissionCallback = callback;
		PermissionUtil.activity = activity;
		requestPermissionCount = permission.length;
		havePermissioned.clear();
		successPermission.clear();
		Permissions.requestPermission(activity,PermissionUtil.callback ,permission);
	}
	static PermissionCallback permissionCallback;
	public interface PermissionCallback{
		void requestAllPermissionSuccess();
		void hasAllPermission();
	}
}