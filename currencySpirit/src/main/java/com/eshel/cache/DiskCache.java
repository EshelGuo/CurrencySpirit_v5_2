package com.eshel.cache;

import com.eshel.database.dao.EssenceCacheDao;
import com.eshel.database.table.EssenceCacheTable;

import java.util.List;

/**
 * createBy Eshel
 * createTime: 2017/10/5 03:39
 * desc: 磁盘缓存
 */

public class DiskCache {
	/*private static int maxCache = 200;
	private static int count = 20;
	private static int currentCacheSize = 0;
	public static void cacheToDisk(CacheType type, int start, String json){
		List<EssenceCacheTable> essenceCacheTables = null;
		if(currentCacheSize == 0) {
			essenceCacheTables = EssenceCacheDao.queryAll();
			currentCacheSize = essenceCacheTables.size();
		}
		if(currentCacheSize >= maxCache){
			if(essenceCacheTables == null)
				essenceCacheTables = EssenceCacheDao.queryAll();
			EssenceCacheDao.delByMaxStart(essenceCacheTables,count*maxCache/2);
		}
		switch (type){
			case Type_Essence:
				EssenceCacheTable essenceCacheTable = EssenceCacheDao.queryById(start);
				if(essenceCacheTable != null){
					essenceCacheTable.setTime(System.currentTimeMillis());
					EssenceCacheDao.update(essenceCacheTable,json);
				}else {
					EssenceCacheDao.add(start,json,System.currentTimeMillis());
				}
				break;
		}
	}
	public static String getJsonFromDiskCache(CacheType type, int start){
		String json = null;
		switch (type){
			case Type_Essence:
				EssenceCacheTable essenceCacheTable = EssenceCacheDao.queryById(start);
				if(essenceCacheTable != null)
					json = essenceCacheTable.getJson();
				break;
		}
		return json;
	}*/
}
