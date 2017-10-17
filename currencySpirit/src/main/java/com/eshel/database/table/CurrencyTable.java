package com.eshel.database.table;

import com.eshel.model.CurrencyModel;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by guoshiwen on 2017/10/16.
 */
@DatabaseTable(tableName = "CurrencyTable")
public class CurrencyTable {
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(columnName = "chinesename")
	public String chinesename;
	@DatabaseField(columnName = "coin_id")
	public String coin_id;
	@DatabaseField(columnName = "englishname")
	public String englishname;
	@DatabaseField(columnName = "marketSymbol")
	public String marketSymbol;
	@DatabaseField(columnName = "market_type")
	public int market_type;
	@DatabaseField(columnName = "percent")
	public double percent;
	@DatabaseField(columnName = "platform")
	public String platform;
	@DatabaseField(columnName = "price")
	public double price;
	@DatabaseField(columnName = "rank")
	public int rank;
	@DatabaseField(columnName = "symbol")
	public String symbol;
	@DatabaseField(columnName = "turnnumber")
	public double turnnumber;
	@DatabaseField(columnName = "turnvolume")
	public double turnvolume;
	@DatabaseField(columnName = "update_time")
	public long update_time;
	@DatabaseField(columnName = "url")
	public String url;
	@DatabaseField(columnName = "yprice")
	public String yprice;
	@DatabaseField(columnName = "imageurl")
	public String imageurl;

	public void set(CurrencyModel model) {
		chinesename = model.chinesename;
		coin_id = model.coin_id;
		englishname = model.englishname;
		marketSymbol = model.marketSymbol;
		market_type = model.market_type;
		percent = model.percent;
		platform = model.platform;
		price = model.price;
		rank = model.rank;
		symbol = model.symbol;
		turnnumber = model.turnnumber;
		turnvolume = model.turnvolume;
		update_time = model.update_time;
		url = model.url;
		yprice = model.yprice;
		if(model.infoBean != null)
			imageurl = model.infoBean.imageurl;
	}
	public CurrencyModel get(CurrencyModel model){
		if(model == null)
			model = new CurrencyModel();
		model.chinesename = chinesename;
		model.coin_id = coin_id;
		model.englishname = englishname;
		model.marketSymbol = marketSymbol;
		model.market_type = market_type;
		model.percent = percent;
		model.platform = platform;
		model.price = price;
		model.rank = rank;
		model.symbol = symbol;
		model.turnnumber = turnnumber;
		model.turnvolume = turnvolume;
		model.update_time = update_time;
		model.url = url;
		model.yprice = yprice;
		if(model.infoBean != null)
			model.infoBean.imageurl = imageurl;
		return model;
	}
}
