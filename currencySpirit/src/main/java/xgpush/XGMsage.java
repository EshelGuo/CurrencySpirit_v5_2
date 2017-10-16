package xgpush;

import android.content.Intent;

import com.eshel.currencyspirit.activity.CurrencyDetailsActivity;
import com.eshel.currencyspirit.activity.EssenceDetailsActivity;
import com.eshel.currencyspirit.activity.WeiboDetailsActivity;
import com.eshel.model.CurrencyModel;
import com.eshel.model.EssenceModel;
import com.eshel.model.InformationModel;

import org.json.JSONException;
import org.json.JSONObject;

import baseproject.base.BaseActivity;

/**
 * createBy Eshel
 * createTime: 2017/10/14 22:26
 * desc: TODO
 */

public class XGMsage {
	public XGMsage(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public static XGMsage msg;
	public String title;
	public String content;

	public static void showMsg(BaseActivity baseActivity) {
	if(msg != null)
		try {
			JSONObject json = new JSONObject(msg.content);
			String type = json.optString("type");
			Intent intent = null;
			switch (type){
				case "NEWS":
					intent = new Intent(baseActivity, EssenceDetailsActivity.class);
					EssenceModel essenceModel = new EssenceModel();
					essenceModel.title = msg.title;
					essenceModel.imageurl = json.optString("icon");
					essenceModel.url = json.optString("newsurl");
					intent.putExtra(EssenceDetailsActivity.key,essenceModel);
					break;
				case "COIN":
					intent = new Intent(baseActivity, CurrencyDetailsActivity.class);
					CurrencyModel currencyModel = new CurrencyModel();
					currencyModel.infoBean.imageurl = json.optString("image");
					currencyModel.englishname = json.optString("englishname");
					currencyModel.infoBean.englishname = json.optString("englishname");
					currencyModel.chinesename =  msg.title;
					currencyModel.infoBean.chinesename =  msg.title;
					currencyModel.rank = json.optInt("rank");
					currencyModel.platform = json.optString("platform");
					currencyModel.url = json.optString("url");
					intent.putExtra(CurrencyDetailsActivity.key,currencyModel);
					break;
				case "WEIBO":
					intent = new Intent(baseActivity, WeiboDetailsActivity.class);
					InformationModel informationModel = new InformationModel();
					informationModel.wbname = msg.title;
					informationModel.imageurl = json.optString("imageurl");
					informationModel.wbid = json.optString("weiboid");
					informationModel.uid = json.optString("userid");
					intent.putExtra(WeiboDetailsActivity.key,informationModel);
					break;
			}
			if(intent != null)
				baseActivity.startActivity(intent);
			msg = null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
