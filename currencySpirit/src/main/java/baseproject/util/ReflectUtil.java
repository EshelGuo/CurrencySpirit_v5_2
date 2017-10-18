package baseproject.util;

import java.lang.reflect.Field;

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
}
