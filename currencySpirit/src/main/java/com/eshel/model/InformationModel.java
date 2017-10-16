package com.eshel.model;

import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.EssenceFragment;
import com.eshel.currencyspirit.fragment.InformationFragment;

import java.io.Serializable;
import java.util.ArrayList;

import baseproject.base.BaseFragment;

/**
 * Created by guoshiwen on 2017/10/10.
 */

public class InformationModel implements Serializable{
	/**
	 * from_device : 微博 weibo.com
	 * from_web : WEIBO
	 * id : 3517
	 * imageurl : https://tva1.sinaimg.cn/crop.0.0.179.179.180/d87f5f8bjw1e9uvsfd8t6j2050050glq.jpg
	 * rawText : null
	 * text : 【俄罗斯将封禁提供比特币的网站 态度暧昧之后终现强硬立场】<a data-url="http://t.cn/ROIDrk4" target="_blank" href="https://weibo.cn/sinaurl/blocked6756ee4d?luicode=10000011&lfid=1076033632226187&u=http%3A%2F%2Fwww.bitcoin86.com%2Fnews%2F17229.html&ep=FpKuau7s1%2C3632226187%2CFpKuau7s1%2C3632226187" class=""><span class="url-icon"><img src="//h5.sinaimg.cn/upload/2015/09/25/3/timeline_card_small_web_default.png"></span></i><span class="surl-text">网页链接</a> 今日，“俄罗斯将封禁提供比特币的网站”的消息一时之间吸引币圈儿注意力，但该消息的出现并非偶然，因为此前俄罗斯方面已经有不少针对比特币监管的消息出炉。 ​​​
	 * uid : 3632226187
	 * update_time : 1507631113000
	 * wbid : 4161368947178485
	 * wbname : 比特币的那点事
	 */
	public static ArrayList<InformationModel> informationData = new ArrayList<>();
	public static int loadDataCount = 20;
	public String from_device;
	public String from_web;
	public int id;
	public String imageurl;
	public String rawText;
	public String text;
	public String uid;
	public long update_time;
	public String wbid;
	public String wbname;

	public static InformationModel getInformationDataByPosition(int position){
		return informationData.get(position);
	}
	public static void notifyView(boolean isSuccess){
		BaseFragment informationFragment = (BaseFragment) FragmentFactory.getFragment(InformationFragment.class);
		if(isSuccess) {
			if (informationFragment.getCurrState() != BaseFragment.LoadState.StateLoadSuccess)
				informationFragment.changeState(BaseFragment.LoadState.StateLoadSuccess);
			else {
				informationFragment.notifyView();
			}
		}else {
			informationFragment.changeState(BaseFragment.LoadState.StateLoadFailed);
		}
	}
}
