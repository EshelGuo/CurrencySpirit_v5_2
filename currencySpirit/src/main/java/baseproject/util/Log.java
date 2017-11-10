package baseproject.util;

import android.content.Context;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import baseproject.interfaces.Utilable;

/**
 * Created by Eshel on 2017/5/23.
 * Android 日志工具类
 * openLog() 开启日志输出
 * closeLog() 关闭日志输出
 * xxxWriteToFile() 输出日志到文件中
 * setLogFileName() 设置日志文件名 日志文件保存在 Android/data/packagename/file/log/xxx_yy_MM_dd.log
 * setLogValve() 设置日志输出阀值 当 log_levele 大于该级别时输出日志
 * closeWriteLogToFile() 关闭写入日志到文件中
 * addTagForWriteLog() 设置写入到日志文件中的 TAG 当包含该 TAG 时 会自动将日志写入文件
 * setLogTag() 设置统一的日志 TAG
 */

public class Log implements Utilable {

private static String TAG = "DefaultTag";
	public static void setLogTag(String tag){
		tagCache.add(tag);
		TAG = tag;
	}
private static Context mContext;
private static SimpleDateFormat logFormat;
private static volatile boolean logWriting;
private static volatile LinkedList<String> logCache;
private static LinkedHashSet<String> tagCache;
private static String logFileName = "defaultLog";
	public static void setLogFileName(String logFileName){
		if(logFileName != null)
			Log.logFileName = logFileName;
	}
private static int writeLogValve = 100;
	public static void setLogValve(int valve){
		writeLogValve = valve;
	}
	public static void closeWriteLogToFile(){
		writeLogValve = -1;
	}
private static ArrayList<String> tagForWriteLog;
	public static void addTagForWriteLog(String tag){
		if(tag == null)
			return;
		if(tagForWriteLog != null && tagForWriteLog.indexOf(tag) == -1)
			tagForWriteLog.add(tag);
	}
	public static void removeTagForWriteLog(String tag){
		if(tagForWriteLog != null && tagForWriteLog.indexOf(tag) != -1)
			tagForWriteLog.remove(tag);
	}

public final static int LEVELE_V = 0;
public final static int LEVELE_D = 1;
public final static int LEVELE_I = 2;
public final static int LEVELE_W = 3;
public final static int LEVELE_E = 4;
public final static int LEVELE_WTF = 5;
public final static int LEVELE_CLOSE_LOG = 6;

private static int log_levele = LEVELE_V;
	public static void setLogLevele(int logLevele){
		log_levele = logLevele;
	}
	public static int getLogLevele(){
		return log_levele;
	}
	public static void openLog(){
		log_levele = LEVELE_V;
	}
	public static void closeLog(){
		log_levele = LEVELE_CLOSE_LOG;
	}

	public static int v(Object msg){
		if(msg == null)
			msg = "null";
		return v(TAG,msg.toString());
	}
	public static int d(Object msg){
		if(msg == null)
			msg = "null";
		return d(TAG,msg.toString());
	}
	public static int i(Object msg){
		if(msg == null)
			msg = "null";
		return i(TAG,msg.toString());
	}
	public static int w(Object msg){
		if(msg == null)
			msg = "null";
		return w(TAG,msg.toString());
	}
	public static int e(Object msg){
		if(msg == null)
			msg = "null";
		return e(TAG,msg.toString());
	}
	public static int wtf(Object msg){
		if(msg == null)
			msg = "null";
		return wtf(TAG,msg.toString());
	}

	public static int v(Object tag,Object msg){
		if(msg == null)
			msg = "null";
		return v(tag.getClass().getSimpleName(),msg.toString());
	}
	public static int d(Object tag,Object msg){
		if(msg == null)
			msg = "null";
		return d(tag.getClass().getSimpleName(),msg.toString());
	}
	public static int i(Object tag,Object msg){
		if(msg == null)
			msg = "null";
		return i(tag.getClass().getSimpleName(),msg.toString());
	}
	public static int w(Object tag,Object msg){
		if(msg == null)
			msg = "null";
		return w(tag.getClass().getSimpleName(),msg.toString());
	}
	public static int e(Object tag,Object msg){
		if(msg == null)
			msg = "null";
		return e(tag.getClass().getSimpleName(),msg.toString());
	}
	public static int wtf(Object tag, Object msg){
		if(msg == null)
			msg = "null";
		return wtf(tag.getClass().getSimpleName(),msg.toString());
	}

