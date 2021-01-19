package com.cykj.controller;

import com.cykj.bean.AdminInf;
import com.cykj.service.AdminInfService;
import com.cykj.util.LayuiJson;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/Admin")
@Controller
public class AdminController {
    @Autowired
    private AdminInfService adminInfService;


    //表格展示
    @RequestMapping(value = "/getSelectTable")
    @ResponseBody
    public Map<String,Object> getSelectTable(@RequestParam String limit, @RequestParam String page, HttpServletRequest request){
        int newPage = Integer.parseInt(page);
        int newLimit=Integer.parseInt(limit);
        Map<String,Object> hasMap=new HashMap<>();
        RowBounds rb = new RowBounds((newPage-1)*newLimit,newLimit);
        List<AdminInf> adminInfs = adminInfService.adminSelectTable(hasMap,rb);
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("count", adminInfService.adminSelectCount(hasMap));
        map.put("data",adminInfs);
        return map;
    }

    //删除
    @RequestMapping(value = "/getDelete")
    @ResponseBody
    public String  getDelete(HttpServletRequest request){
        AdminInf adminInf= new AdminInf();
        adminInf.setAdminId(Integer.parseInt(request.getParameter("adminId")));
        boolean flag = adminInfService.deleteAdminInf(adminInf);
        return flag == true ? "删除成功" : "删除失败";
    }

    //重置密码
    @RequestMapping(value = "/getResetPwd")
    @ResponseBody
    public String getResetPwd(HttpServletRequest request,String account){
       int flag = adminInfService.resetPwd(account);
        return flag > 0 ? "重置密码成功" : "重置密码失败";
    }
    //新增
    @RequestMapping(value = "/getAdd")
    @ResponseBody
    public String getAdd(HttpServletRequest request,@RequestBody AdminInf adminInf){
        boolean flag = adminInfService.insertAdminInf(adminInf);
        return flag == true ? "新增成功" : "新增失败";
    }
    //修改
    @RequestMapping(value = "/getReset")
    @ResponseBody
    public String getReset(HttpServletRequest request,@RequestBody AdminInf adminInf){
        boolean flag = adminInfService.updateAdminInf(adminInf);
        return flag == true ? "修改成功" : "修改失败";
    }

    @RequestMapping(value = "/getAdminTable")
    @ResponseBody
    public LayuiJson getTableData(HttpServletRequest req){
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String adminName = req.getParameter("adminName");
        int start = (page-1)*limit;
        HashMap<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("end",limit);
        if (adminName != null && !adminName.equals("")){
            map.put("adminName",adminName);
        }
        LayuiJson LayuiJson = adminInfService.selectAdmin(map);
        return LayuiJson;
    }

    //删除
    @RequestMapping("/deleteAdmin")
    @ResponseBody
    public String deleteAdmin(HttpServletRequest req) {
        AdminInf adminInf = new AdminInf();
        adminInf.setAdminId(Integer.parseInt(req.getParameter("adminId")));
        boolean deleteResult = adminInfService.deleteAdmin(adminInf);
        return deleteResult == true ? "删除成功" : "删除失败";

    }

    //新增
    @RequestMapping("/addAdmin")
    @ResponseBody
    public String addAdmin(HttpServletRequest req, @RequestBody AdminInf adminInf) {
        System.out.println(222);
        int addResult = adminInfService.addAdmin(adminInf);
        return addResult > 0 ? "添加成功" : "添加失败";
    }

    //修改
    @RequestMapping("/updateAdmin")
    @ResponseBody
    public String updateAdmin(HttpServletRequest req, @RequestBody AdminInf adminInf) {
        int updateResult = adminInfService.updateAdmin(adminInf);
        return updateResult > 0 ? "修改成功" : "修改失败";
    }

    //修改状态
    @RequestMapping("/updateState")
    @ResponseBody
    public String  changeStaupdateStatete(HttpServletRequest request, @RequestBody AdminInf adminInf){
        int flag=adminInfService.updateState(adminInf);
        if(flag>0){
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/adminInfLogin")
    @ResponseBody
    public String adminInfLogin(@RequestBody AdminInf adminInf, HttpServletRequest request){

        AdminInf admin = adminInfService.selectAdminInf(adminInf);
        System.out.println(admin);
        if (admin!=null){
            request.getSession().setAttribute("adminInf",admin);
            return "登录成功";
        }else {
            return "账号密码错误";
        }
    }
    @RequestMapping("/getService")
    @ResponseBody
    public AdminInf getService(HttpServletRequest request){
        return (AdminInf) request.getSession().getAttribute("adminInf");
    }
    @RequestMapping("/exit")
    @ResponseBody
    public void exit(HttpSession session){
        session.removeAttribute("adminInf");
    }
}
