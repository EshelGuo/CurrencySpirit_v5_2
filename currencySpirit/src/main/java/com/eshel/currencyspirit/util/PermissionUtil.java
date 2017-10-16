package com.eshel.currencyspirit.util;
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

/**
 * Created by GuoShiwen on 2017/9/6.
 */
public class PermissionUtil {
	static Activity activity;
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
				NeverAgainUtil.newInstance().gotoPermissionSettingUI(activity, 0);
			}
		};

		@Override
		public void requestSuccess(Permission permission) {
			Log.i("permission", "request permission(" + permission.permission + ") success");
			successPermission.add(permission.permission);
			if(successPermission.size() == requestPermissionCount){
				if(permissionCallback != null)
					permissionCallback.requestAllPermissionSuccess();
			}
		}

		@Override
		public boolean requestFailed(Permission permission) {
			Permissions.savePermission();
			new AlertDialog.Builder(activity)
					.setNegativeButton(R.string.cancle, cancleClick)
					.setCancelable(false)
					.setMessage(UIUtil.getString(R.string.permission_failed_msg))
					.setTitle(R.string.warn)
					.setPositiveButton(R.string.again_accredit, confirmClick)
					.show();
			return true;
		}

		@Override
		public void havePermissioned(Permission permission) {
			havePermissioned.add(permission.permission);
			if(havePermissioned.size() == requestPermissionCount){
				if(permissionCallback != null)
					permissionCallback.hasAllPermission();
			}
		}

		@Override
		public boolean userSelectNeverAgain(Permission permission, NeverAgainUtil neverAgainUtil) {
			Permissions.savePermission();
			new AlertDialog.Builder(activity)
					.setNegativeButton(R.string.cancle, cancleClick)
					.setCancelable(false)
					.setMessage(UIUtil.getString(R.string.never_again))
					.setTitle(R.string.warn)
					.setPositiveButton(R.string.goto_setting, gotoSettingClick)
					.show();
			return true;
		}
	};
	private static HashSet<String> havePermissioned = new HashSet<>();
	private static HashSet<String> successPermission = new HashSet<>();
	private static int requestPermissionCount;
	public static void requestPermission(Activity activity,PermissionCallback callback, String ... permission){
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