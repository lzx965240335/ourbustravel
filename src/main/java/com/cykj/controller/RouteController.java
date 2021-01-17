package com.cykj.controller;


import com.cykj.service.AdminInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RouteController {
    @Autowired
    private AdminInfService adminInfService;
}
