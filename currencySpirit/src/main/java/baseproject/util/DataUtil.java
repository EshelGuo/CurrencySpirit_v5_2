package baseproject.util;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * createBy Eshel
 * createTime: 2017/10/15 10:32
 * desc: TODO
 */

public class DataUtil {
	public static  <K, V> Map.Entry<K, V> getTailByReflection(LinkedHashMap<K, V> map)
			throws NoSuchFieldException, IllegalAccessException {
		Field tail = map.getClass().getDeclaredField("tail");
		tail.setAccessible(true);
		return (Map.Entry<K, V>) tail.get(map);
	}

	/**
	 *
	 * @param d
	 * @param useSeparator false "999999" true "999,999"
	 * @return
	 */
	public static String double2Str(Double d,boolean useSeparator) {
		if (d == null) {
			return "";
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(useSeparator);
		return (nf.format(d));
	}

	/**
	 * 保留小数点
	 * @param value 小数
	 * @param format 小数点位数
	 * @return
	 */
	public static float saveD(float value, int format){
		return (float) (Math.round(value*Math.pow(format,format))/Math.pow(format,format));
	}
}
