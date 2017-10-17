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
	public static void del(String coin_id){
		try {
			getDao().delete(queryByCoinId(coin_id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void update(CurrencyModel model){
		CurrencyTable currencyTable = queryByCoinId(model.coin_id);
		currencyTable.set(model);
		try {
			getDao().update(currencyTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static CurrencyTable queryByCoinId(String coin_id){
		try {
			List<CurrencyTable> currencyTableList = getDao().queryForEq("coin_id", coin_id);
			if(currencyTableList.size() > 0)
				return currencyTableList.get(0);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<CurrencyTable> queryAll(){
		try {
			Dao<CurrencyTable, Integer> dao = getDao();
			if(dao!= null)
				return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
