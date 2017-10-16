package xgpush.receiver;

import android.content.Context;

import com.eshel.currencyspirit.bean.XGNotification;
import com.eshel.currencyspirit.util.UIUtil;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import baseproject.util.Log;
import xgpush.XGMsage;

/**
 * Created by guoshiwen on 2017/10/10.
 */

public class XGMessageReceiver extends XGPushBaseReceiver {
//	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show(String text) {
		UIUtil.debugToast(text);
	}
	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		XGNotification notific = new XGNotification();
		notific.setMsg_id(notifiShowedRlt.getMsgId());
		notific.setTitle(notifiShowedRlt.getTitle());
		notific.setContent(notifiShowedRlt.getContent());
		// notificationActionType==1为Activity，2为url，3为intent
		notific.setNotificationActionType(notifiShowedRlt
				.getNotificationActionType());
		//Activity,url,intent都可以通过getActivity()获得
		notific.setActivity(notifiShowedRlt.getActivity());
		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
//		NotificationService.getInstance(context).save(notific);
//		context.sendBroadcast(intent);
		// TODO: 2017/10/13 需要将消息展示到通知栏
		show("您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
		Log.i("LC","+++++++++++++++++++++++++++++展示通知的回调");
	}
	//反注册的回调
	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "取消注册成功";
		} else {
			text = "取消注册失败" + errorCode;
		}
		Log.i(LogTag, text);
		show(text);
	}
	//设置tag的回调
	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		Log.i(LogTag, text);
		show(text);
	}
	//删除tag的回调
	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		Log.i(LogTag, text);
		show(text);
	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		Log.e("LC","++++++++++++++++++");
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			XGMsage.msg = new XGMsage(message.getTitle(),message.getCustomContent());
			text = "通知被打开 :" + message;
			/*if(!ProcessUtil.appIsForeground() && CurrencySpiritApp.isExit){
				UIUtil.debugToast("app 在后台");
				ProcessUtil.moveAppToForeground();
//				XGMsage.showMsg( BaseActivity.getTopActivity());
			}else{
				BaseActivity lastActivity = BaseActivity.getLastActivity();
				if(!(lastActivity instanceof HomeActivity) && !(lastActivity instanceof SplashActivity)){
					XGMsage.showMsg( lastActivity);
				}
			}*/
			/*if(ProcessUtil.appIsForeground()){
				// app 在前台
			}else {
				if (CurrencySpiritApp.isExit) {
					ProcessUtil.moveAppToForeground();
				} else {
					Intent intent = new Intent(context, SplashActivity.class);
					context.startActivity(intent);
				}
			}*/
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
		UIUtil.debugShortToast("广播接收到通知被点击:" + message.toString());
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.i(LogTag, "get custom value:" + value);}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();}}
		// APP自主处理的过程。。。
		Log.i(LogTag, text);
		show(text);
	}

	//注册的回调
	@Override
	public void onRegisterResult(Context context, int errorCode,XGPushRegisterResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
		Log.i(LogTag, text);
		show(text);
	}

	// 消息透传的回调
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();

		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.i(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...
		Log.i(LogTag, text);
		show(text);
	}
}
