package com.cykj.bean;


import java.util.HashMap;
import java.util.List;


public class LayuiJson<C> extends HashMap  {

    public LayuiJson(int totalRecords, List<?> list) {
        totalRecords = totalRecords == 0 ? 1 : totalRecords;
        put("code", 0);
        put("msg", "");
        put("count", totalRecords);
        put("data", list);
    }


}