package com.cykj.service;

import com.cykj.bean.Driver;

public interface DriverService {
    Driver login(String account, String password);
    Driver getSigns(String year, String month, int days, int drivId);
    int do_clock(int drivId);
}
