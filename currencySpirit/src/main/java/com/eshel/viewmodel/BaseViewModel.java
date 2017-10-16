package com.eshel.viewmodel;

/**
 * createBy Eshel
 * createTime: 2017/10/15 22:02
 * desc: TODO
 */

public abstract class BaseViewModel {
	int start = 0;
	int count = 20;
	long refreshTime = 2000;

	public abstract void getData( Mode mode);
	public void refreshData(){
		start = 0;
		getData(Mode.REFRESH);
	}
	public enum Mode{
		NORMAL,REFRESH,LOADMORE;
	}
	static long getTimeDifference(long ago){
		long afterTime = System.currentTimeMillis();
		return afterTime - ago;
	}
	long getRefreshTime(Mode mode,long ago){
		start += count;
		long refreshTime;
		if(mode == Mode.REFRESH){
			refreshTime = this.refreshTime - BaseViewModel.getTimeDifference(ago);
			if(refreshTime < 0)
				refreshTime = 0;
		}else {
			refreshTime = 0;
		}
		return refreshTime;
	}
}
