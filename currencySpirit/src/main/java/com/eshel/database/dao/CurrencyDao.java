package com.eshel.database.dao;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.database.DatabaseHelper;
import com.eshel.database.table.CurrencyTable;
import com.eshel.database.table.EssenceHistory;
import com.eshel.model.CurrencyModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by guoshiwen on 2017/10/16.
 */

public class CurrencyDao {
	private static Dao<CurrencyTable, Integer> getDao(){
		try {
			return DatabaseHelper.getHelper(CurrencySpiritApp.getContext()).getDao(CurrencyTable.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void add(CurrencyModel model){
		try {
			CurrencyTable table = new CurrencyTable();
			table.set(model);
			getDao().create(table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*public static void del(String url){
		try {
			getDao().delete(queryByUrl(url));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	/*public static void update(EssenceModel model){
		EssenceHistory history = queryByUrl(model.url);
		history.set(model);
		try {
			getDao().update(history);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static EssenceHistory queryByUrl(String url){
		try {
			List<EssenceHistory> essenceCacheTables = getDao().queryForEq("url", url);
			if(essenceCacheTables.size() > 0)
				return essenceCacheTables.get(0);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<EssenceHistory> queryAll(){
		try {
			Dao<EssenceHistory, Integer> dao = getDao();
			if(dao!= null)
				return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
