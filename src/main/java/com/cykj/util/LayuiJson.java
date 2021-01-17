package com.cykj.util;


import java.util.HashMap;
import java.util.List;


public class LayuiJson extends HashMap<String,Object> {

    public LayuiJson(int totalRecords, List<?> list) {
        totalRecords = totalRecords == 0 ? 1 : totalRecords;
        put("code", 0);
        put("msg", "");
        put("count", totalRecords);
        put("data", list);
    }
}