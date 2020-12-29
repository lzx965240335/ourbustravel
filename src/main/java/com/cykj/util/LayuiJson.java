package com.cykj.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LayuiJson extends HashMap implements Map {

    public LayuiJson(int totalRecords, List<?> list) {
        totalRecords = totalRecords == 0 ? 1 : totalRecords;
        put("code", 0);
        put("msg", "");
        put("count", totalRecords);
        put("data", list);
    }
}