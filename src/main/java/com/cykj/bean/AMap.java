package com.cykj.bean;

import java.util.HashMap;

public class AMap extends HashMap {
  //  {className:'AMap.LngLat',lat:24.492822,lng:118.176976,kT:24.492822,KL:118.176976}

    public AMap(Double X, Double Y,Double[] bmc) {
        put("className","AMap.LngLat");
        put("KL",X);
        put("kT",Y);
        put("lng",X);
        put("lat",Y);
        put("pos",bmc);
    }


}
