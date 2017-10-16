package com.eshel.database.dao;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.database.DatabaseHelper;
import com.eshel.database.table.EssenceHistory;

/**
 * Created by guoshiwen on 2017/10/16.
 */
@Deprecated
public class Dao {
	public static com.j256.ormlite.dao.Dao<EssenceHistory, Integer> getDao(Class tableClass){
		try {
			return DatabaseHelper.getHelper(CurrencySpiritApp.getContext()).getDao(tableClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
