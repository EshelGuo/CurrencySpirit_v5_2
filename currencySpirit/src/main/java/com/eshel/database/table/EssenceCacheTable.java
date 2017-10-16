package com.eshel.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by guoshiwen on 2017/10/12.
 */
@DatabaseTable(tableName = "EssenceCacheTable")
public class EssenceCacheTable {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(columnName = "json")
	private String json;
	@DatabaseField(columnName = "start")
	private int start;
	@DatabaseField(columnName = "time")
	private long time;

	public EssenceCacheTable(String json, int start) {
		this.json = json;
		this.start = start;
	}

	public EssenceCacheTable(String json, int start, long time) {
		this.json = json;
		this.start = start;
		this.time = time;
	}

	public EssenceCacheTable() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
