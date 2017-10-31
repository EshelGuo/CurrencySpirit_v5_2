package baseproject.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by guoshiwen on 2017/10/18.
 */

public class ReflectUtil {

	public static int getPublicStaticInt(Class clazz,String fieldName){
		try {
			Field field = clazz.getField(fieldName);
			return field.getInt(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 通过反射获取某个成员变量的值
	 * @param object 如果是静态变量,object 需要传 Class, 否则传对象
	 * @param fieldName 字段名
	 * @return 字段的值
	 */
	public static Object getFiledValue(Object object,String fieldName){
		if(object instanceof Class) {
			return getFiledValue((Class) object,null,fieldName);
		}else {
			return getFiledValue(object.getClass(),object,fieldName);
		}
	}
	public static Object getFiledValue(Class clazz, Object object,String fieldName){
		if(fieldName == null||clazz == null)
			return null;
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if(!Modifier.isPublic(field.getModifiers())){
				field.setAccessible(true);
			}
			return field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