	public static int v(Class tag,Object msg){
		if(msg == null)
			msg = "null";
		return v(tag.getSimpleName(),msg.toString());
	}
	public static int d(Class tag,Object msg){
		if(msg == null)
			msg = "null";
		return d(tag.getSimpleName(),msg.toString());
	}
	public static int i(Class tag,Object msg){
		if(msg == null)
			msg = "null";
		return i(tag.getSimpleName(),msg.toString());
	}
	public static int w(Class tag,Object msg){
		if(msg == null)
			msg = "null";
		return w(tag.getSimpleName(),msg.toString());
	}
	public static int e(Class tag,Object msg){
		if(msg == null)
			msg = "null";
		return e(tag.getSimpleName(),msg.toString());
	}
	public static int wtf(Class tag, Object msg){
		if(msg == null)
			msg = "null";
		return wtf(tag.getSimpleName(),msg.toString());
	}

	public static int vWriteToFile(Object msg){
		if(msg == null)
			msg = "null";
		return vWriteToFile(TAG,msg.toString());
	}
	public static int dWriteToFile(Object msg){
		if(msg == null)
			msg = "null";
		return dWriteToFile(TAG,msg.toString());
	}
	public static int iWriteToFile(Object msg){
		if(msg == null)
			msg = "null";
		return iWriteToFile(TAG,msg.toString());
	}
	public static int wWriteToFile(Object msg){
		if(msg == null)
			msg = "null";
		return wWriteToFile(TAG,msg.toString());
	}
	public static int eWriteToFile(Object msg){
		if(msg == null)
			msg = "null";
		return eWriteToFile(TAG,msg.toString());
	}
	public static int wtfWriteToFile(Object msg){
		if(msg == null)
			msg = "null";
		return wtfWriteToFile(TAG,msg.toString());
	}

	public static int vWriteToFile(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		int code = v(TAG, msg.toString());
		writeLogToFile(TAG,msg.toString(),LEVELE_V);
		return code;
	}
	public static int dWriteToFile(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		int code = d(TAG, msg.toString());
		writeLogToFile(TAG,msg.toString(),LEVELE_D);
		return code;
	}
	public static int iWriteToFile(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		int code = i(TAG, msg.toString());
		writeLogToFile(TAG,msg.toString(),LEVELE_I);
		return code;
	}
	public static int wWriteToFile(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		int code = w(TAG, msg.toString());
		writeLogToFile(TAG,msg.toString(),LEVELE_W);
		return code;
	}
	public static int eWriteToFile(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		int code = e(TAG, msg.toString());
		writeLogToFile(TAG,msg.toString(),LEVELE_E);
		return code;
	}
	public static int wtfWriteToFile(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		int code = wtf(TAG, msg.toString());
		writeLogToFile(TAG,msg.toString(),LEVELE_WTF);
		return code;
	}

	public static int v(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		return v(TAG,msg.toString());
	}
	public static int d(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		return d(TAG,msg.toString());
	}
	public static int i(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		return i(TAG,msg.toString());
	}
	public static int w(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		return w(TAG,msg.toString());
	}
	public static int e(String TAG, Object msg){
		if(msg == null)
			msg = "null";
		return e(TAG,msg.toString());
	}
	public static int wtf(String TAG, Object msg){

		return wtf(TAG,msg.toString());
	}

