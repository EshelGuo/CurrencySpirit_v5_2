package com.eshel.database;
  
import java.sql.SQLException;  
import java.util.HashMap;  
import java.util.Map;  
  
import android.content.Context;  
import android.database.sqlite.SQLiteDatabase;

import com.eshel.database.table.EssenceCacheTable;
import com.eshel.database.table.EssenceHistory;
import com.eshel.database.table.JsonCacheTable;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.json.JSONObject;

public  class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "CurrencySpiritSqlite.db";
    private Map<String, Dao> daos = new HashMap<String, Dao>();  
    private Dao<EssenceCacheTable, Integer> EssenceCacheDao;
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }
  
    @Override  
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, EssenceCacheTable.class);
            TableUtils.createTable(connectionSource, EssenceHistory.class);
        } catch (SQLException e) {
            e.printStackTrace();  
        }  
    }
  
    @Override  
    public void onUpgrade(SQLiteDatabase database,  
            ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, EssenceCacheTable.class, true);
            TableUtils.dropTable(connectionSource, EssenceHistory.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();  
        }  
    }  
  
    private static DatabaseHelper instance;  
  
    /** 
     * 单例获取该Helper 
     * @param context
     * @return 
     */  
    public static synchronized DatabaseHelper getHelper(Context context){
        context = context.getApplicationContext();  
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)  
                    instance = new DatabaseHelper(context);  
            }
        }
        return instance;  
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    public Dao<EssenceCacheTable, Integer> getEssenceCacheDao() throws SQLException{
        if (EssenceCacheDao == null) {
            EssenceCacheDao = getDao(EssenceCacheTable.class);
        }
        return EssenceCacheDao;
    }
  
    /** 
     * 释放资源 
     */  
    @Override  
    public void close() {
        super.close();
            EssenceCacheDao = null;
    }
}