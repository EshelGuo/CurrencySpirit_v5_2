package baseproject.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by guoshiwen on 2017/10/30.
 */

public class ViewUtil {
	@Deprecated
	public static void findAllViewById(@NonNull Object taget,@NonNull View view,Class R_ID){
		if(taget == null){
			throw new NullPointerException("taget 不能为 null");
		}
		if(view == null){
			throw new NullPointerException("view 不能为 null");
		}
		Field[] tagets = taget.getClass().getDeclaredFields();
		for (Field field : tagets) {
			if(View.class.isAssignableFrom(field.getType())){
				String idName = parseNameForIdName(field.getName());
				try {
					Field idField = R_ID.getField(idName);
					if(idField != null){
						int id = idField.getInt(null);
						View v = view.findViewById(id);
						if(v == null){
							Log.e(Config.TAG,"没有字段名为 "+idName+" 的View~!!!");
						}else {
							if(!Modifier.isPublic(field.getModifiers())){
								field.setAccessible(true);
								field.set(taget,v);
								field.setAccessible(false);
							}else
								field.set(taget,v);
						}
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					Log.e(Config.TAG,"字段名为 "+idName+" 资源ID字段找不到~!!!");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					Log.e(Config.TAG,"字段名为 "+idName+" 资源ID值找不到~!!!");
				}
			}
		}
	}

	private static String parseNameForIdName(String name) {
		StringBuilder sb = new StringBuilder();
		boolean firstChar = true;
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if(firstChar){
				firstChar = false;
				if(c == 'm'){
					chars[1] = Character.toLowerCase(chars[1]);
					continue;
				}
			}
			if(Character.isUpperCase(c)){
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			}else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
