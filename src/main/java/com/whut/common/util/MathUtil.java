package com.whut.common.util;

import com.alibaba.fastjson.JSONArray;

public class MathUtil {

    public static String mean(JSONArray array)
    {
        double value = 0;
        for(int i=0;i!=array.size();++i){
            value += array.getDouble(i);
        }
        return String.format("%.2f",value/array.size());
    }
}
