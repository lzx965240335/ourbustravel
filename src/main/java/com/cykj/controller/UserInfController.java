package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.ChatInf;
import com.cykj.bean.UserInf;
import com.cykj.service.UserInfService;
import com.cykj.util.AdminChatJson;
import com.cykj.util.UserChatJson;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/userMain")
public class UserInfController {

    @Autowired
    private UserInfService userInfService;


    @RequestMapping(value = "/getUserTable")
    @ResponseBody
    public LayuiJson getTableData(HttpServletRequest req){
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String userName = req.getParameter("userName");
        int start = (page-1)*limit;
        HashMap<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("end",limit);
        if (userName != null && !userName.equals("")){
            map.put("userName",userName);
        }
         LayuiJson LayuiJson = userInfService.selectUser(map);
        return LayuiJson;
    }


    //返回页面方式
    @RequestMapping(value = "/getView")
    public String getView(){
        return "login";
    }
    //post请求传递对象
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestBody @Validated UserInf userInf, BindingResult br){
        System.out.println("我要来登录了");
        System.out.println(userInf.getAccount());
        if (br.hasErrors()){
            List<ObjectError> errors = br.getAllErrors();
            StringBuffer sb = new StringBuffer();
            for (ObjectError error : errors){
                sb.append(error.getDefaultMessage()+" ");
                System.out.println(error.getDefaultMessage());
            }
            return JSON.toJSONString(sb);
        }
        int result = userInfService.login(userInf);
        return JSON.toJSONString(result);
    };

    @RequestMapping("/codeLogin")
    @ResponseBody
    public String codeLogin(String phoneNum, String verificationCode, HttpSession session){
        System.out.println("我要来登录了");
        String code = (String) session.getAttribute("verificationCode");
        if (code.equals(verificationCode)){
            UserInf userInf = new UserInf();
            userInf.setPhoneNum(phoneNum);
            int result = userInfService.login(userInf);
            return JSON.toJSONString(result);
        }
        return "";
    }
    @RequestMapping("/backstage")
    public String getBackstage() {
        System.out.println("进来后台了");
        return "backstage";
    }

//    @RequestMapping("/preview")
//    public String getpreview() {
//        System.out.println("preview");
//        return "preview";
//    }

    @RequestMapping("/map")
    public String getqt() {
        System.out.println("进来map.......");
        return "map";
    }
    @RequestMapping("/wallet")
    public String getwallet() {
        System.out.println("进来我的钱包界面");
        return "wallet";
    }
    @RequestMapping("/userInf")
    public String getUserInf(Model model) {
        System.out.println("进来获取UserInf界面");
        model.addAttribute("userInf",userInfService.getLoginner());
        return "UserInf";
    }

    @RequestMapping("/updatePwdView")
    public String getUpdatePwd() {
        System.out.println("进来获取修改密码界面");
        return "updatePwd";
    }
    @RequestMapping("/getLoginner")
    @ResponseBody
    public UserInf getLoginner(){
        System.out.println("进来获取用户");
        return userInfService.getLoginner();
    }
    @RequestMapping("/verification")
    @ResponseBody
    public String getVerificationCode(String phoneNum , HttpSession session){
        System.out.println("进来获取验证码");
        return userInfService.sendMSG(phoneNum , session);
    }
    @RequestMapping("/register")
    @ResponseBody
    public String register(String phoneNum , String code , String password , HttpSession session){
        System.out.println("进来注册账号");
        System.out.println(phoneNum);
        return userInfService.register(phoneNum , code , password , session);
    }
    @RequestMapping("/updatePwd")
    @ResponseBody
    public String updatePwd(String phoneNum , String code , String password , HttpSession session){
        System.out.println("进来修改密码");
        return userInfService.updatePwd(phoneNum , code , password , session);
    }
    @RequestMapping("/upAvatar")
    @ResponseBody
    public String upAvatar(HttpServletRequest request, MultipartFile uploadFile, HttpSession session){
        System.out.println("进来上传头像");
        userInfService.upLoadFile(request,uploadFile,session);
        return "";
    }
    @RequestMapping("/updateUserInf")
    @ResponseBody
    public String updateUserInf(UserInf userInf){
        System.out.println("进来修改个人信息");
        System.out.println(JSON.toJSONString(userInf));
        String result = userInfService.updateUserInf(userInf);
        return result;
    }
    @RequestMapping("/userChat")
    @ResponseBody
    public String userChat(HttpSession session){
        System.out.println("获取聊天界面");
        String result =JSON.toJSONString(new UserChatJson(session));
        return result;
    }
    @RequestMapping("/adminChat")
    @ResponseBody
    public String adminChat(HttpSession session){
        System.out.println("获取聊天界面");
        String result =JSON.toJSONString(new AdminChatJson(session));
        return result;
    }
    @RequestMapping(value = "/chatLoad")
    @ResponseBody
    public String  chatLoad(@RequestBody ChatInf chatInf){
        System.out.println(JSON.toJSONString(chatInf));
        userInfService.chatLoad(chatInf);
        return "";
    }
    @RequestMapping(value = "/userExit")
    @ResponseBody
    public String  userExit(HttpSession session){
        session.removeAttribute("loginner");
        return "已退出";
    }
}
