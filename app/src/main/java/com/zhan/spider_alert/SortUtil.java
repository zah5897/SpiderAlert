package com.zhan.spider_alert;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zah on 2017/6/19.
 */

public class SortUtil {

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, ?> sortMapByKey(Map<String, ?> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return str1.compareTo(str2);
                    }
                });

        sortMap.putAll(map);

        return sortMap;
    }

}
