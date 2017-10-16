package com.eshel.database.table;
  
import com.j256.ormlite.field.DatabaseField;  
import com.j256.ormlite.table.DatabaseTable;  
  
@DatabaseTable(tableName = "JsonCacheTable")
public class JsonCacheTable {
    @DatabaseField(generatedId = true)  
    private int id;  
    @DatabaseField(columnName = "json")
    private String json;
    @DatabaseField(columnName = "baseUrl")
    private String baseUrl;
    @DatabaseField(columnName = "start")
    private int start;
  
    public JsonCacheTable() {}

    public JsonCacheTable(String json, String baseUrl) {
        this.json = json;
        this.baseUrl = baseUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}