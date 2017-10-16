package com.eshel.database.dao;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.database.DatabaseHelper;
import com.eshel.database.table.EssenceCacheTable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshiwen on 2017/10/12.
 */

public class EssenceCacheDao {
	static String startColumnName = "start";
	private static Dao<EssenceCacheTable, Integer> getDao(){
		try {
			return DatabaseHelper.getHelper(CurrencySpiritApp.getContext()).getEssenceCacheDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void add(int start, String json,long time){
		try {
			getDao().create(new EssenceCacheTable(json,start,time));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void del(int start){
		try {
			getDao().delete(queryByStart(start));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void update(int start, String json){
		EssenceCacheTable essenceCacheTable = queryByStart(start);
		essenceCacheTable.setJson(json);
		try {
			getDao().update(essenceCacheTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void update(EssenceCacheTable table, String json){
		table.setJson(json);
		try {
			getDao().update(table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static EssenceCacheTable queryByStart(int start){
		try {
			List<EssenceCacheTable> essenceCacheTables = getDao().queryForEq(startColumnName, start);
			if(essenceCacheTables.size() > 0)
				return essenceCacheTables.get(0);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<EssenceCacheTable> queryAll(){
		try {
			return getDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void delByMaxStart(List<EssenceCacheTable> list, int startStart){
		for (EssenceCacheTable essenceCacheTable : list) {
			if(essenceCacheTable.getStart()>=startStart){
				try {
					getDao().delete(essenceCacheTable);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