	public static int v(String tag, String msg) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_V) return -1;
		if(msg == null)
			msg = "null";
		writeLogToFileByTag(tag,msg,LEVELE_V);
		return android.util.Log.v(tag, msg);
	}
	public static int v(String tag, String msg, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_V) return -1;
		if(msg == null)
			msg = "null";
		return  android.util.Log.v(tag, msg, tr);
	}

	public static int d(String tag, String msg) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_D) return -1;
		if(msg == null)
			msg = "null";
		writeLogToFileByTag(tag,msg,LEVELE_D);
		return android.util.Log.d(tag, msg);
	}

	public static int d(String tag, String msg, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_D) return -1;
		if(msg == null)
			msg = "null";
		return android.util.Log.d(tag, msg, tr);
	}

	public static int i(String tag, String msg) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_I) return -1;
		if(msg == null)
			msg = "null";
		writeLogToFileByTag(tag,msg,LEVELE_I);
		return android.util.Log.i(tag, msg);
	}

	public static int i(String tag, String msg, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_I) return -1;
		if(msg == null)
			msg = "null";
		return android.util.Log.i(tag, msg, tr);
	}

	public static int w(String tag, String msg) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_W) return -1;
		if(msg == null)
			msg = "null";
		writeLogToFileByTag(tag,msg,LEVELE_W);
		return android.util.Log.w(tag, msg);
	}

	public static int w(String tag, String msg, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_W) return -1;
		if(msg == null)
			msg = "null";
		return android.util.Log.w(tag, msg, tr);
	}

	public static int w(String tag, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_W) return -1;
		return android.util.Log.w(tag, tr);
	}

	public static int e(String tag, String msg) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_E) return -1;
		writeLogToFileByTag(tag,msg,LEVELE_E);
		if(msg == null)
			msg = "null";
		return android.util.Log.e(tag, msg);
	}

	public static int e(String tag, String msg, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_E) return -1;
		if(msg == null)
			msg = "null";
		return android.util.Log.e(tag, msg, tr);
	}
	public static int wtf(String tag, String msg) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_WTF) return -1;
		if(msg == null)
			msg = "null";
		writeLogToFileByTag(tag,msg,LEVELE_WTF);
		return android.util.Log.wtf(tag, msg);
	}

	public static int wtf(String tag, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_WTF) return -1;
		return android.util.Log.wtf(tag, tr);
	}

	public static int wtf(String tag, String msg, Throwable tr) {
		if(tag != null)
			tagCache.add(tag);
		if(log_levele > LEVELE_WTF) return -1;
		if(msg == null)
			msg = "null";
		return android.util.Log.wtf(tag, msg, tr);
	}

	public static String toString(String objName, Object obj){
		// TODO: 2017/11/10  集合 Map List 如果集合中的元素没有实现toString方法, 会有问题  待做
		if(objName == null)
			objName = "null";
		if(obj == null)
			return String.format("(%s=[null])",objName);
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("(%s=[",objName));
		for (Field field : obj.getClass().getDeclaredFields()) {
			try {
				if(!Modifier.isPublic(field.getModifiers())){
					field.setAccessible(true);
				}
				String name = field.getName();
				Object value = field.get(obj);
				sb.append(name);
				sb.append(":");
				if((value instanceof Integer)||(value instanceof Float)||(value instanceof Double)||
						(value instanceof Character)||(value instanceof String)|| (value instanceof Short)||
						(value instanceof Byte)||(value instanceof Collection)||(value instanceof Map)){
					sb.append(value);
				}else {
					sb.append(toString(name,value));
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.append("])");
		return sb.toString();
	}

	private static void writeLogToFileByTag(String tag, String msg, int levele){
		if(tagForWriteLog.indexOf(tag) != -1){
			writeLogToFile(tag,msg,levele);
		}
	}
	private static void writeLogToFile(String tag, String msg , int levele){
		if(tag != null)
			tagCache.add(tag);
		if(writeLogValve <= 0)
			return;
		// add log to cache 10-02 17:02:14.098 11853-11853/? E/LogRecoder: 请先初始化 log = [DaemonReceiver] onReceive
		String logLevele;
		switch (levele){
			case LEVELE_D:
				logLevele = "D";
				break;
			case LEVELE_I:
				logLevele = "I";
				break;
			case LEVELE_W:
				logLevele = "W";
				break;
			case LEVELE_E:
				logLevele = "E";
				break;
			case LEVELE_WTF:
				logLevele = "WTF";
				break;
			default:
				logLevele = "V";
				break;
		}
		String date = logFormat.format(new Date());
		String lineLog = date + " /? " + logLevele + "/" + tag + ": " + msg;
		logCache.addLast(lineLog);
		if(logWriting)
			return;
		logWriting = true;
		if(logCache.size() > writeLogValve)
			new Thread(writeLogTask).start();
	}

	public static void logTags(){
		i(tagCache);
	}

	private static Runnable writeLogTask;

	@Override
	public void init(Context context) {
		mContext = context;
		logFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss",Locale.getDefault());
		tagForWriteLog = new ArrayList<>();
		logCache = new LinkedList<>();
		tagCache = new LinkedHashSet<>();
		writeLogTask = new Runnable() {
			@Override
			public void run() {
				if(!logWriting)
					logWriting = true;
				writeLogToFile();
				logWriting = false;
			}
			private void writeLogToFile(){
				BufferedWriter bw = null;
				try {
					File appDirAsSDCard = mContext.getExternalFilesDir(null);
					File logDir = new File(appDirAsSDCard,"log");
					if(!logDir.exists() || !logDir.isDirectory())
						logDir.mkdir();
					SimpleDateFormat format = new SimpleDateFormat("_yyyy_MM_dd", Locale.getDefault());
					File logFile = new File(logDir,logFileName + format.format(new Date()) + ".log");
					if(!logFile.exists() || !logFile.isFile())
						logFile.createNewFile();
					bw = new BufferedWriter(new FileWriter(logFile,true));
					while (!logCache.isEmpty()){
						bw.write(logCache.remove());
						bw.newLine();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e){
					e.printStackTrace();
				} finally {
					SteamUtils.closeStream(bw);
				}
			}
		};
	}

	@Override
	public void deInit() {
		writeLogValve = -1;
		mContext = null;
		TAG = "DefaultTag";
		logFileName = "defaultLog";
		if(logCache != null)
			logCache.clear();
		logCache = null;
		if(tagCache != null)
			tagCache.clear();
		tagCache = null;
		tagForWriteLog = null;
		logFormat = null;
	}
}