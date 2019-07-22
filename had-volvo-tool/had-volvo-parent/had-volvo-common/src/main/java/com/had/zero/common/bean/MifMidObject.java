package com.had.zero.common.bean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MIF,MID实体对象
 * @author Quan Wei
 * @date 2019/4/10
 **/
public class MifMidObject {

    private int columnLength;

    /**
     * 处理的MIF文件的路径
     */
    private String mifPath;

    /**
     * 字段索引字典
     */
    private Map<String, Integer> indexMap;

    /**
     * 字段值列表
     */
    private List<String> valueList;

    /**
     * 构造函数，传入MIF的路径，构建MifMid的结构
     * @param mifPath MIF的路径
     */
    public MifMidObject(String mifPath) throws IOException {
        indexMap = new HashMap<>();
        valueList = new ArrayList<>();
        String tempString = null;
        int columnLine = 0;
        int readCount = 0;
        BufferedReader reader = null;
        try {
            this.mifPath = mifPath;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(mifPath), "UTF-8"));
            Integer index = 0;
            while ((tempString = reader.readLine()) != null) {
                if (!tempString.equals("")) {
                    if (tempString.contains("Columns")) {
                        String[] temp = tempString.split(" ");
                        if (temp.length == 2) {
                            columnLine = Integer.parseInt(temp[1]);
                            columnLength = columnLine;
                        }
                        continue;
                    }
                    if (columnLine > 0) {
                        String[] column = tempString.split(" ");
                        String field = column[0];
                        indexMap.put(field, index++);
                        columnLine--;
                    }
                    if (tempString.equals("Data")) {
                        break;
                    }
                }
                readCount++;
            }
        } finally {
            if(reader != null) reader.close();
        }
        if (0 == readCount) {
            throw new IOException(mifPath + "文件无内容");
        }
    }

    /**
     * 判断字段是否存在
     * @param fieldName
     * @return
     */
    public boolean isExistFieldName(String fieldName){
        if(indexMap.containsKey(fieldName)) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 根据字段名称获取字段值
     * @param fieldName 字段名称
     * @return
     */
    public String getMidByFieldName(String fieldName) {
        if(indexMap.containsKey(fieldName)) {
            int index = indexMap.get(fieldName);
            if(index < valueList.size()) {
                String mid = "";
                for(int i = 0; i < valueList.size(); i++){
                    String value = valueList.get(i);
                    mid += "\"";
                    if(i == index){
                        mid += 0;
                    }else{
                        mid += value;
                    }

                    if(i == columnLength-1){
                        mid += "\"";
                    }else{
                        mid += "\",";
                    }
                }
                return mid;
            }
        }
        return null;
    }

    /**
     * 设置MID数据
     * @param midArray MID数据数组
     */
    public void setMid(String[] midArray) throws IOException {
        // 清空
        valueList.clear();
        if(midArray.length != columnLength) {
            throw new IOException("字段个数无法匹配" + midArray);
        }
        for(String str : midArray) {
            valueList.add(str);
        }
    }

    /**
     * 设置MID数据
     * @param midString MID数据字符串
     */
    public void setMid(String midString) throws IOException {
        String[] strArray = midString.replace("\"", "").split(",");
        setMid(strArray);
    }
}
