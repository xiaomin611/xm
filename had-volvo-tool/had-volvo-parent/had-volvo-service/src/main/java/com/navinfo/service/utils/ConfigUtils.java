package com.navinfo.service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Resources;
import com.had.zero.common.bean.DeleteConfigBean;
import org.apache.commons.io.Charsets;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ConfigUtils {

    public static void main(String[] args) {
        System.out.println(getModifyTables());
    }

    static DeleteConfigBean b;
    static {
        b= getJsonResource("DeleteConfig").toJavaObject(DeleteConfigBean.class);
    }
    public static List<String> getDeleteTables(){
      return b.getDELETE_TABLES();
    }

    public static List<String> getModifyTables(){
        List<Map<String, List<String>>> l = b.getDELETE_COLUMNS();
        List<String> ret=null ;
        for (Map<String, List<String>> m :l){
            Set<String> s = m.keySet();
            ret = new ArrayList<>(s);
        }

        return  ret;
    }
    public static List<String> getDeleteColByTable(String table){

        List<Map<String, List<String>>> l = b.getDELETE_COLUMNS();
        for (Map<String, List<String>> m :l){
            if (m.containsKey(table)){
                return m.get(table);
            }
        }
        return null;
    }


    public static JSONObject getJsonResource(String fileName){
        fileName+=".json";
        ClassLoader classLoader = getClassLoader();

        Enumeration<URL> resources;
        JSONObject jsonObject = new JSONObject();
        try {
            resources = classLoader.getResources(fileName);
        } catch (IOException e) {
            return jsonObject;
        }

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            try {
                String json = Resources.toString(url, Charsets.UTF_8);
                jsonObject.putAll(JSON.parseObject(json)); // 有多个的时候，后面的覆盖前面的
            } catch (IOException e) {
            }
        }
        return jsonObject;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            return classLoader;
        }
        return ConfigUtils.class.getClassLoader();
    }

    /**
     * 私有构造方法，防止工具类被new
     */
    private ConfigUtils() {
        throw new IllegalAccessError();
    }

}
