package baseproject.manager;


import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.util.UIUtil;

import baseproject.base.BaseApplication;
import baseproject.interfaces.Utilable;
import baseproject.util.Config;
import baseproject.util.Log;
import baseproject.util.shape.ShapeUtil;

import static com.eshel.config.AppConfig.utilables;

/**
 * createBy Eshel
 * createTime: 2017/10/2 15:26
 * desc: 工具类管理类
 */

public class UtilManager {

	public static void initUtils(){
		for (Class<?> utilable : utilables) {
			try {
				Object utilObj = utilable.newInstance();
				if(utilObj instanceof Utilable){
					Utilable util = (Utilable) utilObj;
					util.init(BaseApplication.getContext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}
		}
		setUtilConfig();
	}

	public static void setUtilConfig(){
		if(Config.isApkDebugable(CurrencySpiritApp.getContext())) {
			Log.openLog();
			UIUtil.setDebug(true);
		}else {
			Log.closeLog();
			UIUtil.setDebug(false);
		}
		ShapeUtil.setDefaultName("config.sp");
	}

	public static void deinitUtils(){
		for (Class<?> utilable : utilables) {
			try {
				Object utilObj = utilable.newInstance();
				if(utilObj instanceof Utilable){
					Utilable util = (Utilable) utilObj;
					util.deInit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}
		}
	}
}
