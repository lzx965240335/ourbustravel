package com.cykj.service.impl;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.DrivSign;
import com.cykj.bean.Driver;
import com.cykj.mapper.DriverMapper;
import com.cykj.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    DriverMapper driverMapper;
    @Override
    public Driver login(String account, String password) {
        Driver driver = new Driver();
        driver.setDrivAccount(account);
        driver.setDrivPassword(password);
        List<Driver> drivers =driverMapper.selectByLogin(driver);
        if (drivers.size()>0){
            return drivers.get(0);
        }
        return null;
    }

    @Override
    public Driver getSigns(String DSYear, String DSMonth,int days ,int drivId) {
        System.out.println(DSMonth.equals("0"));
        if (DSMonth.equals("0")){
            Calendar cal = Calendar.getInstance();
            days = getMonthDays(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH) + 1);
        }
        HashMap map = new HashMap();
        map.put("DSYear",DSYear);
        map.put("DSMonth",DSMonth);
        map.put("drivId",drivId);
        List<Driver> drivers =driverMapper.getSigns(map);
        if (drivers.size()>0){
            return drivers.get(0);
        }else{
            String signs = "0000000000000000000000000000";
            switch (days){
                case 29 :signs+="0";
                break;
                case 30: signs+="00";
                break;
                case 31 : signs+="000";
                break;
                default: break;
            }
            map.put("DSSign",signs);
            int x = driverMapper.newMonth(map);
            if (x>0){
                return driverMapper.getSigns(map).get(0);
            }
        }
        return null;
    }

    @Override
    public int do_clock(int drivId) {
        Calendar rightNow    =    Calendar.getInstance();
		/*用Calendar的get(int field)方法返回给定日历字段的值。
		HOUR 用于 12 小时制时钟 (0 - 11)，HOUR_OF_DAY 用于 24 小时制时钟。*/
        Integer year = rightNow.get(Calendar.YEAR);
        Integer month = rightNow.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
        Integer day = rightNow.get(rightNow.DAY_OF_MONTH);
        HashMap map = new HashMap();
        map.put("DSYear",year);
        map.put("DSMonth",month);
//        map.put("day",day);
        map.put("drivId",drivId);
        DrivSign drivSign =driverMapper.getSigns(map).get(0).getDrivSigns().get(0);
        System.out.println(drivSign.getDSSign());
        StringBuffer sb = new StringBuffer(drivSign.getDSSign());
        map.put("signs",sb.replace(day-1,day,"1").toString());
        return driverMapper.do_clock(map);
    }


    public static int getMonthDays(int year, int month) {
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }
}
