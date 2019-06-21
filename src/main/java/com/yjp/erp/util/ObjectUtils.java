package com.yjp.erp.util;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author wyf 判断对象是否为空或null
 * @date 2019/3/28 上午 10:59
 **/
public class ObjectUtils {

    private static final String JAVAP = "java.";
    private static final String JAVADATESTR = "java.util.Date";

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        return false;
    }

    public static String randomNewNum (String str) {

        int i = new Random().nextInt(100000) + 1;

        return str + i;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(16);
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 利用递归调用将Object中的值全部进行获取
     *
     * @param timeFormatStr 格式化时间字符串默认<strong>2017-03-10 10:21</strong>
     * @param obj 对象
     * @param excludeFields 排除的属性
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMapString(String timeFormatStr, Object obj, String... excludeFields)
            throws IllegalAccessException {
        Map<String, String> map = new HashMap<>(16);

        if (excludeFields.length != 0) {
            List<String> list = Arrays.asList(excludeFields);
            objectTransfer(timeFormatStr, obj, map, list);
        } else {
            objectTransfer(timeFormatStr, obj, map, null);
        }
        return map;
    }

    /**
     * 递归调用函数
     *
     * @param obj 对象
     * @param map map
     * @param excludeFields 对应参数
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, String> objectTransfer(String timeFormatStr, Object obj, Map<String, String> map,
            List<String> excludeFields) throws IllegalAccessException {
        boolean isExclude = false;
        //默认字符串
        String formatStr = "YYYY-MM-dd HH:mm:ss";
        //设置格式化字符串
        if (timeFormatStr != null && !timeFormatStr.isEmpty()) {
            formatStr = timeFormatStr;
        }
        if (excludeFields != null) {
            isExclude = true;
        }
        Class<?> clazz = obj.getClass();
        //获取值
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = clazz.getSimpleName() + "." + field.getName();
            //判断是不是需要跳过某个属性
            if (isExclude && excludeFields.contains(fieldName)) {
                continue;
            }
            //设置属性可以被访问
            field.setAccessible(true);
            Object value = field.get(obj);
            Class<?> valueClass = value.getClass();
            if (valueClass.isPrimitive()) {
                map.put(fieldName, value.toString());
                //判断是不是基本类型
            } else if (valueClass.getName().contains(JAVAP)) {
                if (valueClass.getName().equals(JAVADATESTR)) {
                    //格式化Date类型
                    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                    Date date = (Date) value;
                    String dataStr = sdf.format(date);
                    map.put(fieldName, dataStr);
                } else {
                    map.put(fieldName, value.toString());
                }
            } else {
                objectTransfer(timeFormatStr, value, map, excludeFields);
            }
        }
        return map;
    }

    /**
     * @Title: filterArray   
     * @Description: 过滤字符串数组中的空值并去重
     * @param: arr 字符串数组
     * @return: String[]      
     */
	public static String[] filterArray(String[] arr) {
		Set<String> set = Sets.newConcurrentHashSet();
		if(isNotEmpty(arr)){
			for (String str : arr) {
				if (isNotEmpty(str)) {
					set.add(str);
				}
			}
		}
		return set.toArray(new String[0]);
	}
	
	/**
	 * @Title: listString2Long   
	 * @Description: 字符串集合转长整型集合并去重去空
	 * @param: strList 字符串集合
	 * @return: List<Long> 长整型集合
	 */
	public static List<Long> listString2Long(List<String> strList) {
		List<Long> list = Lists.newArrayList();
		if (strList == null || strList.isEmpty()) {
			return list;
		}
		for (String str : strList) {
			if (isNotEmpty(str)) {
				String[] strArr = str.split(",");
				for (String v : strArr) {
					if (!"null".equals(v) && isNotEmpty(v)) {
						list.add(Long.valueOf(v));
					}
				}
			}
		}
		return list.stream().distinct().collect(Collectors.toList());
	}
	
}
